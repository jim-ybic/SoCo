package com.soco.SoCoClient.db.test;

/**
 * Created by jenny on 28/12/14.
 */

public class Person {
    public int _id;
    public String name;
    public int age;
    public String info;

    public Person() {
    }

    public Person(String name, int age, String info) {
        this.name = name;
        this.age = age;
        this.info = info;
    }
}
