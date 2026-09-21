package com.example.climbingcanvas.ui;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.DatabaseProvider;

public class BoulderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boulder_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        long boulderId = getIntent().getLongExtra("boulderId", -1);
        String imageUri = getIntent().getStringExtra("imageUri");

        HoldDrawingView holdDrawingView = findViewById(R.id.holdDrawingViewDetail);
        if (imageUri != null) {
            holdDrawingView.setImageURI(Uri.parse(imageUri));
            holdDrawingView.post(() -> holdDrawingView.fitImageToWidth());
        }

        TextView textViewName = findViewById(R.id.textViewDetailName);
        TextView textViewGrade = findViewById(R.id.textViewDetailGrade);
        TextView textViewDescription = findViewById(R.id.textViewDetailDescription);

        DatabaseProvider.getDatabase(this).boulderDao().getBoulderById(boulderId).observe(this, boulder -> {
            if (boulder != null) {
                textViewName.setText(boulder.name);
                textViewGrade.setText(boulder.grade);
                textViewDescription.setText(boulder.description);
                toolbar.setTitle(boulder.name);
            }
        });

        DatabaseProvider.getDatabase(this).holdDao().getHoldsForBoulder(boulderId).observe(this, holds -> {
            if (holds != null) {
                holdDrawingView.setDimmingEnabled(true);
                holdDrawingView.setHolds(holds);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
