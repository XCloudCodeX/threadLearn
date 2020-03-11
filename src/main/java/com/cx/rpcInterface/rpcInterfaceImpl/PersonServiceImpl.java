package com.cx.rpcInterface.rpcInterfaceImpl;

import com.cx.annotion.RpcService;
import com.cx.entity.Person;
import com.cx.rpcInterface.PersonService;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/11
 * Time: 15:44
 * Description: No Description
 */
@RpcService(PersonService.class)
public class PersonServiceImpl implements PersonService {
    @Override
    public Person getInfo() {
        Person person = new Person();
        person.setAge(28);
        person.setName("CloudCx");
        person.setSex("男");
        return person;
    }

    @Override
    public boolean printInfo(Person person) {
        if (person != null){
            System.out.println(person);
            return true;
        }
        return false;
    }
}
