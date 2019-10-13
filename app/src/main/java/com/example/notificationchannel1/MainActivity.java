package com.example.notificationchannel1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.notificationchannel1.AppNotification.CHANNEL_1_ID;
import static com.example.notificationchannel1.AppNotification.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etMessage;
    private NotificationManagerCompat notificationManagerCompat; // this notification manager is backward compatible and wraps the normal NotificationManager
                                       // but it can not create notificationChannel. so we used normal notificationManager to create notification channel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTitle = findViewById(R.id.etTitle);
        etMessage = findViewById(R.id.etMsg);
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    public void onSendToChannel1(View v){
        String title = etTitle.getText().toString().trim();
        String msg = etMessage.getText().toString().trim();

        Intent notificationIntent = new Intent(MainActivity.this,MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(MainActivity.this,
        0, notificationIntent, 0);

        Intent broadcastIntent = new Intent(MainActivity.this,NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage",msg);
        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                0,broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT); // updated message should be passed to NotificationReceiver

        Notification notification1 = new NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_looks_one)
                .setPriority(NotificationCompat.PRIORITY_HIGH)     // if sdk version <26 , these properties will be set to our notification1
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)  // different types of notifications.. optional
                .setContentIntent(pendingNotificationIntent)
                .setAutoCancel(true)                               // after tapping the notification it will disappear and open the mainActivity
                .setOnlyAlertOnce(true)                            // notification sound for 1st time. not on update message
                .addAction(R.mipmap.ic_launcher,"Toast",broadcastPendingIntent)  //we can add upto 3 Action button in a notification
                .build();

        notificationManagerCompat.notify(1,notification1);
    }

    public void onSendToChannel2(View v){
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.drawable.dog_cartoon);
        String title = etTitle.getText().toString().trim();
        String msg = etMessage.getText().toString().trim();
        Notification notification2 = new NotificationCompat.Builder(this,CHANNEL_2_ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_looks_two)            // mandatory
                .setLargeIcon(largeIcon)                          //optional
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("This is line 1")
                        .addLine("This is line 2")
                        .addLine("This is line 3")
                        .addLine("This is line 4")
                        .addLine("This is line 5")
                        .addLine("This is line 6")
                        .addLine("This is line 7")
                        .setBigContentTitle("Big Content Title")
                        .setSummaryText("Summary Text")
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)     // if sdk version <26 , these properties will be set to our notification1
                .setCategory(NotificationCompat.CATEGORY_EMAIL)  // different types of notifications.. optional
                .build();

        notificationManagerCompat.notify(2,notification2);

    }
}
