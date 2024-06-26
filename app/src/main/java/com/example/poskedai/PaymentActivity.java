package com.example.poskedai;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView paymentItem;
    private List<CartItem> cartItemList;
    private AdapterPayment adapterPayment;
    private TextView totalPriceTextView, totalQuantityTextView;
    private int totalQuantity = 0;
    private int totalPrice = 0;

    private DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentItem = findViewById(R.id.payment_item);
        totalQuantityTextView = findViewById(R.id.total_quantity_order);
        totalPriceTextView = findViewById(R.id.total_price_order);

        cartItemList = new ArrayList<>();
        adapterPayment = new AdapterPayment(PaymentActivity.this, cartItemList);
        paymentItem.setAdapter(adapterPayment);
        paymentItem.setLayoutManager(new LinearLayoutManager(PaymentActivity.this));

        showMenu();

    }

    private void showMenu() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear(); // Bersihkan list sebelum menambahkan data baru
                for (DataSnapshot item : snapshot.getChildren()) {
                    CartItem cartItem = item.getValue(CartItem.class);
                    cartItemList.add(cartItem);
                }
                adapterPayment.notifyDataSetChanged(); // Beritahu adapter bahwa data telah berubah
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PaymentActivity.this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateTotals() {
        DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference("tb_cart");
        paymentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                double totalQuantityTextView = 0;
                double totalPriceTextView = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()) {
                        Double qty = snapshot.child("qty").getValue(Double.class);
                        Double price = snapshot.child("menu_price").getValue(Double.class);

                        if (qty != null && price != null) {
                            totalQuantityTextView += qty;
                            totalPriceTextView += price * qty; // Kalkulasi total price berdasarkan qty
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }

        });


    }
}
