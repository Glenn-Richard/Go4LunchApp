package MVVM;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

import api.ApiClient;
import api.DetailsPlaces;
import api.GoogleMapAPI;
import api.JSONResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitRepository {


    private static GoogleMapAPI googleMapAPI = null;
    private static RetrofitRepository newRepository;

    public static RetrofitRepository getInstance(){
        if (newRepository == null){
            newRepository = new RetrofitRepository();
        }
        return newRepository;
    }
    public RetrofitRepository(){
        googleMapAPI = ApiClient.getInterface();
    }


    public LiveData<JSONResponse> getPlaceResultsLiveData(String location) {
        MutableLiveData <JSONResponse> PlaceResultsMutableLiveData = new MutableLiveData<>();
        String typ = "restaurant";
        String apiKey = "AIzaSyDu_b1WHImUbpk593yksTIpdcJJ3JjVwVY";
        // Put apiKey in private gradle file
        int rad = 1000;
        Call<JSONResponse> placeResultsCall = googleMapAPI.getRestaurants(location, rad, typ, apiKey);
        placeResultsCall.enqueue(new Callback<JSONResponse>() {
                                     @Override
                                     public void onResponse(@NonNull Call<JSONResponse> call, @NonNull Response<JSONResponse> response) {
                                         PlaceResultsMutableLiveData.setValue(response.body());
                                     }

                                     @Override
                                     public void onFailure(@NonNull Call<JSONResponse> call, @NonNull Throwable t) {
                                         PlaceResultsMutableLiveData.setValue(null);
                                     }
                                 });

        return PlaceResultsMutableLiveData;
    }

    public LiveData<DetailsPlaces> getRestaurantsDetails(String place_id){
        MutableLiveData<DetailsPlaces> detailsPlacesMutableLiveData = new MutableLiveData<>();
        String fields = "formatted_phone_number,website";
        String apiKey = "AIzaSyDu_b1WHImUbpk593yksTIpdcJJ3JjVwVY";

        Call<DetailsPlaces> detailsPlacesCall = googleMapAPI.getDetailsPlaces(place_id,fields,apiKey);
        detailsPlacesCall.enqueue(new Callback<DetailsPlaces>() {
            @Override
            public void onResponse(@NonNull Call<DetailsPlaces> call, @NonNull Response<DetailsPlaces> response) {
                detailsPlacesMutableLiveData.setValue(response.body());
                Log.d("DETAILS_ARRAY_SIZE: ", String.valueOf(Objects.requireNonNull(Objects.requireNonNull(response.body()).getRestaurantsDetails())));
            }

            @Override
            public void onFailure(@NonNull Call<DetailsPlaces> call, @NonNull Throwable t) {
                detailsPlacesMutableLiveData.setValue(null);
                Log.e("DetailsPlace",t.getLocalizedMessage());
            }
        });
        return detailsPlacesMutableLiveData;
    }
}
