package com.example.db.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.Class.MD5;
import com.example.db.Database.Database;
import com.example.db.R;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    EditText loginEditText;
    EditText passwordEditText;
    TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.errorTextView);
    }

    @SuppressLint("SetTextI18n")
    public void login(View view){

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
            if(Database.permission.equalsIgnoreCase("user")) {
                Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
                startActivity(intent);
            }
            else if(Database.permission.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(getApplicationContext(), AdminPanelActivity.class);
                startActivity(intent);
            }

            Toast.makeText(this, "Zalogowano się pomyślnie.", Toast.LENGTH_SHORT).show();
        }
    }

    public void continueWithoutLogin(View view){
        Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}