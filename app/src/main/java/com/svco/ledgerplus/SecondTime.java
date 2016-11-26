package com.svco.ledgerplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SecondTime extends AppCompatActivity {
    LedgerDBManager myDb;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_time);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Statistics");
    }
}
