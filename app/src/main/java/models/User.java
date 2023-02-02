package models;

import java.util.List;

public class User {
    private String name;
    private String email;
    private String photo;
    private Restaurant choice;
    private List<Restaurant> favorites;

    public User() {}

    public User(String name, String email, String photo, Restaurant choice, List<Restaurant> favorites) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.choice = choice;
        this.favorites = favorites;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Restaurant getChoice() {
        return choice;
    }

    public void setChoice(Restaurant choice) {
        this.choice = choice;
    }

    public List<Restaurant> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Restaurant> favorites) {
        this.favorites = favorites;
    }
}
