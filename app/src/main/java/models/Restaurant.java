package models;

public class Restaurant {

    private String name;
    private String address;
    private int stars;
    private int number;
    private String website;

    public Restaurant() {
    }

    public Restaurant(String name, String address, int stars, int number, String website) {
        this.name = name;
        this.address = address;
        this.stars = stars;
        this.number = number;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
