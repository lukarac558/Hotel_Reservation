package com.example.db.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.example.db.utils.MD5;
import com.example.db.database.Database;
import com.example.db.utils.Validator;
import com.example.db.utils.WindowDirector;
import com.example.db.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText password2EditText;
    private EditText emailEditText;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginEditText = findViewById(R.id.rLoginEditText);
        passwordEditText = findViewById(R.id.rPasswordEditText);
        password2EditText = findViewById(R.id.rPassword2EditText);
        emailEditText = findViewById(R.id.rEmailEditText);
        errorTextView = findViewById(R.id.rErrorTextView);
    }

    @SuppressLint("SetTextI18n")
    public void register(View view) {

        errorTextView.clearComposingText();

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String password2 = password2EditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (login.isEmpty() || password.isEmpty() || password2.isEmpty() || email.isEmpty()) {
            errorTextView.setText("Należy wypełnić wszystkie wymagane pola");
        } else if (login.length() < 5 || password.length() < 5) {
            errorTextView.setText("Minimalna długość loginu i hasła to 5 znaków");
        } else if (!password.equalsIgnoreCase(password2)) {
            errorTextView.setText("Hasła nie zgadzają się");
        } else if (!Validator.validateEmail(email)) {
            errorTextView.setText("Niepoprawny format email");
        } else {
            try {
                Database.register(login, MD5.hashPassword(password), email);
                Toast.makeText(this, "Pomyślnie utworzono konto.", Toast.LENGTH_SHORT).show();
                WindowDirector.changeActivity(this, LoginActivity.class);
            } catch (SQLException exception) {
                errorTextView.setText("Podany login bądź email istnieje już w bazie");
            } catch (NoSuchAlgorithmException e) {
                errorTextView.setText("Problem z hashowaniem hasła");
            }
        }
    }
}