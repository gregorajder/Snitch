package com.gregorajdergmail.snitch.model;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.gregorajdergmail.snitch.App;
import com.gregorajdergmail.snitch.R;
import com.gregorajdergmail.snitch.view.MainActivity;

import javax.inject.Inject;

public class NotificationService extends Service {
    @Inject
    protected FileHelper fileHelper;

    public static void newInstance(Activity activity, String data) {
        Intent serviceIntent = new Intent(activity, NotificationService.class);
        serviceIntent.putExtra("data", data);
        activity.startService(serviceIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        App.getComponent().inject(this);

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, new Intent(this, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle(getResources().getString(R.string.snitch_watch_to_file))
                .setContentText(getResources().getString(R.string.notification_content))
                .setTicker(getResources().getString(R.string.snitch_watch_to_file))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_search_white_48dp)
                .build();

        startForeground(123, notification);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String path = intent.getExtras().getString("data");
        if (fileHelper.addNewFile(path, this)) {
            return START_REDELIVER_INTENT;
        } else {
            Toast.makeText(getApplicationContext(),
                    "Unsuccessful. Try again!", Toast.LENGTH_SHORT).show();
            return START_NOT_STICKY;
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
