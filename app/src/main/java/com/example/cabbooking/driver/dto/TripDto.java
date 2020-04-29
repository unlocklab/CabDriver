package com.example.cabbooking.driver.dto;

public class TripDto {
    private String trip_id;
    private String driver_id;
    private String rider_id;
    private String distance;
    private String duration;
    private String fare;
    private String pay_type;
    private String from_lat_lng;
    private String from_str;
    private String to_lat_lng;
    private String to_str;
    private String status;
    private int ride_type;
    private String date_str;
    private long datetime;

    public TripDto() {
    }

    public TripDto(String trip_id, String driver_id, String rider_id, String distance, String duration, String fare, String pay_type, String from_lat_lng, String from_str, String to_lat_lng, String to_str, String status, int ride_type, String date_str, long datetime) {
        this.trip_id = trip_id;
        this.driver_id = driver_id;
        this.rider_id = rider_id;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.pay_type = pay_type;
        this.from_lat_lng = from_lat_lng;
        this.from_str = from_str;
        this.to_lat_lng = to_lat_lng;
        this.to_str = to_str;
        this.status = status;
        this.ride_type = ride_type;
        this.date_str = date_str;
        this.datetime = datetime;
    }

    public String getDate_str() {
        return date_str;
    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public int getRide_type() {
        return ride_type;
    }

    public void setRide_type(int ride_type) {
        this.ride_type = ride_type;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getFrom_lat_lng() {
        return from_lat_lng;
    }

    public void setFrom_lat_lng(String from_lat_lng) {
        this.from_lat_lng = from_lat_lng;
    }

    public String getFrom_str() {
        return from_str;
    }

    public void setFrom_str(String from_str) {
        this.from_str = from_str;
    }

    public String getTo_lat_lng() {
        return to_lat_lng;
    }

    public void setTo_lat_lng(String to_lat_lng) {
        this.to_lat_lng = to_lat_lng;
    }

    public String getTo_str() {
        return to_str;
    }

    public void setTo_str(String to_str) {
        this.to_str = to_str;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
