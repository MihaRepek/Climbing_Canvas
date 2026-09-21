package com.example.climbingcanvas.data;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "spray_walls",
        foreignKeys = @ForeignKey(entity = Gym.class,
                parentColumns = "id",
                childColumns = "gymId",
                onDelete = CASCADE))
public class SprayWall {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long gymId;
    public String name;
    public String imageUri;

    public SprayWall() {}

    public SprayWall(long gymId, String name, String imageUri) {
        this.gymId = gymId;
        this.name = name;
        this.imageUri = imageUri;
    }
}
