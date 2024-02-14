package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> dashboardOptions= new ArrayList<>();
    public static String currentFragment;
    public static MediaPlayer mediaPlayerOnClick;
    public static MediaPlayer mediaPlayerOnDelete;
    public static Map<String, Drawable> imageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageMap = new HashMap<>();

        // Add images to the HashMap
        imageMap.put("Entertainment", getApplicationContext().getResources().getDrawable(R.drawable.entertainment));
        imageMap.put("House Hold", getApplicationContext().getResources().getDrawable(R.drawable.household));
        imageMap.put("Others", getApplicationContext().getResources().getDrawable(R.drawable.other));
        imageMap.put("Education", getApplicationContext().getResources().getDrawable(R.drawable.education));
        imageMap.put("Job", getApplicationContext().getResources().getDrawable(R.drawable.job));
        imageMap.put("Hospital", getApplicationContext().getResources().getDrawable(R.drawable.hospital));
        imageMap.put("Traveling", getApplicationContext().getResources().getDrawable(R.drawable.traveling));
        imageMap.put("Food", getApplicationContext().getResources().getDrawable(R.drawable.food));
        imageMap.put("Personal", getApplicationContext().getResources().getDrawable(R.drawable.personal));
//        // DashBoard Updation
        Spinner dashboard = findViewById(R.id.dashBoard);
        dashboardOptions.add("Dashboard");
        dashboardOptions.add("Savings");
        dashboardOptions.add("Expenses");

        CustomDropDown dashBoardDropDown = new CustomDropDown(this, dashboardOptions);
        dashBoardDropDown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dashboard.setAdapter(dashBoardDropDown);
        Log.d("ExpenseTracker", "Default Dashboard Fragment is Show Successfully");
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            dashboard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = dashboardOptions.get(position);
                    try {
                        if (selectedItem.equals("Dashboard")) {
                            fragmentManager.beginTransaction().
                                    replace(R.id.fragmentContainerView,Dashboard.class,null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("Dashboard")
                                    .commit();
                        } else if (selectedItem.equals("Savings")) {
                            fragmentManager.beginTransaction().
                                    replace(R.id.fragmentContainerView, Savings.class,null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("Expenses")
                                    .commit();
                        } else {
                            fragmentManager.beginTransaction().
                                    replace(R.id.fragmentContainerView, Expenses.class,null)
                                    .setReorderingAllowed(true)
                                    .addToBackStack("Expenses")
                                    .commit();
                        }
                        currentFragment = selectedItem;
                    Log.d("ExpenseTracker", "onItemSelected() called with:"+"CurrentOption = ["+selectedItem+"]");
                    }catch (Exception e){
                        Log.e("ExpenseTracker","onItemSelected"+e.toString());
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d("ExpenseTracker","onNothingSelected is Triggered");
                }
            });
        } catch (Exception e) {
            Log.e("CurrentFragment", e.toString());
            Log.e("ExpenseTracker","Error Occurred In DashBoard.SetItemClickListener : "+e.toString());
        }
        FloatingActionButton fab = findViewById(R.id.addExpenses);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, AddCustomExpenses.class);
                Log.d("MainActivity", "FloatingActionButton clicked");
                startActivity(intent);
                Log.i("ExpenseTracker", "Starting Activity To Add a New Transaction");
        });
    }
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        if (currentFragment instanceof Savings) {
            Log.i("ExpenseTracker", "onBackPressed: Blocked Savings");
        } else if (currentFragment instanceof Expenses) {
            Log.i("ExpenseTracker", "onBackPressed: Blocked Expenses");
        }else {
            Log.i("ExpenseTracker", "onBackPressed: Blocked Dashboard");
        }
    }
    public static void playOnClickSound(Activity activity) {
        mediaPlayerOnClick = MediaPlayer.create(activity,R.raw.deleted);
        mediaPlayerOnClick.start();
    }
    public static void playOnDeleteSound(Activity activity) {
        mediaPlayerOnDelete = MediaPlayer.create(activity,R.raw.deleted);
        mediaPlayerOnDelete.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayerOnClick != null) {
            mediaPlayerOnClick.release();
            mediaPlayerOnClick = null;
        }
        if (mediaPlayerOnDelete != null) {
            mediaPlayerOnDelete.release();
            mediaPlayerOnDelete = null;
        }
    }
}