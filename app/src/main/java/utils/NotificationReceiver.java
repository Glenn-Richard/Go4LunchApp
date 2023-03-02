package utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.go4lunchapp.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"101")
                .setSmallIcon(R.drawable.ic_baseline_lunch_dining_green_24)
                .setContentTitle("À taaable!!!")
                .setContentText("Votre déjeuner vous attend")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(0,builder.build());
    }
}
