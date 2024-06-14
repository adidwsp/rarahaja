package com.example.poskedai;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class orderFragment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btn_add;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);
        recyclerView = findViewById(R.id.recyclerView);
        btn_add = findViewById(R.id.btn_add);

    }

}