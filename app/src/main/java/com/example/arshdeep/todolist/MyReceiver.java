package com.example.arshdeep.todolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver{
    private static int i = 0;
    private int RESULT_REQUEST_CODE = i;
    MediaPlayer mMediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("notification_title");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title + " task reminder.")
                .setAutoCancel(true)
                .setContentText("Alarm!!!");
        Intent resultIntent = new Intent(context , HomeFragment.class);
        resultIntent.putExtra("id" ,i);
        RESULT_REQUEST_CODE = i+1;
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context , RESULT_REQUEST_CODE , resultIntent , PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(RESULT_REQUEST_CODE , mBuilder.build());

        mMediaPlayer = MediaPlayer.create(context , R.raw.theknack);
        mMediaPlayer.start();
    }
}
