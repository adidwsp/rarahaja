package com.example.poskedai;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OrderBeverageFragment extends Fragment {

    AdapterDatabase adapterDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelDatabase> foodArrayList;
    RecyclerView order_beverage;
    ImageButton btn_back;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_beverage, container, false);

        order_beverage = view.findViewById(R.id.order_beverage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        order_beverage.setLayoutManager(layoutManager);
        order_beverage.setItemAnimator(new DefaultItemAnimator());

        showMenu();

        return view;
    }

    private void showMenu() {
        databaseReference.child("tb_menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodArrayList = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    ModelDatabase modelDatabase = item.getValue(ModelDatabase.class);
                    if (modelDatabase != null && "Minuman".equals(modelDatabase.getMenu_type())) {
                        foodArrayList.add(modelDatabase);
                    }
                }
                adapterDatabase = new AdapterDatabase(getContext(), foodArrayList);
                order_beverage.setAdapter(adapterDatabase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}