package com.example.db.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import com.example.db.Class.MD5;
import com.example.db.Class.Regex;
import com.example.db.Database.Database;
import com.example.db.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText rLoginEditText;
    private EditText rPasswordEditText;
    private EditText rPassword2EditText;
    private EditText rEmailEditText;
    private TextView rErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rLoginEditText = findViewById(R.id.rLoginEditText);
        rPasswordEditText = findViewById(R.id.rPasswordEditText);
        rPassword2EditText = findViewById(R.id.rPassword2EditText);
        rEmailEditText = findViewById(R.id.rEmailEditText);
        rErrorTextView = findViewById(R.id.rErrorTextView);
    }

    @SuppressLint("SetTextI18n")
    public void register(View view){

        rErrorTextView.clearComposingText();

        String login = rLoginEditText.getText().toString();
        String password = rPasswordEditText.getText().toString();
        String password2 = rPassword2EditText.getText().toString();
        String email = rEmailEditText.getText().toString();

        if(login.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty())
            rErrorTextView.setText("Należy wypełnić wszystkie wymagane pola");
        else if(!password.equalsIgnoreCase(password2))
            rErrorTextView.setText("Hasła nie zgadzają się");
        else if(!Regex.emailValidation(email)){
            rErrorTextView.setText("Niepoprawny format email");
        }
        else{
            try {

                Database.register(login, MD5.hashPassword(password), email);
                Toast.makeText(this, "Pomyślnie utworzono konto.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            } catch (SQLException exception) {
                rErrorTextView.setText("Podany login bądź email istnieje już w bazie");
            } catch (NoSuchAlgorithmException e) {
                rErrorTextView.setText("Problem z hashowaniem hasła");
            }
        }
    }
}