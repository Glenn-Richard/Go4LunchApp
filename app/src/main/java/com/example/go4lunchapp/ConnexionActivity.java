package com.example.go4lunchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ConnexionActivity extends AppCompatActivity {

    private Button btn_facebook;
    private Button btn_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        btn_facebook = findViewById(R.id.btn_connexion_facebook);
        btn_google = findViewById(R.id.btn_connexion_google);
    }
}