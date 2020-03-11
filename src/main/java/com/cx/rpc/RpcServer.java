package com.cx.rpc;

import com.sun.deploy.util.StringUtils;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/10
 * Time: 22:43
 * Description: Rpc服务
 */
public class RpcServer {
    /**
     * 启动rpc服务器
     * @param port
     * @param clazz 服务类所在的包名,多个使用英文逗号隔开
     */
    public void start(int port,String clazz){
        ServerSocket server = null;
        try {
            //1 创建socket连接
            server = new ServerSocket(port);
            //2 获取所有rpc服务，发布的服务
            Map<String,Object> services = getService(clazz);
            //3 创建线程池 使用有界阻塞队列
            Executor executor = new ThreadPoolExecutor(5,10,10,
                            TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
            while (true){
                //4 获取客户端连接 Bio模式
                Socket client = server.accept();
                //5 查找并执行服务
                RpcService service = new RpcService(client,services);
                executor.execute(service);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 实例化所有rpc服务
     * @param clazz 服务类所在的包名,多个使用英文逗号分隔
     * @return
     */
    private Map<String,Object> getService(String clazz) {
        try {
            Map<String, Object> services = new HashMap<>();
            //获取所有的服务类
            String[] clazzes = clazz.split(",");
            List<Class<?>> classes = new ArrayList<>();
            for (String cl : clazzes) {
                List<Class<?>> classList = getClasses(cl);
                classes.addAll(classList);
            }
            //循环实例化
            for (Class<?> cla : classes) {
                Object obj = cla.newInstance();
                services.put(cla.getAnnotation(com.cx.annotion.RpcService.class).value().getName(),obj);
            }
            return services;
        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    /**
     * 获取包下所有有@RpcService注解的类
     * @param packageName
     * @return
     */
    @SneakyThrows
    private List<Class<?>> getClasses(String packageName) {
        //寄存所有带注解的实例化类
        List<Class<?>> classes = new ArrayList<>();
        File directory = null;
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        if(cld == null)
            throw new ClassNotFoundException("cannot get classLoader!");
        String path = packageName.replace('.','/');
        URL resource = cld.getResource(path);
        if(resource == null)
            throw new ClassNotFoundException("No resource for"+ path);
        directory = new File(resource.getFile());
        if (directory.exists()) {
            //获取所有文件
            String[] files = directory.list();
            File[] filesList = directory.listFiles();
            for (int i=0; filesList !=null&& i<filesList.length;i++){
                File file = filesList[i];
                //判断是否class文件
                if (file.isFile() && file.getName().endsWith(".class")){
                    Class<?> clazz = Class.forName(packageName +
                            '.'+ files[i].substring(0,files[i].length() - 6));
                    if(clazz.getAnnotation(com.cx.annotion.RpcService.class) != null){
                        classes.add(clazz);
                    }
                }else if(file.isDirectory()){ //递归目录查找实例化
                    List<Class<?>> result = getClasses(packageName + '.'+file.getName());
                    if(result!=null && result.size() !=0){
                        classes.addAll(result);
                    }
                }
            }
        }else {
            throw  new ClassNotFoundException(packageName + "is not found!");
        }
        return classes;
    }
}
