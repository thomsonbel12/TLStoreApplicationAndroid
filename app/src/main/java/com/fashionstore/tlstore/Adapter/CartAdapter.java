package com.fashionstore.tlstore.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final CartRecycleInterface cartRecycleInterface;

    List<CartModel> cartList;
    Context context;

    public CartAdapter(CartRecycleInterface cartRecycleInterface, List<CartModel> cartList, Context context) {
        this.cartRecycleInterface = cartRecycleInterface;
        this.cartList = cartList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImg;
        TextView tvProductName, tvCartProductPrice, tvCartQuantity, tvCartPrice;
        ConstraintLayout clDecrease, clIncrease;


        public ViewHolder(@NonNull View itemView, CartRecycleInterface cartRecycleInterface) {
            super(itemView);
            ivProductImg = itemView.findViewById(R.id.ivProductImgCart);

            tvProductName = itemView.findViewById(R.id.tvProductNameCart);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQuantity = itemView.findViewById(R.id.tvProductCartQuantity);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);

            clDecrease = itemView.findViewById(R.id.clDecreaseCartQuantity);
            clIncrease = itemView.findViewById(R.id.clIncreaseCartQuantity);

            clDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCart();
                }
            });

            clIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCart();
                }
            });
        }

        public void decreaseCart() {
            int cartNumber = cartList.get(getAdapterPosition()).getQuantity();
            if (cartNumber > 1) {
                cartNumber--;

                cartList.get(getAdapterPosition()).setQuantity(cartNumber);
                updateCart(cartList.get(getAdapterPosition()).getId(), cartNumber);

                cartRecycleInterface.updateCartTotalPrice((-1) * cartList.get(getAdapterPosition()).getProduct().getPrice());
            }

        }

        public void increaseCart() {
            int cartNumber = cartList.get(getAdapterPosition()).getQuantity();
            if (cartNumber <= cartList.get(getAdapterPosition()).getProduct().getQuantity()) {
                cartNumber++;

                cartList.get(getAdapterPosition()).setQuantity(cartNumber);
                updateCart(cartList.get(getAdapterPosition()).getId(), cartNumber);

                cartRecycleInterface.updateCartTotalPrice((1) * cartList.get(getAdapterPosition()).getProduct().getPrice());
            }

        }

        public void updateCart(long cartId, int cartNumber) {
            CartAPI.CART_API.updateCartQuantity(cartId, cartNumber).enqueue(new Callback<CartModel>() {
                @Override
                public void onResponse(Call<CartModel> call, Response<CartModel> response) {
                    tvCartQuantity.setText(cartNumber + "");
                    tvCartPrice.setText((cartNumber * cartList.get(getAdapterPosition()).getProduct().getPrice()) + "");

                }

                @Override
                public void onFailure(Call<CartModel> call, Throwable t) {
                    Log.e("CAll cart api", "Fail");
                }
            });
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_cart, parent, false);

        return new ViewHolder(view, cartRecycleInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
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
