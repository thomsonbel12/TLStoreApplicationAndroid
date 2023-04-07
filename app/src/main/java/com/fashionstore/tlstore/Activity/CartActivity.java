package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fashionstore.tlstore.API.CartAPI;
import com.fashionstore.tlstore.Adapter.CartAdapter;
import com.fashionstore.tlstore.Model.CartModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    ConstraintLayout clHome;
    RecyclerView rvCart;
    List<CartModel> cartList;
    CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_cart);

        anhXa();

        loadCart();

        backToHome();
    }

    void anhXa(){
        clHome = (ConstraintLayout) findViewById(R.id.clHomeAppBar);
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
    }
    void loadCart(){
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(layout);

        CartAPI.CART_API.getCartByUserId(SharedPrefManager.getInstance(this).getUser().getId()).enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {
                if(response.isSuccessful()){
                    cartList = response.body();
                    cartAdapter = new CartAdapter(cartList, CartActivity.this);
                    rvCart.setAdapter(cartAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                Log.e("Cart API Error", "Call cart API fail");
            }
        });
    }
    void backToHome(){
        clHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}