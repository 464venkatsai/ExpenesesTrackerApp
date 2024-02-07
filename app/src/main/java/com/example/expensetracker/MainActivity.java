package com.example.expensetracker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> dashboardOptions= new ArrayList<>();
    View myView;
    RecyclerView expenseRecyclerView;
    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String expenseData = data.getStringExtra("expenseKey");
                                Log.d("MainActivity", "Expense Data Received: " + expenseData);
                                Toast.makeText(MainActivity.this, "Data Received " + expenseData, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // DashBoard Updation
        Spinner dashboard = findViewById(R.id.dashBoard);
        dashboardOptions.add("Dashboard");
        dashboardOptions.add("Savings");
        dashboardOptions.add("Expenses");

        CustomDropDown dashBoardDropDown = new CustomDropDown(this, dashboardOptions);
        dashBoardDropDown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dashboard.setAdapter(dashBoardDropDown);
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            dashboard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = dashboardOptions.get(position);
                    if (selectedItem.equals("Dashboard")) {
                        fragmentManager.beginTransaction().
                                replace(R.id.fragmentContainerView,Dashboard.class,null)
                                .setReorderingAllowed(true)
                                .addToBackStack("Dashboard") // Name can be null
                                .commit();
                    } else if (selectedItem.equals("Savings")) {
                        fragmentManager.beginTransaction().
                                replace(R.id.fragmentContainerView, Savings.class,null)
                                .setReorderingAllowed(true)
                                .addToBackStack("Expenses") // Name can be null
                                .commit();

                    } else {
                        fragmentManager.beginTransaction().
                                replace(R.id.fragmentContainerView, Expenses.class,null)
                                .setReorderingAllowed(true)
                                .addToBackStack("Expenses") // Name can be null
                                .commit();

                    }
                    Log.d("CurrentFragment", "Current Fragment is " + selectedItem);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("CurrentFragment", "onNothingSelected: ");
                }
            });
        } catch (Exception e) {
            Log.e("CurrentFragment", e.toString());
        }
//        // Creating New Fragment For Adding New Expenses
        FloatingActionButton fab = findViewById(R.id.addExpenses);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, AddCustomExpenses.class);
                Log.d("MainActivity", "FloatingActionButton clicked");
                startActivity(intent);
        });
    }
//     Receiving Data Of New Expenses
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 464 && resultCode == RESULT_OK && data != null) {
            String expenseData = data.getStringExtra("expenseKey");
            Toast.makeText(this, "Data Received " + expenseData, Toast.LENGTH_SHORT).show();
        }
    }
}