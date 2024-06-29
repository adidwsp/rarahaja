package com.example.poskedai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.poskedai.R;
import com.example.poskedai.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.CartViewHolder> {

    private Context context;
    private List<CartItem> paymentItemList;

    public AdapterPayment(Context context, List<CartItem> paymentItemList) {
        this.context = context;
        this.paymentItemList = paymentItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_payment, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = paymentItemList.get(position);
        holder.menuName.setText(cartItem.getMenu_name());
        holder.menuPrice.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(cartItem.getMenu_price()));
        holder.quantity.setText(String.valueOf(cartItem.getQty()));
        holder.total_item_price.setText("Rp. " + NumberFormat.getNumberInstance(Locale.getDefault()).format(cartItem.getQty() * cartItem.getMenu_price()));
        Glide.with(context)
                .load(cartItem.getImageUrl())
                .into(holder.menu_image);

    }

    @Override
    public int getItemCount() {
        return paymentItemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView menu_image;
        TextView menuName, menuRemarks, menuPrice, quantity, total_item_price;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            menuRemarks = itemView.findViewById(R.id.menu_remarks);
            menuPrice = itemView.findViewById(R.id.menu_price);
            quantity = itemView.findViewById(R.id.qty_order);
            menu_image = itemView.findViewById(R.id.menu_image);
            total_item_price = itemView.findViewById(R.id.total_item_price);
        }
    }
}
