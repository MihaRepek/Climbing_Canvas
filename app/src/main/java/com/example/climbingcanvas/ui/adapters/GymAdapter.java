package com.example.climbingcanvas.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.Gym;
import java.util.ArrayList;
import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.GymViewHolder> {
    private List<Gym> gyms = new ArrayList<>();
    private OnGymClickListener listener;

    public interface OnGymClickListener {
        void onGymClick(Gym gym);
        void onEditClick(Gym gym);
    }

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
        notifyDataSetChanged();
    }

    public void setOnGymClickListener(OnGymClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GymViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym, parent, false);
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder holder, int position) {
        Gym gym = gyms.get(position);
        holder.textViewName.setText(gym.name);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onGymClick(gym);
        });
        holder.buttonEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(gym);
        });
    }

    @Override
    public int getItemCount() {
        return gyms.size();
    }

    static class GymViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageButton buttonEdit;

        public GymViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewGymName);
            buttonEdit = itemView.findViewById(R.id.buttonEditGym);
        }
    }
}
