package com.example.shridharmemoir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.shridharmemoir.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapsFragment extends Fragment {
    NetworkConnection networkConnection = null;
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        networkConnection = new NetworkConnection();
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        SharedPreferences sharedPref= getActivity().getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        String personid= sharedPref.getString("personid",null);
        FetchPersonAndCinemaAddress fetchPersonAddress = new FetchPersonAndCinemaAddress();
        fetchPersonAddress.execute(personid);
        mMapView = view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    //Async method to fetch the address of the person and the cinemas
    private class FetchPersonAndCinemaAddress extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            return networkConnection.fetchAddress(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            final ArrayList<Double> latitudes = new ArrayList<>();
            final ArrayList<Double> longitudes = new ArrayList<>();
            final ArrayList<LatLng> latlngs = new ArrayList<>();
            Geocoder geocoder = new Geocoder(getActivity());
            //convert the addresses to latitude and longitude
           final List<String> cinemaNames = new ArrayList<>();
           List<Address> addresses = new ArrayList<>();
            try {
                for(int i=0;i<result.size();i++)
                {
                    List<Address> tempaddresses = new ArrayList<>();
                    tempaddresses = geocoder.getFromLocationName(result.get(i),1);
                    if(tempaddresses.size()>0)
                    {
                     addresses.add(tempaddresses.get(0));
                     cinemaNames.add(result.get(i));
                    }
                }
                for(int i=0;i<addresses.size();i++)
                {
                    latitudes.add(addresses.get(i).getLatitude());
                    longitudes.add(addresses.get(i).getLongitude());
                    latlngs.add(new LatLng(latitudes.get(i),longitudes.get(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMapView.onResume(); // needed to get the map to display immediately

            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;
                    //set the markers
                    for(int i=0;i<latlngs.size();i++)
                    {
                        if(i==0)
                        {
                            googleMap.addMarker(new MarkerOptions().position(latlngs.get(i)).title("Your location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        }
                        else
                         {
                             googleMap.addMarker(new MarkerOptions().position(latlngs.get(i)).title(cinemaNames.get(i)));
                        }
                    }
                    //zoom
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(latlngs.get(0)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });

}
        }
    }

