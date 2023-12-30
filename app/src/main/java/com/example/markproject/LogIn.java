package com.example.markproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }
    public void onClickSignUp(View view)
    {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }
}