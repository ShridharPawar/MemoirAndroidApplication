package com.example.shridharmemoir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.shridharmemoir.networkconnection.NetworkConnection;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private TextView dateText;
    private Spinner statespinner;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private String dob;
    private TextView address;
    private TextView postcode;
    private TextView email;
    private TextView password;
    private Button signupbtn;
    NetworkConnection networkConnection=null;
    private AwesomeValidation awesomeValidation;
    //Toast toastForSignUp = Toast.makeText(getApplicationContext(),
           // "Signing up, please wait!",
            //Toast.LENGTH_SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        dob=null;
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        networkConnection = new NetworkConnection();
        dateText = findViewById(R.id.etdate);
        statespinner= findViewById(R.id.cinemaspinner);
        signupbtn = findViewById(R.id.btnsignup);
        firstName = findViewById(R.id.etfirstname);
        lastName = findViewById(R.id.etlastname);
        address = findViewById(R.id.etaddress);
        postcode = findViewById(R.id.etpostcode);
        email = findViewById(R.id.etemail);
        password = findViewById(R.id.etpassword);
        radioGroup = findViewById(R.id.rggender);

        //awesome validation to perform all validation checks
        awesomeValidation.addValidation(this, R.id.etfirstname, "^[A-Za-z _]{1,20}$", R.string.firstnameerror);
        awesomeValidation.addValidation(this, R.id.etlastname, "^[A-Za-z _]{1,20}$", R.string.lastnameerror);
        awesomeValidation.addValidation(this, R.id.etaddress, "^[A-Za-z0-9,.: _]{1,50}$", R.string.addresserror);
        awesomeValidation.addValidation(this, R.id.etpostcode, "^[/^\\d]{1,4}$", R.string.postcodeerror);
        awesomeValidation.addValidation(this, R.id.etpassword, "^(?!\\s*$).+", R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.etemail, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        findViewById(R.id.dobdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //call the async method to post the values
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                   String selectedState = statespinner.getSelectedItem().toString();
                        String gender = null;
                        if(radioButton!=null){gender = radioButton.getText().toString();}
                  SignUpPerson signUpPerson = new SignUpPerson();
          signUpPerson.execute(firstName.getText().toString(), lastName.getText().toString(), gender, dob, address.getText().toString(),
                                selectedState, postcode.getText().toString(), email.getText().toString(), password.getText().toString());
    }
            }
        });
}

    //call the REST method to post the values
      private class SignUpPerson extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return networkConnection.postPersonDetails(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8]);
         }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Username already exists!"))
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Username already exists!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Signup done!",
                    Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(SignUpActivity.this,
                    SignInActivity.class);
            startActivity(intent);
            }
        }
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
   }

   public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

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
        dob = year + "-" + month1 + "-" + dayOfMonth1;
        dateText.setText(dob);
    }
}
