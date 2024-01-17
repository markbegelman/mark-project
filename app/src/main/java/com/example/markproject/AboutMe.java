package com.example.markproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutMe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
    }
    public void onClickBack(View view)
    {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class); // in the future change to exit to the previous screen maybe
        startActivity(intent);
        finish();
    }
}