package com.example.shridharmemoir.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shridharmemoir.entity.Watchlist;
import com.example.shridharmemoir.repository.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {
    private WatchlistRepository wRepository;
    private MutableLiveData<List<Watchlist>> allWatchlists;

    public WatchlistViewModel () {
        allWatchlists=new MutableLiveData<>();
    }

    public void setWatchlists(List<Watchlist> watchlists) {
        allWatchlists.setValue(watchlists);
    }

    public LiveData<List<Watchlist>> getAllWatchlists() {
        return wRepository.getAllWatchlists();
    }
    public void initalizeVars(Application application){
        wRepository = new WatchlistRepository(application);
    }
    public void insert(Watchlist watchlist)
    {
        wRepository.insert(watchlist);
    }
    public void insertAll(Watchlist... watchlists) {
        wRepository.insertAll(watchlists);
    }
    /*public void delete(Watchlist watchlist)
    {
        wRepository.delete(watchlist);
    }*/

    public void delete(int watchlistId)
    {
        wRepository.delete(watchlistId);
    }


    public void deleteAll() {
        wRepository.deleteAll();
    }

    public void update(Watchlist... watchlist) {
        wRepository.updateWatchlists(watchlist);
    }
    public Watchlist insertAll(int id) {
        return wRepository.findByID(id);
    }
    public Watchlist findByID(int watchlist){
        return wRepository.findByID(watchlist);
    }
}
