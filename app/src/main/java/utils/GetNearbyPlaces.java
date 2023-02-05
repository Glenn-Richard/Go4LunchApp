package utils;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GetNearbyPlaces extends AsyncTask<Object,String,String> {

    private String googlePlaceData;
    private GoogleMap googleMap;
    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        String url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlaceData = downloadUrl.readTheUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlacesList;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);

        displayNearbyPlaces(nearbyPlacesList);
    }
    private void displayNearbyPlaces(List<HashMap<String,String>> nearbyPlacesList){
        for (int i=0;i<nearbyPlacesList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String,String> googleNearbyPLace = nearbyPlacesList.get(i);
            String nameOfPlace = googleNearbyPLace.get("place_name");
            String vicinity = googleNearbyPLace.get("vicinity");
            double lat = Double.parseDouble(Objects.requireNonNull(googleNearbyPLace.get("lat")));
            double lng = Double.parseDouble(Objects.requireNonNull(googleNearbyPLace.get("lng")));

            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace+" : "+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));


        }
    }
}
