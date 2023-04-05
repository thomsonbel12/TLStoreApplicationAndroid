package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fashionstore.tlstore.API.CategoryAPI;
import com.fashionstore.tlstore.API.ProductAPI;
import com.fashionstore.tlstore.Model.CategoryModel;
import com.fashionstore.tlstore.Model.ProductModel;
import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;
import com.fashionstore.tlstore.Adapter.CategoryAdapter;
import com.fashionstore.tlstore.Adapter.CategoryRecycleInterface;
import com.fashionstore.tlstore.Adapter.ProductAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProductActivity extends AppCompatActivity implements CategoryRecycleInterface {
    RecyclerView rvCategory, rvShowProduct;

    ImageView userAvatar;

    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    TextView tvUserAction, tvShowAllProduct;
    EditText searchProductEdit;

    AppCompatButton appBarHomeBtn;
    List<CategoryModel> categoryList;
    List<ProductModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        setContentView(R.layout.activity_show_product);

        UserModel user = SharedPrefManager.getInstance(this).getUser();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (!(user.getAvatar() == null)) {
                Glide.with(getApplicationContext()).load(user.getAvatar()).into(userAvatar);
            }
        }
        anhXa();
        loadCategory();
        showAllProduct();
        showProduct();

        backToHome();

        searchProduct();
    }

    public void anhXa() {
        userAvatar = (ImageView) findViewById(R.id.userAvatar);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvShowProduct = (RecyclerView) findViewById(R.id.rvShowProduct);
        appBarHomeBtn = (AppCompatButton) findViewById(R.id.appBarHomeBtn);
        tvUserAction = (TextView) findViewById(R.id.tvUserAction);
        tvShowAllProduct = (TextView) findViewById(R.id.tvShowAllProduct);
        searchProductEdit = (EditText) findViewById(R.id.searchProductEdit);


        LinearLayoutManager layout = new GridLayoutManager(this, 2);
//        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvShowProduct.setLayoutManager(layout);
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
                    categoryAdapter = new CategoryAdapter(categoryList, ShowProductActivity.this, categoryRecycleInterface);
                    rvCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("=====", "Call Api Fail");
            }
        });
    }

    public void showAllProduct(){
        tvShowAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductAPI.PRODUCT_API.getAllProduct().enqueue(new Callback<List<ProductModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                        if(response.isSuccessful()){
                            productList = response.body();
                            productAdapter = new ProductAdapter(productList, ShowProductActivity.this);
                            rvShowProduct.setAdapter(productAdapter);
                            tvUserAction.setText("All products - " + productList.size() + " results");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<ProductModel>> call, Throwable t) {

                    }
                });
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

    public void showProduct() {
        if(getIntent().getSerializableExtra("categoryId") != null){
            long categoryId = (long) getIntent().getSerializableExtra("categoryId");
            loadCategoryProduct(categoryId);
            tvUserAction.setText(getIntent().getSerializableExtra("tvUserActionCategoryClick").toString());
            getIntent().removeExtra("tvUserActionCategoryClick");
            getIntent().removeExtra("categoryId");
        }else{
            tvUserAction.setText("Search for \""+getIntent().getSerializableExtra("searchProduct") + "\"");
            searchProductEdit.setText((CharSequence) getIntent().getSerializableExtra("searchProduct"));
            String name = String.valueOf(searchProductEdit.getText());
            searchProductByName(name);
        }

    }

    public void loadCategoryProduct(long id) {
        ProductAPI.PRODUCT_API.getProductByCategoryId(id).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
//                if (response.isSuccessful()) {
                    productList = response.body();
                    productAdapter = new ProductAdapter(productList, ShowProductActivity.this);
                    tvUserAction.setText(tvUserAction.getText().toString() + " - "+ (productList!=null?productList.size():0) + " results");
                    rvShowProduct.setAdapter(productAdapter);
//                } else {
//                    Log.e("=====Product", "Empty");
//                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCategoryItemClick(int position) {
        tvUserAction.setText("Category - " + categoryList.get(position).getName());
        loadCategoryProduct(categoryList.get(position).getId());
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
                        String name = String.valueOf(searchProductEdit.getText());
                        tvUserAction.setText("Search for \""+ name + "\"");
                        searchProductByName(name);
                        closeKeyboard();
                    }
                    return true;
                }
                return false;
            }
        });
    }
    public void searchProductByName(String name){
        ProductAPI.PRODUCT_API.getProductByName(name).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
//                if(response.isSuccessful()){
                    productList = response.body();
                    productAdapter = new ProductAdapter(productList, ShowProductActivity.this);
                    tvUserAction.setText(tvUserAction.getText() + " - " + (productList!=null?productList.size():0) + " results");
                    rvShowProduct.setAdapter(productAdapter);
//                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {

            }
        });
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}