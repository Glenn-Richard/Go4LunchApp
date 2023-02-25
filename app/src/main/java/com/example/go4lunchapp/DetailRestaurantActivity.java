package com.example.go4lunchapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import MVVM.GeneralViewModel;
import models.Restaurant;
import models.User;

public class DetailRestaurantActivity extends AppCompatActivity{

    ImageView photo, call, favorite, website;
    TextView name;
    TextView address;

    FloatingActionButton fab;

    String website_reference;
    String phone;
    String rating;

    private GeneralViewModel viewModel;

    Intent intent;
    Restaurant restaurant;
    User currentUser;


    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Détails");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name_restaurant);
        address = findViewById(R.id.address_restaurant);
        photo = findViewById(R.id.photo_restaurant);
        fab = findViewById(R.id.fab);

        call = findViewById(R.id.call_icon);
        favorite = findViewById(R.id.favorites_icon);
        website = findViewById(R.id.website_icon);

        intent = getIntent();

        catchRestaurantIntent();

        getCurrentUser();

        isFavorite(restaurant);
        isSelected(restaurant);

        fab.setOnClickListener(view -> {
            initViewModel();
            viewModel.setChoice(restaurant)
                        .observe(this, aBoolean -> {
                            if (aBoolean){
                                fab.setImageResource(R.drawable.ic_baseline_check_circle_green_24);
                                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.green));
                                fab.setBackgroundTintList(colorStateList);
                            }else{
                                fab.setImageResource(R.drawable.ic_baseline_lunch_dining_green_24);
                                ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.yellow));
                                fab.setBackgroundTintList(colorStateList);
                            }
                            initViewModel();
                            viewModel.getWorkmatesCount(restaurant);
                        });
        });

        call.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_DIAL);
            intent1.setData(Uri.parse("tel:" + phone));
            startActivity(intent1);
        });
        favorite.setOnClickListener(view -> {
            initViewModel();
            viewModel.setFavorite(restaurant)
                    .observe(this, aBoolean -> {
                        if (aBoolean){
                            favorite.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
                        }else{

                            favorite.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
                        }
                    });
        });
        website.setOnClickListener(view -> {
            if (website_reference.contains("null")) {
                Toast.makeText(getApplicationContext(), "Pas de site renseigné", Toast.LENGTH_SHORT).show();
            } else {
                getWebSite(website_reference);
            }
        });

    }
    private void getCurrentUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        initViewModel();
        viewModel.getCurrentUser(Objects.requireNonNull(user1).getUid())
                .observe(this, user -> currentUser = user);
    }

    private  void isSelected(Restaurant restaurant){
        initViewModel();
        viewModel.isSelected(restaurant)
                .observe(this, aBoolean -> {
                    if (aBoolean){
                        fab.setImageResource(R.drawable.ic_baseline_check_circle_green_24);
                        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.green));
                        fab.setBackgroundTintList(colorStateList);
                    }else{
                        fab.setImageResource(R.drawable.ic_baseline_lunch_dining_green_24);
                        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.yellow));
                        fab.setBackgroundTintList(colorStateList);
                    }
                });
    }
    private void isFavorite(Restaurant restaurant){
        initViewModel();
        viewModel.isFavorite(restaurant)
                .observe(this, aBoolean -> {
                    if (aBoolean) {
                        favorite.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
                        viewModel.updateFavoritesUser(currentUser, "favorites", restaurant.getPlace_id());
                    } else {
                        favorite.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
                        viewModel.deleteFavoritesField(currentUser,"favorites",restaurant.getPlace_id());
                    }
                });

    }

    private void getCurrentRestaurant(String nameR,String addressR,String place_id,String phoneR,String rating){

        restaurant = new Restaurant();
        restaurant.setName(nameR);
        restaurant.setAddress(addressR);
        restaurant.setPlace_id(place_id);
        restaurant.setPhone(phoneR);
        restaurant.setRating(Double.parseDouble(rating));
    }
    private void catchRestaurantIntent(){

        String nameR = (String) intent.getCharSequenceExtra("name");
        name.setText(nameR);
        String addressR = (String) intent.getCharSequenceExtra("address");
        address.setText(addressR);
        String place_id = (String) intent.getCharSequenceExtra("place_id");
        website_reference = (String) intent.getCharSequenceExtra("website");

        String photoR = null;
        if (!(intent.getIntExtra("photo", 0) == R.mipmap.unavailable_image)) {
            photoR = String.valueOf(intent.getCharSequenceExtra("photo"));
            Glide.with(this)
                    .load(getUrl(photoR))
                    .centerCrop()
                    .into(photo);

        }
        phone = String.valueOf(intent.getCharSequenceExtra("phone"));
        rating = String.valueOf(intent.getDoubleExtra("rating",0));
        getCurrentRestaurant(nameR,addressR,place_id,photoR,rating);

    }
    private  void initViewModel(){
        viewModel = new GeneralViewModel();
        viewModel = new ViewModelProvider(this).get(GeneralViewModel.class);
    }
    private String getUrl(String url) {
        String way = "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=400" +
                "&photo_reference=";
        String key_api = "&key=AIzaSyDu_b1WHImUbpk593yksTIpdcJJ3JjVwVY";
        way += url + key_api;

        return way;
    }

    private void getWebSite(String url) {
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(launchWeb);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
