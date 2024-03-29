package com.example.markproject;

import static com.example.markproject.UserService.setMyUser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class SignUp extends AppCompatActivity {

    EditText emailEditText, passwordEditText, usernameEditText;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Intent intent = new Intent(SignUp.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    public void onClickContinue(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Signed up!", Toast.LENGTH_SHORT).show();
                        addUserDetails();
                        goToHomeScreen();
                    } else {
                        Toast.makeText(SignUp.this, "Please fill in everything correctly", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickLogIn(View view) {
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish();
    }

    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    public void addUserDetails(){
        LinkedList<Habit> habitList = new LinkedList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        UserProfile user = new UserProfile(emailEditText.getText().toString(), passwordEditText.getText().toString(), uid, usernameEditText.getText().toString(), 0, habitList, 0);
        setMyUser(user);


    }


}