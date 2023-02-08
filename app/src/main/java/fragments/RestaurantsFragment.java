package fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunchapp.DetailRestaurantActivity;
import com.example.go4lunchapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import adapter.RestaurantFragmentAdapter;
import models.Restaurant;
import utils.ItemListener;

public class RestaurantsFragment extends Fragment implements ItemListener{

    protected static final int REQUEST_CODE = 44;

    protected static final int REQUEST_CODE_2 = 45;

    RecyclerView recyclerView;

    private double lat;
    private double lng;

    private static final List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantsFragment() {
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        getDataFromMapFragment();

        recyclerView = view.findViewById(R.id.restaurant_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RestaurantFragmentAdapter adapter = new RestaurantFragmentAdapter(getContext(), (ArrayList<Restaurant>) restaurants, (ItemListener) this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }
    private void getDataFromMapFragment(){

        getParentFragmentManager().setFragmentResultListener("restaurants", this, (requestKey, result) -> {

            ArrayList<Object> data = (ArrayList<Object>) result.getSerializable("restaurants");
            double latitude = (double) result.getSerializable("lat");
            double longitude = (double) result.getSerializable("lng");
            lat = latitude;
            lng = longitude;
            if(restaurants.isEmpty()||(latitude!=lat&&longitude!=lng)){
                convertDataToRestaurantList(data,latitude,longitude);
            }if (restaurants.size()<0 && (!data.isEmpty())){
                restaurants.clear();
            }

        });
    }

    public float calculationByDistance(LatLng StartP, LatLng EndP) {

        float[] result = new float[1];

        Location.distanceBetween(StartP.latitude,StartP.longitude,EndP.latitude,EndP.longitude,result);

        return result[0];
    }
    private void convertDataToRestaurantList(ArrayList<Object> data,double currentLat,double currentLng){

        for(int i=0;i< data.size()-1;i++){

            HashMap<String,String> object = (HashMap<String, String>) data.get(i);

            Restaurant restaurant = new Restaurant();

            String name = Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("place_name")).toString();
            String address = Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("vicinity")).toString();
            String photo = object.get("photo_reference");

            if (object.containsKey("website")){
                String website = object.get("website");
                restaurant.setWebsite(website);
            }
            if (object.containsKey("rating")){
                String rating = object.get("rating");
                restaurant.setRating(Double.parseDouble(Objects.requireNonNull(rating)));
            }
            if (object.containsKey("formatted_phone_number")){
                String phone = object.get("formatted_phone_number");
                restaurant.setWebsite(phone);
            }
            if (object.containsKey("opening_hours")){
                boolean opening = Boolean.parseBoolean(Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("opening_hours")).toString());
                restaurant.setOpening(opening);
            }else{
                restaurant.setNo_opening_hours(true);
            }
            double latitude = Double.parseDouble(Objects.requireNonNull(object.get("lat")));
            double longitude = Double.parseDouble(Objects.requireNonNull(object.get("lng")));

            LatLng latLng = new LatLng(currentLat,currentLng);

            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setPhoto(photo);
            restaurant.setLat(latitude);
            restaurant.setLng(longitude);
            getDistance(latLng,restaurant);


            restaurants.add(restaurant);
        }
    }
    private void getDistance(LatLng latLng,Restaurant restaurant){

            LatLng latLngRestaurant = new LatLng(restaurant.getLat(),restaurant.getLng());

            restaurant.setDistance_of_current_location(calculationByDistance(latLngRestaurant,latLng));

    }
    private void sendDataToRestaurantFragment(List<HashMap<String, String>> dataList){

        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurants", (Serializable) dataList);

        getParentFragmentManager().setFragmentResult("restaurants",bundle);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            }
        }
        if (requestCode==REQUEST_CODE_2){
            Toast.makeText(getContext(), "Restaurants", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClicked(Restaurant restaurant) {
        Intent intent = new Intent(getActivity(), DetailRestaurantActivity.class);
        intent.putExtra("name", restaurant.getName());
        intent.putExtra("address", restaurant.getAddress());
        intent.putExtra("photo", restaurant.getPhoto());
        intent.putExtra("opening", restaurant.isOpening());
        intent.putExtra("phone", restaurant.getPhone());
        intent.putExtra("website", restaurant.getWebsite());
        intent.putExtra("rating", restaurant.getRating());

        startActivityForResult(intent,REQUEST_CODE_2);
    }

}