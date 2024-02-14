package com.example.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Savings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Savings extends Fragment {
    View view;
    static TextView totalSavings;
    public  static RecyclerView savingsRecyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Savings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Savings.
     */
    // TODO: Rename and change types and number of parameters
    public static Savings newInstance(String param1, String param2) {
        Savings fragment = new Savings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_savings, container, false);
        Log.d("ExpenseTracker", "Expenses onCreateView: Fragment Expenses Shown Successfully");
        savingsRecyclerView =  view.findViewById(R.id.SavingsRecyclerView);
        totalSavings  = view.findViewById(R.id.savingsTitleAmount);
        try {
            totalSavings.setText("-"+DBHelper.getTotalExpenses(getContext()));
            updateRecyclerViewSavings(getContext(),getActivity());
            Log.i("ExpenseTracker", "onCreateView: Successfully Updated Recyclerview And ExpensesTextView");
        }catch (Exception e){
            Log.e("ExpenseTracker", "Expenses onCreateView: Failed In Updated Recyclerview And ExpensesTextView"+e.toString() );
        }
        return view;
    }
    public static void updateRecyclerViewSavings(Context context, Activity activity) {
        String[] projection = {"sno","name", "amount", "type", "tag", "date", "note"};
        String selection = "type=?";
        String[] selectionArgs = {"Income"};
        ArrayList<ArrayList<String>> incomeData = DBHelper.fetchData(context, projection,selection,selectionArgs);
        ArrayList<String> updatedExpenseCustomName = new ArrayList<>();
        ArrayList<String> updatedExpenseAmount = new ArrayList<>();
        ArrayList<String> updatedExpenseType = new ArrayList<>();
        ArrayList<String> updatedExpenseTag = new ArrayList<>();
        ArrayList<String> updatedExpenseDate = new ArrayList<>();
        ArrayList<String> updatedExpenseNote = new ArrayList<>();
        ArrayList<Integer> updatedId = new ArrayList<>();

        for (ArrayList<String> row : incomeData) {
            updatedId.add(Integer.parseInt(row.get(0)));
            updatedExpenseCustomName.add(row.get(1));
            updatedExpenseAmount.add(row.get(2));
            updatedExpenseType.add(row.get(3));
            updatedExpenseTag.add(row.get(4));
            updatedExpenseDate.add(row.get(5));
            updatedExpenseNote.add(row.get(6));
        }
        CustomRecyclerView customRecyclerView = new CustomRecyclerView(updatedId, updatedExpenseAmount,updatedExpenseType, updatedExpenseTag, updatedExpenseDate, updatedExpenseCustomName,updatedExpenseNote,context);
        Savings.getSavingsRecyclerView().setLayoutManager(new LinearLayoutManager(context));
        Savings.getSavingsRecyclerView().setAdapter(customRecyclerView);
        Savings.setTotalSavings(context);
        customRecyclerView.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    Intent intent = new Intent(context, ExpensesDetails.class);
                    String customName = customRecyclerView.getCustomNameAtPosition(position);
                    String tag = customRecyclerView.getTagAtPosition(position);
                    String date = customRecyclerView.getDateAtPosition(position);
                    String amount = customRecyclerView.getAmountAtPosition(position);
                    String note = customRecyclerView.getNoteAtPosition(position);
                    String type = customRecyclerView.getTypeAtPosition(position);
                    int id = customRecyclerView.getIdAtPosition(position);
                    Log.e("position", "Position :" +position );
                    intent.putExtra("customName", customName);
                    intent.putExtra("tag", tag);
                    intent.putExtra("date", date);
                    intent.putExtra("amount", amount);
                    intent.putExtra("note", note);
                    intent.putExtra("type", type);
                    intent.putExtra("id", id);
                    Log.w("check", "onItemClick: "+id);
                    activity.startActivity(intent);
                    Log.d("ExpenseTracker", "Expenses onItemClick: StartActivity To Show And Update RecyclerView Items");
                }catch (Exception e){
                    Log.e("ExpenseTracker", "onItemClick: "+e.toString() );
                    Log.e("check", "onItemClick: "+e.toString() );
                }
            }

            @Override
            public void onItemLongClick(int position) {
                try {
                    int removedId = updatedId.remove(position);
                    updatedExpenseAmount.remove(position);
                    updatedExpenseDate.remove(position);
                    updatedExpenseNote.remove(position);
                    updatedExpenseTag.remove(position);
                    updatedExpenseType.remove(position);
                    updatedExpenseCustomName.remove(position);
                    customRecyclerView.notifyItemRemoved(position);
                    DBHelper.deleteRecord(context.getApplicationContext(), removedId);
                    Savings.setTotalSavings(context);
                    Log.i("ExpenseTracker", "Expenses onItemLongClick: Deletion Successful ");
                } catch (Exception e) {
                    Log.e("ExpenseTracker", "Expenses onItemLongClick: " + e.toString());
                }
            }
        });
    }
    public static void setTotalSavings(Context context){
        totalSavings.setText("+"+DBHelper.getTotalIncome(context));
    }
    public static RecyclerView getSavingsRecyclerView(){
        return savingsRecyclerView;
    }
}