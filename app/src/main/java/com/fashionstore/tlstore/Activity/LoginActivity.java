package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fashionstore.tlstore.API.UserAPI;
import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView toSignUpPageBtn;
    EditText username;
    EditText password;
    Button loginBtn;
//    UserAPI userAPI;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            Toast.makeText(this, "Welcome - " + SharedPrefManager.getInstance(this).getUser().getName(),Toast.LENGTH_LONG ).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameEdit);
        password = (EditText) findViewById(R.id.passwordEdit);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        toSignUpPageBtn = (TextView) findViewById(R.id.toSignupPageBtn);

        toSignUpPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        String u = String.valueOf(username.getText());
        String p = String.valueOf(password.getText());
        if(TextUtils.isEmpty(u)){
            username.setError("Please enter your username");
            username.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(p)){
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }
        UserAPI.USER_API.login(u, p).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    userModel = response.body();
                    Toast.makeText(LoginActivity.this, userModel.getEmail(), Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Log.e("======", "login fail");
                    Toast.makeText(LoginActivity.this, "Wrong username or password !!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("======", "call api fail");
            }
        });
    }
}