package com.example.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;

import Database.Database;

public class RegisterActivity extends AppCompatActivity {

    EditText rLoginEditText;
    EditText rPasswordEditText;
    EditText rPassword2EditText;
    EditText rEmailEditText;
    EditText rPhoneEditText;
    TextView rErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rLoginEditText = findViewById(R.id.rLoginEditText);
        rPasswordEditText = findViewById(R.id.rPasswordEditText);
        rPassword2EditText = findViewById(R.id.rPassword2EditText);
        rEmailEditText = findViewById(R.id.rEmailEditText);
        rPhoneEditText = findViewById(R.id.rPhoneEditText);
        rErrorTextView = findViewById(R.id.rErrorTextView);
    }

    public void register(View view){

        rErrorTextView.clearComposingText();

        String login = rLoginEditText.getText().toString();
        String password = rPasswordEditText.getText().toString();
        String password2 = rPassword2EditText.getText().toString();
        String email = rEmailEditText.getText().toString();
        int phoneNumber=-1;

        if(!rPhoneEditText.getText().toString().isEmpty())
        phoneNumber = Integer.parseInt(rPhoneEditText.getText().toString());

        if(login.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty())
            rErrorTextView.setText("Należy wypełnić wszystkie wymagane pola");
        else if(!password.equalsIgnoreCase(password2))
            rErrorTextView.setText("Hasła nie zgadzają się");
        else{
            try {
                Database.register(login, password, email, phoneNumber);

                Database.login(login,password);
                Intent intent = new Intent(getApplicationContext(), SearchEngineActivity.class);
                startActivity(intent);
            } catch (SQLException exception) {
                rErrorTextView.setText("Podany login istnieje już w bazie");
            }
        }

    }
}