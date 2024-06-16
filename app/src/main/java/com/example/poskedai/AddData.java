package com.example.poskedai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AddData extends Fragment {


    EditText customerName, no_table;
    Button btn_order;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        customerName = view.findViewById(R.id.customer_name);
        no_table = view.findViewById(R.id.no_table);
        btn_order = view.findViewById(R.id.btn_order);


        return view;
    }
}
