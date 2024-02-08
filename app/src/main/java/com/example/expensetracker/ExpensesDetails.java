package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExpensesDetails extends AppCompatActivity {
    EditText expenseAmount,expenseTag,expenseDate,expenseCustomName,expenseNote,expenseType;
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
        expenseCustomName = findViewById(R.id.expensesName);
        expenseAmount = findViewById(R.id.amount);
        expenseTag = findViewById(R.id.expensesTag);
        expenseDate = findViewById(R.id.expenesDate);
        expenseNote = findViewById(R.id.note);
        expenseType = findViewById(R.id.type);
        expenseCustomName.setText(customName);
        expenseType.setText(type);
        expenseTag.setText(tag);
        expenseNote.setText(note);
        expenseAmount.setText(amount);
        expenseDate.setText(date);
    }
}
