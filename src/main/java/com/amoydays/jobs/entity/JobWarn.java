package com.amoydays.jobs.entity;

import java.util.Date;

public class JobWarn {
    private Integer id;

    private String telephone;

    private String driver;

    private String content;

    private Date time;

    private Boolean isRead;

    public JobWarn(Integer id, String telephone, String driver, String content, Date time, Boolean isRead) {
        this.id = id;
        this.telephone = telephone;
        this.driver = driver;
        this.content = content;
        this.time = time;
        this.isRead = isRead;
    }

    public JobWarn() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver == null ? null : driver.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}