package com.example.db.Class;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String login;
    private String password;
    private String email;
    private boolean isAdmin;

    public User(int id, String login, String password, String email, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User(String login, String email, Boolean isAdmin) {
        this.login = login;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User(int userId, String login, String email, Boolean isAdmin) {
        this.id = userId;
        this.login = login;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User(int id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setPermission(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public String toString() {
        return login;
    }
}
