package com.example.markproject;

import com.google.firebase.database.IgnoreExtraProperties;

import org.w3c.dom.Node;

import java.util.LinkedList;

@IgnoreExtraProperties
public class UserProfile {
    public String email;
    public String password;
    public String uId;
    public String userName;
    public String key;
    public int habitStreak;
    public LinkedList<Habit> habits;

    public UserProfile()
    {

    }
    public UserProfile(String userName, int habitStreak, LinkedList<Habit> habits)
    {
        this.userName = userName;
        this.habitStreak = habitStreak;
    }
    public UserProfile(String userName, int habitStreak)
    {
        this.userName = userName;
        this.habitStreak = habitStreak;
    }
    public UserProfile(String email, String password, String uId, String userName, String key, int habitStreak,LinkedList<Habit> habits) {
        this.email = email;
        this.password = password;
        this.uId = uId;
        this.userName = userName;
        this.key = key;
        this.habitStreak = habitStreak;
        this.habits = habits;
    }

    public LinkedList<Habit> getHabits() {
        return habits;
    }

    public void setHabits(LinkedList<Habit> habits) {
        this.habits = habits;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getHabitStreak() {
        return habitStreak;
    }

    public void setHabitStreak(int habitStreak) {
        this.habitStreak = habitStreak;
    }
}
