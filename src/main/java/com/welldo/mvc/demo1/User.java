package com.welldo.mvc.demo1;

/**
 * author:welldo
 * date: 2021-09-14 18:14
 */
public class User {
    public long id;
    public String name;
    public School school;



    public User(long id, String name, School school) {
        this.id = id;
        this.name = name;
        this.school = school;
    }
}
