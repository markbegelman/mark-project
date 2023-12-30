package com.example.markproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;


public class HomeScreen extends AppCompatActivity {

    private TextView goalTextView, goodTimeTextView;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        goalTextView = findViewById(R.id.goalTextView);
        goodTimeTextView = findViewById(R.id.goodTimeTextView);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        // Load the goal text from SharedPreferences when the HomeScreen is created
        String savedGoal = pref.getString("goal", "[enter a goal]");
        if(savedGoal.isEmpty())
        {
            savedGoal = "[enter a goal]";
        }
        goalTextView.setText(savedGoal);

        if(hour >= 5 && hour <= 12)
        {
            goodTimeTextView.setText(" Good Morning!");
        }
        if(hour >= 12 && hour <= 18)
        {
            goodTimeTextView.setText(" Good Afternoon!");
        }
        if(hour >= 19 && hour <= 21)
        {
            goodTimeTextView.setText(" Good Evening!");
        }
        if((hour >= 21 && hour <= 23) || hour == 0)
        {
            goodTimeTextView.setText(" Good Night!");
        }
    }

    public void onClickEditGoal(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_goal, null);

        EditText editTextGoal = dialogView.findViewById(R.id.editTextGoal);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Edit Your Goal")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newGoal = editTextGoal.getText().toString();
                        goalTextView.setText(newGoal);

                        if(checkGoal(newGoal))
                        {
                            // Save the new goal text to SharedPreferences
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("goal", newGoal);
                            editor.apply();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Cancel
                    }
                });

        builder.create().show();
    }

    public boolean checkGoal(String goal)
    {
        if(goal.isEmpty() || goal.equals(" ") || goal.equals("."))
        {
            Toast.makeText(this, "Goal is empty", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public void onClickInfo(View view) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_placement_info, null);

        ImageButton closeButton = dialogView.findViewById(R.id.exitDialogPlacement);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the close button click event
                alertDialog.dismiss();
            }
        });

// Show the dialog
        alertDialog.show();


    }
    public void onClickMenu(View view)
    {
        Toast.makeText(this, "no", Toast.LENGTH_SHORT).show();
    }

    public void onClickReset(View view) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("isSignedIn", 1);
        editor.putString("goal", "");
        editor.apply();
        Toast.makeText(this, "[reset and will go to opening screen when reopened]", Toast.LENGTH_SHORT).show();
    }
}