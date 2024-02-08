package com.example.expensetracker;

import android.content.Context;
import android.graphics.Color;
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
    private ArrayList<String> expenseTag;
    private ArrayList<String> expenseType;
    private ArrayList<String> expenseNote;
    private ArrayList<String> expenseCustomName;
    private static OnItemClickListener onItemClickListener;

    public CustomRecyclerView(ArrayList<ImageView> images, ArrayList<String> expenseAmount, ArrayList<String> expenseType, ArrayList<String> expenseTag,ArrayList<String> expenseDate, ArrayList<String> expenseCustomName, ArrayList<String> expenseNote,Context context){
        this.images = images;
        this.expenseAmount = expenseAmount;
        this.expenseType = expenseType;
        this.expenseTag = expenseTag;
        this.expenseCustomName = expenseCustomName;
        this.expenseDate = expenseDate;
        this.expenseNote = expenseNote;
        this.itemSelectedStates = new ArrayList<>(Collections.nCopies(images.size(), false)); // Initialize all items as not selected
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView tag;
        public TextView amount;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.expenseImageType);
            name = itemView.findViewById(R.id.expenseCustomName);
            tag = itemView.findViewById(R.id.expenseTag);
            date = itemView.findViewById(R.id.expenseDate);
            amount = itemView.findViewById(R.id.expenseAmount);
//            note = itemView.findViewById(R.id.expenseAmount);

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
                        itemView.setSelected(isItemSelected(position)); // Update selected state in UI
                    }
                }
            });
        }

        public void bindData(String name, String amount,String type , String tag, String date) {
            this.name.setText(name);
            if (type.equals("Income")){
                this.amount.setText("+"+amount);
                this.amount.setTextColor(Color.parseColor("#28BD78"));
            }else{
                this.amount.setText("-"+amount);
                this.amount.setTextColor(Color.parseColor("#d44444"));
            }
            this.tag.setText(tag);
            this.date.setText(date);
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_recycler_view,parent,false);
        return new ViewHolder(view);
    }

    public void updateData(ArrayList<String> updatedExpenseAmount, ArrayList<String> updatedExpenseTag, ArrayList<String> updatedExpenseDate, ArrayList<String> updatedExpenseCustomName) {
        this.expenseAmount = updatedExpenseAmount;
        this.expenseTag = updatedExpenseTag;
        this.expenseDate = updatedExpenseDate;
        this.expenseCustomName = updatedExpenseCustomName;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerView.ViewHolder holder, int position) {
        String name = expenseCustomName.get(position);
        String amount = expenseAmount.get(position);
        String date = expenseDate.get(position);
        String type = expenseType.get(position);
        String tag = expenseTag.get(position);
        holder.bindData(name, amount, type,tag, date);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

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
        if (position >= 0 && position < expenseDate.size()) {
            return expenseDate.get(position);
        }
        return null;
    }
    public String getTagAtPosition(int position) {
        if (position >= 0 && position < expenseTag.size()) {
            return expenseTag.get(position);
        }
        return null;
    }

    public String getAmountAtPosition(int position) {
        if (position >= 0 && position < expenseAmount.size()) {
            return expenseAmount.get(position);
        }
        return null;
    }
    public String getNoteAtPosition(int position) {
        if (position >= 0 && position < expenseNote.size()) {
            return expenseNote.get(position);
        }
        return null;
    }
    public String getTypeAtPosition(int position) {
        if (position >= 0 && position < expenseType.size()) {
            return expenseType.get(position);
        }
        return null;
    }
    public ImageView getItemImageViewAtPosition(int position) {
        if (position >= 0 && position < images.size()) {
            return images.get(position);
        }
        return null;
    }
    public static boolean isItemSelected(int position) {
        return itemSelectedStates.get(position);
    }
}
