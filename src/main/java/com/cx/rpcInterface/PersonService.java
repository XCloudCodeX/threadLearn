package com.cx.rpcInterface;

import com.cx.entity.Person;

public interface PersonService {
    public Person getInfo();

    public boolean printInfo(Person person);
}
