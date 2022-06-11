package com.amoydays.jobs.entity;

import java.util.Date;

public class JobUpdateRecord {
    private Integer id;

    private String driver;

    private String telephone;

    private Date time;

    private String columnName;

    private String oriValue;

    private String nowValue;

    public JobUpdateRecord(Integer id, String driver, String telephone, Date time, String columnName, String oriValue, String nowValue) {
        this.id = id;
        this.driver = driver;
        this.telephone = telephone;
        this.time = time;
        this.columnName = columnName;
        this.oriValue = oriValue;
        this.nowValue = nowValue;
    }

    public JobUpdateRecord() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver == null ? null : driver.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getOriValue() {
        return oriValue;
    }

    public void setOriValue(String oriValue) {
        this.oriValue = oriValue == null ? null : oriValue.trim();
    }

    public String getNowValue() {
        return nowValue;
    }

    public void setNowValue(String nowValue) {
        this.nowValue = nowValue == null ? null : nowValue.trim();
    }
}