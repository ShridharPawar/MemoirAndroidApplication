package com.example.shridharmemoir.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.shridharmemoir.dao.WatchListDAO;
import com.example.shridharmemoir.database.WatchlistDatabase;
import com.example.shridharmemoir.entity.Watchlist;

import java.util.List;

public class WatchlistRepository {
    private WatchListDAO dao;
    private LiveData<List<Watchlist>> allWatchlists;
    private Watchlist watchlist;
    public WatchlistRepository(Application application){
        WatchlistDatabase db = WatchlistDatabase.getInstance(application);
        dao=db.watchListDAO();
    }
    public LiveData<List<Watchlist>> getAllWatchlists() {
        allWatchlists=dao.getAll();
        return allWatchlists;
    }
    public void insert(final Watchlist watchlist){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(watchlist);
            }
        });
    }
    public void deleteAll(){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }
    /*public void delete(final Watchlist watchlist)
    {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(watchlist);
            }
        });
    }*/

    public void delete(final int watchlistId)
    {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(watchlistId);
            }
        });
    }


    public void insertAll(final Watchlist... watchlists){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(watchlists);
            }
        });
    }
    public void updateWatchlists(final Watchlist... watchlists){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateWatchlists(watchlists);
            }
        });
    }
    public Watchlist findByID(final int watchlistId){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Watchlist watchlist= dao.findByID(watchlistId);
                setWatchlist(watchlist);
            }
        });
        return watchlist;
    }
    public void setWatchlist(Watchlist watchlist){
        this.watchlist=watchlist;
    }
}
