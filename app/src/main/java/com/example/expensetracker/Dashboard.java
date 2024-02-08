package com.example.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
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
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {
    View view;
    public static TextView totalExpense,totalAmount,totalSavings;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public  static RecyclerView expenseRecyclerView;
    CustomRecyclerView customRecyclerView;
    DBHelper dbHelper;
    FragmentContainerView fragmentContainerView;
    public ArrayList<String> expenseAmount;
    public ArrayList<String> expenseDate;
    public ArrayList<String> expenseType;
    public ArrayList<String> expenseCustomName;
    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
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
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        expenseRecyclerView =  view.findViewById(R.id.expenseRecyclerView);
        totalExpense = view.findViewById(R.id.totalExpenses);
        totalAmount = view.findViewById(R.id.totalAmount);
        totalSavings = view.findViewById(R.id.totalIncome);
        try {
            updateRecyclerViewData(getContext(),expenseRecyclerView,getActivity());
            Log.d("Dashboard", "Dashboard is updated Successfully");
        }catch (Exception e){
            Log.e("Dashboard", e.toString());
        }
        return view;
    }
    public static void updateRecyclerViewData(Context context, RecyclerView recyclerView, Activity activity) {
        String[] projection = {"name", "amount", "type", "tag", "date", "note"};
        ArrayList<ArrayList<String>> incomeData = DBHelper.fetchData(context, projection);
        ArrayList<String> updatedExpenseCustomName = new ArrayList<>();
        ArrayList<String> updatedExpenseAmount = new ArrayList<>();
        ArrayList<String> updatedExpenseType = new ArrayList<>();
        ArrayList<String> updatedExpenseTag = new ArrayList<>();
        ArrayList<String> updatedExpenseDate = new ArrayList<>();
        ArrayList<String> updatedExpenseNote = new ArrayList<>();
        ArrayList<ImageView> images = new ArrayList<>();
        // Separating the dataList
        for (ArrayList<String> row : incomeData) {
            updatedExpenseCustomName.add(row.get(0));
            updatedExpenseAmount.add(row.get(1));
            updatedExpenseType.add(row.get(2));
            updatedExpenseTag.add(row.get(3));
            updatedExpenseDate.add(row.get(4));
            updatedExpenseNote.add(row.get(5));
            images.add(Expenses.img);
        }
        CustomRecyclerView customRecyclerView = new CustomRecyclerView(images, updatedExpenseAmount,updatedExpenseType, updatedExpenseTag, updatedExpenseDate, updatedExpenseCustomName,updatedExpenseNote,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(customRecyclerView);
        totalAmount.setText(""+(DBHelper.getTotalIncome(context) + DBHelper.getTotalExpenses(context)));
        totalExpense.setText("-"+DBHelper.getTotalExpenses(context));
        totalSavings.setText("+"+DBHelper.getTotalIncome(context));
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
                Animation rotateAnimation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.rotate_animation);
                // Get the ViewHolder associated with the long-clicked item
                RecyclerView.ViewHolder viewHolder = expenseRecyclerView.findViewHolderForAdapterPosition(position);
                if (viewHolder instanceof CustomRecyclerView.ViewHolder) {
                    CustomRecyclerView.ViewHolder customViewHolder = (CustomRecyclerView.ViewHolder) viewHolder;
                    ImageView img = customViewHolder.image;
                    img.startAnimation(rotateAnimation);
                    img.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.selector_icon));
                }
            }

        });
        Log.d("MainActivity", "updateRecyclerViewData: CustomRecyclerView is Getting updated");
    }

}