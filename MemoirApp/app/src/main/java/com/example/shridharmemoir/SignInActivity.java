package com.example.shridharmemoir;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shridharmemoir.networkconnection.NetworkConnection;

public class SignInActivity extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private Button login;
    private Button signup;
    private CheckBox showpassword;

    NetworkConnection networkConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#212121"));

        networkConnection = new NetworkConnection();
        userName = findViewById(R.id.etusername);
        password = findViewById(R.id.etpassword);
        login = findViewById(R.id.btnlogin);
        signup = findViewById(R.id.btnsignup);
        showpassword = findViewById(R.id.showpassword);

        //code to show/hide password
        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //call the async method
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckCredentials checkCredentials = new CheckCredentials();
                String username = userName.getText().toString();
                String passwordtext = password.getText().toString();
                if (!username.isEmpty() && !passwordtext.isEmpty()) {
                    checkCredentials.execute(username, passwordtext);
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,
                        SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    //check if the username/password is login
    private class CheckCredentials extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String passWord = params[1];

            return networkConnection.returnLoginResult(userName, passWord);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Incorrect username/password!"))
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Incorrect username/password!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Intent intent = new Intent(SignInActivity.this,
                        MainActivity.class);
                String[] results =  result.split(",");
                Bundle bundle = new Bundle();
                bundle.putString("personid", results[0]);
                bundle.putString("personname", results[1]);
                intent.putExtras(bundle);

                SharedPreferences sharedPref= getSharedPreferences("personidandname", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPref.edit();
                spEditor.putString("personid", results[0]);
                spEditor.putString("personname", results[1]);
                spEditor.apply();
                startActivity(intent);
            }
        }
    }


}
