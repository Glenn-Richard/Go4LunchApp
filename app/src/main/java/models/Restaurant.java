package models;

public class Restaurant {

    private String name;
    private String address;
    private String photo;
    private int stars;
    private int number_of_workmates;
    private double lat;
    private double lng;
    private String website;

    public Restaurant() {
    }

    public Restaurant(String name, String address, String photo, int stars, int number_of_workmates, double lat, double lng, String website) {
        this.name = name;
        this.address = address;
        this.photo = photo;
        this.stars = stars;
        this.number_of_workmates = number_of_workmates;
        this.lat = lat;
        this.lng = lng;
        this.website = website;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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

    public double getLat(double lat) {
        return this.lat;
    }

    public double getLng(double lng) {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
