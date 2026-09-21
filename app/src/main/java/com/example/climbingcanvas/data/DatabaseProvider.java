package com.example.climbingcanvas.data;

import android.content.Context;
import androidx.room.Room;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseProvider {
    private static AppDatabase database;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static synchronized AppDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "climbing_canvas_db")
                    .build();
        }
        return database;
    }

    public static ExecutorService getExecutor() {
        return executor;
    }
}
