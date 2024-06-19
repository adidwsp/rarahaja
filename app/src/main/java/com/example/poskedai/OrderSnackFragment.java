package com.example.poskedai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class OrderSnackFragment extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelDatabase> foodArrayList;
    RecyclerView order_snack;
    private AdapterOrder adapterOrder;
    private OrderActivity orderActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_snack, container, false);

        order_snack = view.findViewById(R.id.order_snack);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        order_snack.setLayoutManager(layoutManager);
        order_snack.setItemAnimator(new DefaultItemAnimator());

        orderActivity = (OrderActivity) getActivity();

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
                    if (modelDatabase != null && "Camilan".equals(modelDatabase.getMenu_type())) {
                        foodArrayList.add(modelDatabase);
                    }
                }
                adapterOrder = new AdapterOrder(getContext(), foodArrayList, orderActivity);
                order_snack.setAdapter(adapterOrder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }
}
