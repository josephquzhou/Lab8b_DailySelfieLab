package com.example.joe_2.lab8b_dailyselfielab;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Joe_2 on 2017-10-02.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    private long[] mVibratePattern = { 0, 200, 200, 300 };

    // Notification Text Elements
    private final CharSequence tickerText = "Time for a selfie";
    private final CharSequence contentTitle = "Daily Selfie";
    private final CharSequence contentText = "Time for another selfie";
    @Override
    public void onReceive(Context context, Intent intent) {
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.camera))
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(mVibratePattern);
        notificationManager.notify(MY_NOTIFICATION_ID, mNotifyBuilder.build());
    }

}