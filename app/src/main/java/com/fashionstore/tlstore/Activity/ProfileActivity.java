package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fashionstore.tlstore.R;

public class ProfileActivity extends AppCompatActivity {

    ConstraintLayout clHome, clCart, clOrder;
    AppCompatButton appBarOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_profile);

        anhXa();
        toHome();
        toCart();
        toOrder();
    }

    void anhXa() {
        clHome = findViewById(R.id.clHomeAppBar);
        clCart = findViewById(R.id.clCartAppBar);
        clOrder = findViewById(R.id.clOrderAppBar);

        appBarOrderBtn = findViewById(R.id.appBarProfileBtn);
    }

    void toHome() {
        clHome.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        });
    }

    void toCart() {
        clCart.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ProfileActivity.this, CartActivity.class));
        });
    }
    void toOrder() {
        clOrder.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ProfileActivity.this, OrdersActivity.class));
        });
    }
}

