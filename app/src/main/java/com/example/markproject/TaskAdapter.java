package com.example.markproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    Context context;
    List<Task> object;
    CheckBox checkBox;
    ImageButton imgBtn;
    TextView tasksCompletedTV;
    int tasksCompleted = 0;

    public TaskAdapter(Context context, List<Task> object) {
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
        ImageButton imgBtn = view.findViewById(R.id.imageButton);

        Task temp = object.get(position);
        title.setText(temp.getTitle());

        // Update CheckBox state based on the 'done' status of the task
        checkBox.setChecked(temp.isDone());

        return view;
    }


}
