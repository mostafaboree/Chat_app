package com.mostafabor3e.messenger;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationManager notificationManager;

    NotificationCompat.Builder nBuilder;
    @Override

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            Intent intent=new Intent(getBaseContext(), Chat_Activity.class);

            PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),1,intent,0);
            nBuilder=new NotificationCompat.Builder(getBaseContext(),"notify_001");


            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.bigText(" "+title);
            bigText.setBigContentTitle(message);
            bigText.setSummaryText("Text in detail");
            Bitmap icon = BitmapFactory.decodeResource(getApplication().getResources(),
                    R.drawable.masslog);

            nBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
            nBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL).
                    setWhen(System.currentTimeMillis()).
                    setContentTitle(title).
                    setContentText("Your text").
                    setContentIntent(pendingIntent).
                    setLargeIcon(icon).
                    setSound(Uri.parse(String.valueOf(Notification.DEFAULT_SOUND))).
                    setPriority(NotificationCompat.PRIORITY_MAX).
                    setSmallIcon(R.mipmap.ic_launcher);
            nBuilder.setStyle(bigText);
            notificationManager=(NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                nBuilder.setChannelId(channelId);
            }
            int randm=new Random().nextInt(9999-1)+1;
            notificationManager.notify(randm,nBuilder.build());
        }


    }}
