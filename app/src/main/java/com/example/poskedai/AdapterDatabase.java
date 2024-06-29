package com.example.poskedai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterDatabase extends RecyclerView.Adapter<AdapterDatabase.ViewHolder> {
    private List<ModelDatabase> list;
    private Context context;
    private int layoutResourceId;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterDatabase(Context context, List<ModelDatabase> list, int layoutResourceId) {
        this.list = list;
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutResourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelDatabase data = list.get(position);
        holder.menu_name.setText(data.getMenu_name());
        holder.menu_price.setText("Rp. " + data.getFormattedMenuPrice());
        holder.menu_remarks.setText(data.getMenu_remarks());
        // Load image using Glide
        Glide.with(context)
                .load(data.getImageUrl())
                .into(holder.menu_image);

        // Set onClickListener for Edit button
        holder.btn_edit_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                EditMenuFragment editMenuFragment = EditMenuFragment.newInstance(data);
                transaction.replace(R.id.content, editMenuFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView menu_image;
        TextView menu_name, menu_price, menu_remarks;
        ImageButton btn_edit_menu;
        CardView card_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = itemView.findViewById(R.id.menu_name);
            menu_price = itemView.findViewById(R.id.menu_price);
            menu_remarks = itemView.findViewById(R.id.menu_remarks);
            menu_image = itemView.findViewById(R.id.menu_image);
            btn_edit_menu = itemView.findViewById(R.id.btn_edit_menu);
            card_menu = itemView.findViewById(R.id.card_menu);
        }
    }
}
