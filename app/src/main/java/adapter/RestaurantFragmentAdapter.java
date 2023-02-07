package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.go4lunchapp.R;

import java.util.ArrayList;

import models.Restaurant;

public class RestaurantFragmentAdapter extends  RecyclerView.Adapter<RestaurantFragmentAdapter.RestaurantViewHolder>{

    Context context;
    ArrayList<Restaurant> restaurants;

    public RestaurantFragmentAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
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

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {

        Restaurant restaurant = restaurants.get(position);

        holder.name.setText(restaurant.getName());
        holder.address.setText(restaurant.getAddress());
        Glide.with(context)
                .load(getUrl(restaurant.getPhoto()))
                .into(holder.photo_restaurant);

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{

        TextView name,address,opening,distance,workmates_number;
        ImageView photo_restaurant,account,star1,star2,star3;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            address = itemView.findViewById(R.id.restaurant_address);
            opening = itemView.findViewById(R.id.opening);
            distance = itemView.findViewById(R.id.distance);
            workmates_number = itemView.findViewById(R.id.count);

            photo_restaurant = itemView.findViewById(R.id.restaurant_photo);
            account = itemView.findViewById(R.id.workmates_number);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
        }
    }

}
