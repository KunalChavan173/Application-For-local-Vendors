package com.example.passion_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class User_explore extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReelAdapter reelAdapter; // Assuming you have created this adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_explore);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);

        // Set LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create ReelItems (dummy data for now)
        List<ReelItem> reelItemList = generateDummyData();

        // Initialize and set adapter
        reelAdapter = new ReelAdapter(reelItemList);
        recyclerView.setAdapter(reelAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                             @Override
                                             public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                 super.onScrollStateChanged(recyclerView, newState);
                                                 if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                                     int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                                                     int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                                                     // Loop through all items
                                                     for (int i = 0; i < reelAdapter.getItemCount(); i++) {
                                                         ReelAdapter.ReelViewHolder viewHolder = (ReelAdapter.ReelViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                                                         if (viewHolder != null) {
                                                             if (i >= firstVisiblePosition && i <= lastVisiblePosition) {
                                                                 // Start playing the video for this item
                                                                 viewHolder.videoView.start();
                                                             } else {
                                                                 // Pause the video for this item
                                                                 viewHolder.videoView.pause();
                                                             }
                                                         }
                                                     }
                                                 }
                                             }
                                         });

        // Set up Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int itemId = item.getItemId();
                        if (itemId == R.id.navigation_explore) {
                            // Do nothing or add specific logic if needed
                            return true;
                        } else if (itemId == R.id.navigation_main) {
                            startActivity(new Intent(User_explore.this, User_main.class));
                            finish();
                            return true;
                        } else if (itemId == R.id.navigation_account) {
                            startActivity(new Intent(User_explore.this, User_account.class));
                            finish(); // Close the current activity
                            return true;
                        }
                        return false;
                    }
                }
        );
    }

    // Method to generate dummy data (replace with your actual data retrieval logic)
    private List<ReelItem> generateDummyData() {
        List<ReelItem> dummyData = new ArrayList<>();
        // Add dummy data here
        dummyData.add(new ReelItem(R.raw.sample6, "Merchant 1", "Product description 1"));
        dummyData.add(new ReelItem(R.raw.sample7, "Merchant 2", "Product description 2"));
        dummyData.add(new ReelItem(R.raw.sample5, "Merchant 3", "Product description 3"));
        return dummyData;
    }
}
