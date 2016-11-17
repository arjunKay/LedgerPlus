//Haris 9:40 drawer ittind..some changes have to be done


package com.svco.ledgerplus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    int d,m,y;
    LedgerDBManager myDb;
    int amount;
        Drawer result= null;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        myDb = new LedgerDBManager(this);
        toolbar= (Toolbar) findViewById(R.id.toolbar);


        //  pbar.setScaleY(3f);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(true);


        AccountHeader header=new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.sac)
                .withSelectionListEnabled(true)
                .withTextColor(Color.parseColor("#FF0000"))

               //.addProfiles(new ProfileDrawerItem().withName("USER"))
                //.withHeaderBackground(R.color.cyan)

                .build();


        //Nav Drawer

        result = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withSliderBackgroundColor(Color.parseColor("#eef9f7"))
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Graph Statistics").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Balance Sheet").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Categories").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Reminder").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings").withSelectable(false)

                      )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override

                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position)
                        {
                            case 3:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this,GraphStats.class) );
                                break;
                            case 5:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this,BalanceSheet.class) );
                                break;
                            case 7:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this,Categories.class) );
                                break;
                            case 9:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this,Reminder.class) );
                                break;
                            case 11:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this,Settings.class) );
                                break;
                        }

                        return true;
                    }
                })
                .build();


         //Expense Chart

            DonutProgress progress= (DonutProgress) findViewById(R.id.circle);
            progress.setMax(100);
            progress.setProgress(45);



            //Floating Menu and Button


        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#00838F"));
