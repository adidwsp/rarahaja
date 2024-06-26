// MenuFragment.java
package com.example.poskedai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MenuFragment extends Fragment {
    TextView textView;
    CardView foodCard, beverageCard, snackCard;
    private FloatingActionButton btn_add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        if (view != null) {
            foodCard = view.findViewById(R.id.food_card);
            beverageCard = view.findViewById(R.id.beverage_card);
            snackCard = view.findViewById(R.id.snack_card);
            btn_add = view.findViewById(R.id.btn_add);

            foodCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new FoodFragment());
                }
            });

            beverageCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new BeverageFragment());
                }
            });
            snackCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new SnackFragment());
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { loadFragment(new CreateFragment());
                }
            });
        }
        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
