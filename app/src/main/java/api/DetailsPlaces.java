package api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.Restaurant;

public class DetailsPlaces {
    @SerializedName("result")
    @Expose
    private Restaurant restaurantsDetails;

    public Restaurant getRestaurantsDetails() {
        return restaurantsDetails;
    }

    public void setRestaurantsDetails(Restaurant restaurantsDetails) {
        this.restaurantsDetails = restaurantsDetails;
    }
}
