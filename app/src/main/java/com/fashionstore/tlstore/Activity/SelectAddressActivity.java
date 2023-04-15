package com.fashionstore.tlstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fashionstore.tlstore.Adapter.DeliveryDetailAdapter;
import com.fashionstore.tlstore.Interface.DeliveryAddressSelectInterface;
import com.fashionstore.tlstore.Object.DeliveryDetail;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SelectAddressActivity extends AppCompatActivity implements DeliveryAddressSelectInterface {
    ConstraintLayout clAddNewDeliveryAddress, clProceedToPayment, clBackBtn;

    DeliveryDetailAdapter deliveryDetailAdapter;

    RecyclerView rvDeliveryAddress;

    ImageView ivNoAddressFound;

    int position = -1;
    List<DeliveryDetail> deliveryDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_select_address);

        anhXa();

        clBackBtn.setOnClickListener(v -> onBackPressed());

        loadAddress();

        clAddNewDeliveryAddress.setOnClickListener(v -> openAddAddressDialog());
    }

    void anhXa() {
        ivNoAddressFound = findViewById(R.id.ivNoAddressFound);
        rvDeliveryAddress = findViewById(R.id.rvDeliveryAddress);
        clAddNewDeliveryAddress = findViewById(R.id.clAddNewDeliveryAddress);
        clProceedToPayment = findViewById(R.id.clProceedToPayment);
        clBackBtn = findViewById(R.id.clbackBtn);
    }

    public void loadAddress() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDeliveryAddress.setLayoutManager(manager);

//        List<DeliveryDetail> list = new ArrayList<>();
//        list.add(new DeliveryDetail("Thanh Thao", "0974991941", "61/11, Đường Hàng Tre, Tầng 2, Quận 9, TP.Thủ Đức, TP.Hồ Chí Minh."));
//        list.add(new DeliveryDetail("Cong Tu", "0974991941", "61/11, Đường Hàng Tre, Tầng 2, Quận 9, TP.Thủ Đức, TP.Hồ Chí Minh."));
//        deliveryDetails = SharedPrefManager.getInstance(this).getDeliveryDetail();

//        deliveryDetails.add(new DeliveryDetail("Hoang Lam", "0974991941", "61/11, Đường Hàng Tre, Tầng 2, Quận 9, TP.Thủ Đức, TP.Hồ Chí Minh."));
//        SharedPrefManager.getInstance(this).setList("DeliveryList", deliveryDetails);

        deliveryDetails = SharedPrefManager.getInstance(this).getDeliveryDetail();
        if (deliveryDetails.size() == 0) {
            clProceedToPayment.setVisibility(View.GONE);
            ivNoAddressFound.setVisibility(View.VISIBLE);
        } else {
            clProceedToPayment.setVisibility(View.VISIBLE);
            ivNoAddressFound.setVisibility(View.GONE);
        }
        for (DeliveryDetail detail : deliveryDetails) {
            Log.e("Address list" + detail.getReceiverName(), detail.getReceiverAddress());
        }
        deliveryDetailAdapter = new DeliveryDetailAdapter(deliveryDetails, SelectAddressActivity.this, this);
        rvDeliveryAddress.setAdapter(deliveryDetailAdapter);
    }

    public void openAddAddressDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_address);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = Gravity.CENTER;
//        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        TextView receiverNameEdit = dialog.findViewById(R.id.receiverNameEdit);
        TextView receiverPhoneEdit = dialog.findViewById(R.id.receiverPhoneEdit);
        TextView receiverAddressEdit = dialog.findViewById(R.id.receiverAddressEdit);

        AppCompatButton add = dialog.findViewById(R.id.addAddressBtn);
        AppCompatButton cancel = dialog.findViewById(R.id.dialogCancleBtn);


        add.setOnClickListener(v -> {
            String u = String.valueOf(receiverNameEdit.getText());
            String p = String.valueOf(receiverPhoneEdit.getText());
            String a = String.valueOf(receiverAddressEdit.getText());
            if (!TextUtils.isEmpty(u) && !TextUtils.isEmpty(p) && !TextUtils.isEmpty(a)) {
                deliveryDetails.add(new DeliveryDetail(String.valueOf(receiverNameEdit.getText()),
                        String.valueOf(receiverPhoneEdit.getText()),
                        String.valueOf(receiverAddressEdit.getText())));
                SharedPrefManager.getInstance(this).setList("DeliveryList", deliveryDetails);
                deliveryDetailAdapter.notifyDataSetChanged();
                clProceedToPayment.setVisibility(View.VISIBLE);
                ivNoAddressFound.setVisibility(View.GONE);
                cancel.performClick();
            }
            if (TextUtils.isEmpty(u)) {
                receiverNameEdit.setError("Please enter receiver name");
                receiverNameEdit.requestFocus();
            }
            if (TextUtils.isEmpty(p)) {
                receiverPhoneEdit.setError("Please enter receiver phone");
                receiverPhoneEdit.requestFocus();
            }

            if (TextUtils.isEmpty(a)) {
                receiverAddressEdit.setError("Please enter receiver address");
                receiverAddressEdit.requestFocus();
            }
        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onDeliveryDetailClick(int position) {
        Toast.makeText(this, "Selected: " + position, Toast.LENGTH_SHORT).show();
        deliveryDetailAdapter.notifyDataSetChanged();

        this.position = position;
    }

    @Override
    public void onDeleteDeliveryDetailClick(int size) {
        if (size == 0) {
            clProceedToPayment.setVisibility(View.GONE);
            ivNoAddressFound.setVisibility(View.VISIBLE);
        } else {
            clProceedToPayment.setVisibility(View.VISIBLE);
            ivNoAddressFound.setVisibility(View.GONE);
        }
    }

}