package com.example.poskedai;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddData extends AppCompatActivity {

    Database transactions;
    EditText customerName, no_table;
    Button save;
    protected void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        customerName = findViewById(R.id.customer_name);
        no_table = findViewById(R.id.no_table);
        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
            }
        });
    }

    void save_data() {
        transactions.save_data(
                customerName.getText().toString(),
                no_table.getText().toString()
        );
    }
}
