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

    private int number_of_workmates;

    @SerializedName("geometry")
    Geometry geometry;

    @SerializedName("formatted_phone_number")
    private String phone;

    @SerializedName("types")
    @Expose
    List<String> types;

    @SerializedName("rating")
    private double rating;

    @SerializedName("opening_hours")
    @Expose
    OpeningHours opening_hours;

    private boolean opening;

    @SerializedName("website")
    private String website;

    public Restaurant() {
    }


    public Restaurant(String place_id, String name, Geometry geometry,String address, List<Photo> photo, int number_of_workmates, String phone, List<String> types, int rating, boolean opening, String website) {
        this.place_id = place_id;
        this.name = name;
        this.geometry = geometry;
        this.address = address;
        this.photo = photo;
        this.number_of_workmates = number_of_workmates;
        this.phone = phone;
        this.types = types;
        this.rating = rating;
        this.opening = opening;
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

    public void setOpening(boolean opening) {
        this.opening = opening;
    }

    public List<Photo> getPhoto() {
        return photo;
    }


    public int getNumber_of_workmates() {
        return number_of_workmates;
    }

    public void setNumber_of_workmates(int number_of_workmates) {
        this.number_of_workmates = number_of_workmates;
    }



    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public boolean isOpening() {
        return opening;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getTypes() {
        return types;
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
