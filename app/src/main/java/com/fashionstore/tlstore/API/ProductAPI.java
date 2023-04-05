package com.fashionstore.tlstore.API;

import com.fashionstore.tlstore.Model.ProductModel;
import com.fashionstore.tlstore.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface  ProductAPI {

    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    ProductAPI PRODUCT_API = RETROFIT_CLIENT.getRetrofit().create(ProductAPI.class);

    @GET("/Product")
    Call<List<ProductModel>> getAllProduct();
    @GET("/Product/best-selling")
    Call<List<ProductModel>> getTop10BestSelling();
    @GET("Product/new")
    Call<List<ProductModel>> getTop10Newest();
    @GET("Product/category/{id}")
    Call<List<ProductModel>> getProductByCategoryId(@Path("id")long id);
    @GET("Product/search/name?")
    Call<List<ProductModel>> getProductByName(@Query("name") String name);
}
