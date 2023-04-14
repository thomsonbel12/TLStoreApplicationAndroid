package com.fashionstore.tlstore.API;

import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {

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
