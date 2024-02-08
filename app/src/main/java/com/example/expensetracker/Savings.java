package com.example.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
    public  static TextView totalSavings;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_savings, container, false);
        savingsRecyclerView =  view.findViewById(R.id.SavingsRecyclerView);
        totalSavings = view.findViewById(R.id.savingsTitleAmount);
        totalSavings.setText("+"+DBHelper.getTotalIncome(getContext()));
        try {
            updateRecyclerViewSavings(getContext(),savingsRecyclerView,getActivity());
            Log.d("Dashboard", "Dashboard is updated Successfully");
        }catch (Exception e){
            Log.e("Dashboard", e.toString());
        }
        return view;
    }
    public static void updateRecyclerViewSavings(Context context, RecyclerView recyclerView, Activity activity) {
//        DBHelper dbHelper = new DBHelper();
        String[] projection = {"sno","name", "amount", "type", "tag", "date", "note"};
        String selection = "type=?";
        String[] selectionArgs = {"Income"};

        ArrayList<ArrayList<String>> incomeData = DBHelper.fetchData(context, projection, selection, selectionArgs);
        ArrayList<String> updatedExpenseCustomName = new ArrayList<>();
        ArrayList<String> updatedExpenseAmount = new ArrayList<>();
        ArrayList<String> updatedExpenseType = new ArrayList<>();
        ArrayList<String> updatedExpenseTag = new ArrayList<>();
        ArrayList<String> updatedExpenseDate = new ArrayList<>();
        ArrayList<String> updatedExpenseNote = new ArrayList<>();
        ArrayList<Integer> updatedExpensesId = new ArrayList<>();
        ArrayList<Integer> upddatedId = new ArrayList<>();
        ArrayList<ImageView> images = new ArrayList<>();

        // Extract data from incomeData and populate corresponding ArrayLists
        for (ArrayList<String> row : incomeData) {
            upddatedId.add(Integer.parseInt(row.get(0)));
            updatedExpenseCustomName.add(row.get(1));
            updatedExpenseAmount.add(row.get(2));
            updatedExpenseType.add(row.get(3));
            updatedExpenseTag.add(row.get(4));
            updatedExpenseDate.add(row.get(5));
            updatedExpenseNote.add(row.get(6));
            images.add(Expenses.img);
        }

        CustomRecyclerView customRecyclerView = new CustomRecyclerView(updatedExpensesId,images, updatedExpenseAmount, updatedExpenseType,updatedExpenseTag, updatedExpenseDate, updatedExpenseCustomName, updatedExpenseAmount,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(customRecyclerView);
        setTotalSavings(context);
        customRecyclerView.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, ExpensesDetails.class);
                String customName = customRecyclerView.getCustomNameAtPosition(position);
                String tag = customRecyclerView.getTagAtPosition(position);
                String date = customRecyclerView.getDateAtPosition(position);
                String amount = customRecyclerView.getAmountAtPosition(position);
                String note = customRecyclerView.getNoteAtPosition(position);
                String type = customRecyclerView.getTypeAtPosition(position);
                intent.putExtra("customName", customName);
                intent.putExtra("tag", tag);
                intent.putExtra("date", date);
                intent.putExtra("amount", amount);
                intent.putExtra("note", note);
                intent.putExtra("type", type);
                activity.startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position) {
                DBHelper.deleteRecord(context.getApplicationContext(), upddatedId.get(position));
                updatedExpenseAmount.remove(position);
                updatedExpenseDate.remove(position);
                updatedExpenseNote.remove(position);
                updatedExpenseTag.remove(position);
                updatedExpenseType.remove(position);
                updatedExpenseCustomName.remove(position);
                customRecyclerView.notifyItemRemoved(position);
                setTotalSavings(context);
            }
        });

        Log.d("ExpensesAndIncome", "updateRecyclerViewExpensesAndIncome: CustomRecyclerView is Getting updated");
    }
    public static void setTotalSavings(Context context){
        totalSavings.setText("+"+DBHelper.getTotalIncome(context));
    }
}