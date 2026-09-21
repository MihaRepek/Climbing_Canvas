package com.example.climbingcanvas.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SprayWallDao {
    @Insert
    long insert(SprayWall wall);

    @Delete
    void delete(SprayWall wall);

    @Query("SELECT * FROM spray_walls WHERE gymId = :gymId ORDER BY name ASC")
    LiveData<List<SprayWall>> getWallsForGym(long gymId);
}
