package com.example.climbingcanvas.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "holds",
        foreignKeys = @ForeignKey(entity = Boulder.class,
                parentColumns = "id",
                childColumns = "boulderId",
                onDelete = CASCADE))
public class Hold {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long boulderId;
    public float x;
    public float y;
    public float radius;
    public String type; // START, TOP, HAND, FOOT

    public Hold() {}

    public Hold(long boulderId, float x, float y, float radius, String type) {
        this.boulderId = boulderId;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.type = type;
    }
}
