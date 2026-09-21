package com.example.climbingcanvas.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "boulders",
        foreignKeys = @ForeignKey(entity = SprayWall.class,
                parentColumns = "id",
                childColumns = "sprayWallId",
                onDelete = CASCADE))
public class Boulder {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long sprayWallId;
    public String name;
    public String grade;
    public String description;

    public Boulder() {}

    public Boulder(long sprayWallId, String name, String grade, String description) {
        this.sprayWallId = sprayWallId;
        this.name = name;
        this.grade = grade;
        this.description = description;
    }
}
