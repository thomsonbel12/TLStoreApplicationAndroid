package com.fashionstore.tlstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fashionstore.tlstore.Interface.DeliveryAddressSelectInterface;
import com.fashionstore.tlstore.Object.DeliveryDetail;
import com.fashionstore.tlstore.R;
import com.fashionstore.tlstore.SharedPrefManager;

import java.util.List;

public class DeliveryDetailAdapter extends RecyclerView.Adapter<DeliveryDetailAdapter.ViewHolder> {
    List<DeliveryDetail> deliveryDetails;
    Context context;

    int selectedPosition = -1;
    DeliveryAddressSelectInterface selectInterface;

    public DeliveryDetailAdapter(List<DeliveryDetail> deliveryDetails, Context context) {
        this.deliveryDetails = deliveryDetails;
        this.context = context;
    }

    public DeliveryDetailAdapter(List<DeliveryDetail> deliveryDetails, Context context, DeliveryAddressSelectInterface selectInterface) {
        this.deliveryDetails = deliveryDetails;
        this.context = context;
        this.selectInterface = selectInterface;
    }

    @NonNull
    @Override
    public DeliveryDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_delivery_detail, parent, false);
        return new ViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryDetailAdapter.ViewHolder holder, int position) {
        holder.tvDeliveryAddress.setText(deliveryDetails.get(position).getReceiverAddress());
        holder.tvNameReceive.setText(deliveryDetails.get(position).getReceiverName());
        holder.tvPhoneNumber.setText(deliveryDetails.get(position).getReceiverPhone());

        holder.rdAddressSelectBtn.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            holder.rdAddressSelectBtn.setChecked(true);

        });

        holder.rdAddressSelectBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedPosition = holder.getAdapterPosition();
                    selectInterface.onDeliveryDetailClick(holder.getAdapterPosition());

                    holder.clDeliveryDetail.setBackground(context.getDrawable(R.drawable.selected_delivery_bg));
                }else{
                    holder.clDeliveryDetail.setBackground(context.getDrawable(R.drawable.add_new_address_nobackground));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return deliveryDetails == null ? 0 : deliveryDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameReceive, tvPhoneNumber, tvDeliveryAddress, tvDeleteAddress;

        RadioButton rdAddressSelectBtn;

        ConstraintLayout clDeliveryDetail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameReceive = itemView.findViewById(R.id.tvNameRecieve);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvDeleteAddress = itemView.findViewById(R.id.tvDeleteAddress);

            rdAddressSelectBtn = itemView.findViewById(R.id.rdAddressSelectBtn);

            clDeliveryDetail = itemView.findViewById(R.id.clDeliveryDetail);


            tvDeleteAddress.setOnClickListener(v -> deleteAddress());
        }

        void deleteAddress() {
            int p = getAdapterPosition();
            notifyItemRemoved(getAdapterPosition());
            deliveryDetails.remove(p);
            SharedPrefManager.getInstance(context).setList("DeliveryList", deliveryDetails);
            selectInterface.onDeleteDeliveryDetailClick(deliveryDetails.size());
            selectedPosition = -1;
        }
    }


}
