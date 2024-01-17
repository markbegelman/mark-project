package com.example.markproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;


public class HomeScreen extends AppCompatActivity {

    private TextView goalTextView, goodTimeTextView, tipOfTheDay;
    private SharedPreferences pref;
    private boolean isSideMenuVisible = false;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    FirebaseAuth auth;
    FirebaseUser user;

    OutputStream out;
    InputStream in;
    String random_tip;

    String theTips = "1. You get adapted to your surroundings, so change your goal wording often!\n" +
            "2. Look at a small point and move your eyes as little as possible to achieve great focus!\n" +
            "3. The most effective way to boost your performance in anything is to get enough sleep!\n" +
            "4. Everybody is Not equal. Use your avantages!\n" +
            "5. Wake up at the same time every day, +- one hour!\n" +
            "6. Decide what your biggest goal is, and make it your daily priority.";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuButton) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        tipOfTheDay = findViewById(R.id.tipOfTheDayTextView);
        writeToFile(theTips);
        readFromFile(3);
        if(user == null)
        {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            finish();
        }

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

        ImageButton menuButton = findViewById(R.id.menuButton);

        drawerLayout = findViewById(R.id.homeScreen);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.homeMenu:
                        goTo("home");
                        break;
                    case R.id.logOutMenu:
                        goTo("log out");
                        break;
                    case R.id.profileMenu:
                        goTo("profile");
                        break;
                    case R.id.dailyTasksMenu:
                        goTo("daily tasks");
                        break;
                    case R.id.workoutMenu:
                        goTo("workout");
                        break;
                    case R.id.oneVoneMenu:
                        goTo("1v1");
                        break;
                    case R.id.randomChallengeMenu:
                        goTo("random challenge");
                        break;
                    case R.id.timelineMenu:
                        goTo("timeline");
                        break;
                    case R.id.friendsMenu:
                        goTo("friends");
                        break;

                    case R.id.aboutMeMenu:
                        goTo("aboutMe");
                        break;
                    case R.id.exitMenu:
                        goTo("exit");
                        break;
                }
                return false;
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMenu1(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
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

    public void onClickMenu1(View view) {
        // Open the drawer
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void logOut()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


    public void onClickDailyTasks(View view)
    {
        goTo("daily tasks");
    }
    public void onClickWorkout(View view)
    {
        goTo("workout");
    }
    public void onClickRandomChallenge(View view)
    {
        goTo("random challenge");
    }
    public void onClick1v1(View view)
    {
        goTo("1v1");
    }
    public void onClickTimeline(View view)
    {
        goTo("timeline");
    }

    public void goTo(String screen)
    {
        switch (screen)
        {
            case "home":
                Intent intentHome = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intentHome);
                finish();
                break;
            case "log out":
                logOut();
                break;
            case "profile":
            /*

                Intent intent = new Intent(getApplicationContext(), ProfileScreen.class);
                startActivity(intent);
                finish();

             */
                Toast.makeText(this, "not added yet", Toast.LENGTH_SHORT).show();
                break;

            case "daily tasks":
                Intent intent2 = new Intent(getApplicationContext(), DailyTasks.class);
                startActivity(intent2);
                finish();
                break;


            case "workout":
                /*
                Intent intent3 = new Intent(getApplicationContext(), WorkoutScreen.class);
                startActivity(intent3);
                finish();
                 */
                Toast.makeText(this, "not added yet", Toast.LENGTH_SHORT).show();
                break;

            case "1v1":
                Toast.makeText(this, "not added yet", Toast.LENGTH_SHORT).show();
                break;

            case "random challenge":
                Toast.makeText(this, "not added yet", Toast.LENGTH_SHORT).show();
                break;

            case "timeline":
                Toast.makeText(this, "not added yet", Toast.LENGTH_SHORT).show();
                break;

            case "friends":
                Toast.makeText(this, "not added yet", Toast.LENGTH_SHORT).show();
                break;

            case "aboutMe":
                Intent intent = new Intent(getApplicationContext(), AboutMe.class);
                startActivity(intent);
                finish();
                break;

            case "exit":
                finishAndRemoveTask();
                break;
        }
    }
    public void writeToFile(String tip) {
        try {
            out = openFileOutput("randomTips", Context.MODE_PRIVATE);
            out.write(tip.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFromFile(int num) {
        try {
            in = openFileInput("randomTips");
            byte[] buffer = new byte[4096];
            try {
                in.read(buffer);
                random_tip = new String(buffer);
                in.close();

                // Split the tips into sentences
                String[] tips = random_tip.split("\\d+\\.\\s*");
                // Check if the tip number is within the range
                if (num >= 0 && num < tips.length) {
                    tipOfTheDay.setText(tips[num]);
                } else {
                    tipOfTheDay.setText("Invalid tip number");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}