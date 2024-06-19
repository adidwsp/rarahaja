package com.example.poskedai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.NumberFormat;
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
                Intent intent = new Intent(OrderActivity.this, PaymentActivity.class);
                startActivity(intent);
            }

        });
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
}
