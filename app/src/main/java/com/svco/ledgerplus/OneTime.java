package com.svco.ledgerplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class OneTime extends AppCompatActivity {
    Boolean isFirstTime;
    LedgerDBManager myDb;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time);
        myDb = new LedgerDBManager(this);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome to LedgerPlus");
        SharedPreferences app_preferences = PreferenceManager
                .getDefaultSharedPreferences(OneTime.this);
        isFirstTime = app_preferences.getBoolean("isFirstTime", true);

        SharedPreferences.Editor editor = app_preferences.edit();

        //Check if that this activity is running for first time
        if (isFirstTime) {
            editor.putBoolean("isFirstTime", false);
            editor.commit();




        }
        else{
            //app open directly
            //The name and email will be taken from DB on Home.class
            Intent x = new Intent (OneTime.this,Home.class);
            startActivity(x);
            finish();
        }
    }
}
