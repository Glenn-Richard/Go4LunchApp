package api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

     private  static final String base_url = "https://maps.googleapis.com/maps/api/place/";

            private static final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        public static GoogleMapAPI getInterface() {
            return retrofit.create(GoogleMapAPI.class);

    }
}
