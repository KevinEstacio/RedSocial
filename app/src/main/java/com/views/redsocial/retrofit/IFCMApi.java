package com.views.redsocial.retrofit;

import com.views.redsocial.models.FCMBody;
import com.views.redsocial.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAQxUAgdg:APA91bF78N44GdzA9obJtOe3dAoY3mIltM4mOzExLVlCgqj-8Y-tLBBWWQwU1dvavx494EiD0mpHY53Paq1HSMu9GIzDHfY0ULoWY2t80LuAjMm6brO7fKDqO0kVZyhuCwBZahAPjbUu"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
