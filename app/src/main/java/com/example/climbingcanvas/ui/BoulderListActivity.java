package com.example.climbingcanvas.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.DatabaseProvider;
import com.example.climbingcanvas.ui.adapters.BoulderAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class BoulderListActivity extends AppCompatActivity {

    private long wallId;
    private String imageUri;
    private BoulderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boulder_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        wallId = getIntent().getLongExtra("wallId", -1);
        String wallName = getIntent().getStringExtra("wallName");
        imageUri = getIntent().getStringExtra("imageUri");
        toolbar.setTitle(wallName != null ? wallName : "Boulders");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewBoulders);
        adapter = new BoulderAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnBoulderClickListener(boulder -> {
            Intent intent = new Intent(this, BoulderDetailActivity.class);
            intent.putExtra("boulderId", boulder.id);
            intent.putExtra("imageUri", imageUri);
            startActivity(intent);
        });

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddBoulder);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateBoulderActivity.class);
            intent.putExtra("wallId", wallId);
            intent.putExtra("imageUri", imageUri);
            startActivity(intent);
        });

        DatabaseProvider.getDatabase(this).boulderDao().getBouldersForWall(wallId).observe(this, boulders -> {
            adapter.setBoulders(boulders);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
