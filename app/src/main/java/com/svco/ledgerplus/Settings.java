package com.svco.ledgerplus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings extends AppCompatActivity {

    Toolbar toolbar;
    ListView list;
    List<String> titleList = new ArrayList<>();
    List<String> detailList = new ArrayList<>();
    String[] titles= new String[]{"Reset Data","Edit Profile","About us"};
    String[] details = new String[]{"Clear all data and start fresh","Edit Name and e-mail id",""};
    Integer[] image_id= {R.drawable.ic_reset,R.drawable.ic_profile,R.drawable.ic_about};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleList = new ArrayList<>(Arrays.asList(titles));
        detailList = new ArrayList<>(Arrays.asList(details));

        SettingsAdapter adapter = new SettingsAdapter(Settings.this,R.id.activity_settings,titleList,detailList,image_id);
        list=(ListView)findViewById(R.id.list_settings);
        list.setAdapter(adapter);
    }

}
