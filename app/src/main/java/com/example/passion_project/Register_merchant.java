package com.example.passion_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.regex.Pattern;

public class Register_merchant extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextLocation;
    private EditText editTextShopName;
    private EditText editTextShopType;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private int merchantCounter = 1;
    private FirebaseAuth firebaseAuth;
    private Button btnRegisterMerchant;
    private DatabaseReference databaseReference;

    private String generateUniqueIdentifier() {
        String customMerchantId = "merchant" + merchantCounter;
        merchantCounter++; // Increment the counter for the next merchant
        return customMerchantId;
    }

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
        setContentView(R.layout.activity_register_merchant);

        editTextFirstName = findViewById(R.id.editTextMerchantFirstName);
        editTextLastName = findViewById(R.id.editTextMerchantLastName);
        editTextLocation = findViewById(R.id.editTextMerchantShopAddress);
        editTextShopName = findViewById(R.id.editTextMerchantShopName);
        editTextShopType = findViewById(R.id.editTextMerchantShopType);
        editTextEmail = findViewById(R.id.editTextMerchantEmail);
        editTextPassword = findViewById(R.id.editTextMerchantPassword);
        btnRegisterMerchant = findViewById(R.id.btnRegisterMerchant);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("merchants");

        btnRegisterMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = editTextFirstName.getText().toString().trim();
                final String lastName = editTextLastName.getText().toString().trim();
                final String location = editTextLocation.getText().toString().trim();
                final String shopName = editTextShopName.getText().toString().trim();
                final String shopType = editTextShopType.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                final String userRole = "merchant";

                // Check the validity of email and password
                if (!isEmailValid(email)) {
                    editTextEmail.setError("Invalid email address");
                    return;
                }

                if (!isPasswordValid(password)) {
                    editTextPassword.setError("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register_merchant.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful
                                    String customMerchantId = generateUniqueIdentifier();
                                    Log.d("Registration", "User registration successful. User ID: ");

                                    // Save merchant information to Firebase Realtime Database
                                    Merchant merchant = new Merchant(firstName, lastName, email, shopName, location, shopType, userRole);
                                    databaseReference.child(customMerchantId).setValue(merchant)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> innerTask) {
                                                    if (innerTask.isSuccessful()) {
                                                        // Data saved successfully
                                                        showToast("Merchant registration successful and data saved.");
                                                        // Redirect to the login page or the main activity as per your app flow.
                                                    } else {
                                                        showToast("Failed to save merchant data. Error: " + innerTask.getException().getMessage());
                                                        // Handle the error.
                                                    }
                                                }
                                            });
                                } else {
                                    showToast("Merchant registration failed. Error: " + task.getException().getMessage());
                                    // Registration failed, handle the error.
                                }
                            }
                        });
            }
        });
    }
}
