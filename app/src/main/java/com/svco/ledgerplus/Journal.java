package com.svco.ledgerplus;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Journal extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public FloatingActionButton fab;
    public String  clickflag="fromDate";
    ListviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_home);
       /* recyclerView=(RecyclerView)findViewById(R.id.recyclerVi);
        adapter=new RecyclerAdapter(getApplicationContext());
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);*/
        final RelativeLayout activityMain=(RelativeLayout)findViewById(R.id.activity_main);
        final ListView theListView = (ListView) findViewById(R.id.recyclerVi);
        adapter = new ListviewAdapter(getApplicationContext());
        theListView.setAdapter( adapter);
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((FoldingCell)view).toggle(false);
                adapter.registerToggle(position);
            }
        });
// Gets the layout params that will allow you to resize the layout

        fab=(FloatingActionButton)findViewById(R.id.jrnlFloatingBtn);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // -----------------
                final RelativeLayout dialogLayoutJournal = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_layout_journal_filter,null);
                final EditText minAmtIn, maxAmtIn;
                final TextView fromDateIn, toDateIn;
                final Spinner journalSrcSpinner, journalInExSpinner, journalCatSpinner;
                final Calendar cal=Calendar.getInstance();
                final int curYear, curMonth, curDay;
                final LedgerDBManager filterDB=new LedgerDBManager(getApplicationContext());
                final List<String> inexList=new ArrayList<String>();
                final List<String> srcList=new ArrayList<String>();
                final List<String> catList=new ArrayList<String>();
                inexList.add("Expenditure/Income");
                inexList.add("Expenditure");
                inexList.add("Income");
                srcList.add("Cash/Bank");
                srcList.add("Cash");
                srcList.add("Bank");
                catList.add("Select Category");
                curYear=cal.YEAR;
                curMonth=cal.MONTH;
                curDay=cal.DAY_OF_MONTH;
                fromDateIn=(TextView)dialogLayoutJournal.findViewById(R.id.fromDateIn);
                toDateIn=(TextView)dialogLayoutJournal.findViewById(R.id.toDateIn);
                minAmtIn=(EditText)dialogLayoutJournal.findViewById(R.id.minAmtIn);
                maxAmtIn=(EditText)dialogLayoutJournal.findViewById(R.id.maxAmtIn);
                journalInExSpinner=(Spinner)dialogLayoutJournal.findViewById(R.id.inExSpinner);
                journalCatSpinner=(Spinner)dialogLayoutJournal.findViewById(R.id.catSpinner);
                journalSrcSpinner=(Spinner)dialogLayoutJournal.findViewById(R.id.srcSpinner);
                ArrayAdapter<String> dataAdapterInEx = new ArrayAdapter<String>(Journal.this, android.R.layout.simple_spinner_item, inexList);
                dataAdapterInEx.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                journalInExSpinner.setAdapter(dataAdapterInEx);
                ArrayAdapter<String> dataAdapterSrc = new ArrayAdapter<String>(Journal.this, android.R.layout.simple_spinner_item, srcList);
                dataAdapterSrc.setDropDownViewResource(android.R.layout.simple_spinner_item);
                journalSrcSpinner.setAdapter(dataAdapterSrc);
                journalInExSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int index;
                        Cursor cur;
                        index=journalInExSpinner.getSelectedItemPosition();
                        if(index==1)
                        {
                            catList.clear();
                            catList.add("Select Category");
                            cur=filterDB.getExCategory();
                            if(cur.moveToFirst())
                            {
                                while(cur.moveToNext())
                                {
                                    do {

                                        catList.add(cur.getString(1));
                                    }while (cur.moveToNext());
                                    ;
                                }
                            }
                        }
                        if(index==2)
                        {
                            catList.clear();
                            catList.add("Select Category");
                            cur=filterDB.getInCategory();
                            if(cur.moveToFirst())
                            {
                                do
                                {
                                    catList.add(cur.getString(1));
                                }while(cur.moveToNext());
                            }
                        }
                        ArrayAdapter<String> dataAdapterCat = new ArrayAdapter<String>(Journal.this, android.R.layout.simple_spinner_item, catList);
                        dataAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        journalCatSpinner.setAdapter(dataAdapterCat);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                        String dateToSet=""+day+"/"+(month+1)+"/"+year;

                        if(clickflag.equals("fromDate"))
                            fromDateIn.setText(dateToSet);
                        if(clickflag.equals("toDate"))
                            toDateIn.setText(dateToSet);
                    }


                };
                final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(listener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), false);
                fromDateIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickflag="fromDate";
                        datePickerDialog.show(getSupportFragmentManager(),"TAG");
                    }
                });
                toDateIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickflag="toDate";
                        datePickerDialog.show(getSupportFragmentManager(),"TAG");
                    }
                });

                MaterialDialog dialogJournal=new MaterialDialog.Builder(Journal.this)
                        .title("Filter Results")
                        .customView(dialogLayoutJournal,true)
                        .positiveText("Update")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String fromDate, toDate, jrnlInEx="", jrnlCashBank="", jrnlCat, minAmt, maxAmt;
                                int inOrEx, cashOrBank, catIndex;
                                fromDate=fromDateIn.getText().toString();
                                toDate=toDateIn.getText().toString();
                                inOrEx=journalInExSpinner.getSelectedItemPosition();
                                cashOrBank=journalSrcSpinner.getSelectedItemPosition();
                                catIndex=journalCatSpinner.getSelectedItemPosition();
                                minAmt=minAmtIn.getText().toString();
                                maxAmt=maxAmtIn.getText().toString();
                                Cursor filterCur;
                                int fromDateInt=-1, toDateInt=-1;
                                String[] jrnlTemp1;
                                if(fromDate.isEmpty()==false || toDate.isEmpty()==false)
                                {
                                    if(fromDate.isEmpty()==false)
                                    {
                                        jrnlTemp1=new String[3];
                                        jrnlTemp1=fromDate.split("/");
                                        fromDateInt=((Integer.parseInt(jrnlTemp1[2])*10000)+((Integer.parseInt(jrnlTemp1[1]))*100)+(Integer.parseInt(jrnlTemp1[0])));
                                        fromDate=""+fromDateInt;

                                    }
                                    if(toDate.isEmpty()==false)
                                    {
                                        jrnlTemp1=new String[3];
                                        jrnlTemp1=toDate.split("/");
                                        toDateInt=((Integer.parseInt(jrnlTemp1[2])*10000)+((Integer.parseInt(jrnlTemp1[1]))*100)+(Integer.parseInt(jrnlTemp1[0])));
                                        toDate=""+toDateInt;

                                    }
                                }
                                else
                                {
                                    fromDate="0";
                                    toDate="0";
                                }
                                if(fromDateInt==-1)
                                    fromDate="0";
                                if(toDateInt==-1)
                                    toDate="0";

                                //Cash/Bank
                                if(cashOrBank!=0)
                                {
                                    if(cashOrBank==1)
                                        jrnlCashBank="Cash";
                                    if(cashOrBank==2)
                                        jrnlCashBank="Bank";

                                }
                                else
                                {
                                    jrnlCashBank="";
                                }

                                //Income/Expense
                                if(inOrEx!=0)
                                {
                                    if(inOrEx==1)
                                        jrnlInEx="e";
                                    if(inOrEx==2)
                                        jrnlInEx="i";
                                }
                                else
                                {
                                    jrnlInEx="";
                                }

                                if(catIndex!=0)
                                {
                                    jrnlCat=journalCatSpinner.getSelectedItem().toString();
                                }
                                else
                                {
                                    jrnlCat="";
                                }
                                if(minAmt.isEmpty())
                                {
                                    minAmt="0";
                                }
                                if(maxAmt.isEmpty())
                                {
                                    maxAmt="-1";
                                }
                                filterCur=filterDB.filterData(fromDate,toDate,jrnlCashBank,jrnlInEx,jrnlCat,minAmt,maxAmt);
                                adapter=new ListviewAdapter(getApplicationContext(), filterCur);
                                theListView.setAdapter(adapter);
                                //
                                //recyclerView.setAdapter(null);
                                //layoutManager=new LinearLayoutManager(Journal.this);
                               // recyclerView.setLayoutManager(layoutManager);
                               // recyclerView.setHasFixedSize(true);
                              //  recyclerView.setAdapter(adapter);
                                //

                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).build();
                dialogJournal.show();


                // ------------------
            }
        });
       //fc=(FoldingCell)c



    }

}
