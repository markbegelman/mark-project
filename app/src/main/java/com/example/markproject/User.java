package com.example.markproject;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String email;
    public String password;
    public String uId;
    public String userName;
    public String key;

    public User()
    {

    }

    public User(String email, String password, String uId, String userName, String key) {
        this.email = email;
        this.password = password;
        this.uId = uId;
        this.userName = userName;
        this.key = key;
    }
}
