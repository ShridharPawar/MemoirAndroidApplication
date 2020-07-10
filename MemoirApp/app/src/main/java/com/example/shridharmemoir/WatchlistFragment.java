package com.example.shridharmemoir;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.adapter.WatchlistAdapter;
import com.example.shridharmemoir.entity.Watchlist;
import com.example.shridharmemoir.viewmodel.WatchlistViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {
    WatchlistViewModel watchlistViewModel;
    RecyclerView watchlistRecycler;
    public WatchlistFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        final String personid= sharedPref.getString("personid",null);
        watchlistRecycler = view.findViewById(R.id.watchlistrecycler);

        watchlistViewModel = new
                ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getActivity().getApplication());
        //get realtime update on data (if the data in the database changes)
        watchlistViewModel.getAllWatchlists().observe(this, new
                Observer<List<Watchlist>>() {
                    @Override
                    public void onChanged(@Nullable final List<Watchlist> watchlists)
                    {
                        ArrayList<Integer> wids = new ArrayList<Integer>();
                        ArrayList<String> movieNames = new ArrayList<String>();
                        ArrayList<String> releaseDate = new ArrayList<String>();
                        ArrayList<String> dateTimeWatched = new ArrayList<String>();
                        for(Watchlist temp:watchlists){
                            if(temp.getPersonid()==Integer.parseInt(personid))
                            {
                            movieNames.add(temp.getMovieName());
                            releaseDate.add(temp.getReleaseDate());dateTimeWatched.add(temp.getAddedDateTime());
                            wids.add(temp.getWid());
                            }
                       }
                        if(movieNames.size()==0)
                        {
                            Toast toast = Toast.makeText(getActivity(),
                                    "No movies in the watchlist!",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        WatchlistAdapter adapter = new WatchlistAdapter(getContext(),movieNames,releaseDate,dateTimeWatched,wids,watchlistViewModel,Integer.parseInt(personid));
                        watchlistRecycler.setAdapter(adapter);
                        watchlistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                });
return view;
    }


}
