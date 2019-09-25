package com.eazygamers.battlegroundspaidroom_earnmoneybyplayingcustomroom;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PushNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),remoteMessage.getNotification().getBody());
    }

    public void showNotification(String tittle,String message)
    {
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this,"BattleGround's Paid Room")
                .setContentTitle(tittle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message);
        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());
    }


}
