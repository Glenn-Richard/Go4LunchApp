package fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunchapp.DetailRestaurantActivity;
import com.example.go4lunchapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import MVVM.GeneralViewModel;
import adapter.RestaurantFragmentAdapter;
import models.Restaurant;
import utils.ItemListener;

public class RestaurantsFragment extends Fragment implements ItemListener{


    RecyclerView recyclerView;

    private double lat;
    private double lng;

    Intent intent;

    private List<Restaurant> restaurants = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        getDataFromMainActivity(view);

        intent = new Intent(getActivity(), DetailRestaurantActivity.class);


        return view;

    }
    private void getDataFromMainActivity(View view){

        getParentFragmentManager().setFragmentResultListener("restaurants", this, (requestKey, result) -> {

            List<Restaurant> data = (List<Restaurant>) result.getSerializable("restaurants");
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
    private void launchDetailsActivity(Restaurant restaurant){

        intent.putExtra("name", restaurant.getName());
        intent.putExtra("place_id",restaurant.getPlace_id());
        intent.putExtra("geometry",restaurant.getGeometry());
        intent.putExtra("address", restaurant.getAddress());
        if (!(restaurant.getPhoto()==null)){
            intent.putExtra("photo", restaurant
                    .getPhoto()
                    .get(0)
                    .getPhotoReference());
        }else{
            intent.putExtra("photo", R.mipmap.unavailable_image);
        }
        if (!(restaurant.getOpening_hours()==null)){
            intent.putExtra("opening", restaurant.getOpening_hours().getOpen_now());
        }
        intent.putExtra("phone", restaurant.getPhone());
        intent.putExtra("website", restaurant.getWebsite());
        intent.putExtra("rating", restaurant.getRating());

            startActivity(intent);

    }
    @Override
    public void onItemClicked(Restaurant restaurant) {

        new ViewModelProvider(this)
                .get(GeneralViewModel.class)
                .getDetailsPlaceLiveData(restaurant.getPlace_id())
                .observe(getViewLifecycleOwner(), detailsPlaces -> {
                    restaurant.setPhone(detailsPlaces.getRestaurantsDetails().getPhone());
                    restaurant.setWebsite(detailsPlaces.getRestaurantsDetails().getWebsite());
                    launchDetailsActivity(restaurant);
                });
    }

}