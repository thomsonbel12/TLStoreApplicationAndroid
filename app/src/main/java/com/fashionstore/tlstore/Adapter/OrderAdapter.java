package com.fashionstore.tlstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fashionstore.tlstore.Activity.OrdersActivity;
import com.fashionstore.tlstore.Model.OrderModel;
import com.fashionstore.tlstore.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<OrderModel> orderList;
    Context context;

    public OrderAdapter(List<OrderModel> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_recycle_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        holder.tvOrderId.setText(String.valueOf(orderList.get(position).getId()));

        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        holder.tvOrderDate.setText(simpleDateFormat.format(orderList.get(position).getCreateAt()));
        holder.tvOrderItems.setText(String.valueOf(orderList.get(position).getOrderItems().size()));
        holder.tvOrderTotal.setText(String.valueOf(orderList.get(position).getTotal()));
        holder.tvOrderName.setText(String.valueOf(orderList.get(position).getUser().getName()));
        holder.tvPhoneNumber.setText(String.valueOf(orderList.get(position).getPhoneNumber()));
        holder.tvDeliveryAddress.setText(String.valueOf(orderList.get(position).getAddress()));

        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.rvOrderItemList.setLayoutManager(layout);
        holder.orderItemAdapter = new OrderItemAdapter(orderList.get(position).getOrderItems(), context);
        holder.rvOrderItemList.setNestedScrollingEnabled(false);
        holder.rvOrderItemList.setAdapter(holder.orderItemAdapter);

        holder.itemView.setOnClickListener(v -> {
            if (holder.clDeliveryInfo.getVisibility() == View.VISIBLE) {
                holder.clDeliveryInfo.setVisibility(View.GONE);
                holder.clOrderItemList.setVisibility(View.GONE);
                holder.clDeliveryDetail.setBackground(context.getDrawable(R.drawable.product_background_white));
            } else {
                holder.clDeliveryInfo.setVisibility(View.VISIBLE);
                holder.clOrderItemList.setVisibility(View.VISIBLE);
                holder.clDeliveryDetail.setBackground(context.getDrawable(R.drawable.selected_delivery_bg));
                holder.clOrderItemList.requestFocus();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvOrderItems, tvOrderTotal, tvOrderName, tvPhoneNumber, tvDeliveryAddress;
        RecyclerView rvOrderItemList;
        ConstraintLayout clDeliveryInfo, clOrderItemList, clDeliveryDetail;
        OrderItemAdapter orderItemAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderItems = itemView.findViewById(R.id.tvOrderItems);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderName = itemView.findViewById(R.id.tvOrderName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);

            clDeliveryDetail = itemView.findViewById(R.id.clDeliveryDetail);
            clDeliveryInfo = itemView.findViewById(R.id.clDeliveryInfo);
            clOrderItemList = itemView.findViewById(R.id.clOrderItemList);

            rvOrderItemList = itemView.findViewById(R.id.rvOrderItemList);
        }
    }
}
