package com.example.poskedai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.poskedai.adapter.AdapterOrder;
import com.example.poskedai.model.CartItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageButton btn_back;
    private TextView totalQuantityTextView;
    private TextView totalPriceTextView;
    private Button btn_checkout;

    private int totalQuantity = 0;
    private int totalPrice = 0;
    private List<ModelDatabase> foodList;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private AdapterOrder adapterOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        foodList = new ArrayList<>();
        adapterOrder = new AdapterOrder(this, foodList, this);

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
                checkCartBeforeExit();
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });
    }

    public void updateTotals(int quantity, int price) {
        totalQuantity += quantity;
        totalPrice += price;

        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalPriceTextView.setText("Rp " + NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice));
    }

    public void addToCart(ModelDatabase menu) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
        String key = cartRef.push().getKey();
        if (key != null) {
            cartRef.child(key).setValue(new CartItem(menu.getId_menu(), menu.getMenu_name(), menu.getMenu_type(), menu.getMenu_remarks(), menu.getMenu_price(), menu.getQty(), menu.getQty() * menu.getMenu_price(), menu.getImageUrl()));
        }
    }

    public void updateCart(ModelDatabase menu) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
        cartRef.orderByChild("id_menu").equalTo(menu.getId_menu()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem cartItem = snapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItem.setTotal_price(menu.getQty() * menu.getMenu_price());
                        cartItem.setQty(menu.getQty());
                        cartItem.setImageUrl(menu.getImageUrl());
                        snapshot.getRef().setValue(cartItem);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public void removeFromCart(String id_menu) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
        cartRef.orderByChild("id_menu").equalTo(id_menu).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void checkCartBeforeExit() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    showSaveConfirmationDialog();
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OrderActivity.this, "Gagal Menyimpan ke Keranjang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSaveConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Simpan ke keranjang?")
                .setMessage("Apakah Anda ingin menyimpan item di keranjang?")
                .setPositiveButton("Simpan", (dialog, which) -> finish())
                .setNegativeButton("Tidak", (dialog, which) -> clearCartAndFinish())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void clearCartAndFinish() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
        cartRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(OrderActivity.this, "Keranjang dikosongkan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(OrderActivity.this, "Gagal mengosongkan keranjang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        checkCartBeforeExit();
    }
}
