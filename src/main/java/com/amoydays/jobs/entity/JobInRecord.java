package com.amoydays.jobs.entity;

import java.util.Date;

public class JobInRecord {
    private Integer id;

    private String operName;

    private Date time;

    private String tbName;

    private String columnName;

    private String oriValue;

    private String nowValue;

    public JobInRecord(Integer id, String operName, Date time, String tbName, String columnName, String oriValue, String nowValue) {
        this.id = id;
        this.operName = operName;
        this.time = time;
        this.tbName = tbName;
        this.columnName = columnName;
        this.oriValue = oriValue;
        this.nowValue = nowValue;
    }

    public JobInRecord() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName == null ? null : tbName.trim();
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