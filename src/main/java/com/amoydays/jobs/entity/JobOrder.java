package com.amoydays.jobs.entity;

import java.util.Date;

public class JobOrder {
    private String id;

    private String driver;

    private String carNum;

    private String telephone;

    private String goodsInfo;

    private Integer goodsNum;

    private String time;

    private String typeId;

    private String areaId;

    private String goodsId;

    private String goodsName;

    private String areaName;

    private String typeName;

    private Integer weight;

    private Date date;

    private String duty;

    private String vesselVoyage;

    public JobOrder(String id, String driver, String carNum, String telephone, String goodsInfo, Integer goodsNum, String time, String typeId, String areaId, String goodsId, String goodsName, String areaName, String typeName, Integer weight, Date date, String duty, String vesselVoyage) {
        this.id = id;
        this.driver = driver;
        this.carNum = carNum;
        this.telephone = telephone;
        this.goodsInfo = goodsInfo;
        this.goodsNum = goodsNum;
        this.time = time;
        this.typeId = typeId;
        this.areaId = areaId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.areaName = areaName;
        this.typeName = typeName;
        this.weight = weight;
        this.date = date;
        this.duty = duty;
        this.vesselVoyage = vesselVoyage;
    }

    public JobOrder() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver == null ? null : driver.trim();
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum == null ? null : carNum.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo == null ? null : goodsInfo.trim();
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId == null ? null : typeId.trim();
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    public String getVesselVoyage() {
        return vesselVoyage;
    }

    public void setVesselVoyage(String vesselVoyage) {
        this.vesselVoyage = vesselVoyage == null ? null : vesselVoyage.trim();
    }
}