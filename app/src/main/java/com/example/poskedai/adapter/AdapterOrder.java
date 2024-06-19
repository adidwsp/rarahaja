package com.example.poskedai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.poskedai.ModelDatabase;
import com.example.poskedai.OrderActivity;
import com.example.poskedai.R;

import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderViewHolder> {

    private Context context;
    private List<ModelDatabase> foodList;
    private OrderActivity orderActivity;

    public AdapterOrder(Context context, List<ModelDatabase> foodList, OrderActivity orderActivity) {
        this.context = context;
        this.foodList = foodList;
        this.orderActivity = orderActivity;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_order_view, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        ModelDatabase menu = foodList.get(position);
        holder.menuName.setText(menu.getMenu_name());
        holder.menuRemarks.setText(menu.getMenu_remarks());
        holder.menuPrice.setText("Rp. " + menu.getFormattedMenuPrice());
        holder.editTextQuantity.setText(String.valueOf(menu.getQty()));

        Glide.with(context).load(menu.getImageUrl()).into(holder.menuImage);

        holder.btn_plus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editTextQuantity.getText().toString());
            currentQuantity++;
            holder.editTextQuantity.setText(String.valueOf(currentQuantity));
            menu.setQty(currentQuantity);
            orderActivity.updateTotals(1, menu.getMenu_price());
        });

        holder.btn_minus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editTextQuantity.getText().toString());
            if (currentQuantity > 0) {
                currentQuantity--;
                holder.editTextQuantity.setText(String.valueOf(currentQuantity));
                menu.setQty(currentQuantity);
                orderActivity.updateTotals(-1, -menu.getMenu_price());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView menuName, menuRemarks, menuPrice;
        ImageView menuImage;
        ImageButton btn_minus, btn_plus;
        EditText editTextQuantity;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            menuRemarks = itemView.findViewById(R.id.menu_remarks);
            menuPrice = itemView.findViewById(R.id.menu_price);
            menuImage = itemView.findViewById(R.id.menu_image);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            editTextQuantity = itemView.findViewById(R.id.qty_order);
        }
    }
}
