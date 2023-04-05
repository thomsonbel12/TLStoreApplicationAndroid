package com.fashionstore.tlstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fashionstore.tlstore.Model.ProductModel;
import com.fashionstore.tlstore.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<ProductModel> list;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        ConstraintLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            layout = itemView.findViewById(R.id.clProduct);
        }
    }

    public ProductAdapter(List<ProductModel> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getProductImages().get(0).getImage()).into(holder.ivProduct);
        holder.tvProductName.setText(list.get(position).getProductName());
        holder.tvProductPrice.setText("$" + list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }


}
