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
        String photo_reference;
        JSONArray photos;
        String latitude;
        String longitude;

        try {
            if (!googlePlaceJSON.isNull("name")){
                nameOfPlace = googlePlaceJSON.getString("name");
            }
            if (!googlePlaceJSON.isNull("vicinity")){
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            photos = googlePlaceJSON.getJSONArray("photos");
            photo_reference = photos.optJSONObject(0).getString("photo_reference");
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
