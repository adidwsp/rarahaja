package com.example.poskedai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poskedai.adapter.AdapterCart;
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

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CartItem> cartItemList;
    private AdapterCart adapterCart;
    private TextView totalQuantityTextView, totalPriceTextView;
    Button btn_checkout;
    ImageButton btn_plus, btn_minus;
    private int totalQuantity = 0;
    private int totalPrice = 0;

    private DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");

    public CartFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_order_list);
        totalQuantityTextView = view.findViewById(R.id.total_quantity_order);
        totalPriceTextView = view.findViewById(R.id.total_price_order);
        btn_checkout = view.findViewById(R.id.btn_checkout);

        cartItemList = new ArrayList<>();
        adapterCart = new AdapterCart(getContext(), cartItemList, this);
        recyclerView.setAdapter(adapterCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        showMenu();

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }

        });




        return view;

    }

    private void showMenu() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear(); // Bersihkan list sebelum menambahkan data baru
                for (DataSnapshot item : snapshot.getChildren()) {
                    CartItem cartItem = item.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                        totalQuantity += cartItem.getQty();
                        totalPrice += cartItem.getQty() * cartItem.getMenu_price();
                    }
                }
                adapterCart.notifyDataSetChanged(); // Beritahu adapter bahwa data telah berubah
                updateTotals();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotals() {
        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalPriceTextView.setText("Rp. " + NumberFormat.getNumberInstance(Locale.getDefault()).format(totalPrice));
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
}

