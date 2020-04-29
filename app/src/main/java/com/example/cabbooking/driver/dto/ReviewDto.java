package com.example.cabbooking.driver.dto;

public class ReviewDto {
    private String rw_id;
    private String rating;
    private String feedback;
    private String rider_id;
    private String driver_id;
    private String trip_id;
    private String trip_type;
    private long datetime;

    public ReviewDto() {
    }

    public ReviewDto(String rw_id, String rating, String feedback, String rider_id, String driver_id, String trip_id, String trip_type, long datetime) {
        this.rw_id = rw_id;
        this.rating = rating;
        this.feedback = feedback;
        this.rider_id = rider_id;
        this.driver_id = driver_id;
        this.trip_id = trip_id;
        this.trip_type = trip_type;
        this.datetime = datetime;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public void setTrip_type(String trip_type) {
        this.trip_type = trip_type;
    }

    public String getRw_id() {
        return rw_id;
    }

    public void setRw_id(String rw_id) {
        this.rw_id = rw_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRider_id() {
        return rider_id;
    }

    public void setRider_id(String rider_id) {
        this.rider_id = rider_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
