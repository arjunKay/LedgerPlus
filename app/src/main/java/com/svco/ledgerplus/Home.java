package com.svco.ledgerplus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;
    int d,m,y;
    int dt,dm,dy;
    LedgerDBManager myDb;
    int sum=0,spin_in=0,spin_ex=0;
    long diff=0;
    int ex_to=0,in_to=0;

    TextView newb;
    TextView in1;
    TextView in2;
    TextView ex1;
    TextView ex2;
    String name,email;
    TextView st1,st2,st3,st4,st5,st6,st7,st8,st9,st10;
    ImageView im;

    Drawer result= null;
    DonutProgress progress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        myDb = new LedgerDBManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        Cursor cursor;
        cursor=myDb.getProfileName();
        cursor.moveToFirst();
        name = cursor.getString(1);
        email = cursor.getString(2);


        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.sac)
                .withSelectionListEnabled(false)
                .withTextColor(Color.parseColor("#FFFFFF"))
                .addProfiles(new ProfileDrawerItem()
                        .withName(name)
                        .withEmail(email)
                        .withSelectable(true))
                .withOnAccountHeaderSelectionViewClickListener(
                        new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                            @Override
                            public boolean onClick(View view, IProfile profile) {
                                Snackbar.make(findViewById(android.R.id.content),
                                        "Hi "+name+", want to change name?",
                                        Snackbar.LENGTH_LONG)
                                        .setAction("Change", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent x=new Intent(Home.this,Settings.class);
                                                startActivity(x);
                                            }
                                        }).setActionTextColor(Color.YELLOW)
                                        .setDuration(2000).show();
                                return false;
                            }
                        }
                )
                .build();


        //Nav Drawer
        result = new DrawerBuilder()
                .withToolbar(toolbar)
                .withActivity(this)
                .withCloseOnClick(true)
                .withSliderBackgroundColor(Color.parseColor("#eef9f7"))
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Overview").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Graph Statistics")
                                .withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Journal").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Categories").withSelectable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings").withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override

                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem){
                        // do something with the clicked item
                        switch (position) {
                            case 1:
                                result.closeDrawer();
                                break;
                            case 3:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this, GraphStats.class));
                                break;
                            case 5:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this, Journal.class));
                                break;
                            case 7:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this, Categories.class));
                                break;
                            case 9:
                                result.closeDrawer();
                                startActivity(new Intent(Home.this, Settings.class));
                                break;
                        }
                        return true;
                    }
                })
                .build();


        //Calendar Instance

        final Calendar calendar = Calendar.getInstance();
        y=calendar.get(Calendar.YEAR);
        m=calendar.get(Calendar.MONTH)+1;
        d=calendar.get(Calendar.DATE);
        dt=d;
        dm=m;
        dy=y;


        //Progress Bar & Spinner

        progress = (DonutProgress) findViewById(R.id.circle);

        newb = (TextView) findViewById(R.id.no_dat);
        in1 = (TextView) findViewById(R.id.tv_color1);
        in2 = (TextView) findViewById(R.id.tv_in);
        ex1 = (TextView) findViewById(R.id.tv_color2);
        ex2 = (TextView) findViewById(R.id.tv_ex);
        im = (ImageView) findViewById(R.id.imageView);

        st1=(TextView)findViewById(R.id.cur_bal);
        st2=(TextView)findViewById(R.id.cur_bal_val);
        st3=(TextView)findViewById(R.id.tot_exp);
        st4=(TextView)findViewById(R.id.tot_exp_val);
        st5=(TextView)findViewById(R.id.tot_inc);
        st6=(TextView)findViewById(R.id.tot_inc_val);
        st7=(TextView)findViewById(R.id.exp_tod);
        st8=(TextView)findViewById(R.id.exp_tod_val);
        st9=(TextView)findViewById(R.id.inc_tod);
        st10=(TextView)findViewById(R.id.inc_tod_val);

        spin_ex = myDb.sumOfTxn("ex");
        spin_in = myDb.sumOfTxn("in");
        ex_to=myDb.sumOfExpToday(String.valueOf(dy),String.valueOf(dm),String.valueOf(dt));
        in_to=myDb.sumOfInToday(String.valueOf(dy),String.valueOf(dm),String.valueOf(dt));
        sum = spin_ex + spin_in;
        diff=spin_in - spin_ex;

        if (sum == 0)
            spinnerChange(0,0,0);
        else {
            spinnerChange(spin_ex,spin_in,sum);
        }

        st4.setText("₹ "+String.valueOf(spin_ex));
        st6.setText("₹ "+String.valueOf(spin_in));
        st8.setText("₹ "+String.valueOf(ex_to));
        st10.setText("₹ "+String.valueOf(in_to));

        if(diff>0){
            ScaleTextSize(st2,diff);
            st2.setText("₹ "+String.valueOf(diff));
            st2.setTextColor(Color.parseColor("#4caf50"));
        }
        else{
            ScaleTextSize(st2,diff);
            st2.setText("₹ "+String.valueOf(diff));
            st2.setTextColor(Color.parseColor("#f44336"));
        }


        //Floating Menu and Button

        materialDesignFAM =(FloatingActionMenu)findViewById(R.id.material_design_android_floating_action_menu);
        materialDesignFAM.setMenuButtonColorNormal(Color.parseColor("#008fcc"));
        materialDesignFAM.animate();

        floatingActionButton1 =(FloatingActionButton)findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton1.setColorNormal(Color.parseColor("#80cbc4"));
        floatingActionButton1.setColorPressed(Color.parseColor("#80cbc4"));

        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton2.setColorNormal(Color.parseColor("#ff7043"));
        floatingActionButton2.setColorPressed(Color.parseColor("#ff7043"));

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RelativeLayout dialogLayout= (RelativeLayout)getLayoutInflater()
                        .inflate(R.layout.dialog_layout,null);

                final EditText amt= (EditText) dialogLayout.findViewById(R.id.et_amt);
                final TextView date=(TextView)dialogLayout.findViewById(R.id.textdat);
                final EditText description= (EditText)dialogLayout.findViewById(R.id.des);
                final Spinner spinner_src,spinner_cat;

                List<String> SpinnerArray = new ArrayList<>();
                SpinnerArray.add("Cash");
                SpinnerArray.add("Bank");

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(Home.this,
                        android.R.layout.simple_spinner_item, SpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_src = (Spinner)dialogLayout.findViewById(R.id.spinner_src);
                spinner_src.setAdapter(adapter);

                final List<String> incomeSpinnerArray = new ArrayList<>();
                Cursor inCur=myDb.getInCategory();
                incomeSpinnerArray.add("<Select Category>");
                while (inCur.moveToNext()){
                    incomeSpinnerArray.add(inCur.getString(1));
                }
                incomeSpinnerArray.add("++ Add new category");

                final ArrayAdapter<String> catSpinnerAdapter = new ArrayAdapter<>(Home.this,
                        android.R.layout.simple_spinner_item, incomeSpinnerArray);
                catSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat = (Spinner)dialogLayout.findViewById(R.id.spinner_cat);
                spinner_cat.setAdapter(catSpinnerAdapter);
                spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position,
                                               long id) {
                        if(position == catSpinnerAdapter.getCount()-1){
                            MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                                    .widgetColorRes(R.color.colorAccent)
                                    .title("New Category")
                                    .input("Add Category", null, false, new MaterialDialog
                                            .InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog,
                                                            CharSequence input) {
                                            //Add to database
                                            myDb.insertCat(input.toString(),"I");
                                            incomeSpinnerArray.set(position,input.toString());
                                            incomeSpinnerArray.add("Add new ++");
                                            catSpinnerAdapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(),
                                                    "Category  : " + input, Toast.LENGTH_LONG).show();
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

                date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));
                final DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        y=year;
                        m=month+1;
                        d=day;

                        date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));
                    }
                };

                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), false);

                //for changing date
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show(getSupportFragmentManager(),"TAG");
                    }
                });


                //For Dialog Box
                MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                        .title(R.string.income)
                        .customView(dialogLayout,true)
                        .positiveText(R.string.add)
                        .negativeText(R.string.cancel)
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
                                        final String text3=amt.getText().toString();
                                        String text4= description.getText().toString();
                                        myDb.insertTxn(text3,text2,text,text4,
                                                String.valueOf(d),String.valueOf(m),String.valueOf(y));

                                        int x1=spin_in+Integer.parseInt(amount_text);
                                        sum = x1 + spin_ex;

                                        spinnerChange(spin_ex,x1,sum);

                                        spin_ex=myDb.sumOfTxn("ex");
                                        spin_in=myDb.sumOfTxn("in");
                                        ex_to=myDb.sumOfExpToday(
                                                String.valueOf(dy),
                                                String.valueOf(dm),
                                                String.valueOf(dt));
                                        in_to=myDb.sumOfInToday(
                                                String.valueOf(dy),
                                                String.valueOf(dm),
                                                String.valueOf(dt));

                                        diff=spin_in - spin_ex;
                                        st4.setText("₹ "+String.valueOf(spin_ex));
                                        st6.setText("₹ "+String.valueOf(spin_in));
                                        st8.setText("₹ "+String.valueOf(ex_to));
                                        st10.setText("₹ "+String.valueOf(in_to));

                                        if(diff>0){
                                            ScaleTextSize(st2,diff);
                                            st2.setText("₹ "+String.valueOf(diff));
                                            st2.setTextColor(Color.parseColor("#4caf50"));
                                        }
                                        else{
                                            ScaleTextSize(st2,diff);
                                            st2.setText("₹ "+String.valueOf(diff));
                                            st2.setTextColor(Color.parseColor("#f44336"));
                                        }
                                        dialog.dismiss();
                                        materialDesignFAM.close(true);
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
                        .autoDismiss(false)
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

                List<String> SpinnerArray = new ArrayList<>();
                SpinnerArray.add("Cash");
                SpinnerArray.add("Bank");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Home.this, android.R.layout.simple_spinner_item, SpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_src2 = (Spinner)dialogLayout.findViewById(R.id.spinner_src);
                spinner_src2.setAdapter(adapter);

                final List<String> expSpinnerArray = new ArrayList<>();
                Cursor expCursor= myDb.getExCategory();
                expSpinnerArray.add("<Select Category>");

                while (expCursor.moveToNext()){
                    expSpinnerArray.add(expCursor.getString(1));
                }
                expSpinnerArray.add("++ Add new category");

                final ArrayAdapter<String> expSpinnerAdapter = new ArrayAdapter<>(Home.this,
                        android.R.layout.simple_spinner_item,
                        expSpinnerArray);
                expSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_cat2 = (Spinner)dialogLayout.findViewById(R.id.spinner_cat);
                spinner_cat2.setAdapter(expSpinnerAdapter);
                spinner_cat2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        if(position == expSpinnerAdapter.getCount()-1){
                            MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                                    .widgetColorRes(R.color.colorAccent)
                                    .title("New Category")
                                    .input("Add Category", null, false, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                            //Add to database
                                            myDb.insertCat(input.toString(),"E");
                                            expSpinnerArray.set(position,input.toString());
                                            expSpinnerArray.add("Add new ++");
                                            expSpinnerAdapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(),
                                                    "Category  : " + input,
                                                    Toast.LENGTH_LONG).show();
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
                m=calendar.get(Calendar.MONTH)+1;
                d=calendar.get(Calendar.DATE);

                date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));

                final DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        y=year;
                        m=month+1;
                        d=day;
                        date.setText(String.valueOf(d)+"/"+String.valueOf(m)+"/"+String.valueOf(y));
                    }
                };
                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), false);

                //for changing date
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        datePickerDialog.show(getSupportFragmentManager(),"TAG");
                    }
                });


                //For Expenditure Dialog Box
                MaterialDialog dialog=new MaterialDialog.Builder(Home.this)
                        .title(R.string.expenditure)
                        .customView(dialogLayout,true)
                        .positiveText(R.string.add)
                        .negativeText(R.string.cancel)
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //Toasting
                                if(spinner_cat2.getSelectedItemPosition()!=0){
                                    String amount_text=(amt.getText().toString());
                                    if(amount_text.isEmpty()){
                                        Toast.makeText(Home.this,"Enter Amount",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        final String value=amount_text;
                                        amount_text="-"+amount_text;
                                        String text = spinner_cat2.getSelectedItem().toString();
                                        String text2=spinner_src2.getSelectedItem().toString();
                                        myDb.insertTxn(amount_text,text2,text,
                                                description.getText().toString(),
                                                String.valueOf(d),String.valueOf(m),String.valueOf(y));

                                        int x1=spin_ex+Integer.parseInt(value);
                                        sum = x1 + spin_in;

                                        spinnerChange(x1,spin_in,sum);
                                        spin_ex=myDb.sumOfTxn("ex");
                                        spin_in=myDb.sumOfTxn("in");
                                        ex_to=myDb.sumOfExpToday(String.valueOf(dy),String.valueOf(dm),String.valueOf(dt));
                                        in_to=myDb.sumOfInToday(String.valueOf(dy),String.valueOf(dm),String.valueOf(dt));

                                        st4.setText("₹ "+String.valueOf(spin_ex));
                                        st6.setText("₹ "+String.valueOf(spin_in));

                                        st8.setText("₹ "+String.valueOf(ex_to));
                                        st10.setText("₹ "+String.valueOf(in_to));

                                        diff=spin_in - spin_ex;

                                        if(diff>0){
                                            ScaleTextSize(st2,diff);
                                            st2.setText("₹ "+String.valueOf(diff));
                                            st2.setTextColor(Color.parseColor("#4caf50"));
                                        }
                                        else{
                                            ScaleTextSize(st2,diff);
                                            st2.setText("₹ "+String.valueOf(diff));
                                            st2.setTextColor(Color.parseColor("#f44336"));
                                        }
                                        dialog.dismiss();
                                        materialDesignFAM.close(true);
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
                                dialog.dismiss();
                            }
                        })
                        .autoDismiss(false)
                        .build();
                dialog.show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateHomeScreenValues();
    }
    public void updateHomeScreenValues()
    {
        TextView currentBal, totalExp, totalIn,todayExp, todayIn;

        currentBal=(TextView)findViewById(R.id.cur_bal_val);
        totalExp=(TextView)findViewById(R.id.tot_exp_val);
        totalIn=(TextView)findViewById(R.id.tot_inc_val);
        todayExp=(TextView)findViewById(R.id.exp_tod_val);
        todayIn=(TextView)findViewById(R.id.inc_tod_val);

        LedgerDBManager temp=new LedgerDBManager(getApplicationContext());

        final Calendar calendar = Calendar.getInstance();
        int y=calendar.get(Calendar.YEAR);
        int m=calendar.get(Calendar.MONTH)+1;
        int d=calendar.get(Calendar.DATE);
        int bal, exp, inc;

        exp=temp.sumOfTxn("ex");
        inc=temp.sumOfTxn("in");
        bal=inc-exp;

        spinnerChange(exp,inc,exp+inc);
        totalExp.setText("₹ "+ exp);
        totalIn.setText("₹ "+inc);

        if(bal>0){
            ScaleTextSize(currentBal,bal);
            currentBal.setText("₹ "+bal);
            currentBal.setTextColor(Color.parseColor("#4caf50"));
        }
        else{
            ScaleTextSize(currentBal,bal);
            currentBal.setText("₹ "+bal);
            currentBal.setTextColor(Color.parseColor("#f44336"));
        }

        todayExp.setText("₹ "+temp.sumOfExpToday(""+y,""+m,""+d));
        todayIn.setText("₹ "+temp.sumOfInToday(""+y,""+m,""+d));
    }
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity



        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
        else if (materialDesignFAM.isOpened()){
            materialDesignFAM.close(true);
        }
        else {
            super.onBackPressed();
        }
    }

    public void spinnerChange(int expense, int income, int sum){
        if((expense==0) &&( income ==0)) {
            newb.setText(R.string.no_data_available);
            progress.setVisibility(View.INVISIBLE);
            in1.setVisibility(View.INVISIBLE);
            in2.setVisibility(View.INVISIBLE);
            ex1.setVisibility(View.INVISIBLE);
            ex2.setVisibility(View.INVISIBLE);
            im.setVisibility(View.VISIBLE);
            newb.setVisibility(View.VISIBLE);
        }

        else
        {
            progress.setMax(sum);
            progress.setProgress(expense);
            im.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            in1.setVisibility(View.VISIBLE);
            in2.setVisibility(View.VISIBLE);
            ex1.setVisibility(View.VISIBLE);
            ex2.setVisibility(View.VISIBLE);
            newb.setVisibility(View.INVISIBLE);
        }
    }

    public void ScaleTextSize(TextView t,long value)
    {
        if(value>999999999)
        {
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        }else
        {
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        }
    }
}