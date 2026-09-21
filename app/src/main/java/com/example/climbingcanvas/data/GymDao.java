package com.example.climbingcanvas.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface GymDao {
    @Insert
    long insert(Gym gym);

    @Update
    void update(Gym gym);

    @Delete
    void delete(Gym gym);

    @Query("SELECT * FROM gyms ORDER BY name ASC")
    LiveData<List<Gym>> getAllGyms();
}
