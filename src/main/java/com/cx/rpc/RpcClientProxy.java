package com.cx.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/11
 * Time: 15:10
 * Description: No Description
 */
public class RpcClientProxy implements InvocationHandler {

    private String host;

    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 生成代理对象
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T getProxy(Class<T> clazz){
        //如果clazz不是接口 不能使用jdk动态代理
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class<?>[]{clazz},
                RpcClientProxy.this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //封装参数
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(args);
        //链接服务器调用服务
        RpcClient client = new RpcClient();
        return client.start(request,host,port);
    }
}
