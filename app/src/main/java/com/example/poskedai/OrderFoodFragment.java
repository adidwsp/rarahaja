package com.example.poskedai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.adapter.AdapterOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderFoodFragment extends Fragment {
    AdapterDatabase adapterDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelDatabase> foodArrayList;
    RecyclerView order_food;
    ImageButton btn_back;
    private RecyclerView recyclerView;
    private AdapterOrder adapterOrder;
    private List<Integer> orderQuantities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_food, container, false);

        order_food = view.findViewById(R.id.order_food);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        order_food.setLayoutManager(layoutManager);
        order_food.setItemAnimator(new DefaultItemAnimator());

        showMenu();

        recyclerView = view.findViewById(R.id.order_food);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inisialisasi daftar jumlah makanan dengan contoh data
        orderQuantities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            orderQuantities.add(0); // Set awal jumlah pesanan ke 0
        }

        adapterOrder = new AdapterOrder(getContext(), orderQuantities);
        recyclerView.setAdapter(adapterOrder);

        return view;
    }

    private void showMenu() {
        databaseReference.child("tb_menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodArrayList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    ModelDatabase modelDatabase = item.getValue(ModelDatabase.class);
                    if (modelDatabase != null && "Pondasi".equals(modelDatabase.getMenu_type())) {
                        foodArrayList.add(modelDatabase);
                    }
                }
                adapterDatabase = new AdapterDatabase(getContext(), foodArrayList, R.layout.menu_order_view);
                order_food.setAdapter(adapterDatabase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
