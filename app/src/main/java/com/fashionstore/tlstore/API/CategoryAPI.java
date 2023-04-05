package com.fashionstore.tlstore.API;

import com.fashionstore.tlstore.Model.CategoryModel;
import com.fashionstore.tlstore.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    CategoryAPI CATEGORY_API = RETROFIT_CLIENT.getRetrofit().create(CategoryAPI.class);

    @GET("/Category")
    Call<List<CategoryModel>> getAllCategory();

}
