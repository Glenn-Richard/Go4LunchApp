package fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.go4lunchapp.MainActivity;
import com.example.go4lunchapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import utils.DataParser;
import utils.DownloadUrl;


public class MapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private SupportMapFragment mapFragment;

    private FusedLocationProviderClient client;

    private GoogleMap gMap;

    private Double lat;
    private Double lng;


    protected static final int REQUEST_CODE = 44;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment==null){
            FragmentManager fragmentManager = getFragmentManager();
            assert fragmentManager != null;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map_fragment,mapFragment).commit();
        }

        client = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapFragment.getMapAsync(this);
        getCurrentLocation();

        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                client.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if(location!=null){
                        mapFragment.getMapAsync(googleMap -> {
                            gMap = googleMap;
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_baseline_man_4_24))
                                    .title("Vous êtes ici");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                            googleMap.addMarker(markerOptions);

                            getNearbyRestaurants(lat,lng);
                        });
                    }
                });
            }
        }else{
            ActivityCompat
                    .requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }
    private String getUrl(double lat,double lng,String restaurant){
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=").append(lat).append(",").append(lng);
        int radiusSearch = 1000;
        googleUrl.append("&radius=").append(radiusSearch);
        googleUrl.append("&type=").append(restaurant);
        googleUrl.append("&sensor=true");
        googleUrl.append("&key=").append(getString(R.string.google_place_key));

        Log.d("GoogleMapFragment","url = "+ googleUrl);

        return googleUrl.toString();
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void getNearbyRestaurants(double latitude,double longitude){

        Toast.makeText(getContext(),"Searching for nearby restaurants...",Toast.LENGTH_SHORT).show();

        String restaurant = "restaurant";
        Object[] transferData = new Object[3];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        String url = getUrl(latitude,longitude,restaurant);
        transferData[0] = gMap;
        transferData[1] = url;

        Toast.makeText(getContext(),"Showing nearby restaurants",Toast.LENGTH_SHORT).show();
        getNearbyPlaces.execute(transferData);
    }
    private void sendDataToRestaurantFragment(List<HashMap<String, String>> dataList,double lat,double lng){

        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurants", (Serializable) dataList);
        bundle.putSerializable("lat",lat);
        bundle.putSerializable("lng",lng);

        getParentFragmentManager().setFragmentResult("restaurants",bundle);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

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

            for (int i=0;i<nearbyPlacesList.size()-1;i++){
                MarkerOptions markerOptions = new MarkerOptions();

                HashMap<String,String> googleNearbyPLace = nearbyPlacesList.get(i);
                String nameOfPlace = googleNearbyPLace.get("place_name");
                String vicinity = googleNearbyPLace.get("vicinity");
                boolean opening = false;
                if(!googleNearbyPLace.containsValue("opening_hours")){
                    opening = Boolean.parseBoolean(googleNearbyPLace.get("opening_hours"));
                }
                double lat = Double.parseDouble((Objects.requireNonNull(googleNearbyPLace.get("lat"))));
                double lng = Double.parseDouble((Objects.requireNonNull(googleNearbyPLace.get("lng"))));

                sendDataToRestaurantFragment(nearbyPlacesList,lat,lng);

                LatLng latLng = new LatLng(lat,lng);

                markerOptions.position(latLng);
                if (opening){
                    String open = "Open";
                    markerOptions.title(nameOfPlace+" "+open);
                    markerOptions.icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_baseline_lunch_dining_green_24));
                }else{
                    String close = "Close";
                    markerOptions.title(nameOfPlace+" "+close);
                    markerOptions.icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_baseline_lunch_dining_red_24));
                }
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }
        }
    }
}