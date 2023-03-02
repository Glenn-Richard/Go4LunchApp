package api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import models.Restaurant;

public class JSONResponse {

    @SerializedName("results")
    @Expose
    List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

}
