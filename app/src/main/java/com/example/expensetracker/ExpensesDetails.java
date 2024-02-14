package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ExpensesDetails extends AppCompatActivity {
    EditText expenseAmount,expenseDate,expenseCustomName,expenseNote;
    Spinner expenseType,expenseTag;
    TextView updateTransaction;
    RecyclerView savingsView,expensesView;
    View v1,v2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_detail_layout);
        String customName = getIntent().getStringExtra("customName");
        String tag = getIntent().getStringExtra("tag");
        String date = getIntent().getStringExtra("date");
        String amount = getIntent().getStringExtra("amount");
        String note = getIntent().getStringExtra("note");
        String type = getIntent().getStringExtra("type");
        int id = getIntent().getIntExtra("id",0);
        Log.i("check", "ExpensesDetails : Received  id "+id+" values"+customName + tag+date+amount+amount+note+type);
        updateTransaction = findViewById(R.id.updateTransaction);

        expenseCustomName = findViewById(R.id.expensesName);
        expenseAmount = findViewById(R.id.amount);
        expenseTag = findViewById(R.id.expensesTag);
        expenseDate = findViewById(R.id.expenesDate);
        expenseNote = findViewById(R.id.note);
        expenseType = findViewById(R.id.type);

        expenseCustomName.setText(customName);
        expenseNote.setText(note);
        expenseAmount.setText(amount);
        expenseDate.setText(date);

        // Dynamically + Statically Creating And Creating the Types
        ArrayList<String> defaultTypes = new ArrayList<>();
        defaultTypes.add("Income");
        defaultTypes.add("Expense");

        // Dynamically + Statically Creating And Creating the Tags
        ArrayList<String> defaultTags = new ArrayList<>();
        defaultTags.add("Entertainment");
        defaultTags.add("Job");
        defaultTags.add("Education");
        defaultTags.add("House Hold");
        defaultTags.add("Food");
        defaultTags.add("Traveling");
        defaultTags.add("Personal");
        defaultTags.add("Hospital");
        defaultTags.add("Others");

        CustomDropDown Type = new CustomDropDown(this, defaultTypes);
        Type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultTypes.remove(type);
        defaultTypes.add(0,type);
        expenseType.setAdapter(Type);

        CustomDropDown Tag = new CustomDropDown(this, defaultTags);
        Tag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultTags.remove(tag);
        defaultTags.add(0,tag);
        expenseTag.setAdapter(Tag);

        v1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_expenses,null);
        v2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_savings,null);
        savingsView = v2.findViewById(R.id.SavingsRecyclerView);
        expensesView = v1.findViewById(R.id.expensesAndIncomeRecyclerView);

        updateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = String.valueOf(expenseCustomName.getText());
                    String amount = String.valueOf(expenseAmount.getText());
                    String type = String.valueOf(defaultTypes.get(Integer.parseInt(String.valueOf(expenseType.getSelectedItemId()))));
                    String tag = String.valueOf(defaultTags.get(Integer.parseInt(String.valueOf(expenseTag.getSelectedItemId()))));
                    String date = String.valueOf(expenseDate.getText());
                    String note = String.valueOf(expenseNote.getText());
                    DBHelper.updateTransaction(id,new Transaction(name,amount,type,tag,date,note),getApplicationContext());
                    if(MainActivity.currentFragment.equals("Dashboard")){
                        Dashboard.updateRecyclerViewData(getApplicationContext(),Dashboard.getExpenseRecyclerView(),ExpensesDetails.this);
                        Log.d("ExpenseTracker", "ExpensesDetails onClick: Successfully Updated Dashboard Next Expenses");
                    } else if (MainActivity.currentFragment.equals("Expenses")) {
                        Expenses.updateRecyclerViewExpenses(getApplicationContext(),ExpensesDetails.this);
                        Log.d("ExpenseTracker", "ExpensesDetails onClick: Successfully Updated Expenses Next Savings");
                    }else{
                        Savings.updateRecyclerViewSavings(getApplicationContext(),ExpensesDetails.this);
                        Log.d("ExpenseTracker", "ExpensesDetails onClick: Successfully Updated Savings");
                    }
                    AddCustomExpenses.addSnackBar(v,"Successfully Updated "+name,"Success");
                    Log.d("update", "Updation Successfully");
                    Log.i("ExpenseTracker", "ExpensesDetails onClick: Successfully Updated Record");
                    Log.i("check", "DataList"+DBHelper.fetchData(getApplicationContext(), new String[]{"sno", "name", "amount", "type", "tag", "date", "note"}));
                }catch (Exception e){
                    Log.e("update", "Updation Failed"+e.toString());
                    Log.e("ExpenseTracker", "ExpensesDetails onClick: Failed To Update error : "+e.toString());
                }
            }
        });
    }
}