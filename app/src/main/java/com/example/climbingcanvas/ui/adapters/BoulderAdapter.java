package com.example.climbingcanvas.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.Boulder;
import java.util.ArrayList;
import java.util.List;

public class BoulderAdapter extends RecyclerView.Adapter<BoulderAdapter.BoulderViewHolder> {
    private List<Boulder> boulders = new ArrayList<>();
    private OnBoulderClickListener listener;

    public interface OnBoulderClickListener {
        void onBoulderClick(Boulder boulder);
    }

    public void setBoulders(List<Boulder> boulders) {
        this.boulders = boulders;
        notifyDataSetChanged();
    }

    public void setOnBoulderClickListener(OnBoulderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BoulderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boulder, parent, false);
        return new BoulderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoulderViewHolder holder, int position) {
        Boulder boulder = boulders.get(position);
        holder.textViewName.setText(boulder.name);
        holder.textViewGrade.setText(boulder.grade);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBoulderClick(boulder);
        });
    }

    @Override
    public int getItemCount() {
        return boulders.size();
    }

    static class BoulderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewGrade;

        public BoulderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewBoulderName);
            textViewGrade = itemView.findViewById(R.id.textViewBoulderGrade);
        }
    }
}
