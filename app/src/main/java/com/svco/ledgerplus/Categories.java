package com.svco.ledgerplus;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public class Categories extends AppCompatActivity {

    Toolbar toolbar;
    LedgerDBManager myDb;
    TabLayout tablayout;
    ViewPager viewpager;
    ViewPagerAdapter viewPagerAdapter;
    first frag_first;
    second frag_second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        myDb = new LedgerDBManager(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Categories);
        setSupportActionBar(toolbar);

        frag_first=new first();
        frag_second=new second();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tablayout = (TabLayout) findViewById(R.id.tab_layout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(frag_first, getString(R.string.expenditure));
        viewPagerAdapter.addFragments(frag_second, getString(R.string.income));

        viewpager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(viewpager);

    }

    public void addOnclick(View view) {

        MaterialDialog dialog=new MaterialDialog.Builder(this)
                .widgetColorRes(R.color.accent)
                .title("New Category")
                .negativeText("Cancel")
                .input("Add Category", null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (viewpager.getCurrentItem() == 0) {
                            Toast.makeText(getApplicationContext(), "Category Added", Toast.LENGTH_LONG).show();
                            int id=myDb.insertCat(input.toString(),"E");
                            frag_first.cats.add(input.toString());
                            frag_first.ids.add(id);
                            frag_first.adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Category Added", Toast.LENGTH_LONG).show();
                            int id=myDb.insertCat(input.toString(),"I");
                            frag_second.cats.add(input.toString());
                            frag_second.ids.add(id);
                            frag_second.adapter.notifyDataSetChanged();

                        }
                    }
                })
                .build();
        dialog.show();
        }
}

