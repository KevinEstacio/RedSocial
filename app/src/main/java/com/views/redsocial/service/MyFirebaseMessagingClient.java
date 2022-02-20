package com.views.redsocial.service;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.views.redsocial.channel.NotificationHelper;
import com.views.redsocial.models.Message;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");
        if (title != null){
            if ( title.equals("NUEVO MENSAJE")){

                showNotificationMessage(data);
            }
            else {
                showNotification(title,body);

            }
        }
    }
    private void showNotification(String title, String body){
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotification(title,body);
        Random random = new Random();
        int n = random.nextInt(10000);
        notificationHelper.getManager().notify(n,builder.build());
    }
    private void showNotificationMessage(Map<String,String>  data){
        String title  = data.get("title");
        String body  = data.get("body");
        String usernamesender  = data.get("usernamesender");
        String usernamereceiver  = data.get("usernamereceiver");
        String lastMessage  = data.get("lastMessage");
        String messagesJSON  = data.get("messages");
        int idNotification = Integer.parseInt(data.get("idNotification")) ;
        Gson gson = new Gson();
        Message [] messages = gson.fromJson(messagesJSON,Message[].class);

        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationMessage(messages,usernamesender,usernamereceiver,lastMessage);

        notificationHelper.getManager().notify(idNotification,builder.build());
    }
}
