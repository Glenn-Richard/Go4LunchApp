package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunchapp.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import MVVM.FirebaseRepository;
import MVVM.GeneralViewModel;
import models.Restaurant;
import pl.droidsonroids.gif.GifImageView;
import utils.ItemListener;

public class RestaurantFragmentAdapter extends  RecyclerView.Adapter<RestaurantFragmentAdapter.RestaurantViewHolder>{

    Context context;
    ArrayList<Restaurant> restaurants;
    ItemListener listener;
    LatLng currentLocation;
    ViewModel viewModel;

    public RestaurantFragmentAdapter(Context context, ArrayList<Restaurant> restaurants,ItemListener listener,LatLng currentLocation) {
        this.context = context;
        this.restaurants = restaurants;
        this.listener = listener;
        this.currentLocation = currentLocation;
    }

    private String getUrl(String url){
        String way = "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=400" +
                "&photo_reference=";
        String key_api = "&key=AIzaSyDu_b1WHImUbpk593yksTIpdcJJ3JjVwVY";
        way += url+key_api;

        return way;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new RestaurantViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Restaurant restaurant = restaurants.get(position);

        holder.cache.setVisibility(View.INVISIBLE);
        holder.progress_image.setVisibility(View.VISIBLE);

        holder.name.setText(restaurant.getName());
        holder.address.setText(restaurant.getAddress());

        if (!(restaurant.getPhoto()==null)){
            Glide.with(context)
                    .load(getUrl(restaurant.getPhoto().get(0).getPhotoReference()))
                    .centerCrop()
                    .into(holder.photo_restaurant);

        }else{
            Glide.with(context)
                    .load(R.mipmap.unavailable_image)
                    .centerCrop()
                    .into(holder.photo_restaurant);
        }
        double lat = restaurant.getGeometry().getLocation().getLat();
        double lng = restaurant.getGeometry().getLocation().getLng();

        LatLng restaurantLocation = new LatLng(lat,lng);
        holder.distance.setText((int) calculationByDistance(currentLocation, restaurantLocation) +"m");

        if (restaurant.getOpening_hours()==null){
            holder.opening.setText("Aucune informations");
            holder.opening.setTextColor(ContextCompat.getColor(context, R.color.pas_d_info));
        }
        else if (!restaurant.getOpening_hours().getOpen_now()) {
            holder.opening.setText("Fermé");
            holder.opening.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else{
            holder.opening.setText("Ouvert");
            holder.opening.setTextColor(ContextCompat.getColor(context, R.color.green));
        }
        holder.ratingBar.setRating((float) restaurant.getRating());

        new ViewModelProvider((ViewModelStoreOwner) context).get(GeneralViewModel.class)
                .getWorkmatesCount(restaurant)
                .observe((LifecycleOwner) context, integer -> {
                    if (restaurant.getNumber_of_workmates()==0){
                        holder.count.setVisibility(View.GONE);
                        holder.workmates_number.setVisibility(View.GONE);
                    }else{
                        holder.count.setVisibility(View.VISIBLE);
                        holder.workmates_number.setVisibility(View.VISIBLE);
                        holder.count.setText("("+restaurant.getNumber_of_workmates()+")");
                    }
                });

        holder.cache.setVisibility(View.VISIBLE);
        holder.progress_image.setVisibility(View.INVISIBLE);

        holder.container.setOnClickListener(view -> listener.onItemClicked(restaurant));


    }
    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{

        TextView name,address,opening,distance,count;
        ImageView photo_restaurant,workmates_number;
        RatingBar ratingBar;
        ConstraintLayout container,cache;
        GifImageView progress_image;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            address = itemView.findViewById(R.id.restaurant_address);
            opening = itemView.findViewById(R.id.opening);
            distance = itemView.findViewById(R.id.distance);
            count = itemView.findViewById(R.id.count);

            photo_restaurant = itemView.findViewById(R.id.restaurant_photo);
            ratingBar = itemView.findViewById(R.id.simpleRatingBar);
            workmates_number = itemView.findViewById(R.id.workmates_number);

            container = itemView.findViewById(R.id.item_view);
            cache = itemView.findViewById(R.id.list_container);

            progress_image = itemView.findViewById(R.id.progress_bar_restaurant);
        }
    }
    public float calculationByDistance(LatLng StartP, LatLng EndP) {

        float[] result = new float[1];

        Location.distanceBetween(StartP.latitude,StartP.longitude,EndP.latitude,EndP.longitude,result);

        return result[0];
    }

}
