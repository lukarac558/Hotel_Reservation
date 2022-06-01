package com.example.db.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.db.Database.Database;
import com.example.db.R;

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

    public void login(View view){

        errorTextView.clearComposingText();

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (login.isEmpty())
            loginEditText.setError("Należy wprowadzić login");

        if (password.isEmpty())
            loginEditText.setError("Należy wprowadzić hasło");

        Database.login(login, password);

        if (Database.userId == -1)
            errorTextView.setText("Wprowadzono niepoprawne dane.");
        else {
            if(Database.permission.equalsIgnoreCase("user")) {
                Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
                startActivity(intent);
            }
            else if(Database.permission.equalsIgnoreCase("admin")) {
                Intent intent = new Intent(getApplicationContext(), AdminPanelActivity.class);
                startActivity(intent);
            }
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