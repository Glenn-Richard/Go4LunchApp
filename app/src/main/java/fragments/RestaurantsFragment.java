package fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunchapp.DetailRestaurantActivity;
import com.example.go4lunchapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.RestaurantFragmentAdapter;
import models.Restaurant;
import utils.ItemListener;

public class RestaurantsFragment extends Fragment implements ItemListener{

    protected static final int REQUEST_CODE_2 = 45;

    RecyclerView recyclerView;

    private double lat;
    private double lng;

    private List<Restaurant> restaurants = new ArrayList<>();


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle("Restaurants");

        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        getDataFromMapFragment(view);


        return view;

    }
    private void getDataFromMapFragment(View view){

        getParentFragmentManager().setFragmentResultListener("restaurants", this, (requestKey, result) -> {

            ArrayList<Restaurant> data = (ArrayList<Restaurant>) result.getSerializable("restaurants");
            double latitude = (double) result.getSerializable("lat");
            double longitude = (double) result.getSerializable("lng");
            lat = latitude;
            lng = longitude;

                restaurants.clear();
                restaurants = data;
                setRecyclerView(restaurants,view,lat,lng);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView(List<Restaurant> restaurants, View view, double lat, double lng){

        LatLng latLng = new LatLng(lat,lng);
        recyclerView = view.findViewById(R.id.restaurant_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RestaurantFragmentAdapter adapter = new RestaurantFragmentAdapter(getContext(), (ArrayList<Restaurant>) restaurants, this,latLng);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(Restaurant restaurant) {
        Intent intent = new Intent(getActivity(), DetailRestaurantActivity.class);
        intent.putExtra("name", restaurant.getName());
        intent.putExtra("address", restaurant.getAddress());
        intent.putExtra("photo", restaurant.getPhoto().get(0).getPhotoReference());
        intent.putExtra("opening", restaurant.isOpening());
        intent.putExtra("phone", restaurant.getPhone());
        intent.putExtra("website", restaurant.getWebsite());
        intent.putExtra("rating", restaurant.getRating());

        startActivityForResult(intent,REQUEST_CODE_2);
    }

}