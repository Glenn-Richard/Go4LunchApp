package com.example.go4lunchapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ConnexionActivity extends AppCompatActivity {

    private final static int RC_SIGNIN = 100;
    //Facebook Auth values
    private CallbackManager callbackManager;

    private GoogleSignInClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        Button btn_facebook = findViewById(R.id.btn_connexion_facebook);
        Button btn_google = findViewById(R.id.btn_connexion_google);

        callbackManager = CallbackManager.Factory.create();

        google_request();

        btn_facebook.setOnClickListener(view -> facebookAuth());

        btn_google.setOnClickListener(view -> google_signIn());
    }
    private void facebookAuth(){
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(),"Connexion réussie",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(),"Connexion annulée",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(),exception.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void google_request(){
        //Google Auth values
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this, options);
    }
    private void google_signIn(){
        Intent signIn = client.getSignInIntent();
        startActivityForResult(signIn,RC_SIGNIN);
    }
    private void mainActivity() {
        startActivity(new Intent(ConnexionActivity.this,MainActivity.class));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN) {
            mainActivity();
        }
    }
}