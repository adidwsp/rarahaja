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
import com.example.poskedai.model.CartItem;
import com.example.poskedai.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;

    public AdapterCart(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_order_view, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.menuName.setText(cartItem.getMenu_name());
        holder.menuRemarks.setText(cartItem.getMenu_remarks());
        holder.menuPrice.setText("Rp. " + NumberFormat.getNumberInstance(Locale.getDefault()).format(cartItem.getMenu_price()));
        holder.quantity.setText(String.valueOf(cartItem.getQty()));
        Glide.with(context)
                .load(cartItem.getImageUrl())
                .into(holder.menu_image);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView menu_image;
        TextView menuName, menuRemarks, menuPrice, quantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            menuRemarks = itemView.findViewById(R.id.menu_remarks);
            menuPrice = itemView.findViewById(R.id.menu_price);
            quantity = itemView.findViewById(R.id.qty_order);
            menu_image = itemView.findViewById(R.id.menu_image);
        }
    }
}
