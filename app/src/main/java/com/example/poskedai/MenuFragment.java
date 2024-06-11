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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MenuFragment extends Fragment {
    TextView textView;
    CardView foodCard;
    CardView coffeeCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        if (view != null) {
            textView = view.findViewById(R.id.textView);
            foodCard = view.findViewById(R.id.food_card);
            coffeeCard = view.findViewById(R.id.coffee_card);

            foodCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new FoodFragment());
                }
            });

            coffeeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new CoffeeFragment());
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
