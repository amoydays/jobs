package com.amoydays.jobs.entity;

public class SuperUser {
    private String name;

    private String passwd;

    public SuperUser(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    public SuperUser() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }
}