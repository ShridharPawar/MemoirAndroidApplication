package com.example.shridharmemoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shridharmemoir.entity.Watchlist;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchListDAO {
    @Query("SELECT * FROM watchlist")
    LiveData<List<Watchlist>> getAll();


    @Query("SELECT * FROM watchlist WHERE wid = :watchListId LIMIT 1")
    Watchlist findByID(int watchListId);

    @Insert
    void insertAll(Watchlist... watchlists);

    @Insert
    long insert(Watchlist watchlist);

    @Query("DELETE FROM watchlist where wid = :watchlistId")
    void delete(int watchlistId);

    @Update(onConflict = REPLACE)
    void updateWatchlists(Watchlist... watchlists);

    @Query("DELETE FROM watchlist")
    void deleteAll();
}
