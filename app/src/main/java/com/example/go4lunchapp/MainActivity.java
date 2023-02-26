package com.example.go4lunchapp;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunchapp.databinding.ActivityMainBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import MVVM.GeneralViewModel;
import fragments.RestaurantsFragment;
import fragments.WorkmatesFragment;
import models.Restaurant;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    ActivityMainBinding binding;

    DrawerLayout drawerLayout;
    TextView drawer_name,drawer_email,currentUser_id;
    ImageView drawer_photo;
    View header;
    FloatingActionButton fab;

    ActionBarDrawerToggle toggle;

    FirebaseUser user;

    GeneralViewModel viewModel;

    private SupportMapFragment mapFragment;

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

        requirePermission();

        initViewModel();



        drawerLayout = findViewById(R.id.drawer_layout);
        currentUser_id = findViewById(R.id.currentUser_id);

        NavigationView navigationView = findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);
        drawer_name = header.findViewById(R.id.currentUser_name);
        drawer_email = header.findViewById(R.id.currentUser_email);
        drawer_photo = header.findViewById(R.id.currentUser_photo);

        fab = binding.searchButton;

        setUserInformation();

        requirePermission();

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(setMapFragment());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Map");

        fab.setOnClickListener(view -> {
            initViewModel();
            viewModel.initializePlaces(lat,lng);
            List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(getApplicationContext());

            autocompleteLauncher.launch(intent);
        });
        binding.bottomNavbar.setOnItemSelectedListener(item -> {
            selectItem(item);
            return true;
        });
    }
    private final ActivityResultLauncher<Intent> autocompleteLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));
                    gMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(Objects.requireNonNull(place.getLatLng()));
                    if (Boolean.TRUE.equals(place.isOpen())) {
                        String open = "Open";
                        markerOptions.title(place.getName()+ " " + open);
                        markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_lunch_dining_green_24));
                    } else {
                        String close = "Close";
                        markerOptions.title(place.getName() + " " + close);
                        markerOptions.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_baseline_lunch_dining_red_24));
                    }
                    gMap.addMarker(markerOptions);
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                    Log.d(TAG, "Place selected: " + place.getName());
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Intent data = result.getData();
                    Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
                    Log.e(TAG, status.getStatusMessage());
                }
            });

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(GeneralViewModel.class);
    }
    private void setUserInformation(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            drawer_name.setText(user.getDisplayName());
            drawer_email.setText(user.getEmail());
            Glide.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .into(drawer_photo);
        } else {
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }
    }
    private SupportMapFragment setMapFragment(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
        }
        mapFragment.getMapAsync(this);
        return mapFragment;
    }
    @SuppressLint("NonConstantResourceId")
    private void selectItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.map_menu:
                fab.setVisibility(View.VISIBLE);
                requirePermission();
                replaceFragment(setMapFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle("Map");
                break;

            case R.id.restaurants_menu:
                fab.setVisibility(View.GONE);
                requirePermission();
                replaceFragment(new RestaurantsFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle("Restaurants");
                break;

            case R.id.workmates_menu:
                fab.setVisibility(View.GONE);
                replaceFragment(new WorkmatesFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle("Workmates");
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
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
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

    private void sendRestaurantsToRestaurantFragment(List<Restaurant> dataList, double lat, double lng) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurants", (Serializable) dataList);
        bundle.putSerializable("lat", lat);
        bundle.putSerializable("lng", lng);

        getSupportFragmentManager().setFragmentResult("restaurants", bundle);

    }
    private void setGoogleMarkers(){
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
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private void displayNearbyPlaces() {


        String location = lat.toString() + "," + lng.toString();
        initViewModel();
        viewModel.getPlaceResultLiveData(location).observe(this, jsonResponse -> {
            restaurants = new ArrayList<>(Objects.requireNonNull(jsonResponse).getRestaurants());
            setGoogleMarkers();
            sendRestaurantsToRestaurantFragment(restaurants,lat,lng);

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