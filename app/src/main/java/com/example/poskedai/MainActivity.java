package com.example.poskedai;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navbar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homepage) {
                    openFragment(new MainFragment());
                    return true;
                }
                if (item.getItemId() == R.id.transaction) {
                    openFragment(new TransactionFragment());
                    return true;
                }
                if (item.getItemId() == R.id.menu) {
                    openFragment(new MenuFragment());
                    return true;
                }
                if (item.getItemId() == R.id.cart) {
                    openFragment(new CartFragment());
                    return true;
                }
                if (item.getItemId() == R.id.account) {
                    openFragment(new TransactionFragment());
                    return true;
                }
                return false;
            }
        });
        openFragment(new MainFragment());
    }

    Boolean openFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}