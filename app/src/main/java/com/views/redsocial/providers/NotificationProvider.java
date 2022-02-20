package com.views.redsocial.providers;

import com.views.redsocial.models.FCMBody;
import com.views.redsocial.models.FCMResponse;
import com.views.redsocial.retrofit.IFCMApi;
import com.views.redsocial.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {
    private String url = "https://fcm.googleapis.com";

    public NotificationProvider(){

    }
    public Call<FCMResponse> sendNotification (FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
