package com.example.go4lunchapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.go4lunchapp.databinding.ActivityMainBinding;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import api.ApiClient;
import api.ApiInterface;
import api.JSONResponse;
import fragments.MapFragment;
import fragments.RestaurantsFragment;
import fragments.WorkmatesFragment;
import models.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    ActivityMainBinding binding;

    DrawerLayout drawerLayout;
    TextView drawer_name;
    ImageView drawer_photo;
    View header;

    ActionBarDrawerToggle toggle;

    FirebaseAuth mAuth;
    FirebaseUser user;
    private SupportMapFragment mapFragment;

    private FusedLocationProviderClient client;

    private GoogleMap gMap;

    private Double lat;
    private Double lng;

    List<Restaurant> restaurants = new ArrayList<>();

    protected static final int REQUEST_CODE = 44;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);
        drawer_name = header.findViewById(R.id.currentUser_name);
        drawer_photo = header.findViewById(R.id.currentUser_photo);

        setUserInformation();

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new MapFragment());

        setMapFragment();


        binding.bottomNavbar.setOnItemSelectedListener(item -> {
            selectItem(item);
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void setUserInformation(){
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            drawer_name.setText(user.getDisplayName());
            drawer_photo.setImageURI(user.getPhotoUrl());
        } else {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }
    }
    private void setMapFragment(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map_fragment, mapFragment).commit();
        }

        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("NonConstantResourceId")
    private void selectItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.map_menu:
                replaceFragment(new MapFragment());
                requirePermission();
                setMapFragment();
                break;

            case R.id.restaurants_menu:
                requirePermission();
                replaceFragment(new RestaurantsFragment());
                break;

            case R.id.workmates_menu:
                replaceFragment(new WorkmatesFragment());
                break;
        }

    }
    @SuppressLint("NonConstantResourceId")
    private void drawerItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.your_lunch:
                Toast.makeText(getApplicationContext(),"Your lunch",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                    Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    connexionActivity();
                    finish();
                break;
        }
    }
    private void connexionActivity(){
        Intent intent = new Intent(MainActivity.this,ConnexionActivity.class);
        startActivity(intent);
    }
    private void requirePermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                getCurrentLocation();
            }
        } else {
            ActivityCompat
                    .requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        client.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                mapFragment.getMapAsync(googleMap -> {
                    gMap = googleMap;
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    LatLng latLng = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_man_4_24))
                            .title("Vous êtes ici");
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                    googleMap.addMarker(markerOptions);

                    displayNearbyPlaces();
                });
            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void sendDataToRestaurantFragment(List<Restaurant> dataList, double lat, double lng) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurants", (Serializable) dataList);
        bundle.putSerializable("lat", lat);
        bundle.putSerializable("lng", lng);

        getSupportFragmentManager().setFragmentResult("restaurants", bundle);

    }
    private void setMarkerWithData(){
        for (int i = 0; i < restaurants.size() - 1; i++) {
            MarkerOptions markerOptions = new MarkerOptions();

            Restaurant restaurant = restaurants.get(i);
            String nameOfPlace = restaurant.getName();
            boolean opening = Boolean.parseBoolean(null);
            if (!(restaurant.getOpening_hours() == null)){
                opening = restaurant.getOpening_hours().getOpen_now();
            }
            double lat = restaurant.getGeometry().getLocation().getLat();
            double lng = restaurant.getGeometry().getLocation().getLng();

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            if (opening) {
                String open = "Open";
                markerOptions.title(nameOfPlace + " " + open);
                markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_lunch_dining_green_24));
            } else {
                String close = "Close";
                markerOptions.title(nameOfPlace + " " + close);
                markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_lunch_dining_red_24));
            }
            gMap.addMarker(markerOptions);
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }

    private void displayNearbyPlaces() {


        String location = lat.toString() + "," + lng.toString();
        int radius = 1000;
        String type = "restaurant";
        String key = "AIzaSyDu_b1WHImUbpk593yksTIpdcJJ3JjVwVY";
        Retrofit client = ApiClient.getClient("https://maps.googleapis.com/maps/api/place/");
        ApiInterface apiInterface = client.create(ApiInterface.class);

        Call<JSONResponse> call = apiInterface.getRestaurants(location, radius, type, key);

        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(@NonNull Call<JSONResponse> call, @NonNull Response<JSONResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Response failed", Toast.LENGTH_SHORT).show();
                }else{
                    JSONResponse jsonResponse = Objects.requireNonNull(response.body());
                    restaurants = new ArrayList<>(Objects.requireNonNull(jsonResponse).getRestaurants());
                    setMarkerWithData();
                    sendDataToRestaurantFragment(restaurants,lat,lng);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JSONResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Reading error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requirePermission();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        requirePermission();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerItemSelected(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return true;
    }

}