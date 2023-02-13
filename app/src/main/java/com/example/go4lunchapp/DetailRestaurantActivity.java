package com.example.go4lunchapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class DetailRestaurantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView photo;
    TextView name;
    TextView address;

    String website;
    String phone;
    String rating;

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        setTitle("Détails");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name_restaurant);
        address = findViewById(R.id.address_restaurant);
        photo = findViewById(R.id.photo_restaurant);

        bottomNavigationView = findViewById(R.id.menu_details);

        Intent intent = getIntent();

        String nameR = String.valueOf(intent.getCharSequenceExtra("name"));
        name.setText(nameR);
        String addressR = String.valueOf(intent.getCharSequenceExtra("address"));
        address.setText(addressR);
        String photoR = String.valueOf(intent.getCharSequenceExtra("photo"));
        Glide.with(this)
                .load(getUrl(photoR))
                .centerCrop()
                .into(photo);
        website = String.valueOf(intent.getCharSequenceExtra("website"));
        phone = String.valueOf(intent.getCharSequenceExtra("phone"));
        rating = String.valueOf(intent.getCharSequenceExtra("rating"));

        bottomNavigationView.setOnItemSelectedListener(item -> {
            selectItem(item);
            return true;
        });
    }

    private String getUrl(String url){
        String way = "https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=400" +
                "&photo_reference=";
        String key_api = "&key=AIzaSyDu_b1WHImUbpk593yksTIpdcJJ3JjVwVY";
        way += url+key_api;

        return way;
    }
    private void getWebSite(String url){
        Uri uri = Uri.parse(url);
        Intent launchWeb = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(launchWeb);
    }
    @SuppressLint("NonConstantResourceId")
    private void selectItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
                break;

            case R.id.favori:
                break;

            case R.id.website:
                if (website.contains("null")){
                    Toast.makeText(this, "Pas de site renseigné", Toast.LENGTH_SHORT).show();
                }else{
                    getWebSite(website);
                }
                break;
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}