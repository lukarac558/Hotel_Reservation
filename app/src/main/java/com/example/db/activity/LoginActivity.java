package com.example.db.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.db.utils.MD5;
import com.example.db.database.Database;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.loginErrorTextView);
    }

    @SuppressLint("SetTextI18n")
    public void login(View view) {

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (login.isEmpty()) {
            errorTextView.setText("Należy wprowadzić login");
            return;
        }

        if (password.isEmpty()) {
            errorTextView.setText("Należy wprowadzić hasło");
            return;
        }

        try {
            Database.login(login, MD5.hashPassword(password));
        } catch (NoSuchAlgorithmException e) {
            loginEditText.setText("Problem z hashowaniem hasła");
        }

        if (Database.userId == -1)
            errorTextView.setText("Wprowadzono niepoprawne dane");
        else {
            if (!Database.isAdmin) {
                WindowDirector.changeActivity(this, SearchEngineActivity.class);
            } else {
                WindowDirector.changeActivity(this, AdminPanelActivity.class);
            }

            Toast.makeText(this, "Zalogowano się pomyślnie.", Toast.LENGTH_SHORT).show();
        }
    }

    public void continueWithoutLogin(View view) {
        WindowDirector.changeActivity(this, SearchEngineActivity.class);
    }

    public void register(View view) {
        WindowDirector.changeActivity(this, RegisterActivity.class);
    }
}