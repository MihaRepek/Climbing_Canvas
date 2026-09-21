package com.example.climbingcanvas.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.climbingcanvas.R;
import com.example.climbingcanvas.data.Boulder;
import com.example.climbingcanvas.data.DatabaseProvider;
import com.example.climbingcanvas.data.Hold;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

public class CreateBoulderActivity extends AppCompatActivity {

    private long wallId;
    private HoldDrawingView holdDrawingView;
    private final List<Hold> pendingHolds = new ArrayList<>();
    private String currentType = "HAND";
    private Hold currentHold;
    private View layoutHoldControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_boulder);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        wallId = getIntent().getLongExtra("wallId", -1);
        String imageUri = getIntent().getStringExtra("imageUri");

        holdDrawingView = findViewById(R.id.holdDrawingView);
        layoutHoldControls = findViewById(R.id.layoutHoldControls);

        if (imageUri != null) {
            holdDrawingView.setImageURI(Uri.parse(imageUri));
            holdDrawingView.post(() -> holdDrawingView.fitImageToWidth());
        }

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroupHoldType);
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.buttonStart) currentType = "START";
                else if (checkedId == R.id.buttonTop) currentType = "TOP";
                else if (checkedId == R.id.buttonFoot) currentType = "FOOT";
                else currentType = "HAND";
                
                if (currentHold != null) {
                    currentHold.type = currentType;
                    holdDrawingView.invalidate();
                }
            }
        });

        holdDrawingView.setOnHoldPlacedListener(new HoldDrawingView.OnHoldPlacedListener() {
            @Override
            public void onHoldPlaced(float x, float y) {
                if (currentHold != null) return; // Finish current one first

                currentHold = new Hold();
                currentHold.x = x;
                currentHold.y = y;
                currentHold.radius = 0.03f;
                currentHold.type = currentType;
                
                holdDrawingView.setSelectedHold(currentHold);
                List<Hold> all = new ArrayList<>(pendingHolds);
                all.add(currentHold);
                holdDrawingView.setHolds(all);
                
                layoutHoldControls.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHoldEdited(Hold hold) {
                // Already updated in view
            }
        });

        findViewById(R.id.buttonIncreaseRadius).setOnClickListener(v -> holdDrawingView.increaseRadius());
        findViewById(R.id.buttonDecreaseRadius).setOnClickListener(v -> holdDrawingView.decreaseRadius());
        findViewById(R.id.buttonConfirmHold).setOnClickListener(v -> {
            if (currentHold != null) {
                pendingHolds.add(currentHold);
                currentHold = null;
                holdDrawingView.setSelectedHold(null);
                layoutHoldControls.setVisibility(View.GONE);
            }
        });

        EditText editTextName = findViewById(R.id.editTextBoulderName);
        EditText editTextGrade = findViewById(R.id.editTextBoulderGrade);
        Button buttonSave = findViewById(R.id.buttonSaveBoulder);

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String grade = editTextGrade.getText().toString();

            if (!name.isEmpty()) {
                saveBoulder(name, grade);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void saveBoulder(String name, String grade) {
        DatabaseProvider.getExecutor().execute(() -> {
            long boulderId = DatabaseProvider.getDatabase(this).boulderDao().insert(
                    new Boulder(wallId, name, grade, ""));

            for (Hold hold : pendingHolds) {
                hold.boulderId = boulderId;
            }
            DatabaseProvider.getDatabase(this).holdDao().insertAll(pendingHolds);
            finish();
        });
    }
}
