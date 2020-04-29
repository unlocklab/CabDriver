package com.example.cabbooking.driver.dto;

public class SupportDto {
    private String supportId;
    private String title;
    private String description;
    private String userId;
    private String adminId;
    private String datetime;

    public SupportDto() {
    }

    public SupportDto(String supportId, String title, String description, String userId, String adminId, String datetime) {
        this.supportId = supportId;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.adminId = adminId;
        this.datetime = datetime;
    }

    public String getSupportId() {
        return supportId;
    }

    public void setSupportId(String supportId) {
        this.supportId = supportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
