package com.example.passion_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private Button loginButton;
    private TextView registerLink;
    private EditText editTextEmail;
    private EditText editTextpassword;
    private FirebaseAuth firebaseAuth;

    private CheckBox checkboxRememberMe;
    private SharedPreferences sharedPreferences;
    private boolean isEmailValid(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return Pattern.matches(emailPattern, email);
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.btnLogin);
        registerLink = findViewById(R.id.tvRegisterLink);
        editTextpassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);

        firebaseAuth = FirebaseAuth.getInstance();

        checkboxRememberMe = findViewById(R.id.checkbox_remember_me);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // Check if user is already logged in and "Remember Me" is checked
        if (sharedPreferences.getBoolean("RememberMe", false)) {
            if (firebaseAuth.getCurrentUser() != null) {
                // User is already logged in, redirect to main activity
                Intent intent = new Intent(Login.this, User_main.class);
                startActivity(intent);
                finish();
            }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement login logic here
                String email = editTextEmail.getText().toString().trim();
                String password = editTextpassword.getText().toString().trim();

                if (!isEmailValid(email)) {
                    // Display an error message or toast to inform the user that the email is invalid.
                    editTextEmail.setError("Invalid email address");
                    return;
                }

                if (!isPasswordValid(password)) {
                    // Display an error message or toast for invalid password.
                    editTextpassword.setError("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login successful
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("RememberMe", checkboxRememberMe.isChecked());
                                    editor.apply();

                                    Intent intent = new Intent(Login.this, User_main.class);
                                    startActivity(intent);
                                } else {
                                    // Login failed
                                    // Handle the failed login (e.g., show an error message)
                                    showToast("Login failed. Please check your email and password");
                                }
                            }
                        });
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the registration page (RegisterActivity)
                Intent intent = new Intent(Login.this, Register_choice.class);
                startActivity(intent);
            }
        });
    }
}