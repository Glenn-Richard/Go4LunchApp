package com.example.go4lunchapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.go4lunchapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import fragments.MapFragment;
import fragments.RestaurantsFragment;
import fragments.WorkmatesFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;

    DrawerLayout drawerLayout;
    TextView drawer_name;

    ActionBarDrawerToggle toggle;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.drawer_layout);
        drawer_name = findViewById(R.id.currentUser_name);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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
        fragmentTransaction.addToBackStack(null);
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
                Toast.makeText(getApplicationContext(),"Log out",Toast.LENGTH_SHORT).show();
                break;
        }
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