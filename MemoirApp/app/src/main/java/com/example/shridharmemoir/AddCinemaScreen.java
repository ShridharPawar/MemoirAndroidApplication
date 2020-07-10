package com.example.shridharmemoir;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.shridharmemoir.networkconnection.NetworkConnection;

public class AddCinemaScreen extends AppCompatActivity {
    private EditText cinemaname;
    private EditText postcode;
    private Button addbutton;
    NetworkConnection networkConnection = null;
    private AwesomeValidation awesomeValidation;
    String title;
    String imageURL;
    String releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcinema);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        title = bundle.getString("title");
        imageURL = bundle.getString("imageURL");
        releaseDate = bundle.getString("releaseDate");
        cinemaname = findViewById(R.id.cinemaname);
        postcode = findViewById(R.id.postcode);
        addbutton = findViewById(R.id.addbutton);
        networkConnection = new NetworkConnection();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.cinemaname, "^[A-Za-z _0-9]{1,50}$", R.string.cinemanameerror);
        awesomeValidation.addValidation(this, R.id.postcode, "^[/^\\d]{1,4}$", R.string.cinemapostcodeerror);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {         //code to validate the data
                    PostNewCinema postNewCinema = new PostNewCinema();
                    postNewCinema.execute(cinemaname.getText().toString(), postcode.getText().toString());
                }
            }
        });
   }

    //Async method to post the values of the new cinema
   private class PostNewCinema extends AsyncTask<String,Void,Integer> {
        @Override
        protected Integer doInBackground(String... params)
        {
            return networkConnection.postnewCinema(params[0],params[1]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result.equals(0)||result==0)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This cinema already exists!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
            else
                {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Cinema added!",
                            Toast.LENGTH_SHORT);
                    toast.show();
            Intent intent = new Intent(AddCinemaScreen.this, MainActivity.class);
            intent.putExtra("AddCinemaMessage",2);
            intent.putExtra("title",title);
            intent.putExtra("releaseDate",releaseDate);
            intent.putExtra("imageURL",imageURL);
            startActivity(intent);             //go back to the Add to memoir screen through the main activity
            }
        }
    }
}
