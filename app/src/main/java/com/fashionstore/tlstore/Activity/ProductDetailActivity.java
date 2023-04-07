package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fashionstore.tlstore.API.CartAPI;
import com.fashionstore.tlstore.API.ProductAPI;
import com.fashionstore.tlstore.Adapter.ProductImageAdapter;
import com.fashionstore.tlstore.Model.CartModel;
import com.fashionstore.tlstore.Model.ProductImageModel;
import com.fashionstore.tlstore.Model.ProductModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ViewPager vpProductImage;
    CircleIndicator ciProductImage;
    ProductImageAdapter productImageAdapter;
    TextView tvProductSold, tvProductQuantity, tvProductName, tvProductPrice, tvProductCartNumber;
    ConstraintLayout clBackBtn, clDecrease, clIncrease;
    AppCompatButton addToCartBtn;
    int productNumCart = 1;
    long productId;
    ProductModel product;
    List<ProductImageModel> productImageList = new ArrayList<>();
    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_product_detail);


        anhXa();

        clBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productId = (long) getIntent().getSerializableExtra("productId");
        Log.e("ProductId", String.valueOf(productId));
        getProduct(productId);
        getIntent().removeExtra("productId");


        decreaseClick();
        increaseClick();
    }

    public void anhXa() {
        vpProductImage = (ViewPager) findViewById(R.id.vpProductImage);
        ciProductImage = (CircleIndicator) findViewById(R.id.ciProductImage);

        clBackBtn = (ConstraintLayout) findViewById(R.id.clbackBtn);

        tvProductName = (TextView) findViewById(R.id.tvProductNameDetail);
        tvProductPrice = (TextView) findViewById(R.id.tvProductPriceDetail);
        tvProductSold = (TextView) findViewById(R.id.tvProductSold);
        tvProductQuantity = (TextView) findViewById(R.id.tvProductQuantity);

        tvProductCartNumber = (TextView) findViewById(R.id.tvProductNumberAppBar);
        clDecrease = (ConstraintLayout) findViewById(R.id.clDecreaseProductAppBar);
        clIncrease = (ConstraintLayout) findViewById(R.id.clIncreaseProductAppBar);

        addToCartBtn = (AppCompatButton) findViewById(R.id.addToCartBtn);


    }

    public void getProduct(long productId) {
        ProductAPI.PRODUCT_API.getProductById(productId).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful()) {
                    product = response.body();

                    tvProductName.setText(product.getProductName());
                    tvProductPrice.setText(product.getPrice()+"");
                    tvProductSold.setText(product.getSold()+"");
                    tvProductQuantity.setText(product.getQuantity()+"");

                    productImageList = product.getProductImages();
                    loadProductImageSlide(product);
                    productNumCart = Integer.parseInt(String.valueOf(tvProductCartNumber.getText()));
                    changeAddToCartBtn(productNumCart);

                }else{
                    Log.e("Empty Product", "true");
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
            }
        });
    }

    public void loadProductImageSlide(ProductModel product){
//        Log.e("=====", product.getProductName());
        productImageAdapter = new ProductImageAdapter(this, product.getProductImages());
        vpProductImage.setAdapter(productImageAdapter);

        ciProductImage.setViewPager(vpProductImage);
        productImageAdapter.registerDataSetObserver(ciProductImage.getDataSetObserver());

        autoSlideProductImage();

    }
    public void autoSlideProductImage(){
        if(productImageList == null || productImageList.isEmpty() || vpProductImage == null){
            return;
        }
        if(mTimer == null){
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentImg = vpProductImage.getCurrentItem();
//                        Log.e("Current Img", String.valueOf(currentImg));
                        int totalImg = productImageList.size() - 1;
                        if(currentImg < totalImg){
                            currentImg++;
                            vpProductImage.setCurrentItem(currentImg);
                        }else{
                            vpProductImage.setCurrentItem(0);
                        }
//                        Log.e("Current Img", String.valueOf(currentImg));

                    }
                });
            }
        }, 500, 3000);
    }

    public void decreaseClick(){
        clDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productNumCart > 0){
                    productNumCart--;
                    tvProductCartNumber.setText(productNumCart+"");
                }
                changeAddToCartBtn(productNumCart);
            }
        });
    }
    public void increaseClick(){
        clIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productNumCart <= product.getQuantity()){
                    productNumCart++;
                    tvProductCartNumber.setText(productNumCart+"");
                }
                changeAddToCartBtn(productNumCart);
            }
        });
    }

    public void changeAddToCartBtn(int productNum){

        if(productNum <= 0){
            addToCartBtn.setBackgroundDrawable(getDrawable(R.drawable.black_circle_gray_inner));
            addToCartBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icons8_white_shopping_cart, 0, R.drawable.icons8_white_double_right_100, 0);
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ProductDetailActivity.this, "The number of Product is not Available !", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            addToCartBtn.setBackgroundDrawable(getDrawable(R.drawable.selected_bar_item));
            addToCartBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icons8_white_shopping_cart, 0, R.drawable.icons8_white_double_right_100, 0);
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart();
                }
            });
        }
    }

    public void addToCart(){
        long userId = SharedPrefManager.getInstance(this).getUser().getId();
        CartAPI.CART_API.addNewCart(product.getId(),userId, productNumCart).enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(Call<CartModel> call, Response<CartModel> response) {
                if (response.isSuccessful()){
                    CartModel cartModel = response.body();
                    Log.e("Cart ID - ", String.valueOf(cartModel.getId()));
                    Toast.makeText(ProductDetailActivity.this, "Add " +
                            cartModel.getQuantity() + " of \"" +
                            cartModel.getProduct().getProductName() +
                            "\" successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}