materialDesignFAM.animate();
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton1.setColorNormal(Color.parseColor("#80cbc4"));
        floatingActionButton1.setColorPressed(Color.parseColor("#80cbc4"));
          //  floatingActionButton1.setButtonSize(20);

        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton2.setColorNormal(Color.parseColor("#ff7043"));
        floatingActionButton2.setColorPressed(Color.parseColor("#ff7043"));
        //floatingActionButton1.setImageDrawable(R.drawable);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout dialogLayout= (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_layout,null);


                final EditText amt= (EditText) dialogLayout.findViewById(R.id.et_amt);


                final TextView date=(TextView)dialogLayout.findViewById(R.id.textdat);



                final EditText description= (EditText)dialogLayout.findViewById(R.id.des);

                final Spinner spinner_src,spinner_cat;


                List<String> SpinnerArray = new ArrayList<String>();
                SpinnerArray.add("Cash");
                SpinnerArray.add("Bank");
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, SpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_src = (Spinner)dialogLayout.findViewById(R.id.spinner_src);
                spinner_src.setAdapter(adapter);


                final List<String> incomeSpinnerArray = new ArrayList<String>();
                Cursor inCur=myDb.getInCategory();
                incomeSpinnerArray.add("<Select Category>");
                while (inCur.moveToNext()){
                    incomeSpinnerArray.add(inCur.getString(1));
                }
                incomeSpinnerArray.add("Add new ++");
                final ArrayAdapter<String> catSpinnerAdapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, incomeSpinnerArray);
                catSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat = (Spinner)dialogLayout.findViewById(R.id.spinner_cat);
                spinner_cat.setAdapter(catSpinnerAdapter);
                spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        if(position == catSpinnerAdapter.getCount()-1){
                            MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                                    .widgetColorRes(R.color.colorAccent)
                                    .input("Add Category", null, false, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            //Add to database
                                            myDb.insertCat(input.toString(),"I");
                                            incomeSpinnerArray.set(position,input.toString());
                                            incomeSpinnerArray.add("Add new ++");
                                            catSpinnerAdapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(), "Category  : " + input, Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .build();
                            dialog.show();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Calendar calendar = Calendar.getInstance();
                y=calendar.get(Calendar.YEAR);
                m=calendar.get(Calendar.MONTH);
                d=calendar.get(Calendar.DATE);

                date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));

                final DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        y=year;
                        m=month;
                        d=day;

                        date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));
                    }
                };
                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

                //for changing date
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show(getSupportFragmentManager(),"TAG");
                    }
                });

                //For Dialog Box


                MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                        .title("Income")
                        .customView(dialogLayout,true)
                        .positiveText("Add")
                        .negativeText("Cancel")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //Toasting
                                if(spinner_cat.getSelectedItemPosition()!=0){
                                    String amount_text=(amt.getText().toString());
                                    if(amount_text.isEmpty()){
                                        Toast.makeText(Home.this,"Enter Amount",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String text = spinner_cat.getSelectedItem().toString();

                                        String text2 = spinner_src.getSelectedItem().toString();
                                        String text3=amt.getText().toString();
                                       String text4= description.getText().toString();
                                        myDb.insertTxn(text3,text2,text,text4,String.valueOf(d),String.valueOf(m),String.valueOf(y));
                                    }

                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Select a category",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.cancel();
                            }
                        })
                        .build();

                dialog.show();

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RelativeLayout dialogLayout= (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_layout,null);


                final EditText amt= (EditText) dialogLayout.findViewById(R.id.et_amt);


                final TextView date=(TextView)dialogLayout.findViewById(R.id.textdat);



                final EditText description= (EditText) dialogLayout.findViewById(R.id.des);

                final Spinner spinner_src2,spinner_cat2;


                List<String> SpinnerArray = new ArrayList<String>();
                SpinnerArray.add("Cash");
                SpinnerArray.add("Bank");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, SpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_src2 = (Spinner)dialogLayout.findViewById(R.id.spinner_src);
                spinner_src2.setAdapter(adapter);


                final List<String> expSpinnerArray = new ArrayList<String>();
                Cursor expCursor= myDb.getExCategory();
                expSpinnerArray.add("<Select Category>");

                while (expCursor.moveToNext()){
                    expSpinnerArray.add(expCursor.getString(1));
                }
                expSpinnerArray.add("Add New++");
                final ArrayAdapter<String> expSpinnerAdapter = new ArrayAdapter<String>(Home.this,android.R.layout.simple_spinner_item, expSpinnerArray);
                expSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat2 = (Spinner)dialogLayout.findViewById(R.id.spinner_cat);
                spinner_cat2.setAdapter(expSpinnerAdapter);
                spinner_cat2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        if(position == expSpinnerAdapter.getCount()-1){
                            MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                                    .widgetColorRes(R.color.colorAccent)
                                    .input("Add Category", null, false, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            //Add to database
                                            myDb.insertCat(input.toString(),"E");
                                            expSpinnerArray.set(position,input.toString());
                                            expSpinnerArray.add("Add new ++");
                                            expSpinnerAdapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(), "Category  : " + input, Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .build();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Calendar calendar = Calendar.getInstance();
                y=calendar.get(Calendar.YEAR);
                m=calendar.get(Calendar.MONTH);
                d=calendar.get(Calendar.DATE);

                date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));

                final DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        y=year;
                        m=month;
                        d=day;

                        date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));
                    }
                };
                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

                //for changing date
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show(getSupportFragmentManager(),"TAG");
                    }
                });

                //For Dialog Box


                MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                        .title("Expenditure")
                        .customView(dialogLayout,true)
                        .positiveText("Add")
                        .negativeText("Cancel")

                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //Toasting
                                if(spinner_cat2.getSelectedItemPosition()!=0){
                                    String amount_text=(amt.getText().toString());
                                    amount_text="-"+amount_text;
                                    if(amount_text.isEmpty()){
                                        Toast.makeText(Home.this,"Enter Amount",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        String text = spinner_cat2.getSelectedItem().toString();
    String text2=spinner_src2.getSelectedItem().toString();
                                        myDb.insertTxn(amount_text,text2,text,description.getText().toString(),String.valueOf(d),String.valueOf(m),String.valueOf(y));
                                    }

                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Select a category",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.cancel();
                            }
                        })
                        .build();

                dialog.show();

            }
        });
    }

    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}