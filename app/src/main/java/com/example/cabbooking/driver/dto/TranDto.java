package com.example.cabbooking.driver.dto;

public class TranDto {
    private String tran_id;
    private String from;
    private String to;
    private String amount;
    private String user_id;
    private String datetime;

    public TranDto() {
    }

    public TranDto(String tran_id, String from, String to, String amount, String user_id, String datetime) {
        this.tran_id = tran_id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.user_id = user_id;
        this.datetime = datetime;
    }

    public String getTran_id() {
        return tran_id;
    }

    public void setTran_id(String tran_id) {
        this.tran_id = tran_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
