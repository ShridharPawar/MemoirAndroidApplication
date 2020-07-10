package com.example.shridharmemoir;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.shridharmemoir.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddToMemoirFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = "AddToMemoirFragment";

    private TextView datewatched;
    private TextView timeWatched;
    private ArrayList<String> cinemaNames;
    private ArrayList<String> postCodes;
    private Spinner cinemaSpinner;
    //private Spinner postcodeSpinner;
    private NetworkConnection networkConnection = null;
    private EditText memory;
    private RatingBar memoirRating;
    private String title;
    private String imageURL;
    private String releaseDate;
    private String selectedCinema;
    private String selectedPostcode;
    private String personid;

    public AddToMemoirFragment(){}



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.addtomemoir_fragment, container, false);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        personid= sharedPref.getString("personid",null);
        title = getArguments().getString("title");
        releaseDate = getArguments().getString("releaseDate");
        imageURL = getArguments().getString("imageURL");

        ImageView imageView = view.findViewById(R.id.movieImage);
        memory = view.findViewById(R.id.memory);
        memoirRating = view.findViewById(R.id.memoirRating);
        Button otherCinema = view.findViewById(R.id.otherCinema);
        Picasso.get().load(imageURL).into(imageView);
        TextView movieName = view.findViewById(R.id.movieName);
        movieName.setText(title);
        TextView releaseDateText = view.findViewById(R.id.releaseDate);
        String relDate = releaseDate.toString();
        releaseDateText.setText(relDate);
        networkConnection = new NetworkConnection();
        datewatched = view.findViewById(R.id.datewatched);
        Button addButton = view.findViewById(R.id.memoirBtton);
        Button timepicker = view.findViewById(R.id.timepicker);
        timeWatched = view.findViewById(R.id.timeWatched);
        cinemaSpinner = view.findViewById(R.id.cinemaspinner);
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        fetchCinemas fetchCinemas = new fetchCinemas();
        fetchCinemas.execute();

        //code to go to the Add Cinema activity to let the user add a new cinema
        otherCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCinemaScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("imageURL", imageURL);
                bundle.putString("releaseDate", releaseDate);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);


            }
        });

        //set the values of the cinema spinner
        cinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id) {
                selectedCinema = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //call the async method to post the user's memoir
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(datewatched.getText().toString().equals("")||timeWatched.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getActivity(),
                            "Please select the date and time!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    Float ratingBarValue = memoirRating.getRating();
                    PostMemoirValues postMemoirValues = new PostMemoirValues();
                    postMemoirValues.execute(personid,title,releaseDate,datewatched.getText().toString()+" "+timeWatched.getText().toString()
                            ,memory.getText().toString(),ratingBarValue.toString(),selectedCinema);}

            }
        });

        memoirRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar memoirRating, float rating, boolean fromUser) {
                memoirRating.setRating(rating);
            }
        });

        cinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long
                    id)
            {
                selectedCinema = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    //timepicker to take user's input
        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String min = "";
                        String hour = "";
                        if(minute<10){min = "0"+Integer.toString(minute);}else{min = Integer.toString(minute);}
                        if(hourOfDay<10){hour = "0"+Integer.toString(hourOfDay);}else{hour = Integer.toString(hourOfDay);}
                        timeWatched.setText(hour+":"+min+":00");


                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });


        view.findViewById(R.id.dateWatcheddialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        return view;
    }

   //async method to post the user's memoir
    private class PostMemoirValues extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params)
        {
           return networkConnection.postMemoirValues(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
        }


         @Override
       protected void onPostExecute(String result) {
            Toast toast = Toast.makeText(getActivity(),
                 "Added to Memoir!",
                 Toast.LENGTH_SHORT);
             toast.show();}
    }

    //async method to populate the cinema spinner
    private class fetchCinemas extends AsyncTask<Void,Void,ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return networkConnection.fetchCinemas();
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            ArrayAdapter<String> cinemaSpinnerAdapter = new
                    ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, result);

            cinemaSpinner.setAdapter(cinemaSpinnerAdapter);
        }
    }

   //code for the datepicker
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //code to set the date
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String month1="";
        if(month+1<9)
        {
            month1="0"+(month+1);
        }else{month1= String.valueOf(month+1);}
        String dayOfMonth1="";
        if(dayOfMonth<9)
        {
            dayOfMonth1="0"+dayOfMonth;
        }else{dayOfMonth1= String.valueOf(dayOfMonth);}
        String dateWatched = year + "-" + month1 + "-" + dayOfMonth1;
        datewatched.setText(dateWatched);
    }


}
