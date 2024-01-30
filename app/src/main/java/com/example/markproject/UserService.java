package com.example.markproject;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Objects;
import com.example.markproject.UserService;

public class UserService {
    public static UserProfile myUser;

    static Task<Void> setMyUser(UserProfile user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/" + user.getUserName());

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("userName", user.getUserName());
        userMap.put("habitStreak", user.getHabitStreak());

        return ref.setValue(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                myUser = user;
        });
    }

    static Task<UserProfile> getUserById(String userId)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/" + userId);

        return ref.get().continueWith(task -> {
           if(task.isSuccessful()){
               String userName = task.getResult().child("userName").getValue(String.class);
               int habitStreak = task.getResult().child("habitStreak").getValue(int.class);
               UserProfile profile = new UserProfile(userName, habitStreak);

               if(Objects.equals(userId, FirebaseAuth.getInstance().getCurrentUser().getUid()))
               {
                   myUser = profile;
               }
               return profile;
           }
           else
               throw task.getException();
        });
    }
}
