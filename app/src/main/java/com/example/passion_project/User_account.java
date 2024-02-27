package com.example.passion_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_account extends AppCompatActivity {

    private TextView textViewProfileName;
    private Button buttonLogout;
    private FirebaseAuth firebaseAuth;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        textViewProfileName = findViewById(R.id.textViewProfileName);
        buttonLogout = findViewById(R.id.buttonLogout);
        firebaseAuth = FirebaseAuth.getInstance();



       BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.navigation_explore) {
                            // Do nothing or add specific logic if needed
                            startActivity(new Intent(User_account.this, User_explore.class));
                            return true;
                        } else if (itemId == R.id.navigation_main) {
                            startActivity(new Intent(User_account.this, User_main.class));
                            finish();
                            return true;
                        } else if (itemId == R.id.navigation_account) {
                           // startActivity(new Intent(User_explore.this, User_account.class));
                            finish(); // Close the current activity
                            return true;
                        }
                        return false;
                    }
                }
        );



        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut(); // Log out the user

                // Clear "Remember Me" flag in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("RememberMe", false);
                editor.apply();

                // Redirect to login activity
                Intent intent = new Intent(User_account.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}