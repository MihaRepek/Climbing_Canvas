package com.example.climbingcanvas.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface HoldDao {
    @Insert
    void insertAll(List<Hold> holds);

    @Query("SELECT * FROM holds WHERE boulderId = :boulderId")
    LiveData<List<Hold>> getHoldsForBoulder(long boulderId);

    @Query("SELECT * FROM holds WHERE boulderId = :boulderId")
    List<Hold> getHoldsForBoulderSync(long boulderId);
}
