package com.example.markproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyTasks extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<Habit> missionList;
    ListView lv;
    TaskAdapter missionAdapter;
    int taskCompleted = 0;
    TextView taskCompletedTV;
    ArrayAdapter arrayAdapter;
    private ListView listView;
    ArrayList array_list;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);
        taskCompletedTV = findViewById(R.id.tasksCompleted);




        missionList = new ArrayList<>();
        array_list = new ArrayList();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        initializeMissionAdapter();

        loadTasksFromFirebase(userId);

        if (!getCurrentDate().equals(getLastSaved())) {
            resetTasks();
        }
    }



    private void initializeMissionAdapter() {
        missionAdapter = new TaskAdapter(DailyTasks.this, missionList);
        lv = findViewById(R.id.Tasks);
        lv.setAdapter(missionAdapter);
    }
    public void resetTasks() {
        taskCompleted = 0;
        taskCompletedTV.setText("No Tasks Completed");

        ArrayList<Habit> clonedMissionList = new ArrayList<>(missionList);

        for (Habit habit : clonedMissionList) {
            habit.setDone(false);
            databaseReference.child(habit.getKey()).child("done").setValue(false);
        }

        missionList.clear();
        missionList.addAll(clonedMissionList);

        missionAdapter.notifyDataSetChanged();
    }


    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // Inside your DailyTasks class

    private void loadTasksFromFirebase(String userId) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Listen for changes in the "tasksCompleted" node
        userReference.child("tasksCompleted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the tasksCompleted value from Firebase
                if (dataSnapshot.exists()) {
                    taskCompleted = dataSnapshot.getValue(Integer.class);
                    taskCompleted();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });

        // Listen for changes in the "habits" node
        userReference.child("habits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot habitsSnapshot) {
                missionList.clear();

                // Loop through each habit in the "habits" node
                for (DataSnapshot habitSnapshot : habitsSnapshot.getChildren()) {
                    // Check if the habit data is of type HashMap
                    if (habitSnapshot.getValue() instanceof HashMap) {
                        // Convert the habit data to a Habit object
                        Habit task = habitSnapshot.getValue(Habit.class);

                        // Check if the conversion was successful before adding to the list
                        if (task != null) {
                            missionList.add(task);
                        }
                    }
                }

                // Update the UI with the loaded tasks
                missionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur during data retrieval
            }
        });
    }






    public void saveCurrentDate()
    {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lastSavedDate", getCurrentDate());
        editor.apply();

    }
    public String getLastSaved()
    {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String lastSavedDate = preferences.getString("lastSavedDate", "No Date Available");
        return lastSavedDate;
    }


    private void deleteItem(int position) {

        Habit taskToDelete = missionList.get(position);
        String taskKey = taskToDelete.getKey();

        if(taskToDelete.isDone())
        {
            taskCompleted--;
            taskCompleted();
        }

        databaseReference.child(userId).child("habits").child(taskKey).removeValue();

        missionList.remove(position);
        missionAdapter.notifyDataSetChanged();

    }


    public void onClickTrash(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_dialog_layout);

        Button deleteButton = dialog.findViewById(R.id.delete);
        Button cancelButton = dialog.findViewById(R.id.cancel);
        int position = lv.getPositionForView((View) view.getParent());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = missionList.get(position).getTitle();
                deleteItem(position);
                Toast.makeText(DailyTasks.this, name +" deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog when "Cancel" is clicked
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void onClickBack(View view) {
        Intent intent = new Intent(DailyTasks.this, HomeScreen.class);
        startActivity(intent);
        finish();
    }


    public void taskCompleted()
    {
        if(taskCompleted == 0)
        {
            taskCompletedTV.setText("No Tasks Completed");
        }
        else
        {
            taskCompletedTV.setText(taskCompleted + " Tasks Completed");
        }

    }

    public void onClickTick(View view)
    {
        int position = lv.getPositionForView((View) view.getParent());
        Habit task = missionList.get(position);

        String taskKey = task.getKey();
        task.setDone(!task.isDone());


        databaseReference.child(userId).child("habits").child(taskKey).child("done").setValue(task.isDone());
        if(task.isDone())
        {
            taskCompleted++;
            taskCompleted();

        }
        else
        {
            taskCompleted--;
            taskCompleted();
        }
        databaseReference.child(userId).child("tasksCompleted").setValue(taskCompleted);
    }


    public void onClickAdd(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = getLayoutInflater().inflate(R.layout.add_task_dialog, null);
        builder.setView(dialogView);

        EditText titleET = dialogView.findViewById(R.id.title);
        Button dialogButton = dialogView.findViewById(R.id.dialogButton);

        TextView dialogTitle = dialogView.findViewById(R.id.dialogTitle);
        dialogTitle.setText("Create a new task");

        AlertDialog dialog = builder.create();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString();

                createTask(title, false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void createTask(String title, boolean isDone)
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String userId = currentUser.getUid();

        Habit newHabit = new Habit(title, isDone);
        String taskId = databaseReference.child(userId).child("habits").push().getKey();
        newHabit.setKey(taskId);
        databaseReference.child(userId).child("habits").child(taskId).setValue(newHabit);

        missionList.add(newHabit);
        missionAdapter = new TaskAdapter(this, missionList);
        lv = findViewById(R.id.Tasks);
        lv.setAdapter(missionAdapter);
    }
}
