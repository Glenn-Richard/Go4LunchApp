package com.example.go4lunchapp;

import static android.content.ContentValues.TAG;
import static android.provider.ContactsContract.Intents.Insert.EMAIL;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Collections;
import java.util.Objects;

import MVVM.GeneralViewModel;

public class ConnexionActivity extends AppCompatActivity {

    private static final int REQ_ONE_TAP = 2;

    private CallbackManager callbackManager;

    private GoogleSignInClient client;
    private SignInClient oneTapClient;

    private FirebaseAuth auth;

    private GeneralViewModel viewModel;

    LoginButton btn_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        btn_facebook = findViewById(R.id.btn_connexion_facebook);
        Button btn_google = findViewById(R.id.btn_connexion_google);

        auth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        setViewModel();

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        btn_facebook.setReadPermissions(EMAIL);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        btn_facebook.setOnClickListener(view -> facebookAuth());

        btn_google.setOnClickListener(view -> signIn());
    }
    private void setViewModel(){
        viewModel = new GeneralViewModel();
        viewModel = new ViewModelProvider(this).get(GeneralViewModel.class);
    }
    private void oneTapSetter(){
        oneTapClient = Identity.getSignInClient(this);

        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .setAutoSelectEnabled(true)
                .build();
        showOneTap(signInRequest);
    }
    private void showOneTap(BeginSignInRequest signUpRequest){
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(this, e -> Log.d(TAG, e.getLocalizedMessage()));
    }
    private void facebookAuth(){
        btn_facebook.setPermissions("email", "public_profile");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(),"Connexion réussie",Toast.LENGTH_SHORT).show();
                        firebaseAuthWithGoogle(loginResult.getAccessToken().getUserId());
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
                .requestIdToken(String.valueOf(R.string.default_web_client_id))
                .requestEmail()
                .build();

        client = GoogleSignIn.getClient(this, options);
    }
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    String password = credential.getPassword();
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken);
                        Log.d(TAG, "Got ID token.");
                    } else if (password != null) {
                        Log.d(TAG, "Got password.");
                    }
                } catch (ApiException e) {
                    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                }
            });

    private void signIn() {
        google_request();
        oneTapSetter();
        Intent signInIntent = client.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }
    private void mainActivityForResult(String id){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    private void facebookLoginManager(){
        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singletonList("public_profile"));
    }

    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = auth.getCurrentUser();
                        viewModel.userAlreadyExist(user);
                        mainActivityForResult(Objects.requireNonNull(user).getUid());

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.e("AUTH_EXCEPTION",e.toString()));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            try {

                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                String password = credential.getPassword();
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                    Log.d(TAG, "Got ID token.");
                } else if (password != null) {
                    Log.d(TAG, "Got password.");
                }
            } catch (ApiException e) {
                Log.d(TAG, "One-tap dialog was closed.");
            }
        }
    }
}