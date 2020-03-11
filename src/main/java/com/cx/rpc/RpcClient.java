package com.cx.rpc;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/11
 * Time: 15:22
 * Description: No Description
 */
public class RpcClient {

    public Object start(RpcRequest request, String host, int port) throws Throwable {
        Socket server = null;

        ObjectInputStream oin = null;
        ObjectOutputStream oout = null;
        try {
            server = new Socket(host, port);
            //发送请求数据
            oout = new ObjectOutputStream(server.getOutputStream());
            oout.writeObject(request);
            oout.flush();

            //接收数据
            oin = new ObjectInputStream(server.getInputStream());
            Object res = oin.readObject();
            RpcResponse response = null;
            if (!(res instanceof RpcResponse)) {
                throw new RuntimeException("返回参数不正确");
            } else {
                response = (RpcResponse) res;
            }

            //返回结果
            //判断服务器是否有异常
            if (response.getError() != null) {
                throw response.getError();
            }
            return response.getResult();
        }finally {
            try {
                if (oin != null) oin.close();
                if (oout != null) oout.close();
                if (server != null) server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
