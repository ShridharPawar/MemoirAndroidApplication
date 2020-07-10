package com.example.shridharmemoir.networkconnection;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.shridharmemoir.networkconnection.EntityClasses.Movie;

//import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class FetchMoviesConnection {



    //fetch all details of the movie for the movie view screen
    public ArrayList<String> fetchDetailsForMovieView(String title,String releaseDate,String imageURL,int movieid)
    {
        ArrayList<String> movieResult = new ArrayList<>();
        try {
            FetchMoviesConnection fetchMoviesConnection = new FetchMoviesConnection();

            String urlMovieDetails = "https://api.themoviedb.org/3/movie/"+movieid+"?api_key="+"1fa6e23fd2da0e00d45aefb04a6c9983"
                    +"&language=en-US";
            URL new_url = new URL(urlMovieDetails); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();
            InputStream inputStream = connection.getInputStream(); //reading from the object
            String textResultMovieDetails = "";
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResultMovieDetails += scanner.nextLine();
            }
            JSONObject mainObject = null;
            try {
                mainObject = new JSONObject(textResultMovieDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String overview = mainObject.getString("overview");
            String voteaverage = mainObject.getString("vote_average");
            JSONArray arr1 = mainObject.getJSONArray("genres");
            String genre="";
            String country="";
            if(arr1.length()>0){JSONObject temp = arr1.getJSONObject(0);genre=temp.getString("name");}
            JSONArray countries = mainObject.getJSONArray("production_countries");
            if(countries.length()>0){JSONObject temp = countries.getJSONObject(0);country=temp.getString("name");}

            String urlCasteDetails = "https://api.themoviedb.org/3/movie/"+movieid+"/credits?api_key="+"1fa6e23fd2da0e00d45aefb04a6c9983";
            URL casteURL = new URL(urlCasteDetails); //create a url from a String
            HttpURLConnection connection1 = (HttpURLConnection) casteURL.openConnection(); //Opening a http connection  to the remote object
            connection1.connect();
            InputStream inputStream1 = connection1.getInputStream(); //reading from the object
            String textResultCasteDetails = "";
            Scanner scanner1 = new Scanner(connection1.getInputStream());
            while (scanner1.hasNextLine()) {
                textResultCasteDetails += scanner1.nextLine();
            }
            JSONObject mainObject1 = null;
            try {
                mainObject1 = new JSONObject(textResultCasteDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String cast="";
            JSONArray arr2 = mainObject1.getJSONArray("cast");
            if(arr2.length()>0)
            {
                for(int i=0;i<5;i++)
                {
                    JSONObject temp = arr2.getJSONObject(i);
                    if(i!=4){cast = cast+temp.getString("name")+",";}else{cast = cast+temp.getString("name");}

                }
            }
            String director="";
            JSONArray directorArr = mainObject1.getJSONArray("crew");
            if(directorArr.length()>0)
            {
                for(int i=0;i<directorArr.length();i++)
                {
                    JSONObject temp = directorArr.getJSONObject(i);
                    if(temp.getString("job").equals("Director"))
                    {
                        director = temp.getString("name");
                    }
                }
            }
            movieResult.add(genre);
            movieResult.add(cast);
            movieResult.add(releaseDate);
            movieResult.add(country);
            movieResult.add(director);
            movieResult.add(overview);
            movieResult.add(voteaverage);
            movieResult.add(title);
            movieResult.add(imageURL);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return movieResult;

    }


    //common method called in the adapters to find the movieid
    public String fetchDataForWatchlistAndMemoirAdapter(String movieName,String year)
    {
        String urlString = "https://api.themoviedb.org/3/search/movie?api_key="+"1fa6e23fd2da0e00d45aefb04a6c9983"+"&language=en-US&query="+movieName+"&page=1&include_adult=false&primary_release_year="+year;
        String textResult = "";
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = null; //Opening a http connection  to the remote object
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream(); //reading from the object

            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return textResult;
    }


    //fetch data for the movie search screen
    public ArrayList<Movie> fetchData(String url){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {

            URL new_url = new URL(url); //create a url from a String
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
            connection.connect();

            InputStream inputStream = connection.getInputStream(); //reading from the object
            String textResult = "";
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
            parseJson(textResult,movies);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    //called from fetchdata
    public void parseJson(String data, ArrayList<Movie> movies){

        try {
            JSONObject mainObject = null;
            try {
                mainObject = new JSONObject(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray resArray = mainObject.getJSONArray("results"); //Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                Movie movie = new Movie(); //New Movie object
                movie.setId(jsonObject.getInt("id"));
                movie.setOriginalTitle(jsonObject.getString("original_title"));
                movie.setReleaseDate(jsonObject.getString("release_date"));
                movie.setPosterPath(jsonObject.getString("poster_path"));
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
