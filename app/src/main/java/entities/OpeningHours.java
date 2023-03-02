package entities;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {

    @SerializedName("open_now")
    private final Boolean open_now;

    public OpeningHours(Boolean open_now) {
        this.open_now = open_now;
    }

    public Boolean getOpen_now() {
        return open_now;
    }


}
