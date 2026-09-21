package com.example.climbingcanvas.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.SprayWall;
import java.util.ArrayList;
import java.util.List;

public class SprayWallAdapter extends RecyclerView.Adapter<SprayWallAdapter.WallViewHolder> {
    private List<SprayWall> walls = new ArrayList<>();
    private OnWallClickListener listener;

    public interface OnWallClickListener {
        void onWallClick(SprayWall wall);
    }

    public void setWalls(List<SprayWall> walls) {
        this.walls = walls;
        notifyDataSetChanged();
    }

    public void setOnWallClickListener(OnWallClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spray_wall, parent, false);
        return new WallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallViewHolder holder, int position) {
        SprayWall wall = walls.get(position);
        holder.textViewName.setText(wall.name);
        Glide.with(holder.itemView.getContext())
                .load(wall.imageUri)
                .into(holder.imageViewThumbnail);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onWallClick(wall);
        });
    }

    @Override
    public int getItemCount() {
        return walls.size();
    }

    static class WallViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;
        TextView textViewName;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewWallThumbnail);
            textViewName = itemView.findViewById(R.id.textViewWallName);
        }
    }
}
