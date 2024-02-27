package com.example.passion_project;

public class ReelItem {
    private int videoResId;
    private String merchantName;
    private String description;

    public ReelItem(int videoResId, String merchantName, String description) {
        this.videoResId = videoResId;
        this.merchantName = merchantName;
        this.description = description;
    }

    public int getVideoResId() {
        return videoResId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getDescription() {
        return description;
    }
}
