//Haris 9:40 drawer ittind..some changes have to be done


package com.svco.ledgerplus;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AccountHeader header=new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(Color.parseColor("#FF0000"))
                //.addProfiles(new ProfileDrawerItem().withName("USER").withEmail("email id"))
                .build();

        Drawer result = new DrawerBuilder()
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
                .build();


        //Floating Menu and Button

        FloatingActionButton fabexp=new FloatingActionButton(this);
        fabexp.setIcon(R.drawable.expenditure);
        fabexp.setColorNormalResId(R.color.md_red_500);
        fabexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Expenditure Dialog Box",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabinc=new FloatingActionButton(this);
        fabinc.setIcon(R.drawable.income);
        fabinc.setColorNormalResId(R.color.md_green_500);
        fabinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this,"Income Dialog Box",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionsMenu floatingActionsMenu=(FloatingActionsMenu)findViewById(R.id.fam);
        floatingActionsMenu.addButton(fabexp);
        floatingActionsMenu.addButton(fabinc);
    }
}