package com.amoydays.jobs.vo;

public class OrderSearch {
    private String startDate;
    private String startDuty;
    private String endDate;
    private String endDuty;
    private String telephone;

    private String areaId;
    private String goodsId;
    private String typeId;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDuty() {
        return startDuty;
    }

    public void setStartDuty(String startDuty) {
        this.startDuty = startDuty;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDuty() {
        return endDuty;
    }

    public void setEndDuty(String endDuty) {
        this.endDuty = endDuty;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
