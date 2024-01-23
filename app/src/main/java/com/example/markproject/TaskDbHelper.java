package com.example.markproject;
// TaskDbHelper.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper {

    private DBHelper dbHelper;

    public TaskDbHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void saveTaskToSQLite(Habit task) {
        // Open a writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert the task into the tasks table
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, task.getTitle());
        values.put(DBHelper.COLUMN_DONE, task.isDone() ? 1 : 0);
        db.insert(DBHelper.TABLE_TASKS, null, values);

        // Close the database
        db.close();
    }

    public List<Habit> getAllTasks() {
        List<Habit> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to fetch all tasks
        String[] projection = {DBHelper.COLUMN_ID, DBHelper.COLUMN_TITLE, DBHelper.COLUMN_DONE};
        Cursor cursor = db.query(DBHelper.TABLE_TASKS, projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TITLE));
                boolean done = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DONE)) == 1;

                // Create a new Task without setId
                Habit task = new Habit(title, done);
                taskList.add(task);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Close the database
        db.close();

        return taskList;
    }
    // Additional methods for querying and manipulating data
}

