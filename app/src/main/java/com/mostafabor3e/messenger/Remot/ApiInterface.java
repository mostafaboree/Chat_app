package com.mostafabor3e.messenger.Remot;

import com.mostafabor3e.messenger.Model.Response;
import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Authorization: key=AAAAm1JT1Rs:APA91bFZFF7LdkZdt7SEzd33icODNi_dh4If6s-5IJdddWZ4yyTR0PJ38KxOm6KNRO7OGYU8_9AFDdbbxPW1AzGNXqGuPLz1zI1zWaHOK65Hs8vjWuIkEEBFL48sFq80mogglhGjZP8i ",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Call<Response>
    sendNotification(@Body RootModel root);

}
