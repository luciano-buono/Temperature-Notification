package com.firebase.temperaturenotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class NotificationHelper {

    private static String TAG = "NotificationHelper";
    public static void displayNotifications(Context context, String title, String body) {

        Intent intent = new Intent(context,ProfileActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity
                (context,100,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification_bell_svgrepo_com)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        //id:1 es usado para modificar o borrar la notificacion, creo
        notificationManagerCompat.notify(1, mBuilder.build());
        Log.d(TAG,"Showing Notification");

    }

}
