package com.cx.rpc;

import java.lang.ref.Reference;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/11
 * Time: 15:51
 * Description: No Description
 */
    public class ServerTest {
    public static void main(String[] args) {
        RpcServer server = new RpcServer();
        server.start(8899,"com.cx.rpcInterface");
    }
}
