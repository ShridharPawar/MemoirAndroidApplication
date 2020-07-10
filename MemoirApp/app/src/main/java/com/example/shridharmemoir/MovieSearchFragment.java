package com.example.shridharmemoir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.adapter.SearchMoviesAdapter;
import com.example.shridharmemoir.networkconnection.EntityClasses.Movie;
import com.example.shridharmemoir.networkconnection.FetchMoviesConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//import butterknife.ButterKnife;

public class MovieSearchFragment extends Fragment {
    private EditText searchedMovie;
    private String API_KEY;
   private FetchMoviesConnection fetchMoviesConnection=null;
    private RecyclerView searchRecyclerView;
    public MovieSearchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moviesearch_fragment, container, false);
        searchedMovie = view.findViewById(R.id.searchMovie);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchRecyclerView = view.findViewById(R.id.searchMoviesRecycler);
        API_KEY = "1fa6e23fd2da0e00d45aefb04a6c9983";
        fetchMoviesConnection = new FetchMoviesConnection();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedMovieString = searchedMovie.getText().toString();
                if(!searchedMovieString.trim().equals("")){
                    SearchMovies searchMovies = new SearchMovies();
                    searchMovies.execute(searchedMovieString);
                }

            }
        });

        return view;

    }

    //Async method to search all movies using the moviedb API
    private class SearchMovies extends AsyncTask<String, Void, ArrayList<Movie>> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Fetching the searched results for you...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            String moviesURL = "https://api.themoviedb.org/3/search/movie?api_key="+API_KEY+
                    "&language=en-US&query="+params[0]+"&page=1&include_adult=false";
            //movies = new ArrayList<>();
            return fetchMoviesConnection.fetchData(moviesURL);
         }

        @Override
        protected void onPostExecute(ArrayList<Movie> results) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(!(results.size()==0))
            {
                SearchMoviesAdapter adapter = new SearchMoviesAdapter(getContext(), results);
                searchRecyclerView.setAdapter(adapter);
                searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            else{
                Toast toast = Toast.makeText(getActivity(),
                        "No movies match this keyword!",
                        Toast.LENGTH_SHORT);
                toast.show();}
        }
    }



    }

