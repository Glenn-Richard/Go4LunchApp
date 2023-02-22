package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import entities.Geometry;
import entities.OpeningHours;
import entities.Photo;

public class Restaurant {

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("name")
    private String name;

    @SerializedName("vicinity")
    private String address;

    @SerializedName("photos")
    @Expose
    List<Photo> photo;

    private int stars;

    private int number_of_workmates;

    @SerializedName("geometry")
    Geometry geometry;

    private double lat;

    private double lng;

    private float distance_of_current_location;

    @SerializedName("formatted_phone_number")
    private String phone;

    @SerializedName("rating")
    private double rating;

    @SerializedName("opening_hours")
    @Expose
    OpeningHours opening_hours;

    private boolean opening;

    private boolean open_info;

    @SerializedName("website")
    private String website;

    public Restaurant() {
    }


    public Restaurant(String place_id,String name, String address, List<Photo> photo, int stars, int number_of_workmates, double lat, double lng, float distance_of_current_location, String phone, int rating, boolean opening, boolean no_opening_hours, String website) {
        this.place_id = place_id;
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.stars = stars;
        this.number_of_workmates = number_of_workmates;
        this.lat = lat;
        this.lng = lng;
        this.distance_of_current_location = distance_of_current_location;
        this.phone = phone;
        this.rating = rating;
        this.opening = opening;
        this.open_info = no_opening_hours;
        this.website = website;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getNumber_of_workmates() {
        return number_of_workmates;
    }

    public void setNumber_of_workmates(int number_of_workmates) {
        this.number_of_workmates = number_of_workmates;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public float getDistance_of_current_location() {
        return distance_of_current_location;
    }

    public void setDistance_of_current_location(float distance_of_current_location) {
        this.distance_of_current_location = distance_of_current_location;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(OpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }
    public boolean isOpening() {
        return opening;
    }

    public void setOpening(boolean opening) {
        this.opening = opening;
    }

    public boolean isOpen_info() {
        return open_info;
    }

    public void setOpen_info(boolean open_info) {
        this.open_info = open_info;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
