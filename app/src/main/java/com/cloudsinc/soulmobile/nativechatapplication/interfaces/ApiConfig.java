package com.cloudsinc.soulmobile.nativechatapplication.interfaces;

import com.google.gson.JsonElement;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {
    @Multipart
    @POST("upload")
    Call<JsonElement> uploadFile(@Header("Authorization") String token, @Part MultipartBody.Part file,
                                 @Part("name") RequestBody name);
}
