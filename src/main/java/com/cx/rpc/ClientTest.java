package com.cx.rpc;

import com.cx.entity.Person;
import com.cx.rpcInterface.PersonService;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/11
 * Time: 15:47
 * Description: No Description
 */
public class ClientTest {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1",8899);
        PersonService service = proxy.getProxy(PersonService.class);

        System.out.println(service.getInfo());

        Person person = new Person();
        person.setAge(25);
        person.setName("cx");
        person.setSex("boy");
        System.out.println(service.printInfo(person));
    }
}
