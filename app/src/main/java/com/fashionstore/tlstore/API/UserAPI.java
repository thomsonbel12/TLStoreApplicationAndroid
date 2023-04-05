package com.fashionstore.tlstore.API;

import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.Retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {
////    public final String BASE_URL = "http://172.20.10.3:8080";
//    public final String BASE_URL = "http://192.168.1.105:8080";
//    Gson gson = new GsonBuilder().setDateFormat("yyyy MM d HH:mm:ss").create();
//
//    UserAPI USER_API = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build().create(UserAPI.class);
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    UserAPI USER_API = RETROFIT_CLIENT.getRetrofit().create(UserAPI.class);
    @FormUrlEncoded
    @POST("/user/login")
    Call<UserModel> login(@Field("userName")String username,
                          @Field("password")String password);

    @FormUrlEncoded
    @POST("/user")
    Call<UserModel> signup(@Field("userName")String username,
                          @Field("name")String name,
                          @Field("email")String email,
                          @Field("phoneNumber")String phoneNumber,
                          @Field("password")String password);

    @GET("/user/check/{username}")
    Call<UserModel> checkExist(@Path("username") String username);
}
