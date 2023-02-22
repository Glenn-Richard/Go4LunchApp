package api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapAPI {

    @GET("nearbysearch/json")
    Call<JSONResponse> getRestaurants(
            @Query(value = "location", encoded = true) String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );

    @GET("details/json")
    Call<DetailsPlaces> getDetailsPlaces(
            @Query("place_id") String placeId,
            @Query(value = "fields", encoded = true) String fields,
            @Query("key") String key);

}
