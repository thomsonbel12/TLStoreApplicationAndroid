package com.fashionstore.tlstore.Retrofit;

import com.fashionstore.tlstore.API.UserAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //    public final String BASE_URL = "http://172.20.10.3:8080";
    public final String BASE_URL = "http://192.168.1.101:8080";
//    public final String BASE_URL = "http://172.20.10.3:8080";

    Gson gson = new GsonBuilder().setDateFormat("yyyy MM d HH:mm:ss").create();
    public static RetrofitClient retrofitClient;
    private Retrofit retrofit;

    public RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }
    public Retrofit getRetrofit(String url){
        return changeUrl(url);
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
    private Retrofit changeUrl(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}