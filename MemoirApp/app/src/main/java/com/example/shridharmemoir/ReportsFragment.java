package com.example.shridharmemoir;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.shridharmemoir.networkconnection.NetworkConnection;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//public class ReportsFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    public class ReportsFragment extends Fragment{
    private ArrayList<String> postcodes;
    private ArrayList<Float> count;
    private ArrayList<String> barmonths;
    private ArrayList<Float> monthCount;
    private String personid;
    private TextView startDate;
    private TextView endDate;
    private String Datestr;
    private Spinner yearSpinner;
    private NetworkConnection networkConnection=null;
    private BarChart barChart;
    private PieChart chart;
    private View view;
    private Calendar c;
    private DatePickerDialog dpd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reports_fragment, container, false);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        personid= sharedPref.getString("personid",null);
        networkConnection = new NetworkConnection();
        Button startDateButton = view.findViewById(R.id.startDateButton);
        Button endDateButton = view.findViewById(R.id.endDateButton);
        Button piebutton = view.findViewById(R.id.piebutton);
        Button barbutton = view.findViewById(R.id.barbutton);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
        barChart = view.findViewById(R.id.barchart);
        chart = view.findViewById(R.id.piechart);
        chart.setNoDataText("Click on Pie chart to view.");
        barChart.setNoDataText("Click on Bar chart to view.");


        //calls the async method to fetch the data from the REST methods
        piebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startDate.getText().toString().equals("")||endDate.getText().toString().equals(""))
                {
                    Toast toast = Toast.makeText(getActivity(),
                            "Please enter the start and end dates!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                MoviesPerPostcode moviesPerPostcode = new MoviesPerPostcode();
                moviesPerPostcode.execute(personid,startDate.getText().toString(),endDate.getText().toString());}
            }
        });

        //calls the async method to fetch the data from the REST methods
        barbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedYear = yearSpinner.getSelectedItem().toString();
                MoviesPerMonth moviesPerMonth = new MoviesPerMonth();
                moviesPerMonth.execute(personid,selectedYear);
            }
        });

        //set the start date
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                   c = Calendar.getInstance();
                   int day = c.getInstance().get(Calendar.DAY_OF_MONTH);
                   int month = c.getInstance().get(Calendar.MONTH);
                   int year = c.getInstance().get(Calendar.YEAR);
               dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
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
                       Datestr = year + "-" + month1 + "-" + dayOfMonth1;
                       startDate.setText(Datestr);
                   }
               },day,month,year);dpd.updateDate(year,month,day);dpd.show();
         }
        });

        //set the end date
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.getInstance().get(Calendar.DAY_OF_MONTH);
                int month = c.getInstance().get(Calendar.MONTH);
                int year = c.getInstance().get(Calendar.YEAR);
                dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
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
                        Datestr = year + "-" + month1 + "-" + dayOfMonth1;
                        endDate.setText(Datestr);
                    }
                },day,month,year);dpd.updateDate(year,month,day);dpd.show();
     }
        });

        return view;
    }


    //fetch the movies from the REST method
    private class MoviesPerMonth extends AsyncTask<String,Void,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params)
        {
            return networkConnection.fetchMoviesPerMonth(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> results)
        {
            barmonths = new ArrayList<>();
            monthCount = new ArrayList<>();
            for(int i=0;i<results.size();i++)
            {
                String[] res = results.get(i).split(",");
                barmonths.add(res[0]);
                monthCount.add(Float.parseFloat(res[1]));
            }
            setupBarChart();
        }
    }

     private class MoviesPerPostcode extends AsyncTask<String,Void,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params)
        {
            return networkConnection.fetchMoviesPerPostcode(params[0],params[1],params[2]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> results)
        {
            int a=1;
            postcodes = new ArrayList<String>();
            count = new ArrayList<Float>();
            if(results.size()==0)
            {
                Toast toast = Toast.makeText(getActivity(),
                 "No movies watched!",
                 Toast.LENGTH_SHORT);
                 toast.show();
            }
            else{
                float totalcount = 0;
                for(int i=0;i<results.size();i++)
                {
                    String[] res = results.get(i).split(",");
                    totalcount=totalcount+Float.parseFloat(res[1]);
                }
                for(int i=0;i<results.size();i++)
            {
                String[] res = results.get(i).split(",");
                postcodes.add(res[0]);
                count.add(Float.parseFloat(res[1])/totalcount);
            }
           //call the method to show the pie chart
           setupPieChart();}

        }
    }

    //show the bar chart
    private void setupBarChart()
    {
        barChart.setNoDataText("Description that you want");
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i=0;i<monthCount.size();i++)
        {
            barEntries.add(new BarEntry(i,monthCount.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Months");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Movies watched per month");
        barChart.setDescription(description);
        barChart.setFitBars(true);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barmonths));
        //xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelCount(barmonths.size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(1000);
        barChart.invalidate();
        setupBarChart1();
    }

    //show the pie chart
    private void setupPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();

        for(int i=0;i<postcodes.size();i++)
        {
            pieEntries.add(new PieEntry(count.get(i), postcodes.get(i)));
        }
        Description description = new Description();
        description.setText("Movies watched per postcode");
        chart.setDescription(description);
        PieDataSet dataSet = new PieDataSet(pieEntries,"Postcode");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setUsePercentValues(true);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(20f);
        data.setValueFormatter(new PercentFormatter(chart));
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setupBarChart1()
    {
        barChart.setNoDataText("Description that you want");
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i=0;i<monthCount.size();i++)
        {
            barEntries.add(new BarEntry(i,monthCount.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Months");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Movies watched per month");
        barChart.setDescription(description);
        barChart.setFitBars(true);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(barmonths));
        //xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setLabelCount(barmonths.size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(1000);
        barChart.invalidate();
    }


}
