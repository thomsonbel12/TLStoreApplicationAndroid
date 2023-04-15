package com.fashionstore.tlstore.API;

import com.fashionstore.tlstore.Model.OrderModel;
import com.fashionstore.tlstore.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OrderAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();

    OrderAPI ORDER_API = RETROFIT_CLIENT.getRetrofit().create(OrderAPI.class);

    @FormUrlEncoded
    @POST("/Order/add-all")
    Call<OrderModel> addCartToOrder(@Field("userId") long id,
                                    @Field("address") String address,
                                    @Field("phoneNumber") String phone);
}
