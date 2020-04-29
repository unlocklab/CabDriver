package com.example.cabbooking.driver.dto;

public class CatDto {
    private String cat_id;
    private String cat_name;
    private long datetime;

    public CatDto() {
    }

    public CatDto(String cat_id, String cat_name, long datetime) {
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.datetime = datetime;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
