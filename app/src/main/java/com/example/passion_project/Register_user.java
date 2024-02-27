package com.example.passion_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_user extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnRegisterUser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private int userCounter = 1;

    private String generateUniqueIdentifier() {
        String customUserId = String.valueOf(userCounter);
        userCounter++; // Increment the counter for the next user
        return customUserId;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userCounter = sharedPreferences.getInt("userCounter", 1);

        btnRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = editTextFirstName.getText().toString().trim();
                final String lastName = editTextLastName.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                final String userRole = "user"; // This is the user role field

                userCounter++;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("userCounter", userCounter);
                editor.apply();

                String customUserId = "user" + generateUniqueIdentifier();

                // Create a user in Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register_user.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    Log.d("Registration", "User registration successful. User ID: " + userId);

                                    // Save user information to Firebase Realtime Database
                                    User user = new User(firstName, lastName, email, userRole);
                                    databaseReference.child(customUserId).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> innerTask) {
                                                    if (innerTask.isSuccessful()) {
                                                        // Data saved successfully
                                                        Log.d("Registration", "User data saved to Firebase.");
                                                        // Show a Toast message to inform the user
                                                        showToast("Registration successful and data saved.");
                                                        // Redirect to the login page or the main activity as per your app flow.
                                                    } else {
                                                        Log.e("Registration", "Failed to save user data. Error: " + innerTask.getException().getMessage());
                                                        // Handle the error.
                                                    }
                                                }
                                            });
                                } else {
                                    Log.e("Registration", "User registration failed. Error: " + task.getException().getMessage());
                                    // Registration failed, handle the error.
                                }
                            }
                        });
            }
        });
    }
}