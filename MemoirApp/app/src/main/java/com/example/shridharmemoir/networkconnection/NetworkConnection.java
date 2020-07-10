package com.example.shridharmemoir.networkconnection;

import android.os.Build;


import androidx.annotation.RequiresApi;

import com.example.shridharmemoir.networkconnection.EntityClasses.Cinema;
import com.example.shridharmemoir.networkconnection.EntityClasses.Credential;
import com.example.shridharmemoir.networkconnection.EntityClasses.MemoirAndUrl;
import com.example.shridharmemoir.networkconnection.EntityClasses.MovieMemoir;
import com.example.shridharmemoir.networkconnection.EntityClasses.Person;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;

public class NetworkConnection {
    private OkHttpClient client=null;
    private String results;
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");
    public NetworkConnection(){
        client=new OkHttpClient();
    }

    private static final String BASE_URL =
            "http://10.0.2.2:8080/ShridharMemoirApp/webresources/";
  //http://127.0.0.1:8080/ShridharMemoir/webresources/memoirws.credential/findByUsername/shridhar18pawar@gmail.com for local netbeans result


    //fetch the person and cinemas addres from REST methods
    public ArrayList<String> fetchAddress(String personid)
    {
        ArrayList<String> result= new ArrayList<>();
        String methodPath = "memoirws.person/"+personid;
        String result1="";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONObject jObj = null;
        try {
            Response response = client.newCall(request).execute();
            result1=response.body().string();
            jObj = new JSONObject(result1);
            result.add(jObj.getString("address")+","+jObj.getString("postcode"));
        }catch (Exception e){
            e.printStackTrace();
        }

        //below code to fetch cinema address
        String methodPath2 = "memoirws.cinema";
        String result2="";
        Request.Builder builder2 = new Request.Builder();
        builder2.url(BASE_URL + methodPath2);
        Request request2 = builder2.build();
        JSONArray arr2 = new JSONArray();
        try {
            Response response = client.newCall(request2).execute();
            result2=response.body().string();
            arr2 = new JSONArray(result2);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            for(int i=0;i<arr2.length();i++)
            {
                String cinemaname = arr2.getJSONObject(i).getString("cinemaname");
                String postcode = arr2.getJSONObject(i).getString("location");
                result.add(cinemaname+" "+postcode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


    //fetch movies per month for the bar chart
    public ArrayList<String> fetchMoviesPerMonth(String personid,String year)
    {
        ArrayList<String> results = new ArrayList<>();
        String result1="";
        String methodPath = "memoirws.memoir/Task4bListMonthsAndMoviesWatched"+"/"+personid+"/"+year;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            result1=response.body().string();
            arr = new JSONArray(result1);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jObj = null;
        try {
            for(int i=0;i<arr.length();i++)
            {
                jObj = arr.getJSONObject(i);
                String key = jObj.keys().next();
                int value = jObj.getInt(key);
                results.add(key+","+value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    //fetch movies per postcode for the pie chart
    public ArrayList<String> fetchMoviesPerPostcode(String personid,String startdate,String enddate)
    {
        ArrayList<String> results = new ArrayList<>();
        String result1="";
        String methodPath = "memoirws.memoir/Task4aListCinemaPostcodesAndCount/"+personid+"/"
        +startdate+"/"+enddate;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            result1=response.body().string();
            arr = new JSONArray(result1);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jObj = null;
        try {
            for(int i=0;i<arr.length();i++)
            {
                jObj = arr.getJSONObject(i);
                results.add(jObj.getString("postcode")+","+jObj.getString("count"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }


    //post the user's memoir in the database
    public String postMemoirValues(String personid,String moviename,String releasedate,String datetimewatched,
                                   String comment, String rating,String cinemaname)
    {
        String[] nameandpostcode = cinemaname.split(",");
        String cinemaid="";
        String methodpath = "memoirws.cinema/getIdByNameAndPost/"+nameandpostcode[0]+"/"+nameandpostcode[1];
            String result5 = "";
            Request.Builder builder5 = new Request.Builder();
            builder5.url(BASE_URL + methodpath);
            Request request5 = builder5.build();
            JSONArray arr5 = new JSONArray();
            try {
                Response response = client.newCall(request5).execute();
                result5=response.body().string();
                arr5 = new JSONArray(result5);
            }catch (Exception e){
                e.printStackTrace();
            }
            JSONObject jObj5 = null;
            try {
                jObj5 = arr5.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                cinemaid = jObj5.getString("CinemaId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        String methodPathForMemoir = "memoirws.memoir/getHighestMemoirId";
        String result3="";
        Request.Builder builder1 = new Request.Builder();
        builder1.url(BASE_URL + methodPathForMemoir);
        Request request2 = builder1.build();
        JSONArray arr1 = new JSONArray();
        String memoirid="";
        try {
            Response response = client.newCall(request2).execute();
            result3=response.body().string();
            arr1 = new JSONArray(result3);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(arr1.length()==0)
        {
            memoirid = "0";
        }
        else
            {
        JSONObject jObj1 = null;
        try {
            jObj1 = arr1.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            memoirid = jObj1.getString("MemoirId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
            }
        String strResponse1="";
        MovieMemoir movieMemoir = new MovieMemoir(Integer.parseInt(memoirid)+1,new Person(Integer.parseInt(personid))
                ,new Cinema(Integer.parseInt(cinemaid)),moviename,releasedate,datetimewatched,comment,Double.parseDouble(rating));
        Gson gson1 = new Gson();
        String memoirJson = gson1.toJson(movieMemoir);
        String memoirmethodPath = "memoirws.memoir";
        RequestBody body1 = RequestBody.create(memoirJson, JSON);
        Request request4 = new Request.Builder()
                .url(BASE_URL + memoirmethodPath)
                .post(body1)
                .build();
        try {
            Response response= client.newCall(request4).execute();
            strResponse1= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "All Good";
    }

    //post the new cinema added by the user
    public Integer postnewCinema(String cinemaname,String postcode)
    {
        String methodPathForExisting = "memoirws.cinema/findByCinemaAndLocation/"+cinemaname+"/"+postcode;
        String resultForExisting = "";
        Request.Builder builderForExisting = new Request.Builder();
        builderForExisting.url(BASE_URL + methodPathForExisting);
        Request requestForExisting = builderForExisting.build();
        try {
            Response response = client.newCall(requestForExisting).execute();
            resultForExisting=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!resultForExisting.equals("[]"))
        {
            return 0;
        }


        String methodPathForCinema = "memoirws.cinema/getHighestCinemaId";
        String cinemaid="";
        String result3="";
        Request.Builder builder1 = new Request.Builder();
        builder1.url(BASE_URL + methodPathForCinema);
        Request request2 = builder1.build();
        JSONArray arr1 = new JSONArray();
        try {
            Response response = client.newCall(request2).execute();
            result3=response.body().string();
            arr1 = new JSONArray(result3);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(arr1.length()==0)
        {
            cinemaid = "0";
        }
        else{
        JSONObject jObj1 = null;
        try {
            jObj1 = arr1.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            cinemaid = jObj1.getString("CinemaId");
        } catch (JSONException e) {
            e.printStackTrace();
        }}
        String strResponse1="";
        Cinema cinema = new Cinema(Integer.parseInt(cinemaid)+1,cinemaname,postcode);
        Gson gson1 = new Gson();
        String cinemaJson = gson1.toJson(cinema);
        String cinemamethodPath = "memoirws.cinema";
        RequestBody body1 = RequestBody.create(cinemaJson, JSON);
        Request request4 = new Request.Builder()
                .url(BASE_URL + cinemamethodPath)
                .post(body1)
                .build();
        try {
            Response response= client.newCall(request4).execute();
            strResponse1= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Integer.parseInt(cinemaid)+1;
    }


    //fetch cinemas for the spinner in the add to memoir screen
    public ArrayList<String> fetchCinemas()
    {
        ArrayList<String> cinemas = new ArrayList<String>();
        String result1="";
        String methodPath = "memoirws.cinema";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            result1=response.body().string();
            arr = new JSONArray(result1);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jObj = null;
        try {
            for(int i=0;i<arr.length();i++)
            {
                jObj = arr.getJSONObject(i);
                cinemas.add(jObj.getString("cinemaname")+","+jObj.getString("location"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cinemas;
    }

    //fetch memoirs of the user
    public ArrayList<MemoirAndUrl> fetchMemoir(String personid)
    {
        ArrayList<MemoirAndUrl> result = new ArrayList<MemoirAndUrl>();
        String result1="";
        String methodPath = "memoirws.memoir/findByPersonid/"+Integer.parseInt(personid);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            result1=response.body().string();
            arr = new JSONArray(result1);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i=0;i<arr.length();i++)
        {
            JSONObject jObj = null;
            try {
                jObj = arr.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MemoirAndUrl memoirAndUrl = new MemoirAndUrl();
            try {
                Cinema cinema = new Cinema();
                Person person = new Person();
                String moviesURL = "https://api.themoviedb.org/3/search/movie?api_key="+"1fa6e23fd2da0e00d45aefb04a6c9983"+"&language=en-US&query="
                        +jObj.getString("moviename")+"&page=1&include_adult=false&primary_release_year="
                        +jObj.getString("moviereleasedate").substring(0,4);
                URL newurl = new URL(moviesURL);
                HttpURLConnection connection = null; //Opening a http connection  to the remote object
                connection = (HttpURLConnection) newurl.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream(); //reading from the object
                String textResult = "";
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    textResult += scanner.nextLine();
                }
                JSONObject mainObject = null;
                try {
                    mainObject = new JSONObject(textResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray resArray = mainObject.getJSONArray("results");
                int movieid = resArray.getJSONObject(0).getInt("id");
                memoirAndUrl.setUrl("https://image.tmdb.org/t/p/w185/"+resArray.getJSONObject(0).getString("poster_path"));
                memoirAndUrl.setMemoirid(jObj.getInt("memoirid"));
                memoirAndUrl.setPublicRating(resArray.getJSONObject(0).getDouble("vote_average"));
                memoirAndUrl.setSynopsis(resArray.getJSONObject(0).getString("overview"));


                String urlMovieDetails = "https://api.themoviedb.org/3/movie/"+movieid+"?api_key="+"1fa6e23fd2da0e00d45aefb04a6c9983"
                        +"&language=en-US";
                URL new_url = new URL(urlMovieDetails); //create a url from a String
                HttpURLConnection connection1 = (HttpURLConnection) new_url.openConnection(); //Opening a http connection  to the remote object
                connection1.connect();
                InputStream inputStream1 = connection1.getInputStream(); //reading from the object
                String textResultMovieDetails = "";
                Scanner scanner1 = new Scanner(connection1.getInputStream());
                while (scanner1.hasNextLine()) {
                    textResultMovieDetails += scanner1.nextLine();
                }
                JSONObject mainObject1 = null;
                try {
                    mainObject1 = new JSONObject(textResultMovieDetails);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray arr2 = mainObject1.getJSONArray("genres");
                String genre="";
                if(arr2.length()>0){JSONObject temp = arr2.getJSONObject(0);genre=temp.getString("name");}
                memoirAndUrl.setGenre(genre);

                memoirAndUrl.setMoviename(jObj.getString("moviename"));
                memoirAndUrl.setMoviereleasedate(jObj.getString("moviereleasedate"));
                memoirAndUrl.setDatetimewatched(jObj.getString("datetimewatched"));
                memoirAndUrl.setComment(jObj.getString("comment"));
                memoirAndUrl.setUserrating(jObj.getDouble("userrating"));
                cinema.setCinemaid(jObj.getJSONObject("cinemaid").getInt("cinemaid"));
                cinema.setLocation(jObj.getJSONObject("cinemaid").getString("location"));
                person.setPersonid(jObj.getJSONObject("personid").getInt("personid"));
                memoirAndUrl.setCinemaid(cinema);
                memoirAndUrl.setPersonid(person);
                result.add(memoirAndUrl);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    //fetch the top 5 memories of the user
    public JSONArray homeScreen(String personid)
    {
        String result = "";
        String methodPath = "memoirws.memoir/Task4fTopMovies/"+Integer.parseInt(personid);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();

        try {
            Response response = client.newCall(request).execute();
            result=response.body().string();
            arr = new JSONArray(result);
        }catch (Exception e){
            e.printStackTrace();
        }

     return arr;
    }


    //post the details in the person and credential table after user signs up
    public String postPersonDetails(String firstname,String surname,String gender,String dobdate,String address,String selectedState,String postcode,
                                    String username,String password)
    {

        String methodPathForExisting = "memoirws.credential/findByUsername/"+username;
        String resultForExisting = "";
        Request.Builder builderForExisting = new Request.Builder();
        builderForExisting.url(BASE_URL + methodPathForExisting);
        Request requestForExisting = builderForExisting.build();
        try {
            Response response = client.newCall(requestForExisting).execute();
            resultForExisting=response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!resultForExisting.equals("[]"))
        {
            return "Username already exists!";
        }

        String methodPath = "memoirws.person/getHighestPersonId";
        String result = "";
        String personid="";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            result=response.body().string();
            arr = new JSONArray(result);
         }catch (Exception e){
            e.printStackTrace();
        }
        if(arr.length()==0)
        {
            personid = "0";
        }
        else{
        JSONObject jObj = null;
        try {
            jObj = arr.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            personid = jObj.getString("PersonId");
        } catch (JSONException e) {
            e.printStackTrace();
        }}

        String strResponse="";
        Person person = new Person(Integer.parseInt(personid)+1,firstname,surname,gender,dobdate,address,selectedState,postcode);
        Gson gson = new Gson();
        String personJson = gson.toJson(person);
        methodPath = "memoirws.person";
        RequestBody body = RequestBody.create(personJson, JSON);
        Request request1 = new Request.Builder()
                .url(BASE_URL + methodPath)
                .post(body)
                .build();
        try {
            Response response= client.newCall(request1).execute();
            strResponse= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Below is the code to make an entry in the credential table.
        //Credential credential = new Credential()

        String methodPathForCredential = "memoirws.credential/getHighestCredentialId ";
        String credentialid="";
        String result3="";
        Request.Builder builder1 = new Request.Builder();
        builder1.url(BASE_URL + methodPathForCredential);
        Request request2 = builder1.build();
        JSONArray arr1 = new JSONArray();
        try {
            Response response = client.newCall(request2).execute();
            result3=response.body().string();
            arr1 = new JSONArray(result3);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(arr1.length()==0)
        {
            credentialid = "0";
        }else{
        JSONObject jObj1 = null;
        try {
            jObj1 = arr1.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            credentialid = jObj1.getString("CredentialId");
        } catch (JSONException e) {
            e.printStackTrace();
        }}

        String strResponse1="";
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        Credential credential = new Credential(Integer.parseInt(credentialid)+1,username,hashtext,strDate);
        credential.setPersonid(Integer.parseInt(personid)+1,firstname,surname,gender,dobdate,address,selectedState,postcode);
        Gson gson1 = new Gson();
        String credentialJson = gson1.toJson(credential);
        String credentialmethodPath = "memoirws.credential";
        RequestBody body1 = RequestBody.create(credentialJson, JSON);
        Request request4 = new Request.Builder()
                .url(BASE_URL + credentialmethodPath)
                .post(body1)
                .build();
        try {
            Response response= client.newCall(request4).execute();
            strResponse1= response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
         return "All good";
    }


    //check if the user has entered correct password and username or not
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String returnLoginResult(String username, String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        final String methodPath = "memoirws.credential/findByUsernameAndPasswordHash/"+username+"/"+hashtext;
        String personid = "";
        String firstName = "";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        JSONArray arr = new JSONArray();
        try {
            Response response = client.newCall(request).execute();
            results=response.body().string();
            arr = new JSONArray(results);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(results.equals("[]"))
        {
            return "Incorrect username/password!";
        }

        JSONObject jObj = null;
        try {
            jObj = arr.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject a = jObj.getJSONObject("personid");
            personid = a.getString("personid");
            firstName = a.getString("firstname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
         results=personid + "," + firstName;

        return results;
  }


}



