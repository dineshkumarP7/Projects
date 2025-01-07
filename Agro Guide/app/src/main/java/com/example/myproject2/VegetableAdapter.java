package com.example.myproject2;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VegetableAdapter extends RecyclerView.Adapter<VegetableAdapter.VegetableViewHolder> {

    private Context context;
    private List<VegetableItem> vegetableList;

    // Constructor
    public VegetableAdapter(Context context, List<VegetableItem> vegetableList) {
        this.context = context;
        this.vegetableList = vegetableList;
    }

    @NonNull
    @Override
    public VegetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the CardView layout
        View view = LayoutInflater.from(context).inflate(R.layout.card_vegetable_item, parent, false);
        return new VegetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VegetableViewHolder holder, int position) {
        // Get the current VegetableItem
        VegetableItem vegetableItem = vegetableList.get(position);

        // Bind data to the views
        holder.itemName.setText(vegetableItem.getItemName());
        holder.district.setText(vegetableItem.getDistrict());
        holder.date.setText(vegetableItem.getDate());
        holder.price.setText("Rs: ₹"+vegetableItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return vegetableList.size();
    }

    // ViewHolder class to hold references to views
    public static class VegetableViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, district, date, price;

        public VegetableViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.ItemName1);
            district = itemView.findViewById(R.id.District1);
            date = itemView.findViewById(R.id.Date1);
            price = itemView.findViewById(R.id.Price1);
        }
    }
}
