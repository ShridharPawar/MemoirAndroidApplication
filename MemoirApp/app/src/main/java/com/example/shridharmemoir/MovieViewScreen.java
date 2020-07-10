package com.example.shridharmemoir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.shridharmemoir.entity.FirebaseWatchlistEntity;
import com.example.shridharmemoir.entity.Watchlist;
import com.example.shridharmemoir.networkconnection.FetchMoviesConnection;
import com.example.shridharmemoir.viewmodel.WatchlistViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MovieViewScreen extends AppCompatActivity {


    DatabaseReference firebaseWatchlists; //firebase

    private Button addToWatchlist;
    String title;
    String releaseDate;
    String imageURL;
    String personid;
    WatchlistViewModel watchlistViewModel;
    private TextView watchlistTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movieviewscreen);
        FetchMovieDetails fetchMovieDetails = new FetchMovieDetails();
        fetchMovieDetails.execute();
        //getIncomingIntent();
        Button addToMemoir = findViewById(R.id.addtomemoir);
        addToWatchlist = findViewById(R.id.addtowatchlist);
        SharedPreferences sharedPref= getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        personid= sharedPref.getString("personid",null);

        firebaseWatchlists = FirebaseDatabase.getInstance().getReference("firebasewatchlists"); //firebase instance

        watchlistViewModel = new
                ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initalizeVars(getApplication());

        //observer pattern to check if the movie has been already added to the watchlist
        watchlistViewModel.getAllWatchlists().observe(this, new
                Observer<List<Watchlist>>() {
                    @Override
                    public void onChanged(@Nullable final List<Watchlist> watchlists)
                    {
                        watchlistTextView = findViewById(R.id.watchlistnotification);
                        for (Watchlist temp : watchlists) {
                            if(temp.getPersonid()==Integer.parseInt(personid) && temp.getMovieName().equals(title)
                            &&temp.getReleaseDate().equals(releaseDate))
                            {
                                addToWatchlist.setEnabled(false);
                                watchlistTextView.setText("Already added to watchlist!");
                                break;
                            }
                        }


                    }
                });

        //add the movie to the watchlist
        addToWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(date);
                Watchlist watchlist = new Watchlist(title,releaseDate,strDate,Integer.parseInt(personid));
                //creat the entity
                FirebaseWatchlistEntity firebaseWatchlist = new FirebaseWatchlistEntity(title,releaseDate,strDate,Integer.parseInt(personid));

                String id = firebaseWatchlists.push().getKey();      //firebase
                firebaseWatchlist.setFid(id);            //firebase
                //add the movie to the firebasewatchlist database
                firebaseWatchlists.child(id).setValue(firebaseWatchlist);              //firebase

                //insert the watchlist
                watchlistViewModel.insert(watchlist);
     Toast toast = Toast.makeText(getApplicationContext(),
                        "Added to Watchlist!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        //go to the addtomemoir screen through the main activity
        addToMemoir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MovieViewScreen.this, MainActivity.class);
                intent.putExtra("movieViewMessage",1);
                intent.putExtra("title",title);
                intent.putExtra("releaseDate",releaseDate);
                intent.putExtra("imageURL",imageURL);
                startActivity(intent);

            }
        });

    }

    //set the values of the elements in the movie view screen
    private void setImage(String imageURL,float rating,String title,String synopsis,String releaseDate,String genre,String Cast,
                          String Director,String Country)
    {
        TextView cast = findViewById(R.id.cast);
        TextView director = findViewById(R.id.director);
        TextView country = findViewById(R.id.country);
        cast.setText(Cast);
        director.setText(Director);
        country.setText(Country);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(rating/2);
        ImageView img = findViewById(R.id.movieImage);
        Picasso.get().load(imageURL).into(img);
        TextView movieName = findViewById(R.id.movieName);
        movieName.setText(title);
        TextView synopsistext = findViewById(R.id.synopsis);
        synopsistext.setText(synopsis);
        TextView releasedate = findViewById(R.id.screenReleaseDate);
        releasedate.setText(releaseDate);
        TextView genre1 = findViewById(R.id.genre);
        genre1.setText(genre);
    }

    //make a call to the API to fetch all data of the movies
    private class FetchMovieDetails extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
                Intent intent=getIntent();
                Bundle bundle=intent.getExtras();
                title = bundle.getString("title");
                releaseDate = bundle.getString("releaseDate");
                imageURL = bundle.getString("image_url");
                int movieid = bundle.getInt("movieid");
                FetchMoviesConnection fetchMoviesConnection = new FetchMoviesConnection();
                return fetchMoviesConnection.fetchDetailsForMovieView(title,releaseDate,imageURL,movieid);
        }

        @Override
        protected void onPostExecute(ArrayList<String> movieResult) {
            setImage(movieResult.get(8),Float.parseFloat(movieResult.get(6)),movieResult.get(7),movieResult.get(5),
                    movieResult.get(2),movieResult.get(0),movieResult.get(1),movieResult.get(4),movieResult.get(3));

        }
    }
}
