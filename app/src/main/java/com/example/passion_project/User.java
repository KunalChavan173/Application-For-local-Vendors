package com.example.passion_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.Exclude;

//public class User extends AppCompatActivity {

    public class User {
        private String firstName;
        private String lastName;
        private String email;
        private String userRole;



        public User(String firstName, String lastName, String email, String userRole) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.userRole = userRole;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUserRole() {
            return userRole;
        }
        public void setUserRole(String userRole) {
            this.userRole = userRole;
        }

        @Exclude // Exclude this method from Firebase serialization
        public boolean isChangingConfigurations() {
            // You can leave this method empty or provide a custom implementation
            return false;
        }
    }
//}