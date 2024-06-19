package com.example.poskedai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        holder.menu_price.setText("Rp. " + data.getMenu_price());
        holder.menu_remarks.setText(data.getMenu_remarks());
        // Load image using Glide
        Glide.with(context)
                .load(data.getImageUrl())
                .into(holder.menu_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView menu_image;
        TextView menu_name, menu_price, menu_remarks;
        CardView card_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = itemView.findViewById(R.id.menu_name);
            menu_price = itemView.findViewById(R.id.menu_price);
            menu_remarks = itemView.findViewById(R.id.menu_remarks);
            menu_image = itemView.findViewById(R.id.menu_image);
            card_menu = itemView.findViewById(R.id.card_menu);
        }
    }
}
