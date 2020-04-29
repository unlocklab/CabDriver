package com.example.cabbooking.driver.dto;
/*
 * normal pojo classes with cunstructors and setter/getter
 * for Settings Screen
 * */
public class SetViewDto {
    private int rtype;
    private String value;
    private String value1;

    public SetViewDto() {
    }

    public SetViewDto(int rtype, String value, String value1) {
        this.rtype = rtype;
        this.value = value;
        this.value1 = value1;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
