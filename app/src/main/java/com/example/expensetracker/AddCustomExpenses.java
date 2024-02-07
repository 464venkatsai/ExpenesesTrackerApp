package com.example.expensetracker;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AddCustomExpenses extends Activity {
    EditText newCustomName,newAmount,newDate,newNote;
//    TextView newRecyclerExpenseCustomName,newRecyclerExpenseAmount,newRecyclerExpenseDate,newRecyclerExpenseType;
    TextView addTransaction;
    DBHelper dbHelper;
//    RecyclerView expenseRecyclerView;
    ArrayList<String> expenseCustomName = new ArrayList<>();
    ArrayList<String> expenseDate = new ArrayList<>();
    ArrayList<String> expenseAmount = new ArrayList<>();
    ArrayList<String> expenseType = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_add_floaing_button);

        newCustomName = findViewById(R.id.newCustomName);
        newAmount = findViewById(R.id.newAmount);
        newDate = findViewById(R.id.newDate);
        newNote= findViewById(R.id.newNote);

        addTransaction = findViewById(R.id.addNewTransaction);

        // Dynamically + Statically Creating And Creating the Tags
        ArrayList<String> defaultTypes = new ArrayList<>();
        defaultTypes.add("Income");
        defaultTypes.add("Expense");

        ArrayList<String> defaultTags = new ArrayList<>();
        defaultTags.add("Entertainment");
        defaultTags.add("Food");
        defaultTags.add("Traveling");

        Spinner transactionType = findViewById(R.id.transactionType);
        CustomDropDown type = new CustomDropDown(this, defaultTypes);
        type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionType.setAdapter(type);

        Spinner inputTag = findViewById(R.id.inputTag);
        CustomDropDown tag = new CustomDropDown(this, defaultTags);
        tag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputTag.setAdapter(tag);

        // DataBase Section
        dbHelper = new DBHelper(getApplicationContext());
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customName = newCustomName.getText().toString();
                String amount = newAmount.getText().toString();
                String date = newDate.getText().toString();
                String note = newNote.getText().toString();
                String type = transactionType.getSelectedItem().toString(); ;
                String tag = inputTag.getSelectedItem().toString();
                // Filling all the Values
                String[] items = {customName, String.valueOf(amount), date};
                String[] itemHints = {"Title", "Amount", "Date"};
                System.out.println("newrowid");
                long newRowId = 0;
                try{
                    newRowId = dbHelper.addTransaction(new Transaction(customName,amount,type,tag,date,note));
//                    View mainActivity = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main,null);
//                    expenseRecyclerView = mainActivity.findViewById(R.id.expenseRecyclerView);
                    Dashboard.updateRecyclerViewData(getApplicationContext(),Dashboard.expenseRecyclerView,AddCustomExpenses.this);
                    Expenses.updateRecyclerViewExpenses(getApplicationContext(),Expenses.expenseRecyclerView,AddCustomExpenses.this);
                    Savings.updateRecyclerViewSavings(getApplicationContext(),Savings.savingsRecyclerView,AddCustomExpenses.this);
                }catch (Exception e){
                    Log.d("DBHelper", e.toString());
                }
                if (newRowId != -1) {
                    addSnackBar(v, "Successfully Added " + customName, "Success");
                    Log.d("DBHelper", "Insertion Successful");
                } else {
                    Log.d("DBHelper", "InsertionFailed");
                    addSnackBar(v, "Failed To Add " + customName, "Fail");
                }
                newCustomName.setText("");
                newAmount.setText("");
                newDate.setText("");
                newNote.setText("");
//                try {
////                    View customExpenses = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_expenses,null);
////                    View customSavings = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_savings,null);
////                    RecyclerView expenseRecyclerView = customExpenses.findViewById(R.id.expensesAndIncomeRecyclerView);
////                    RecyclerView savingRecyclerView = customSavings.findViewById(R.id.SavingsRecyclerView);
////                    Dashboard.updateRecyclerViewData(getApplicationContext(),Dashboard.expenseRecyclerView,AddCustomExpenses.this);
//                    Log.d("updateRecyclerViewData","updateRecyclerViewData : Successfully fetched and Updated in the ExpenseRecyclerView");
//                }catch (Exception e){
//                    Log.e("updateRecyclerViewData", "updateRecyclerViewData : "+e.toString() );
//                }
            }
        });
    }
    public static void addSnackBar(View view, String message, String messageType) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        if (messageType.equals("Success")) {
            snackbar.setBackgroundTint(ContextCompat.getColor( view.getContext(),R.color.green));
        } else if (messageType.equals("Fail") || messageType.equals("Warning")) {
            snackbar.setBackgroundTint(ContextCompat.getColor(view.getContext(), R.color.warning));
        }
        snackbar.show();
    }

}
