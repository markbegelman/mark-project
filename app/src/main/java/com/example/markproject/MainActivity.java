package com.example.markproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int isSignedIn; // 1 if just opened app, 2 if not first time opened, 3 if signed in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        isSignedIn = pref.getInt("isSignedIn", 1); // Default value is 1 (just opened app)

        if (isSignedIn == 3) {
            // User is signed in, navigate to the home screen
            goToHomeScreen();
        }
    }

    public void onClickStart(View view) {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }

    public void onClickExit(View view) {
        finishAndRemoveTask();
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }
}
