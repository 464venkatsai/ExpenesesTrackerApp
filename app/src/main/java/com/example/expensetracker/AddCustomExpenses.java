package com.example.expensetracker;

import static java.security.AccessController.getContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
        Log.d("ExpenseTracker", "AddCustomExpenses onCreate: ExpensesAddFloatingButton To Add New Transaction Is Shown ");
        newCustomName = findViewById(R.id.newCustomName);
        newAmount = findViewById(R.id.newAmount);
        newDate = findViewById(R.id.newDate);
        newNote= findViewById(R.id.newNote);
        newDate.setText(getCustomFormattedDateTime());

        addTransaction = findViewById(R.id.addNewTransaction);

        // Dynamically + Statically Creating And Creating the Types
        ArrayList<String> defaultTypes = new ArrayList<>();
        defaultTypes.add("Income");
        defaultTypes.add("Expense");

        // Dynamically + Statically Creating And Creating the Tags
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

                long newRowId = 0;
                try{
                    newRowId = dbHelper.addTransaction(new Transaction(customName,amount,type,tag,date,note));
                    Log.d("ExpenseTracker", "AddCustomExpenses onClick: Added New Transaction");
                    Dashboard.updateRecyclerViewData(getApplicationContext(),Dashboard.getExpenseRecyclerView(),AddCustomExpenses.this);
                    Log.d("ExpenseTracker", "AddCustomExpenses onClick: Updated Dashboard next Expenses");
                    Expenses.updateRecyclerViewExpenses(getApplicationContext(),Expenses.expenseRecyclerView,AddCustomExpenses.this);
                    Log.d("ExpenseTracker", "AddCustomExpenses onClick: Updated Expenses next Savings");
                    Savings.updateRecyclerViewSavings(getApplicationContext(),Savings.savingsRecyclerView,AddCustomExpenses.this);
                    Log.w("ExpenseTracker", "AddCustomExpenses onClick: Updated Saving");
                    Log.i("ExpenseTracker", "AddCustomExpenses onClick Successfully Updated Dashboard, Expenses, Savings Fragments");
                }catch (Exception e){
                    Log.e("ExpenseTracker", "AddCustomExpenses onClick: Trying To Update Dashboard, Expenses, Savings Fragments Error : "+e.toString());
                }
                if (newRowId != -1) {
                    addSnackBar(v, "Successfully Added " + customName, "Success");
                    Log.d("DBHelper", "Insertion Successful");
                    Log.d("ExpenseTracker", "AddCustomExpenses onClick: Inserting Successful");
                    Log.d("ExpenseTracker", "AddCustomExpenses SnackBar: Successfully Shown MSG : "+customName+" Is Added");
                } else {
                    Log.d("DBHelper", "InsertionFailed");
                    Log.d("ExpenseTracker", "InsertionFailed");
                    Log.d("ExpenseTracker", "AddCustomExpenses SnackBar: Failed Shown MSG : "+customName+" Is Added");
                    addSnackBar(v, "Failed To Add " + customName, "Fail");
                }
                newCustomName.setText("");
                newAmount.setText("");
                newNote.setText("");
                Log.d("ExpenseTracker", "AddCustomExpenses onClick: Rested Name, Amount, Note");
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
    public static String getCustomFormattedDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d , yyyy  h:mm a", Locale.ENGLISH);
        Log.d("ExpenseTracker", "getCustomFormattedDateTime() returned: " +currentDateTime.format(formatter) );
        return currentDateTime.format(formatter);
    }
}
