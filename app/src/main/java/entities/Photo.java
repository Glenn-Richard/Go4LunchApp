package entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Photo implements Serializable {

    @SerializedName("height")
    private Integer height;

    @SerializedName("photo_reference")
    private final String photoReference;

    @SerializedName("width")
    private Integer width;

    public Photo(String photoReference) {
        this.photoReference = photoReference;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public String getPhotoReference() {
        return photoReference;
    }


    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
