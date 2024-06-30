package com.example.poskedai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.R;
import com.example.poskedai.model.TransactionGroup;

import java.util.List;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.ViewHolder> {

    private List<TransactionGroup> transactionGroupList;

    public TransactionGroupAdapter(List<TransactionGroup> transactionGroupList) {
        this.transactionGroupList = transactionGroupList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionGroup transactionGroup = transactionGroupList.get(position);
        holder.tvId.setText(transactionGroup.gettransactionId());

        TransactionAdapter transactionAdapter = new TransactionAdapter(transactionGroup.getTransactions());
        holder.recyclerViewChild.setAdapter(transactionAdapter);
        holder.recyclerViewChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
    }

    @Override
    public int getItemCount() {
        return transactionGroupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        RecyclerView recyclerViewChild;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            recyclerViewChild = itemView.findViewById(R.id.recycler_view_child);
        }
    }
}
