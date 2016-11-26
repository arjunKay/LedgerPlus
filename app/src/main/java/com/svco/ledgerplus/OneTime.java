package com.svco.ledgerplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OneTime extends AppCompatActivity {
    Boolean isFirstTime;
    LedgerDBManager myDb;
    Toolbar toolbar;
    EditText name,email;
    Button next;

    TextInputLayout name_layout,email_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);
        name= (EditText) findViewById(R.id.name);
       // name_layout= (TextInputLayout) findViewById(R.id.name_layout);

        email= (EditText) findViewById(R.id.email);
        email_layout= (TextInputLayout) findViewById(R.id.email_layout);
//        email_layout.animate();
        next= (Button) findViewById(R.id.next);
        myDb = new LedgerDBManager(this);
        name.requestFocus();
        email.requestFocus();
        SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(OneTime.this);
        isFirstTime = app_preferences.getBoolean("isFirstTime", true);

        SharedPreferences.Editor editor = app_preferences.edit();

        //Check if that this activity is running for first time
        if (isFirstTime) {
            editor.putBoolean("isFirstTime", false);
            editor.commit();
            next.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isFirstTime=false;
                            String n=name.getText().toString();
                            String e=email.getText().toString();
                            if(n.isEmpty() ){
                                name.setError("Required");
                            }
                            if(e.isEmpty() ){
                                email.setError("Required");
                            }
                            else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()){
                                email_layout.setError("Invalid email address");
                            }
                            else{
                            myDb.addProfileName(n,e);

                            Intent x = new Intent (OneTime.this,Home.class);
                            startActivity(x);
                            finish();}
                        }
                    }
            );


        }
        else{
            //app open directly
            //The name and email will be taken from DB on Home.class
            Intent x = new Intent (OneTime.this,Home.class);
            startActivity(x);
            finish();
        }
    }
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity

        myDb.addProfileName("user","user@svco.com");
        isFirstTime=true;

        if (!isFirstTime) {
            isFirstTime=true;
             } else {
            super.onBackPressed();
        }
    }
}
