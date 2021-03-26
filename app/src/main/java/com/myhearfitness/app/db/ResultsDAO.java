package com.myhearfitness.app.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultsDAO {
    @Insert
    void insert(Results results);

    @Query("DELETE FROM results_table")
    void deleteAll();

    @Query("SELECT * from results_table")
    LiveData<List<Results>> getAllResults();

    @Query("SELECT * FROM results_table WHERE   _ID = (SELECT MAX(_ID)  FROM results_table)")
    LiveData<Results> getLastResults();
}
