package com.example.cabbooking.driver.dto;

public class CommDto {

    private String com_per_trip;
    private String discount;
    private String price_id;

    public CommDto() {
    }

    public CommDto(String com_per_trip, String discount, String price_id) {
        this.com_per_trip = com_per_trip;
        this.discount = discount;
        this.price_id = price_id;
    }

    public String getCom_per_trip() {
        return com_per_trip;
    }

    public void setCom_per_trip(String com_per_trip) {
        this.com_per_trip = com_per_trip;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice_id() {
        return price_id;
    }

    public void setPrice_id(String price_id) {
        this.price_id = price_id;
    }
}
