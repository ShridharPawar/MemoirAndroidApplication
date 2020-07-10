package com.example.shridharmemoir.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.shridharmemoir.MovieViewScreen;
import com.example.shridharmemoir.MovieViewScreen;
import com.example.shridharmemoir.R;
import com.example.shridharmemoir.networkconnection.EntityClasses.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchMoviesAdapter extends RecyclerView.Adapter<SearchMoviesAdapter.MyViewHolder> {
    ArrayList<Movie> data1;
    Context context;

    public SearchMoviesAdapter(Context ct, ArrayList<Movie> movies)
    {
        context = ct;
        data1 = movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.moviesearch_rv,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final String movieName = data1.get(position).getOriginalTitle();
        String releaseDate = data1.get(position).getReleaseDate();
        String url = "https://image.tmdb.org/t/p/w185/"+ data1.get(position).getPosterPath();
        holder.myText1.setText(movieName);
        holder.myText2.setText(releaseDate.length()>3?releaseDate.substring(0,4):releaseDate);
        Picasso.get().load(url).into(holder.myImage1);
        //go to the movie view screen when the movie is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "https://image.tmdb.org/t/p/w185/" + data1.get(position).getPosterPath();
                Intent intent = new Intent(context, MovieViewScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_url", url1);
                bundle.putString("title", data1.get(position).getOriginalTitle());
                bundle.putString("releaseDate", data1.get(position).getReleaseDate());
                bundle.putInt("movieid", data1.get(position).getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView myText1, myText2;
        ImageView myImage1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.movieName);
            myText2 = itemView.findViewById(R.id.releaseYear);
            myImage1 = itemView.findViewById(R.id.movieImage);
        }
    }






}
