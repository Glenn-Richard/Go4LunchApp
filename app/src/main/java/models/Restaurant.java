package models;

public class Restaurant {

    private String name;
    private String address;
    private String photo;
    private int stars;
    private int number_of_workmates;
    private double lat;
    private double lng;
    private float distance_of_current_location;
    private String phone;
    private double rating;
    private boolean opening;
    private boolean no_opening_hours;
    private String website;

    public Restaurant() {
    }


    public Restaurant(String name, String address, String photo, int stars, int number_of_workmates, double lat, double lng, float distance_of_current_location, String phone, int rating, boolean opening, boolean no_opening_hours, String website) {
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
        this.no_opening_hours = no_opening_hours;
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

    public boolean isOpening() {
        return opening;
    }

    public void setOpening(boolean opening) {
        this.opening = opening;
    }

    public boolean isNo_opening_hours() {
        return no_opening_hours;
    }

    public void setNo_opening_hours(boolean no_opening_hours) {
        this.no_opening_hours = no_opening_hours;
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
