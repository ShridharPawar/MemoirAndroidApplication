package com.example.shridharmemoir.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.MovieViewScreen;
import com.example.shridharmemoir.R;
import com.example.shridharmemoir.networkconnection.FetchMoviesConnection;
import com.example.shridharmemoir.viewmodel.WatchlistViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FirebaseWatchlistAdapter extends RecyclerView.Adapter<FirebaseWatchlistAdapter.MyViewHolder>{
    ArrayList<String> data1; ArrayList<String> data2;ArrayList<String> data3;
    int personid;ArrayList<String> fids;
    Context ct;

    public FirebaseWatchlistAdapter(Context con,ArrayList<String> movieNames,ArrayList<String> releaseDates,ArrayList<String> dateTimeWatched,
                            int personid,ArrayList<String> fids)
    {
        ct = con;
        data1 = movieNames;
        data2 = releaseDates;
        data3 = dateTimeWatched;
        this.personid = personid;
        this.fids = fids;
    }

    @NonNull
    @Override
    public FirebaseWatchlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.firebase_rv,parent,false);
        return new FirebaseWatchlistAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirebaseWatchlistAdapter.MyViewHolder holder, final int position)
    {
        holder.myText1.setText(data1.get(position));
        holder.myText2.setText(data2.get(position));
        holder.myText3.setText(data3.get(position));

        //action to do when view button is clicked
        holder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              FetchMovieViewForWatchList fetchMovieViewForWatchList = new FetchMovieViewForWatchList();
                fetchMovieViewForWatchList.execute(data1.get(position),data2.get(position));

            }
        });
        //action to do when delete button is clicked
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(ct);
                builder.setMessage("Are you sure want to delete?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fid = fids.get(position);
                                DatabaseReference delWatchlist = FirebaseDatabase.getInstance().getReference("firebasewatchlists").child(fid);
                                delWatchlist.removeValue();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
       }
        });
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1, myText2, myText3;
        Button viewbtn;Button deletebtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.firebasemovieName);
            myText2 = itemView.findViewById(R.id.firebasereleaseDate);
            myText3 = itemView.findViewById(R.id.firebasedateTimeAdded);
            viewbtn = itemView.findViewById(R.id.firebaseviewbtn);
            deletebtn = itemView.findViewById(R.id.firebasedeletebtn);

        }
    }

    //code to fetch movie details to be shown in the movie view screen
    private class FetchMovieViewForWatchList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String year = params[1].substring(0,4);
            FetchMoviesConnection fetchMoviesConnection = new FetchMoviesConnection();
            return fetchMoviesConnection.fetchDataForWatchlistAndMemoirAdapter(params[0],year);

        }

        @Override
        protected void onPostExecute(String textResult) {
            //call the movie view screen
            JSONArray resArray = null;
            try {
                JSONObject mainObject = new JSONObject(textResult);
                resArray = mainObject.getJSONArray("results");
                resArray.getJSONObject(0).getString("poster_path");
                Intent intent = new Intent(ct, MovieViewScreen.class);
                Bundle bundle = new Bundle();
                String imageurl = "https://image.tmdb.org/t/p/w185/"+resArray.getJSONObject(0).getString("poster_path");
                int movieid = resArray.getJSONObject(0).getInt("id");
                bundle.putString("image_url", imageurl);
                bundle.putString("title", resArray.getJSONObject(0).getString("original_title"));
                bundle.putString("releaseDate", resArray.getJSONObject(0).getString("release_date"));
                bundle.putInt("movieid", movieid);
                intent.putExtras(bundle);
                ct.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
