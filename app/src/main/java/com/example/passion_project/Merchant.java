package com.example.passion_project;

import com.google.firebase.database.Exclude;

public class Merchant {
    private String firstName;
    private String lastName;
    private String email;
    private String shopName;
    private String shopAddress;
    private String shopType;
    private String userRole;

    public Merchant() {
        // Default constructor is required for Firebase.
    }

    public Merchant(String firstName, String lastName, String email, String shopName, String shopAddress, String shopType, String userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }
}
