package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.fashionstore.tlstore.Adapter.MyProfileViewPagerAdapter;
import com.fashionstore.tlstore.R;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    ConstraintLayout clHome, clCart, clOrder;
    AppCompatButton appBarOrderBtn;

    TabLayout tlMyProfileItem;
    ViewPager2 vp2MyProfileItem;

    MyProfileViewPagerAdapter profileViewPagerAdapter;
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
        loadAdapter();
    }

    void anhXa() {
        clHome = findViewById(R.id.clHomeAppBar);
        clCart = findViewById(R.id.clCartAppBar);
        clOrder = findViewById(R.id.clOrderAppBar);

        tlMyProfileItem = findViewById(R.id.tlMyProfileItem);
        vp2MyProfileItem = findViewById(R.id.vp2MyProfileItem);

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

    void loadAdapter(){
        tlMyProfileItem.addTab(tlMyProfileItem.newTab().setText("My Information"));
        tlMyProfileItem.addTab(tlMyProfileItem.newTab().setText("My Wallet"));
        tlMyProfileItem.addTab(tlMyProfileItem.newTab().setText("Other..."));

        profileViewPagerAdapter = new MyProfileViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        vp2MyProfileItem.setAdapter(profileViewPagerAdapter);

        tlMyProfileItem.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp2MyProfileItem.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp2MyProfileItem.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tlMyProfileItem.selectTab(tlMyProfileItem.getTabAt(position));
            }
        });
    }
}

