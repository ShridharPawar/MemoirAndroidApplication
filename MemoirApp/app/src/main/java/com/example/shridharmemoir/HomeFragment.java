package com.example.shridharmemoir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shridharmemoir.adapter.HomeRecyclerAdapter;
import com.example.shridharmemoir.networkconnection.NetworkConnection;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {
    private String personid;
    private String personname;
    private TextView personTextView;
    private TextView currentDateTextView;
    NetworkConnection networkConnection=null;
    RecyclerView homeRecyclerView;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        networkConnection = new NetworkConnection();
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        String personid= sharedPref.getString("personid",null);
        String personname= sharedPref.getString("personname",null);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        personTextView = view.findViewById(R.id.firstName);
        personTextView.setText("Welcome "+personname+"!");
        currentDateTextView = view.findViewById(R.id.currentDate);
        currentDateTextView.setText(strDate);
        homeRecyclerView = view.findViewById(R.id.homeRecycler);
        HomeScreenMovies homeScreenMovies = new HomeScreenMovies();
        homeScreenMovies.execute(personid);
        return view;
    }

   //Async method top fetch the top 5 movies of the user
   private class HomeScreenMovies extends AsyncTask<String, Void, JSONArray> {

       //just a waiting dialog box till the data is fetched
       private ProgressDialog dialog = new ProgressDialog(getActivity());
       @Override
       protected void onPreExecute() {
           this.dialog.setMessage("Fetching your favourite movies...");
           this.dialog.show();
       }

        @Override
        protected JSONArray doInBackground(String... params) {
            return networkConnection.homeScreen(params[0]);
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            String[] movieNames = new String[5];
            String[] releaseYear = new String[5];
            String[] ratings = new String[5];
            for(int i=0;i<result.length();i++)
            {
                try {
                    JSONObject object = result.getJSONObject(i);
                    movieNames[i] = object.getString("MovieName");
                    releaseYear[i] = object.getString("ReleaseDate");
                    ratings[i] = object.getString("RatingScore");
                 } catch (JSONException e) {
                    e.printStackTrace();
                }
             }
            //call the recycler view to set the data
            if(!(result.length()==0))
            {
                HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(getContext(), movieNames, releaseYear, ratings);
                homeRecyclerView.setAdapter(adapter);
                homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
            else{
                Toast toast = Toast.makeText(getActivity(),
                        "No top memories!",
                        Toast.LENGTH_SHORT);
                toast.show();}

        }
    }
}
