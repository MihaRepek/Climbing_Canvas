package com.example.climbingcanvas.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BoulderDao {
    @Insert
    long insert(Boulder boulder);

    @Delete
    void delete(Boulder boulder);

    @Query("SELECT * FROM boulders WHERE sprayWallId = :wallId ORDER BY name ASC")
    LiveData<List<Boulder>> getBouldersForWall(long wallId);

    @Query("SELECT * FROM boulders WHERE id = :id")
    LiveData<Boulder> getBoulderById(long id);
}
