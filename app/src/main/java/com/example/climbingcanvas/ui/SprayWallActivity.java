package com.example.climbingcanvas.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.DatabaseProvider;
import com.example.climbingcanvas.data.SprayWall;
import com.example.climbingcanvas.ui.adapters.SprayWallAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class SprayWallActivity extends AppCompatActivity {

    private long gymId;
    private SprayWallAdapter adapter;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private String pendingWallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spray_wall);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        gymId = getIntent().getLongExtra("gymId", -1);
        String gymName = getIntent().getStringExtra("gymName");
        toolbar.setTitle(gymName != null ? gymName : "Walls");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewWalls);
        adapter = new SprayWallAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnWallClickListener(wall -> {
            Intent intent = new Intent(this, BoulderListActivity.class);
            intent.putExtra("wallId", wall.id);
            intent.putExtra("wallName", wall.name);
            intent.putExtra("imageUri", wall.imageUri);
            startActivity(intent);
        });

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                saveWall(uri.toString());
            }
        });

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddWall);
        fab.setOnClickListener(v -> showAddWallDialog());

        DatabaseProvider.getDatabase(this).sprayWallDao().getWallsForGym(gymId).observe(this, walls -> {
            adapter.setWalls(walls);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showAddWallDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_gym, null);
        EditText editText = view.findViewById(R.id.editTextGymName);
        editText.setHint("Wall Name");

        new AlertDialog.Builder(this)
                .setTitle("Add Spray Wall")
                .setView(view)
                .setPositiveButton("Pick Image", (dialog, which) -> {
                    pendingWallName = editText.getText().toString();
                    if (!pendingWallName.isEmpty()) {
                        pickMedia.launch(new PickVisualMediaRequest.Builder()
                                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                .build());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveWall(String imageUri) {
        DatabaseProvider.getExecutor().execute(() -> {
            DatabaseProvider.getDatabase(this).sprayWallDao().insert(new SprayWall(gymId, pendingWallName, imageUri));
        });
    }
}
