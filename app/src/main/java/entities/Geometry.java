package entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geometry implements Serializable {

    @SerializedName("location")
    @Expose
    Location location;

    public Geometry(){}
    public Geometry(Location location){
        this.location= location;
    }

    public Location getLocation() {
        return location;
    }
}
