package com.example.climbingcanvas.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gyms")
public class Gym {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;

    public Gym() {}

    public Gym(String name) {
        this.name = name;
    }
}
