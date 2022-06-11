package com.amoydays.jobs.entity;

public class JobUpdateString {
    private Integer id;

    private String content;

    private Boolean isRead;

    public JobUpdateString(Integer id, String content, Boolean isRead) {
        this.id = id;
        this.content = content;
        this.isRead = isRead;
    }

    public JobUpdateString() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}