package com.example.shridharmemoir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.adapter.FirebaseWatchlistAdapter;
import com.example.shridharmemoir.entity.FirebaseWatchlistEntity;
import com.example.shridharmemoir.entity.Watchlist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseWatchlist extends Fragment {
    RecyclerView firebasewatchlistrecycler;
    DatabaseReference firebaseWatchlists; //firebase
    String personid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.firebase_watchlist, container, false);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        personid= sharedPref.getString("personid",null);
        firebasewatchlistrecycler = view.findViewById(R.id.firebasewatchlistrecycler);
        firebaseWatchlists = FirebaseDatabase.getInstance().getReference("firebasewatchlists"); //firebase



       return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        //get realtime update on data (if the data in the database changes)
        firebaseWatchlists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> movieNames = new ArrayList<String>();
                ArrayList<String> releaseDate = new ArrayList<String>();
                ArrayList<String> dateTimeWatched = new ArrayList<String>();
                ArrayList<String> fids = new ArrayList<String>();
                movieNames.clear();
                releaseDate.clear();
                dateTimeWatched.clear();
                fids.clear();
                for(DataSnapshot watchlistSnapshot:dataSnapshot.getChildren())
                {
                    FirebaseWatchlistEntity watchlist = watchlistSnapshot.getValue(FirebaseWatchlistEntity.class);
                    if(watchlist.getPersonid()==Integer.parseInt(personid)) {
                        movieNames.add(watchlist.getMovieName());
                        releaseDate.add(watchlist.getReleaseDate());
                        dateTimeWatched.add(watchlist.getAddedDateTime());
                        fids.add(watchlist.getFid());
                    }

                }
                if(movieNames.size()==0)
                {
                    Toast toast = Toast.makeText(getActivity(),
                            "No movies in the watchlist!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                //call the recycler view adapter to set data
                FirebaseWatchlistAdapter adapter = new FirebaseWatchlistAdapter(getContext(),movieNames,releaseDate,dateTimeWatched,Integer.parseInt(personid),fids);
                firebasewatchlistrecycler.setAdapter(adapter);
                firebasewatchlistrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
