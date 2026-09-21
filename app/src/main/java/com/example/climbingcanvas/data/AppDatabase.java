package com.example.climbingcanvas.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Gym.class, SprayWall.class, Boulder.class, Hold.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GymDao gymDao();
    public abstract SprayWallDao sprayWallDao();
    public abstract BoulderDao boulderDao();
    public abstract HoldDao holdDao();
}
