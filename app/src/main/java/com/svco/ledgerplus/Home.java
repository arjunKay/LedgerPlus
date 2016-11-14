//Haris 9:40 drawer ittind..some changes have to be done


package com.svco.ledgerplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}