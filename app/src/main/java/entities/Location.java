package entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public Location(){}

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

}
