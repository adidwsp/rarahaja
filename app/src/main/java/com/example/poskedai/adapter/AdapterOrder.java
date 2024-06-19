package com.example.poskedai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.R;

import java.util.List;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.OrderViewHolder> {

    private Context context;
    private List<Integer> orderQuantities;

    public AdapterOrder(Context context, List<Integer> orderQuantities) {
        this.context = context;
        this.orderQuantities = orderQuantities;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_order_view, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        int quantity = orderQuantities.get(position);
        holder.editTextQuantity.setText(String.valueOf(quantity));

        holder.btn_plus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editTextQuantity.getText().toString());
            currentQuantity++;
            holder.editTextQuantity.setText(String.valueOf(currentQuantity));
            orderQuantities.set(position, currentQuantity);
        });

        holder.btn_minus.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.editTextQuantity.getText().toString());
            if (currentQuantity > 0) {
                currentQuantity--;
            }
            holder.editTextQuantity.setText(String.valueOf(currentQuantity));
            orderQuantities.set(position, currentQuantity);
        });
    }

    @Override
    public int getItemCount() {
        return orderQuantities.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageButton btn_minus, btn_plus;
        EditText editTextQuantity;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            editTextQuantity = itemView.findViewById(R.id.qty_order);
        }
    }
}
