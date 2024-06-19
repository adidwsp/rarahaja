package com.example.poskedai;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FoodFragment extends Fragment {
    AdapterDatabase adapterDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelDatabase> foodArrayList;
    RecyclerView view_menu;
    ImageButton btn_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        view_menu = view.findViewById(R.id.view_menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view_menu.setLayoutManager(layoutManager);
        view_menu.setItemAnimator(new DefaultItemAnimator());

        // Initialize the adapter with an empty list
        foodArrayList = new ArrayList<>();
        adapterDatabase = new AdapterDatabase(getContext(), foodArrayList, R.layout.data_menu);
        view_menu.setAdapter(adapterDatabase);

        showMenu();

        btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.content, new MenuFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void showMenu() {
        databaseReference.child("tb_menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodArrayList.clear();  // Clear the list first to avoid duplication
                for (DataSnapshot item : snapshot.getChildren()) {
                    ModelDatabase modelDatabase = item.getValue(ModelDatabase.class);
                    if (modelDatabase != null && "Pondasi".equals(modelDatabase.getMenu_type())) {
                        foodArrayList.add(modelDatabase);
                    }
                }
                adapterDatabase.notifyDataSetChanged();  // Notify the adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
