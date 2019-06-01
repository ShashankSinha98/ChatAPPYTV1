package com.example.shashanksinha.chatappytv1.Fragments;
import com.example.shashanksinha.chatappytv1.Notifications.MyResponse;
import com.example.shashanksinha.chatappytv1.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA6BrEZBw:APA91bHl1PkQX0ubWWZtIdhnaw0c0kYL54fKldW8cLPF1lBohSl910lEVYtVrbM6_ktaRz73z_KOIYIEZfBLQjUP4VCKSKHMhxt0LTtXz3_q_iK2DCk7C1gUwbnrG94eXPdhYjS-3u9k"

            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
