package com.fashionstore.tlstore.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //    public final String BASE_URL = "http://172.16.30.254:8080";
    public final String BASE_URL = "http://172.20.10.3:8080";
//        public final String BASE_URL = "http://192.168.1.103:8080";
//        public final String BASE_URL = "http://192.168.1.104:8080";
//    public final String BASE_URL = "http://172.16.31.209:8080";
//    public final String BASE_URL = "http://172.20.10.3:8080";
//    public final String BASE_URL = "http://172.20.10.10:8080";
//    public final String BASE_URL = "http://192.168.1.101:8080";
//    public final String BASE_URL = "http://172.16.31.82:8080";
//    public final String BASE_URL = "http://192.168.1.105:8080";
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    Gson gson = new GsonBuilder()
//            .setLenient()
            .setDateFormat("yyyy MM d HH:mm:ss").create();
    public static RetrofitClient retrofitClient;
    private Retrofit retrofit;

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public Retrofit getRetrofit(String url) {
        return changeUrl(url);
    }

    public Retrofit getRetrofit() {
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