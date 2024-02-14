package com.example.expensetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {
    View view;
    public static TextView totalExpense,totalAmount,totalSavings,remainingAmount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public  static RecyclerView expenseRecyclerView;
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
        Log.d("ExpenseTracker", "onCreateView: Successfully Shown The FragmentDashboard");
        expenseRecyclerView =  view.findViewById(R.id.expenseRecyclerView);
        totalExpense = view.findViewById(R.id.totalExpenses);
        totalAmount = view.findViewById(R.id.totalAmount);
        totalSavings = view.findViewById(R.id.totalIncome);
        remainingAmount = view.findViewById(R.id.remainingAmount);
        try {
            updateRecyclerViewData(getContext(),expenseRecyclerView,getActivity());
            Log.d("Dashboard", "Dashboard is updated Successfully");
            Log.d("ExpenseTracker", "Dashboard onCreateView: DashBoard Is Getting Updated Successfully");
        }catch (Exception e){
            Log.e("Dashboard", e.toString());
            Log.d("ExpenseTracker", "Dashboard onCreateView: "+e.toString());
        }
        return view;
    }
    public static void updateRecyclerViewData(Context context, RecyclerView recyclerView, Activity activity) {
        String[] projection = {"sno","name", "amount", "type", "tag", "date", "note"};
        ArrayList<ArrayList<String>> incomeData = DBHelper.fetchData(context, projection);
        ArrayList<String> updatedExpenseCustomName = new ArrayList<>();
        ArrayList<Integer> updatedId = new ArrayList<>();
        ArrayList<String> updatedExpenseAmount = new ArrayList<>();
        ArrayList<String> updatedExpenseType = new ArrayList<>();
        ArrayList<String> updatedExpenseTag = new ArrayList<>();
        ArrayList<String> updatedExpenseDate = new ArrayList<>();
        ArrayList<String> updatedExpenseNote = new ArrayList<>();
        ArrayList<ImageView> images = new ArrayList<>();

        for (ArrayList<String> row : incomeData) {
            updatedId.add(Integer.parseInt(row.get(0)));
            updatedExpenseCustomName.add(row.get(1));
            updatedExpenseAmount.add(row.get(2));
            updatedExpenseType.add(row.get(3));
            updatedExpenseTag.add(row.get(4));
            updatedExpenseDate.add(row.get(5));
            updatedExpenseNote.add(row.get(6));
            images.add(Expenses.img);
        }
        CustomRecyclerView customRecyclerView = new CustomRecyclerView(updatedId,images, updatedExpenseAmount,updatedExpenseType, updatedExpenseTag, updatedExpenseDate, updatedExpenseCustomName,updatedExpenseNote,context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(customRecyclerView);
        Dashboard.setAllAmounts(context);
        customRecyclerView.setOnItemClickListener(new CustomRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    MainActivity.playOnClickSound(activity);
                    Intent intent = new Intent(context, ExpensesDetails.class);
                    String customName = customRecyclerView.getCustomNameAtPosition(position);
                    String tag = customRecyclerView.getTagAtPosition(position);
                    String date = customRecyclerView.getDateAtPosition(position);
                    String amount = customRecyclerView.getAmountAtPosition(position);
                    String note = customRecyclerView.getNoteAtPosition(position);
                    String type = customRecyclerView.getTypeAtPosition(position);
                    int id = customRecyclerView.getIdAtPosition(position);
//                    HashMap<String, Object> images = new HashMap<>();
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
                    Log.d("ExpenseTracker", "Dashboard onItemClick: StartActivity To Show And Update RecyclerView Items");
                }catch (Exception e){
                    Log.e("ExpenseTracker", "onItemClick: "+e.toString() );
                    Log.e("check", "onItemClick: "+e.toString() );
                }
            }

            @Override
            public void onItemLongClick(int position) {
                try {
                    MainActivity.playOnDeleteSound(activity);
                    customRecyclerView.notifyItemRemoved(position);
                    int removedId = updatedId.remove(position);
                    updatedExpenseAmount.remove(position);
                    updatedExpenseDate.remove(position);
                    updatedExpenseNote.remove(position);
                    updatedExpenseTag.remove(position);
                    updatedExpenseType.remove(position);
                    updatedExpenseCustomName.remove(position);
                    DBHelper.deleteRecord(context.getApplicationContext(), removedId);
                    Dashboard.setAllAmounts(context);
                    Log.i("ExpenseTracker", "Dashboard onItemLongClick: Deletion Successful "+position);
                } catch (Exception e) {
                    Log.e("ExpenseTracker", "Dashboard onItemLongClick: " + e.toString());
                }
            }
        });
    }
    public static void setAllAmounts(Context context){
        totalAmount.setText(""+(DBHelper.getTotalIncome(context) + DBHelper.getTotalExpenses(context)));
        int val = DBHelper.getTotalIncome(context) - DBHelper.getTotalExpenses(context);
        String expenseColor = "#d44444";
        String savingsColor = "#28BD78";
        if (val>=0){
            remainingAmount.setText("+"+val);
//            remainingAmount.setTextColor(Color.parseColor(savingsColor));
        }else{
            remainingAmount.setText(""+val);
//            remainingAmount.setTextColor(Color.parseColor((expenseColor)));
        }
        totalExpense.setText("-"+DBHelper.getTotalExpenses(context));
        totalSavings.setText("+"+DBHelper.getTotalIncome(context));
        Log.i("ExpenseTracker", "Dashboard setAllAmounts Is Executed Successfully");
    }
    public static RecyclerView getExpenseRecyclerView(){
        Log.d("ExpenseTracker", "Dashboard getExpenseRecyclerView() returned: " + expenseRecyclerView);
        return expenseRecyclerView;
    }
}