package com.example.shridharmemoir;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.adapter.MovieMemoirAdapter;
import com.example.shridharmemoir.networkconnection.EntityClasses.MemoirAndUrl;
import com.example.shridharmemoir.networkconnection.EntityClasses.Movie;
import com.example.shridharmemoir.networkconnection.NetworkConnection;
import com.google.common.collect.Lists;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class MovieMemoirFragment extends Fragment {
    private ArrayList<MemoirAndUrl> sortedMovies;
    private ArrayList<MemoirAndUrl> tempMovies;
    private RecyclerView memoirRecyclerView;
    NetworkConnection networkConnection=null;
    private String selectedSortSpinner;
    private String selectedFilter;

    public MovieMemoirFragment()
    {

    }
    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.moviememoir_fragment, container, false);
        SharedPreferences sharedPref= getActivity().
                getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        String personid = sharedPref.getString("personid", null);
        memoirRecyclerView = view.findViewById(R.id.memoirrecycler);
        networkConnection = new NetworkConnection();
        sortedMovies = new ArrayList<>();
        ArrayList<String> vals = Lists.newArrayList("Sort by Date","Sort by Rating","Sort by Public Score");
        ArrayList<String> filterVals = Lists.newArrayList("All","Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family",
                "Fantasy","History","Horror","Music","Mystery","Romance","Science Fiction","TV Movie","War","Western");
        Spinner sortbyspinner = view.findViewById(R.id.sortbyspinner);
        Spinner filterSpinner = view.findViewById(R.id.filterSpinner);
        ArrayAdapter<String> spinnerAdapter = new
                ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, vals);
        ArrayAdapter<String> filterSpinnerAdapter = new
                ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, filterVals);
        selectedSortSpinner="Sort by Date";
        selectedFilter="All";
        sortbyspinner.setAdapter(spinnerAdapter);
        filterSpinner.setAdapter(filterSpinnerAdapter);
        MemoirMovies memoirMovies = new MemoirMovies();
        memoirMovies.execute(personid);
        //code to set the value to the spinner
        sortbyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                selectedSortSpinner = parent.getItemAtPosition(position).toString();
                if(selectedSortSpinner.equals("Sort by Rating")){sortMoviesByUserScore(sortedMovies);}
                else if(selectedSortSpinner.equals("Sort by Public Score")){sortMoviesByPublicRating(sortedMovies);}
                else if(selectedSortSpinner.equals("Sort by Date")){sortMoviesByDate(sortedMovies);}

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //genre filter spinner
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                selectedFilter = parent.getItemAtPosition(position).toString();
                filterMovies(selectedFilter);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    //async method to fetch all memories
    private class MemoirMovies extends AsyncTask<String, Void, ArrayList<MemoirAndUrl>> {

        //show the dialog box till all memories are fetched
        private ProgressDialog dialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Fetching your memories...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<MemoirAndUrl> doInBackground(String... params) {
            return networkConnection.fetchMemoir(params[0]);
       }

        @Override
        protected void onPostExecute(ArrayList<MemoirAndUrl> result)
        {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(result.size()==0){ Toast toast = Toast.makeText(getActivity(),
                    "No memoirs!",
                    Toast.LENGTH_SHORT);
                toast.show();}
            else{sortedMovies = result;
                sortMoviesByDate(sortedMovies);}

     }
    }

    //code to filter the movies based on the genre
    public void filterMovies(String genre)
    {
        if(genre.equals("All"))
        {
            if(selectedSortSpinner.equals("Sort by Rating")){sortMoviesByUserScore(sortedMovies);}
            else if(selectedSortSpinner.equals("Sort by Public Score")){sortMoviesByPublicRating(sortedMovies);}
            else if(selectedSortSpinner.equals("Sort by Date")){sortMoviesByDate(sortedMovies);}
        }
        else
         {
            tempMovies = new ArrayList<>();
            for (MemoirAndUrl m : sortedMovies)
            {
                if (m.getGenre().equals(genre)) {
                    tempMovies.add(m);
                }
            }
            if(tempMovies.size()==0){Toast toast = Toast.makeText(getActivity(),
                    "No movies match this criteria!",
                    Toast.LENGTH_SHORT);
                toast.show();}
            else{
             if(selectedSortSpinner.equals("Sort by Rating")){sortMoviesByUserScore(tempMovies);}
             else if(selectedSortSpinner.equals("Sort by Public Score")){sortMoviesByPublicRating(tempMovies);}
             else if(selectedSortSpinner.equals("Sort by Date")){sortMoviesByDate(tempMovies);}}
        }
    }

    //function to sort the movies by the datetimewatched
    public void sortMoviesByDate(ArrayList<MemoirAndUrl> toBeSorted)
    {
        ArrayList<MemoirAndUrl> temp = new ArrayList<>();
        temp = toBeSorted;
        if(!(selectedFilter.equals("All")))
        {
            temp = tempFunction(temp);
        }
        Collections.sort(temp, new Comparator<MemoirAndUrl>() {
            @Override
            public int compare(MemoirAndUrl lhs, MemoirAndUrl rhs) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date1=null;Date date2=null;
                try{
                date1 = format.parse(lhs.getDatetimewatched());
                date2 = format.parse(rhs.getDatetimewatched());}catch (Exception e){}
                return (date1).compareTo(date2);
      }
      });
     if(!(temp.size()==0)) {

            Collections.reverse(temp);
            MovieMemoirAdapter movieMemoirAdapter = new MovieMemoirAdapter(getContext(), temp,false);
            memoirRecyclerView.setAdapter(movieMemoirAdapter);
            memoirRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

    }

    //function to sort the memories by the user rating
    public void sortMoviesByUserScore(ArrayList<MemoirAndUrl> toBeSorted)
    {
        ArrayList<MemoirAndUrl> temp = new ArrayList<>();
        temp = toBeSorted;
        if(!(selectedFilter.equals("All")))
        {
            temp = tempFunction(temp);

        }
        Collections.sort(temp, new Comparator<MemoirAndUrl>() {
            @Override
            public int compare(MemoirAndUrl lhs, MemoirAndUrl rhs) {
                return (Double.compare(lhs.getUserrating(),rhs.getUserrating()));
            }
        });
        if(!(temp.size()==0)) {
            Collections.reverse(temp);
            MovieMemoirAdapter movieMemoirAdapter = new MovieMemoirAdapter(getContext(), temp,false);
            memoirRecyclerView.setAdapter(movieMemoirAdapter);
            memoirRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    //function to sort the movies by the public rating
    public void sortMoviesByPublicRating(ArrayList<MemoirAndUrl> toBeSorted)
    {
        ArrayList<MemoirAndUrl> temp = new ArrayList<>();
        temp = toBeSorted;
        if(!(selectedFilter.equals("All")))
        {
            temp = tempFunction(temp);
        }
        Collections.sort(temp, new Comparator<MemoirAndUrl>() {
            @Override
            public int compare(MemoirAndUrl lhs, MemoirAndUrl rhs) {
                return (Double.compare(lhs.getPublicRating()/2,rhs.getPublicRating()/2));
            }
        });
        if(!(temp.size()==0))
        {
            Collections.reverse(temp);
            MovieMemoirAdapter movieMemoirAdapter = new MovieMemoirAdapter(getContext(), temp,true);
            memoirRecyclerView.setAdapter(movieMemoirAdapter);
            memoirRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    //function to facilitate the function of sort and filter both
    public ArrayList<MemoirAndUrl> tempFunction(ArrayList<MemoirAndUrl> temp)
    {
        tempMovies = new ArrayList<>();
        for (MemoirAndUrl m : sortedMovies)
        {
            if (m.getGenre().equals(selectedFilter)) {
                tempMovies.add(m);
            }
        }
        temp=tempMovies;
        if(tempMovies.size()==0){Toast toast = Toast.makeText(getActivity(),
                "No movies match this criteria!",
                Toast.LENGTH_SHORT);
            toast.show();}
        return temp;
    }

}
