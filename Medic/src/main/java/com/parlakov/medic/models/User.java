package com.parlakov.medic.models;

/**
 * Created by georgi on 13-10-30.
 */
public class User {

    private String Username;
    private String DisplayName;
    private String Password;
    private String Email;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        this.DisplayName = displayName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String toString(){
        return String.format("{Username:%s,DisplayName:%s,Password:%s,Email:%s}", Username, DisplayName, Password, Email);
    }
}
