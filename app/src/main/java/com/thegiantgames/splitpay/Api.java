package com.thegiantgames.splitpay;

import retrofit2.Call;
import retrofit2.http.Body;
import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("/characters")
    Call<JsonObject> signUp(@Body JsonObject object);

    @GET("/")
    Call<JsonObject> getcall();

}
