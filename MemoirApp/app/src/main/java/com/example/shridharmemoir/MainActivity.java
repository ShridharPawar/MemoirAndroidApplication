package com.example.shridharmemoir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  implements
        NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        SharedPreferences sharedPref= getSharedPreferences("personidandname", Context.MODE_PRIVATE);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#212121"));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundDrawable(colorDrawable);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        toggle = new ActionBarDrawerToggle(this,
                drawerLayout,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //these two lines of code show the navicon drawer icon top left hand side
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intentFromMovieView = getIntent();
        //value of the intent if it is retrieved from the Movie View Screen
        int value = intentFromMovieView.getIntExtra("movieViewMessage", 0);
        Intent intentFromAddCinema = getIntent();
        //value of the intent if it is retrieved from the Add Cinema Screen
        int value2 = intentFromAddCinema.getIntExtra("AddCinemaMessage", 0);
        if(value==1)
        {
             String title = intentFromMovieView.getStringExtra("title");
             String releaseDate = intentFromMovieView.getStringExtra("releaseDate");
             String imageURL = intentFromMovieView.getStringExtra("imageURL");
             Bundle bundleForAddToMemoir = new Bundle();
             bundleForAddToMemoir.putString("title", title);
             bundleForAddToMemoir.putString("releaseDate", releaseDate);
             bundleForAddToMemoir.putString("imageURL", imageURL);
             AddToMemoirFragment addToMemoirFragmentObj = new AddToMemoirFragment();
             addToMemoirFragmentObj.setArguments(bundleForAddToMemoir);
             replaceFragment(addToMemoirFragmentObj);
        }
        else if(value2==2)
        {
            String title = intentFromAddCinema.getStringExtra("title");
            String releaseDate = intentFromAddCinema.getStringExtra("releaseDate");
            String imageURL = intentFromAddCinema.getStringExtra("imageURL");
            Bundle bundleForAddToMemoir = new Bundle();
            bundleForAddToMemoir.putString("title", title);
            bundleForAddToMemoir.putString("releaseDate", releaseDate);
            bundleForAddToMemoir.putString("imageURL", imageURL);
            AddToMemoirFragment addToMemoirFragmentObj = new AddToMemoirFragment();
            addToMemoirFragmentObj.setArguments(bundleForAddToMemoir);
            replaceFragment(addToMemoirFragmentObj);

        }
        else
            {
                 replaceFragment(new HomeFragment());
            }
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String message = data.getStringExtra("message1");
                replaceFragment(new WatchlistFragment());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    //switch case to replace the fragments when they are called from the navigation drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.homeScreen:
                replaceFragment(new HomeFragment());
                break;
            case R.id.movieSearch:
                replaceFragment(new MovieSearchFragment());
                break;
            case R.id.addtowatchlist:
                replaceFragment(new WatchlistFragment());
                break;
            case R.id.movieMemoir:
                replaceFragment(new MovieMemoirFragment());
                break;
            case R.id.reports:
                replaceFragment(new ReportsFragment());
                break;
            case R.id.maps:
                replaceFragment(new MapsFragment());
                break;
            case R.id.firebasewatchlist:
                replaceFragment(new FirebaseWatchlist());
                break;
     }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    //method to replace the fragment
    public void replaceFragment(Fragment nextFragment)
    {
         FragmentManager fragmentManager = getSupportFragmentManager();
         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }


}
