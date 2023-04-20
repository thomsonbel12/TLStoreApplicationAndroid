package com.fashionstore.tlstore.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fashionstore.tlstore.Activity.IntroActivity;
import com.fashionstore.tlstore.Activity.ProfileActivity;
import com.fashionstore.tlstore.Model.UserModel;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

public class MyProfileDetailFragment extends Fragment {

    TextView tvName, tvUsername, tvEmail, tvViewAllAddress, tvChangePassword;

    AppCompatButton appCompatEditProfileBtn, appCompatLogoutBtn;

    public MyProfileDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        anhXa();
        loadDetail();

        logout();
    }

    void anhXa(){
        tvName = getView().findViewById(R.id.tvName);
        tvUsername = getView().findViewById(R.id.tvUsername);
        tvEmail = getView().findViewById(R.id.tvEmail);
        tvViewAllAddress = getView().findViewById(R.id.tvViewAllAddress);
        tvChangePassword = getView().findViewById(R.id.tvChangePassword);

        appCompatEditProfileBtn = getView().findViewById(R.id.appCompatEditProfileBtn);
        appCompatLogoutBtn = getView().findViewById(R.id.appCompatLogoutBtn);
    }
    void loadDetail(){
        UserModel user = SharedPrefManager.getInstance(getContext()).getUser();
        if (user != null){
            tvName.setText(user.getName());
            tvUsername.setText(user.getUserName());
            tvEmail.setText(user.getEmail());
        }
    }
    void logout(){
        appCompatLogoutBtn.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Warning!");
            alert.setIcon(R.drawable.icons8_warning_96);
            alert.setMessage("If you continue to logout, it will delete all your delivery Address list!!\nDo you want to logout?");
            alert.setPositiveButton("Yes", (dialog, which) -> {
                SharedPrefManager.getInstance(getContext()).deleteDeliveryList();
                SharedPrefManager.getInstance(getContext()).userLogout();
                startActivity(new Intent(getContext(), IntroActivity.class));
            });
            alert.setNegativeButton("No", (dialog, which) -> {

            });

            alert.show();
        });
    }
}