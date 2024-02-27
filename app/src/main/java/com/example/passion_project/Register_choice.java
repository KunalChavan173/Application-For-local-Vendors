package com.example.passion_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Register_choice extends AppCompatActivity {

    private RadioGroup radioGroupChoice;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_choice);

        radioGroupChoice = findViewById(R.id.radioGroupChoice);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupChoice.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                if (selectedRadioButton != null) {
                    String choice = selectedRadioButton.getText().toString();
                    if (choice.equals("Merchant")) {
                        // Redirect to MerchantRegistrationActivity
                        Intent intent = new Intent(Register_choice.this, Register_merchant.class);
                        startActivity(intent);
                    } else if (choice.equals("User")) {
                        // Redirect to UserRegistrationActivity
                        Intent intent = new Intent(Register_choice.this, Register_user.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}