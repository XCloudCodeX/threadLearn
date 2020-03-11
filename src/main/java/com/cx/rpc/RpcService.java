package com.cx.rpc;

import lombok.SneakyThrows;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/10
 * Time: 23:01
 * Description: Rpc服务
 */
public class RpcService implements Runnable {
    private Socket client;
    private Map<String,Object> services;

    public RpcService(Socket client, Map<String, Object> services) {
        super();
        this.client = client;
        this.services = services;
    }


    @Override
    public void run() {
        ObjectInputStream oin = null;
        ObjectOutputStream oout = null;
        RpcResponse response = new RpcResponse();
        try {
            //获取流
            oin = new ObjectInputStream(client.getInputStream());
            oout = new ObjectOutputStream(client.getOutputStream());
            //获取请求数据
            Object param = oin.readObject();
            RpcRequest request = null;
            if (!(param instanceof RpcRequest)){
                response.setError(new Exception("参数错误"));
                oout.writeObject(response);
                oout.flush();
                return;
            }else {
                request = (RpcRequest) param;
            }

            //查找并执行服务方法
            Object service = services.get(request.getClassName());
            Class<?> clazz = service.getClass();
            Method method = clazz.getMethod(request.getMethodName(),request.getParamTypes());
            Object result = method.invoke(service,request.getParams());

            //得到结果并返回
            response.setResult(result);
            oout.writeObject(response);
            oout.flush();
        }catch (Exception e){
            if(oout != null){
                try {
                    response.setError(e);
                    oout.writeObject(response);
                    oout.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return;
        }finally {
            try {
                if (oin !=null) oin.close();
                if (oout !=null) oout.close();
                if (client !=null) client.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
