package com.example.expensetracker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.ViewHolder> {
    private ArrayList<ImageView> images;
    private static ArrayList<Boolean> itemSelectedStates;
    private ArrayList<String> expenseAmount;
    private ArrayList<String> expenseDate;
    private ArrayList<String> expenseType;
    private ArrayList<String> expenseCustomName;
    private static OnItemClickListener onItemClickListener;

    public CustomRecyclerView(ArrayList<ImageView> images, ArrayList<String> expenseAmount, ArrayList<String> expenseType, ArrayList<String> expenseDate, ArrayList<String> expenseCustomName, Context context){
        this.images = images;
        this.expenseAmount = expenseAmount;
        this.expenseType = expenseType;
        this.expenseCustomName = expenseCustomName;
        this.expenseDate = expenseDate;
        this.itemSelectedStates = new ArrayList<>(Collections.nCopies(images.size(), false)); // Initialize all items as not selected
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView type;
        public TextView amount;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.expenseImageType);
            name = itemView.findViewById(R.id.expenseCustomName);
            type = itemView.findViewById(R.id.expenseType);
            date = itemView.findViewById(R.id.expenseDate);
            amount = itemView.findViewById(R.id.expenseAmount);

            itemView.setOnLongClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemLongClick(position);
                        return true; // Consume the long click event
                    }
                }
                return false;
            });

            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                        toggleItemSelected(position); // Toggle selected state
                        itemView.setSelected(isItemSelected(position)); // Update selected state in UI
                    }
                }
            });
        }

        public void bindData(String name, String amount, String type, String date, boolean isSelected) {
            this.name.setText(name);
            this.amount.setText(amount);
            this.type.setText(type);
            this.date.setText(date);
            itemView.setSelected(isSelected);
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    public void updateData(ArrayList<String> updatedExpenseAmount, ArrayList<String> updatedExpenseType, ArrayList<String> updatedExpenseDate, ArrayList<String> updatedExpenseCustomName) {
        this.expenseAmount = updatedExpenseAmount;
        this.expenseType = updatedExpenseType;
        this.expenseDate = updatedExpenseDate;
        this.expenseCustomName = updatedExpenseCustomName;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerView.ViewHolder holder, int position) {
        String name = expenseCustomName.get(position);
        String amount = expenseAmount.get(position);
        String date = expenseDate.get(position);
        String type = expenseType.get(position);
        boolean isSelected = isItemSelected(position);

        holder.bindData(name, amount, type, date, isSelected);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

//    public void onItemLongClick(int position) {
//        toggleItemSelected(position);
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return expenseCustomName.size();
    }

    public String getCustomNameAtPosition(int position) {
        if (position >= 0 && position < expenseCustomName.size()) {
            return expenseCustomName.get(position);
        }
        return null;
    }

    public String getDateAtPosition(int position) {
        if (position >= 0 && position < expenseCustomName.size()) {
            return expenseCustomName.get(position);
        }
        return null;
    }

    public String getAtPosition(int position) {
        if (position >= 0 && position < expenseCustomName.size()) {
            return expenseCustomName.get(position);
        }
        return null;
    }

    public ImageView getItemImageViewAtPosition(int position) {
        if (position >= 0 && position < expenseCustomName.size()) {
            return images.get(position);
        }
        return null;
    }

    public static void toggleItemSelected(int position) {
        itemSelectedStates.set(position, !itemSelectedStates.get(position));
        Log.d("CustomRecyclerView", "toggleItemSelected: Working");
        System.out.println("maya");
    }

    public static boolean isItemSelected(int position) {
        return itemSelectedStates.get(position);
    }
}
