package com.example.poskedai;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.poskedai.model.CartItem;
import com.example.poskedai.model.TransactionItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QRISActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private Button btn_paid;
    private DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("tb_cart");
    private DatabaseReference transactionRef = FirebaseDatabase.getInstance().getReference("tb_transaction");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qris);

        btn_back = findViewById(R.id.btn_back);
        btn_paid = findViewById(R.id.btn_paid);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QRISActivity.this);
        builder.setTitle("Konfirmasi Pembayaran");
        builder.setMessage("Apakah Anda yakin ingin mengkonfirmasi pembayaran ini?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                generateTransactionIdAndSave();
                dialog.dismiss();
                Intent intent = new Intent(QRISActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generateTransactionIdAndSave() {
        final String datePrefix = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        transactionRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String newTransactionId = datePrefix + "0001";
                if (snapshot.exists()) {
                    for (DataSnapshot lastSnapshot : snapshot.getChildren()) {
                        String lastId = lastSnapshot.getKey();
                        if (lastId != null && lastId.startsWith(datePrefix)) {
                            int lastNumber = Integer.parseInt(lastId.substring(8, 12)); // Ambil hanya bagian angka
                            newTransactionId = datePrefix + String.format(Locale.getDefault(), "%04d", lastNumber + 1);
                        }
                    }
                }
                saveTransaction(newTransactionId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QRISActivity.this, "Gagal menghasilkan ID transaksi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void saveTransaction(String idTransaction) {
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    int itemIndex = 1;
                    for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                        CartItem cartItem = cartSnapshot.getValue(CartItem.class);
                        if (cartItem != null) {
                            String itemId = idTransaction + "_" + String.format(Locale.getDefault(), "%03d", itemIndex);

                            TransactionItem transactionItem = new TransactionItem(
                                    idTransaction,
                                    itemId,
                                    currentDate,
                                    currentTime,
                                    cartItem.getId_menu(),
                                    cartItem.getMenu_type(),
                                    cartItem.getMenu_name(),
                                    cartItem.getMenu_price(),
                                    cartItem.getMenu_remarks(),
                                    cartItem.getImageUrl(),
                                    cartItem.getQty(),
                                    cartItem.getQty() * cartItem.getMenu_price()
                            );

                            transactionRef.child(itemId).setValue(transactionItem); // Simpan dengan idTransaction_item
                            itemIndex++;
                        }
                    }

                    // Hapus semua item di keranjang setelah transaksi disimpan
                    cartRef.removeValue();

                    Toast.makeText(QRISActivity.this, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QRISActivity.this, "Keranjang kosong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QRISActivity.this, "Gagal memuat data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
