package com.example.poskedai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CartItem> cartItemList;
    private AdapterCart adapterCart;
    private TextView totalQtyTextView, totalPriceTextView;
    Button btn_checkout;

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
        totalQtyTextView = view.findViewById(R.id.total_qty_order);
        totalPriceTextView = view.findViewById(R.id.total_price_order);
        btn_checkout = view.findViewById(R.id.btn_checkout);

        cartItemList = new ArrayList<>();
        adapterCart = new AdapterCart(getContext(), cartItemList);
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
                    cartItemList.add(cartItem);
                }
                adapterCart.notifyDataSetChanged(); // Beritahu adapter bahwa data telah berubah
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

