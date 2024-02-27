package com.example.passion_project;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ReelViewHolder> {

    private List<ReelItem> reelItemList;

    public ReelAdapter(List<ReelItem> reelItemList) {
        this.reelItemList = reelItemList;
    }

    @NonNull
    @Override
    public ReelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reel_item, parent, false);
        return new ReelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelViewHolder holder, int position) {
        ReelItem reelItem = reelItemList.get(position);

        // Set data to views
        holder.videoView.setVideoURI(Uri.parse("android.resource://" + holder.itemView.getContext().getPackageName() + "/" + reelItem.getVideoResId()));
        holder.videoView.start();
        holder.merchantName.setText(reelItem.getMerchantName());
        holder.description.setText(reelItem.getDescription());

        // Set up interaction button click listeners if needed

        // Example: Share button click listener
        holder.btnShare.setOnClickListener(v -> {
            // Handle share button click
            // Implement your share functionality here
        });

        // Example: Comment button click listener
        holder.btnComment.setOnClickListener(v -> {
            // Handle comment button click
            // Implement your comment functionality here
        });
    }

    @Override
    public int getItemCount() {
        return reelItemList.size();
    }

    public static class ReelViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView merchantName;
        TextView description;
         ImageButton btnShare;
        ImageButton btnComment;

        public ReelViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
           merchantName = itemView.findViewById(R.id.username);
            description = itemView.findViewById(R.id.description);
          btnShare = itemView.findViewById(R.id.btnShare);
           btnComment = itemView.findViewById(R.id.btnComment);
        }
    }
}
