package com.example.poskedai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.poskedai.model.ModelOrder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageButton btn_back;
    private TextView totalQuantityTextView;
    private TextView totalPriceTextView;
    Button btn_checkout;

    private int totalQuantity = 0;
    private int totalPrice = 0;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        totalQuantityTextView = findViewById(R.id.total_qty_order);
        totalPriceTextView = findViewById(R.id.total_price);
        tabLayout = findViewById(R.id.tab_menu);
        viewPager = findViewById(R.id.viewPager);
        btn_back = findViewById(R.id.btn_back);
        btn_checkout = findViewById(R.id.btn_checkout);

        totalQuantityTextView.setText("0");
        totalPriceTextView.setText("Rp 0.00");

        TabOrderAdapter tabOrderAdapter = new TabOrderAdapter(this);
        viewPager.setAdapter(tabOrderAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Pondasi");
                        break;
                    case 1:
                        tab.setText("Koncian");
                        break;
                    case 2:
                        tab.setText("Camilan");
                        break;
                }
            }
        }).attach();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextOrderId(new OrderIdCallback() {
                    @Override
                    public void onOrderIdGenerated(String orderId) {
                        saveOrderToDatabase(orderId);
                        Intent intent = new Intent(OrderActivity.this, PaymentActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void getNextOrderId(OrderIdCallback callback) {
        DatabaseReference orderCountRef = databaseReference.child("order_count");
        orderCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long orderCount = snapshot.exists() ? (long) snapshot.getValue() : 0;
                orderCount++;
                orderCountRef.setValue(orderCount);
                String orderId = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()) + String.format("%04d", orderCount);
                callback.onOrderIdGenerated(orderId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderActivity.this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveOrderToDatabase(String id_order) {
        ArrayList<ModelOrder> orders = new ArrayList<>();

        // Assuming you have a method to get all items in the cart
        for (CartItem item : getCartItems()) {
            if (item.getQuantity() > 0) {
                String id_menu = item.getId();
                String order_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String order_time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                int quantity = item.getQuantity();
                int total_price = item.getPrice() * quantity;

                ModelOrder order = new ModelOrder(id_order, id_menu, order_date, order_time, quantity, total_price);
                orders.add(order);
            }
        }

        for (ModelOrder order : orders) {
            databaseReference.child("tb_cart").child(order.getId_order()).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        // Order saved successfully
                        Toast.makeText(OrderActivity.this, "Order saved successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Failed to save order
                        Toast.makeText(OrderActivity.this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void updateTotals(int quantity, int price) {
        totalQuantity += quantity;
        totalPrice += price;

        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalPriceTextView.setText("Rp. " + getFormattedTotalPrice());
    }

    public String getFormattedTotalPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);
        return numberFormat.format(totalPrice);
    }

    interface OrderIdCallback {
        void onOrderIdGenerated(String orderId);
    }

    // Dummy method to get items in the cart, replace with actual implementation
    private ArrayList<CartItem> getCartItems() {
        ArrayList<CartItem> cartItems = new ArrayList<>();
        // Add items to cartItems
        return cartItems;
    }
}
