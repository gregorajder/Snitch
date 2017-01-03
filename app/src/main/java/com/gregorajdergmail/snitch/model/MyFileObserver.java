package com.gregorajdergmail.snitch.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.FileObserver;

import com.gregorajdergmail.snitch.App;
import com.gregorajdergmail.snitch.R;
import com.gregorajdergmail.snitch.view.MainActivity;

import java.io.IOException;

import javax.inject.Inject;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyFileObserver extends FileObserver {
    @Inject
    protected FileHelper fileHelper;
    int i = 1;
    private NotificationManager notificationManager;
    private Context context;

    public MyFileObserver(Context context, String path) {
        super(path);
        App.getComponent().inject(this);
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        this.context = context;
    }


    @Override
    public void onEvent(int event, String file) {

        if (event == FileObserver.CLOSE_WRITE) {
            try {
                if (fileHelper.checkForUpdates()) {
                    sendNotification(
                            context.getResources().getString(R.string.quick_notification),
                            context.getResources().getString(R.string.quick_notification)
                    );
                    fileHelper.updateTempCopy();
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendNotification(
                        context.getResources().getString(R.string.wrong_title),
                        context.getResources().getString(R.string.lost_notification)

                );
                stopWatching();
            }
        }
    }

    private void sendNotification(String title, String content) {
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, new Intent(context, MainActivity.class),
                PendingIntent.FLAG_CANCEL_CURRENT);


        Notification notification = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(title)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();

        notificationManager.notify(i++, notification);
    }

    @Override
    public void stopWatching() {
        super.stopWatching();
        fileHelper.onStopWatching();
    }
}
