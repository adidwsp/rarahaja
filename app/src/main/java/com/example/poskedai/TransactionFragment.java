package com.example.poskedai;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.adapter.TransactionGroupAdapter;
import com.example.poskedai.model.TransactionGroup;
import com.example.poskedai.model.TransactionItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionFragment extends Fragment {

    private RecyclerView recyclerViewParent;
    private TransactionGroupAdapter transactionGroupAdapter;
    private List<TransactionGroup> transactionGroupList;
    private DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference("tb_transaction");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        recyclerViewParent = view.findViewById(R.id.recycler_view_parent);
        transactionGroupList = new ArrayList<>();
        transactionGroupAdapter = new TransactionGroupAdapter(transactionGroupList);
        recyclerViewParent.setAdapter(transactionGroupAdapter);
        recyclerViewParent.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTransactions();

        return view;
    }

    private void loadTransactions() {
        transactionRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, List<TransactionItem>> groupedTransactions = new HashMap<>();

                for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                    TransactionItem transaction = transactionSnapshot.getValue(TransactionItem.class);
                    if (transaction != null) {
                        String idTransaction = transaction.getId_transaction();
                        if (!groupedTransactions.containsKey(idTransaction)) {
                            groupedTransactions.put(idTransaction, new ArrayList<>());
                        }
                        groupedTransactions.get(idTransaction).add(transaction);
                    }
                }

                transactionGroupList.clear();
                List<String> sortedKeys = new ArrayList<>(groupedTransactions.keySet());
                sortedKeys.sort(Collections.reverseOrder()); // Mengurutkan dari transaksi terbaru ke yang lama

                for (String key : sortedKeys) {
                    transactionGroupList.add(new TransactionGroup(key, groupedTransactions.get(key)));
                }

                transactionGroupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load transactions: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
