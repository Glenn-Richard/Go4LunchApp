package com.example.go4lunchapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.go4lunchapp.databinding.ActivityMainBinding;

import fragments.MapFragment;
import fragments.RestaurantsFragment;
import fragments.WorkmatesFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new MapFragment());

        binding.bottomNavbar.setOnItemSelectedListener(item -> {
            selectItem(item);
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    private void selectItem(MenuItem item){
        switch (item.getItemId()){
            case R.id.map_menu:
                replaceFragment(new MapFragment());
                break;

            case R.id.restaurants_menu:
                replaceFragment(new RestaurantsFragment());
                break;

            case R.id.workmates_menu:
                replaceFragment(new WorkmatesFragment());
                break;
        }
    }
}