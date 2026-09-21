package com.example.climbingcanvas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingcanvas.data.DatabaseProvider;
import com.example.climbingcanvas.data.Gym;
import com.example.climbingcanvas.ui.SprayWallActivity;
import com.example.climbingcanvas.ui.adapters.GymAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GymAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewGyms);
        adapter = new GymAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnGymClickListener(new GymAdapter.OnGymClickListener() {
            @Override
            public void onGymClick(Gym gym) {
                Intent intent = new Intent(MainActivity.this, SprayWallActivity.class);
                intent.putExtra("gymId", gym.id);
                intent.putExtra("gymName", gym.name);
                startActivity(intent);
            }

            @Override
            public void onEditClick(Gym gym) {
                showAddGymDialog(gym);
            }
        });

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddGym);
        fab.setOnClickListener(v -> showAddGymDialog(null));

        DatabaseProvider.getDatabase(this).gymDao().getAllGyms().observe(this, gyms -> {
            adapter.setGyms(gyms);
        });
    }

    private void showAddGymDialog(Gym existingGym) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_gym, null);
        EditText editText = view.findViewById(R.id.editTextGymName);
        if (existingGym != null) {
            editText.setText(existingGym.name);
        }

        new AlertDialog.Builder(this)
                .setTitle(existingGym == null ? "Add Gym" : "Edit Gym")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = editText.getText().toString();
                    if (!name.isEmpty()) {
                        DatabaseProvider.getExecutor().execute(() -> {
                            if (existingGym == null) {
                                DatabaseProvider.getDatabase(this).gymDao().insert(new Gym(name));
                            } else {
                                existingGym.name = name;
                                DatabaseProvider.getDatabase(this).gymDao().update(existingGym);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
