package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fashionstore.tlstore.API.OrderAPI;
import com.fashionstore.tlstore.Adapter.OrderAdapter;
import com.fashionstore.tlstore.Model.OrderModel;
import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    ConstraintLayout clHome, clCart, clProfile, clEmptyOrder, clOrderList;
    AppCompatButton appBarOrderBtn;
    OrderAdapter orderAdapter;
    RecyclerView rvOrderList;

    TextView tvTotalOrders;
    List<OrderModel> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_orders);

        anhXa();

        loadOrder();


        toHome();
        toCart();
        reloadOrders();
    }
    void loadOrder(){
        UserModel user= SharedPrefManager.getInstance(this).getUser();
        if(user != null){
            long id = user.getId();
            OrderAPI.ORDER_API.getAllOrderByUserId(id).enqueue(new Callback<List<OrderModel>>() {
                @Override
                public void onResponse(Call<List<OrderModel>> call, Response<List<OrderModel>> response) {
                    if(response.isSuccessful()){
                        orderList = response.body();
//                        Toast.makeText(OrdersActivity.this, orderList.size(), Toast.LENGTH_SHORT).show();

                        if(!orderList.isEmpty()){
                            Log.e("Order List", orderList.toString());
                            clEmptyOrder.setVisibility(View.GONE);
                            clOrderList.setVisibility(View.VISIBLE);

                            tvTotalOrders.setText(String.valueOf(orderList.size()));

                            LinearLayoutManager layout = new LinearLayoutManager(OrdersActivity.this, LinearLayoutManager.VERTICAL, false);
                            rvOrderList.setLayoutManager(layout);
                            orderAdapter = new OrderAdapter(orderList, OrdersActivity.this);
                            rvOrderList.setAdapter(orderAdapter);
                        }else{
                            Toast.makeText(OrdersActivity.this, "Empty Order!!", Toast.LENGTH_SHORT).show();
                            clEmptyOrder.setVisibility(View.VISIBLE);
                            clOrderList.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderModel>> call, Throwable t) {
                    Log.e("Order API", t.getMessage());
                }
            });
        }
    }

    private void reloadOrders() {
        appBarOrderBtn.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(OrdersActivity.this, OrdersActivity.class));
        });
    }

    void anhXa(){
        tvTotalOrders = findViewById(R.id.tvTotalOrders);

        clHome = findViewById(R.id.clMainAppBar);
        clCart = findViewById(R.id.clCartAppBar);
        clProfile = findViewById(R.id.clProfileAppBar);
        clEmptyOrder = findViewById(R.id.clEmpyOrder);
        clOrderList = findViewById(R.id.clOrderList);

        rvOrderList = findViewById(R.id.rvOrderList);

        appBarOrderBtn = findViewById(R.id.appBarOrderBtn);
    }
    void toHome(){
        clHome.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(OrdersActivity.this, MainActivity.class));
        });
    }
    void toCart(){
        clCart.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(OrdersActivity.this, CartActivity.class));
        });
    }

}