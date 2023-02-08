package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String,String> getNearbyPlace(JSONObject googlePlaceJSON){

        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String nameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String photo_reference = null;
        String opening;
        JSONArray photos;
        String latitude;
        String longitude;
        String phone;
        String website;
        String rating;

        try {
            if (!googlePlaceJSON.isNull("name")){
                nameOfPlace = googlePlaceJSON.getString("name");
            }
            if (!googlePlaceJSON.isNull("vicinity")){
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            if (!(googlePlaceJSON.isNull("opening_hours"))){
                opening = googlePlaceJSON.getJSONObject("opening_hours").getString("open_now");
                googlePlaceMap.put("opening_hours",opening);
            }
            if (!(googlePlaceJSON.isNull("formatted_phone_number"))){
                phone = googlePlaceJSON.getString("formatted_phone_number");
                googlePlaceMap.put("formatted_phone_number",phone);
            }
            if (!(googlePlaceJSON.isNull("website"))){
                website = googlePlaceJSON.getString("website");
                googlePlaceMap.put("website",website);
            }
            photos = googlePlaceJSON.getJSONArray("photos");
            if (!(photos.optJSONObject(0).isNull("photo_reference"))){
                photo_reference = photos.optJSONObject(0).getString("photo_reference");
            }
            if (!(googlePlaceJSON.isNull("rating"))){
                rating = googlePlaceJSON.getString("rating");
                googlePlaceMap.put("rating",rating);
            }
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");


            googlePlaceMap.put("place_name",nameOfPlace);
            googlePlaceMap.put("vicinity",vicinity);
            googlePlaceMap.put("photo_reference",photo_reference);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    private List<HashMap<String,String>> getAllNearbyPlaces(JSONArray jsonArray){
        int counter = jsonArray.length();

        List<HashMap<String,String>> nearbyPlacesList = new ArrayList<>();

        HashMap<String,String> nearbyPlaceMap;

        for (int i=0;i<counter;i++){
            try {
                nearbyPlaceMap = getNearbyPlace((JSONObject) jsonArray.get(i));
                nearbyPlacesList.add(nearbyPlaceMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nearbyPlacesList;
    }

    public List<HashMap<String,String>> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert jsonArray != null;
        return  getAllNearbyPlaces(jsonArray);
    }
}
