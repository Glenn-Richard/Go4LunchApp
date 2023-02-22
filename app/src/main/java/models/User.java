package models;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String photo;
    private Restaurant choice;
    private List<String> favorites;

    public User() {}

    public User(String id,String name, String email, String photo, Restaurant choice, List<String> favorites) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.choice = choice;
        this.favorites = favorites;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

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

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }
}
