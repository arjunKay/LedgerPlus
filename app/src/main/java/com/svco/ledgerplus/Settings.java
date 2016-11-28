package com.svco.ledgerplus;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    Integer[] image_id= {R.drawable.ic_reset,R.drawable.ic_profile,R.drawable.ic_about_us};
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

        SettingsAdapter adapter = new SettingsAdapter(Settings.this,R.layout.settings_list_item,titleList,detailList,image_id);
        list=(ListView)findViewById(R.id.list_settings);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0: //clicked Reset Data

                        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(parent.getContext());
                        alertDialog.setTitle("RESET DATA");
                        alertDialog.setMessage("Are you sure you want reset all data? All your transactions will be deleted");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LedgerDBManager myDb=new LedgerDBManager(getApplicationContext());
                                myDb.resetDB();
                                Toast.makeText(getApplicationContext(), "Reset Data Successfully !!!", Toast.LENGTH_LONG).show();
                            }
                        });
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();


                        break;
                    case 1: //clicked Edit Profile
                        Toast.makeText(getApplicationContext(), "You Clicked at "+titles[1], Toast.LENGTH_SHORT).show();
                        break;
                    case 2: //clicked About Us
                        Toast.makeText(getApplicationContext(), "You Clicked at "+titles[2], Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        list.setAdapter(adapter);

    }
}
