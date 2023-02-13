package api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("nearbysearch/json")
    Call<JSONResponse> getRestaurants(
            @Query(value = "location", encoded = true) String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );

}
