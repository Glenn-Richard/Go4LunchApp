package utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.go4lunchapp.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlToBitmap extends AsyncTask<String,Void, Bitmap> {

    MarkerOptions markerOptions;

    public UrlToBitmap(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = null;
        try {
            InputStream stream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }
}
