package com.example.markproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Habit> {
    Context context;
    List<Habit> object;
    CheckBox checkBox;
    ImageButton imgBtn;
    TextView tasksCompletedTV;
    int tasksCompleted = 0;

    public TaskAdapter(Context context, List<Habit> object) {
        super(context, R.layout.activity_daily_tasks, object);
        this.context = context;
        this.object = object;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.tasks_list_view, parent, false);

        CheckBox checkBox = view.findViewById(R.id.checkBox);
        TextView title = view.findViewById(R.id.textView);

        Habit temp = object.get(position);
        title.setText(temp.getTitle());

        checkBox.setChecked(temp.isDone());

        /*
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkBox.isChecked())
                {

                }
                else
                {
                    String taskName = title.getText().toString();
                    Toast.makeText(context, taskName + " is completed", Toast.LENGTH_SHORT).show();
                }

            }
        });


         */
        return view;
    }




}
