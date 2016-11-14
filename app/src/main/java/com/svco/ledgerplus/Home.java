//Haris 9:40 drawer ittind..some changes have to be done


package com.svco.ledgerplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

import static com.svco.ledgerplus.R.id.spinner_type;

public class Home extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);
        AccountHeader header=new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabled(false)
                .withTextColor(Color.parseColor("#FF0000"))

               //.addProfiles(new ProfileDrawerItem().withName("USER"))
                .withHeaderBackground(R.color.colorPrimaryDark)

                .build();

        Drawer result = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Graph Statistics"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Balance Sheet"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Categories"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Reminder"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings")

                      )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override

                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position)
                        {
                            case 1:
                                startActivity(new Intent(Home.this,GraphStats.class) );
                                break;
                            case 3:
                                startActivity(new Intent(Home.this,BalanceSheet.class) );
                                break;
                            case 5:
                                startActivity(new Intent(Home.this,Categories.class) );
                                break;
                            case 7:
                                startActivity(new Intent(Home.this,Reminder.class) );
                                break;
                            case 9:
                                startActivity(new Intent(Home.this,Settings.class) );
                                break;
                        }

                        return true;
                    }
                })
                .build();


        //Floating Menu and Button
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout dialogLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_layout,null);
            //    Button date= (Button) dialogLayout.findViewById(R.id.date);
                final EditText et= (EditText) dialogLayout.findViewById(R.id.amt_et);
                EditText amt= (EditText) findViewById(R.id.amt_et);
                EditText description= (EditText) findViewById(R.id.des);
                Spinner spinner_src,spinner_cat;
                List<String> SpinnerArray = new ArrayList<String>();
                SpinnerArray.add("Cash");
                SpinnerArray.add("Bank");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, SpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_src = (Spinner)dialogLayout.findViewById(spinner_type);
                spinner_src.setAdapter(adapter);


                List<String> SpinnerArray2 = new ArrayList<String>();
                SpinnerArray2.add("<Cartegory>");
                SpinnerArray2.add("1");
                SpinnerArray2.add("2");
                SpinnerArray2.add("3");
                SpinnerArray2.add("4");
                SpinnerArray2.add("Custom");
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, SpinnerArray2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat = (Spinner)dialogLayout.findViewById(R.id.spinner_cat);
                spinner_cat.setAdapter(adapter2);


                MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                        .title("Income")
                        .customView(dialogLayout,true)
                        .positiveText("Add")
                        .negativeText("Cancel")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //boolean xx=  myDb.insertData(et.getText().toString(),"expense","null","null","null");
                            }
                        })
                        .build();

                dialog.show();

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LinearLayout dialogLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_layout,null);
               // Button date= (Button) dialogLayout.findViewById(R.id.date);
                final EditText et= (EditText) dialogLayout.findViewById(R.id.amt_et);
                Spinner spinner_src,spinner_cat;
                List<String> SpinnerArray = new ArrayList<String>();
                SpinnerArray.add("Cash");
                SpinnerArray.add("Bank");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, SpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_src = (Spinner)dialogLayout.findViewById(spinner_type);
                spinner_src.setAdapter(adapter);


                List<String> SpinnerArray2 = new ArrayList<String>();
                SpinnerArray2.add("<Cartegory>");
                SpinnerArray2.add("1");
                SpinnerArray2.add("2");
                SpinnerArray2.add("3");
                SpinnerArray2.add("4");
                SpinnerArray2.add("Custom");
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, SpinnerArray2);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat = (Spinner)dialogLayout.findViewById(R.id.spinner_cat);
                spinner_cat.setAdapter(adapter2);


                MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                        .title("Expenditure")
                        .customView(dialogLayout,true)
                        .positiveText("Add")
                        .negativeText("Cancel")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //boolean xx=  myDb.insertData(et.getText().toString(),"expense","null","null","null");
                            }
                        })
                        .build();

                dialog.show();
            }
        });
    }
}