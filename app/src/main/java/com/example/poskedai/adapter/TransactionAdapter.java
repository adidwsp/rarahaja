package com.example.poskedai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.R;
import com.example.poskedai.model.TransactionItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<TransactionItem> transactionList;

    public TransactionAdapter(List<TransactionItem> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionItem transaction = transactionList.get(position);
        holder.tvMenuName.setText(transaction.getMenu_name());

        // Format harga dengan pemisah ribuan
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMANY);
        String formattedPrice = numberFormat.format(transaction.getTotal_price());

        holder.tvTotalPrice.setText("Rp. " + formattedPrice);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMenuName, tvTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = itemView.findViewById(R.id.tv_menu_name);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
        }
    }
}
