package fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.go4lunchapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import adapter.RestaurantFragmentAdapter;
import models.Restaurant;

public class RestaurantsFragment extends Fragment {

    RecyclerView recyclerView;

    private final List<Restaurant> restaurants = new ArrayList<>();


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);

        getDataFromMapFragment();

        recyclerView = view.findViewById(R.id.restaurant_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RestaurantFragmentAdapter adapter = new RestaurantFragmentAdapter(getContext(), (ArrayList<Restaurant>) restaurants);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }
    private List<Restaurant> getDataFromMapFragment(){

        getParentFragmentManager().setFragmentResultListener("restaurants", this, (requestKey, result) -> {

            ArrayList data = (ArrayList) result.getSerializable("restaurants");

            convertDataToRestaurantList(data);
        });
        return restaurants;
    }
    private void convertDataToRestaurantList(ArrayList data){
        for(int i=0;i< data.size()-1;i++){
            String name = Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("place_name")).toString();
            String address = Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("vicinity")).toString();
            String photo = Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("photo_reference")).toString();
            double lat = Double.parseDouble(Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("lat")).toString());
            double lng = Double.parseDouble(Objects.requireNonNull(((HashMap<?, ?>) data.get(i)).get("lng")).toString());

            Restaurant restaurant = new Restaurant();

            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setPhoto(photo);
            restaurant.getLat(lat);
            restaurant.getLng(lng);

            restaurants.add(restaurant);
        }
    }

}