package com.example.shridharmemoir.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.MovieViewScreen;
import com.example.shridharmemoir.R;
import com.example.shridharmemoir.networkconnection.EntityClasses.MemoirAndUrl;
import com.example.shridharmemoir.networkconnection.EntityClasses.MovieMemoir;
import com.example.shridharmemoir.networkconnection.FetchMoviesConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieMemoirAdapter extends RecyclerView.Adapter<MovieMemoirAdapter.MyViewHolder> {
    ArrayList<MemoirAndUrl> data1;
    Context ct;
    boolean publicRatingVisible;

    public MovieMemoirAdapter(Context ct, ArrayList<MemoirAndUrl> memoir,boolean publicRatingVisible)
    {
            this.ct = ct;
            this.data1 = memoir;
            this.publicRatingVisible = publicRatingVisible;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.memoir_rv,parent,false);
        return new MovieMemoirAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.myText1.setText(data1.get(position).getMoviename());
        holder.myText2.setText(data1.get(position).getMoviereleasedate());
        holder.myText3.setText(data1.get(position).getDatetimewatched());
        holder.myText4.setText(data1.get(position).getCinemaid().getLocation());
        holder.myText5.setText(data1.get(position).getComment());
        Double ratingdob = data1.get(position).getUserrating();
        String ratingstr = ratingdob.toString();
        holder.rating.setRating(Float.parseFloat(ratingstr));
        Double publicratingdob = data1.get(position).getPublicRating();
        String publicratingstr = publicratingdob.toString();
        holder.publicMemoirRating.setRating(Float.parseFloat(publicratingstr)/2);
        Picasso.get().load(data1.get(position).getUrl()).into(holder.myImage1);
        //go to the movie view screen when the movie is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchMovieViewForMemoir fetchMovieViewForMemoir = new FetchMovieViewForMemoir();
                fetchMovieViewForMemoir.execute(data1.get(position).getMoviename(),
                        data1.get(position).getMoviereleasedate());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1, myText2,myText3,myText4,myText5;
        RatingBar rating;
        ImageView myImage1;
        RatingBar publicMemoirRating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.movieName);
            myText2 = itemView.findViewById(R.id.releaseDate);
            myText3 = itemView.findViewById(R.id.dateWatcheddialog);
            myText4 = itemView.findViewById(R.id.cinemaPostcode);
            myText5 = itemView.findViewById(R.id.comment);
            rating = itemView.findViewById(R.id.memoirRating);
            myImage1 = itemView.findViewById(R.id.movieImage);
            publicMemoirRating = itemView.findViewById(R.id.publicMemoirRating);
            if(publicRatingVisible)
            {
                publicMemoirRating.setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.textView7).setVisibility(View.VISIBLE);
            }
        }
    }

    //code to fetch movie details to be shown in the movie view screen
    private class FetchMovieViewForMemoir extends AsyncTask<String, Void, String> {
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
