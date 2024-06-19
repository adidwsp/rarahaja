package com.example.poskedai;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabOrderAdapter extends FragmentStateAdapter {

    public TabOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderFoodFragment();
            case 1:
                return new OrderBeverageFragment();
            case 2:
                return new OrderSnackFragment();
            default:
                return new OrderFoodFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
