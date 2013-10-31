package com.parlakov.medic.models;

/**
 * Created by georgi on 13-10-31.
 */
public class LoginUser {

    String username;
    String password;
    String grant_type;

    public LoginUser(){
        grant_type = "password";
    }

    public LoginUser(String username, String password){
        this();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString(){
        return String.format("username:%s,password:%s,grant_type:%s", username, password, grant_type);
    }

}
