package models;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String photo;
    private Restaurant choice;
    private List<String> favorites;

    public User(){}

    public User(Restaurant choice, List<String> favorites) {
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

    public void setEmail(String ignoredEmail) {
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

}
