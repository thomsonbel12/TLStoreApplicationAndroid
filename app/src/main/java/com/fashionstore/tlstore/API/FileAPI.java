package com.fashionstore.tlstore.API;

import com.fashionstore.tlstore.Retrofit.RetrofitClient;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    FileAPI FILE_API = RETROFIT_CLIENT.getRetrofit().create(FileAPI.class);


    @Multipart
    @POST("/files/cloud/upload")
    Call<String> uploadFile(@Part MultipartBody.Part file);
}
