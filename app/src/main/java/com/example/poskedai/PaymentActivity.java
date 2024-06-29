package com.example.poskedai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.adapter.AdapterPayment;
import com.example.poskedai.model.CartItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView paymentItem;
    private List<CartItem> cartItemList;
    private AdapterPayment adapterPayment;
    private TextView totalPriceTextView, totalQuantityTextView;
    private int totalQuantity = 0;
    private int totalPrice = 0;
    ImageButton btn_back;

    private DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentItem = findViewById(R.id.payment_item);
        totalQuantityTextView = findViewById(R.id.total_qty_order);
        totalPriceTextView = findViewById(R.id.total_price_order);
        btn_back = findViewById(R.id.btn_back);

        cartItemList = new ArrayList<>();
        adapterPayment = new AdapterPayment(PaymentActivity.this, cartItemList);
        paymentItem.setAdapter(adapterPayment);
        paymentItem.setLayoutManager(new LinearLayoutManager(PaymentActivity.this));

        showMenu();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showMenu() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                totalQuantity = 0;
                totalPrice = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    CartItem cartItem = item.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                        totalQuantity += cartItem.getQty();
                        totalPrice += cartItem.getQty() * cartItem.getMenu_price();
                    }
                }
                adapterPayment.notifyDataSetChanged();
                updateTotals();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotals() {
        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalPriceTextView.setText("Rp. " + NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice));
    }
}
