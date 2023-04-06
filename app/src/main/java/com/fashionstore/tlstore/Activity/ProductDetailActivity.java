package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.fashionstore.tlstore.API.ProductAPI;
import com.fashionstore.tlstore.Adapter.ProductImageAdapter;
import com.fashionstore.tlstore.Model.ProductImageModel;
import com.fashionstore.tlstore.Model.ProductModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ViewPager vpProductImage;
    CircleIndicator ciProductImage;
    ProductImageAdapter productImageAdapter;
    TextView tvProductSold, tvProductQuantity, tvProductName, tvProductPrice;


    long productId;
    ProductModel product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_product_detail);


        anhXa();

        productId = (long) getIntent().getSerializableExtra("productId");
        Log.e("ProductId", String.valueOf(productId));
        getProduct(productId);
        getIntent().removeExtra("productId");

    }

    public void anhXa() {
        vpProductImage = (ViewPager) findViewById(R.id.vpProductImage);
        ciProductImage = (CircleIndicator) findViewById(R.id.ciProductImage);

        tvProductName = (TextView) findViewById(R.id.tvProductNameDetail);
        tvProductPrice = (TextView) findViewById(R.id.tvProductPriceDetail);
        tvProductSold = (TextView) findViewById(R.id.tvProductSold);
        tvProductQuantity = (TextView) findViewById(R.id.tvProductQuantity);
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

                    loadProductImageSlide(product);

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
    }
}