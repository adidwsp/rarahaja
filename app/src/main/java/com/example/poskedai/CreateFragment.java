package com.example.poskedai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.poskedai.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateFragment extends Fragment {

    Spinner add_menu_type;
    FloatingActionButton add;
    EditText add_menu_name, add_menu_price, add_menu_remarks;
    Button btn_add_menu;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @SuppressLint({"MissingInflatedId", "WrongViewCast", "CutPasteId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        add_menu_type = view.findViewById(R.id.spinner_add_menu_type);
        add_menu_name = view.findViewById(R.id.add_menu_name);
        add_menu_price = view.findViewById(R.id.add_menu_price);
        add_menu_remarks = view.findViewById(R.id.add_menu_remarks);
        btn_add_menu = view.findViewById(R.id.btn_add_menu);

        // Data untuk Spinner
        String[] items = new String[]{"Makanan", "Minuman"};

        // Membuat adapter untuk Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        add_menu_type.setAdapter(adapter);

        // Listener untuk Spinner
        add_menu_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btn_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getMenu_type = add_menu_type.getSelectedItem().toString();
                String getMenu_name = add_menu_name.getText().toString();
                String getPrice = add_menu_price.getText().toString();
                String getMenu_remarks = add_menu_remarks.getText().toString();

                if (getMenu_name.isEmpty()) {
                    add_menu_name.setError("Nama Menu Tidak Boleh Kosong");
                } else if (getPrice.isEmpty()) {
                    add_menu_price.setError("Harga Menu Tidak Boleh Kosong");
                } else {
                    database.child("tb_menu").push().setValue(new ModelDatabase(getMenu_type, getMenu_name, getPrice, getMenu_remarks)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                            loadFragment(new MenuFragment());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Data Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }

            private void loadFragment(Fragment fragment) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
