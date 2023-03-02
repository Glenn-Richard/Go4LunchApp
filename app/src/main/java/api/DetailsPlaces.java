package api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import models.Restaurant;

public class DetailsPlaces {
    @SerializedName("result")
    @Expose
    private final Restaurant restaurantsDetails;

    public DetailsPlaces(Restaurant restaurantsDetails) {
        this.restaurantsDetails = restaurantsDetails;
    }

    public Restaurant getRestaurantsDetails() {
        return restaurantsDetails;
    }

}
