package com.example.markproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DailyTasks extends AppCompatActivity {
    ArrayList<Task> missionList;
    ListView lv;
    TaskAdapter missionAdapter;
    int taskCompleted = 0;
    TextView taskCompletedTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);
        taskCompletedTV =  (TextView)findViewById(R.id.tasksCompleted);

        missionList = new ArrayList<>();

    }

    private static final int REQUEST_CODE_ADD = 1;



    private static final int REQUEST_CODE_EDIT = 2;


    public void onClickBack(View view) {
        Intent intent = new Intent(DailyTasks.this, HomeScreen.class);
        startActivity(intent);
        finish();
    }


    public void taskCompleted()//addorremove + check if 1
    {
        taskCompleted++;
        taskCompletedTV.setText(taskCompleted + " Tasks Completed");
    }

    public void onClickTick(View view)
    {
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
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
        missionList.add(newTask);
        missionAdapter = new TaskAdapter(this, missionList);
        lv = findViewById(R.id.Tasks);
        lv.setAdapter(missionAdapter);
    }

}