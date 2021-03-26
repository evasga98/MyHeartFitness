package com.myhearfitness.app.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Results.class}, version = 1, exportSchema = false)
public abstract class ResultsRoomDB extends RoomDatabase {

    public abstract ResultsDAO resultsDAO();
    private static ResultsRoomDB INSTANCE;

    static ResultsRoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ResultsRoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ResultsRoomDB.class, "results_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}