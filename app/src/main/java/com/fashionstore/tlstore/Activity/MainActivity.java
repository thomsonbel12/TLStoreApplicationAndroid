package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fashionstore.tlstore.API.CategoryAPI;
import com.fashionstore.tlstore.API.ProductAPI;
import com.fashionstore.tlstore.Adapter.CategoryRecycleInterface;
import com.fashionstore.tlstore.Model.CategoryModel;
import com.fashionstore.tlstore.Model.ProductModel;
import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;
import com.fashionstore.tlstore.Adapter.CategoryAdapter;
import com.fashionstore.tlstore.Adapter.ProductAdapter;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CategoryRecycleInterface {
    RecyclerView rvCategory;
    RecyclerView rvPopularProduct, rvLatestProduct;

    ImageView userAvatar;
    EditText searchProductEdit;

    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    AppCompatButton appBarHomeBtn;
    List<CategoryModel> categoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_main);
        UserModel user = SharedPrefManager.getInstance(this).getUser();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (!(user.getAvatar() == null)) {
                Glide.with(getApplicationContext()).load(user.getAvatar()).into(userAvatar);
            }
        }
        anhXa();
        loadCategory();
        loadPopularProduct();
        loadLatestProduct();
        searchProduct();
        backToHome();
    }

    public void anhXa() {
        userAvatar = (ImageView) findViewById(R.id.userAvatar);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvPopularProduct = (RecyclerView) findViewById(R.id.rvPopularProduct);
        rvLatestProduct = (RecyclerView) findViewById(R.id.rvLatestProduct);
        appBarHomeBtn = (AppCompatButton) findViewById(R.id.appBarHomeBtn);
        searchProductEdit = (EditText) findViewById(R.id.searchProductEdit);
    }

    public void loadCategory() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layout);
        CategoryRecycleInterface categoryRecycleInterface = this;

        CategoryAPI.CATEGORY_API.getAllCategory().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
//                    for(CategoryModel model : list){
//                        Log.e(String.valueOf(model.getId()), " - "+ model.getName());
//                    }
//                    Log.e("Category list", list.get(0).getName().toString());
                    categoryAdapter = new CategoryAdapter(categoryList, MainActivity.this, categoryRecycleInterface);
                    rvCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("=====Load Category", "Call Api Fail");
            }
        });
    }

    public void loadPopularProduct() {

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layout = new GridLayoutManager(this, 2);
        rvPopularProduct.setLayoutManager(layout);
        ProductAPI.PRODUCT_API.getTop10BestSelling().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> productModels = response.body();
                    productAdapter = new ProductAdapter(productModels, MainActivity.this);
                    rvPopularProduct.setAdapter(productAdapter);
                } else {
                    Log.e("=====Load Popular", "Null");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

            }
        });
    }

    public void loadLatestProduct() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layout = new GridLayoutManager(this, 2);
        rvLatestProduct.setLayoutManager(layout);
        ProductAPI.PRODUCT_API.getTop10Newest().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> productModels = response.body();
                    productAdapter = new ProductAdapter(productModels, MainActivity.this);
                    rvLatestProduct.setAdapter(productAdapter);
                } else {
                    Log.e("=====Load Latest", "Null");
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

            }
        });
    }

    public void backToHome() {
        appBarHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    List<ProductModel> listCategoryProduct;
    @Override
    public void onCategoryItemClick(int position) {
        if (!(getApplicationContext().toString().contains("ShowProductActivity"))) {
            Intent intent = new Intent(MainActivity.this, ShowProductActivity.class);
            intent.putExtra("tvUserActionCategoryClick", "Category - " + categoryList.get(position).getName());
            intent.putExtra("categoryId", categoryList.get(position).getId());
            startActivity(intent);
        }
        Toast.makeText(MainActivity.this, "Category - " + categoryList.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    public void searchProduct(){
        searchProductEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(!TextUtils.isEmpty(String.valueOf(searchProductEdit.getText()))){
                        Intent intent = new Intent(MainActivity.this, ShowProductActivity.class);
                        intent.putExtra("searchProduct", String.valueOf(searchProductEdit.getText()));
                        startActivity(intent);
                    }

                    return true;
                }
                return false;
            }
        });
    }
}