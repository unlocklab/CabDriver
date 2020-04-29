package com.example.cabbooking.driver.dto;

public class UserDto {
    private String userId;
    private String fname;
    private String lname;
    private String mobile;
    private String token;
    private String email;
    private String password;
    private String image;
    private String veh_image;
    private String user_type;
    private String status;
    private String lic_no;
    private String veh_no;
    private String location;
    private String veh_cat;
    private String milage;
    private String balance;
    private String payment_type;
    private boolean start;
    private boolean economy;
    private boolean comfort;
    private long datetime;

    public UserDto() {
    }

    public UserDto(String userId, String fname, String lname, String mobile, String token, String email, String password, String image, String veh_image, String user_type, String status, String lic_no, String veh_no, String location, String veh_cat, String milage, String balance, String payment_type, boolean start, boolean economy, boolean comfort, long datetime) {
        this.userId = userId;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.token = token;
        this.email = email;
        this.password = password;
        this.image = image;
        this.veh_image = veh_image;
        this.user_type = user_type;
        this.status = status;
        this.lic_no = lic_no;
        this.veh_no = veh_no;
        this.location = location;
        this.veh_cat = veh_cat;
        this.milage = milage;
        this.balance = balance;
        this.payment_type = payment_type;
        this.start = start;
        this.economy = economy;
        this.comfort = comfort;
        this.datetime = datetime;
    }

    public String getMilage() {
        return milage;
    }

    public void setMilage(String milage) {
        this.milage = milage;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEconomy() {
        return economy;
    }

    public void setEconomy(boolean economy) {
        this.economy = economy;
    }

    public boolean isComfort() {
        return comfort;
    }

    public void setComfort(boolean comfort) {
        this.comfort = comfort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVeh_image() {
        return veh_image;
    }

    public void setVeh_image(String veh_image) {
        this.veh_image = veh_image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLic_no() {
        return lic_no;
    }

    public void setLic_no(String lic_no) {
        this.lic_no = lic_no;
    }

    public String getVeh_no() {
        return veh_no;
    }

    public void setVeh_no(String veh_no) {
        this.veh_no = veh_no;
    }

    public String getVeh_cat() {
        return veh_cat;
    }

    public void setVeh_cat(String veh_cat) {
        this.veh_cat = veh_cat;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
