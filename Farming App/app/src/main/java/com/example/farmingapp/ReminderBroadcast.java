package com.example.farmingapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

// Most of the code I got from https://stackoverflow.com/questions/34517520/how-to-give-notifications-on-android-on-specific-time
// Credit goes to 4xMafole on StackOverflow

public class ReminderBroadcast extends BroadcastReceiver
{
    private static final int NOTIFICATION_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationUtils _notificationUtils = new NotificationUtils(context);
        NotificationCompat.Builder _builder = _notificationUtils.
                setNotification("Reminder", "Daily reminder to log your farming inventory!")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);

        _notificationUtils.getManager().notify(NOTIFICATION_ID, _builder.build());
    }
}