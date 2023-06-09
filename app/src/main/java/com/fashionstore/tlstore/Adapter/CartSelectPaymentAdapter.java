package com.fashionstore.tlstore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fashionstore.tlstore.API.CartAPI;
import com.fashionstore.tlstore.Interface.CartRecycleInterface;
import com.fashionstore.tlstore.Model.CartModel;
import com.fashionstore.tlstore.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartSelectPaymentAdapter extends RecyclerView.Adapter<CartSelectPaymentAdapter.ViewHolder> {
    List<CartModel> cartList;
    Context context;

    public CartSelectPaymentAdapter(List<CartModel> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImg;
        TextView tvProductName, tvCartProductPrice, tvCartQuantity, tvCartPrice;
//        ConstraintLayout clDecrease, clIncrease, clDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImg = itemView.findViewById(R.id.ivProductImgCart);

            tvProductName = itemView.findViewById(R.id.tvProductNameCart);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQuantity = itemView.findViewById(R.id.tvProductCartQuantity);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
        }
    }

    @NonNull
    @Override
    public CartSelectPaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_cart, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartSelectPaymentAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(cartList.get(position).getProduct().getProductImages().get(0).getImage()).into(holder.ivProductImg);
        holder.tvProductName.setText(cartList.get(position).getProduct().getProductName());
//        holder.cartNumber = cartList.get(position).getQuantity();
        holder.tvCartProductPrice.setText(cartList.get(position).getProduct().getPrice() + "");
        holder.tvCartQuantity.setText(cartList.get(position).getQuantity() + "");
        holder.tvCartPrice.setText((cartList.get(position).getQuantity() * cartList.get(position).getProduct().getPrice()) + "");
    }

    @Override
    public int getItemCount() {
        if (cartList != null) {
            return cartList.size();
        }
        return 0;
    }
}
