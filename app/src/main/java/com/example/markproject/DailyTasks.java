package com.example.markproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class DailyTasks extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<Task> missionList;
    ListView lv;
    TaskAdapter missionAdapter;
    int taskCompleted = 0;
    TextView taskCompletedTV;
    ArrayAdapter arrayAdapter;
    private ListView listView;
    ArrayList array_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);
        taskCompletedTV =  (TextView)findViewById(R.id.tasksCompleted);

        missionList = new ArrayList<>();
        array_list = new ArrayList();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("tasks");

        loadTasksFromFirebase();
    }

    private void loadTasksFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                missionList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);
                    if (task != null) {
                        missionList.add(task);
                    }
                }

                // Update the adapter and UI
                missionAdapter = new TaskAdapter(DailyTasks.this, missionList);
                lv = findViewById(R.id.Tasks);
                lv.setAdapter(missionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    public void onClickTrash(View view)
    {
        showDeleteDialog(view);
    }
    public void showDeleteDialog(View view) {
        showDeleteConfirmationDialog(view);
    }
    private void deleteItem(int position) {
        Task taskToDelete = missionList.get(position);

        databaseReference.child(taskToDelete.getKey()).removeValue();

        missionList.remove(position);
        missionAdapter.notifyDataSetChanged();
    }


    private void showDeleteConfirmationDialog(View view) {
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
        Task task = missionList.get(position);
        String taskName = task.getTitle().toString();
        task.setDone(!task.isDone());

        databaseReference.child(task.getKey()).setValue(task);
        missionAdapter.notifyDataSetChanged();


        if(task.isDone())
        {
            taskCompleted++;
            taskCompleted();
            Toast.makeText(this,  taskName + " completed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            taskCompleted--;
            taskCompleted();
        }

    }

    public void onClickAdd(View view) {
        // Initialize and show the dialog
        showAddTaskDialog();
    }

    private void showAddTaskDialog() {
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

        // Show the dialog
        dialog.show();
    }

    public void createTask(String title, boolean isDone)
    {
        Task newTask = new Task(title, isDone);

        String taskId = databaseReference.push().getKey();
        newTask.setKey(taskId);
        databaseReference.child(taskId).setValue(newTask);

        missionList.add(newTask);
        missionAdapter = new TaskAdapter(this, missionList);
        lv = findViewById(R.id.Tasks);
        lv.setAdapter(missionAdapter);
    }



}