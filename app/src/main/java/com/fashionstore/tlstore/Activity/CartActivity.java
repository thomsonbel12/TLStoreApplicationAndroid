package com.fashionstore.tlstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fashionstore.tlstore.API.CartAPI;
import com.fashionstore.tlstore.Adapter.CartAdapter;
import com.fashionstore.tlstore.Interface.CartRecycleInterface;
import com.fashionstore.tlstore.Model.CartModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartRecycleInterface {
    ConstraintLayout clHome, clCheckout, clCartPriceCheckout, clOrder;
    AppCompatButton appBarCartBtn;
    RecyclerView rvCart;
    TextView tvTotalCartPrice;
    TextView tvTotalCartItem;
    ImageView ivCartEmpty;
    List<CartModel> cartList;
    CartAdapter cartAdapter;
    int totalCartPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_cart);

        anhXa();

        loadCart();

        goToCheckout();

        backToHome();

        refreshCart();

        clOrder.setOnClickListener(v -> {
//            finish();
            startActivity(new Intent(CartActivity.this, OrdersActivity.class));
        });
    }

    void anhXa() {
        clHome = findViewById(R.id.clHomeAppBar);
        clCheckout = findViewById(R.id.clCheckout);
        clCartPriceCheckout = findViewById(R.id.clProceedToPayment);
        clOrder = findViewById(R.id.clOrderAppBar);

        rvCart = findViewById(R.id.rvCart);
        ivCartEmpty = findViewById(R.id.ivCartEmpty);

        tvTotalCartPrice = findViewById(R.id.tvTotalCartPrice);
        tvTotalCartItem = findViewById(R.id.tvTotalCartItem);
        appBarCartBtn = findViewById(R.id.appBarCartBtn);
    }

    void loadCart() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(layout);
        CartRecycleInterface cartRecycleInterface = this;
        CartAPI.CART_API.getCartByUserId(SharedPrefManager.getInstance(this).getUser().getId()).enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CartModel>> call, @NonNull Response<List<CartModel>> response) {
                if (response.isSuccessful()) {
                    cartList = response.body();
                    cartAdapter = new CartAdapter(cartRecycleInterface, cartList, CartActivity.this);
                    rvCart.setAdapter(cartAdapter);

                    if (cartList != null) {
                        for (CartModel c : cartList) {
                            totalCartPrice += c.getQuantity() * c.getProduct().getPrice();
                        }
                        tvTotalCartItem.setText(String.valueOf(cartList.size()));
                        tvTotalCartPrice.setText(String.valueOf(totalCartPrice));
                        clCartPriceCheckout.setVisibility(View.VISIBLE);
//                        ivCartEmpty.setBackground(null);
                        ivCartEmpty.setVisibility(View.GONE);
                    }
                    else{
                        tvTotalCartPrice.setText("0");
                        clCartPriceCheckout.setVisibility(View.GONE);
//                        clCheckout.setVisibility(View.GONE);
                        ivCartEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CartModel>> call, @NonNull Throwable t) {
                Log.e("Cart API Error", "Call cart API fail");
            }
        });
    }

    void backToHome() {
        clHome.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, MainActivity.class));
            finish();
        });
    }
    void refreshCart(){
        appBarCartBtn.setOnClickListener(v ->{
            startActivity(new Intent(CartActivity.this, CartActivity.class));
            finish();
        });
    }

    @Override
    public void updateCartTotalPrice(int price) {
        Log.e("Update price", price + "");

        totalCartPrice += price;
        tvTotalCartPrice.setText(String.valueOf(totalCartPrice));

        if (totalCartPrice == 0){
            ivCartEmpty.setVisibility(View.VISIBLE);
//            clCheckout.setVisibility(View.GONE);
            clCartPriceCheckout.setVisibility(View.GONE);
        }
    }
    public void goToCheckout(){
        clCheckout.setOnClickListener(v -> startActivity(new Intent(CartActivity.this, SelectAddressActivity.class)));
    }
}