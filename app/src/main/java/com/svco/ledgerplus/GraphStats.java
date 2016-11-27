package com.svco.ledgerplus;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.svco.ledgerplus.LedgerDBManager.AMOUNT;
import static com.svco.ledgerplus.LedgerDBManager.CATEGORY;
import static com.svco.ledgerplus.LedgerDBManager.DAY;
import static com.svco.ledgerplus.LedgerDBManager.MONTH;
import static com.svco.ledgerplus.LedgerDBManager.TABLE_TRANSACTIONS;
import static com.svco.ledgerplus.LedgerDBManager.YEAR;

public class GraphStats extends AppCompatActivity {

    Toolbar toolbar;
    LedgerDBManager myDB;
    PieChart mchart;
    FloatingActionButton filter;
    int selectedId,sum;
    Cursor inCur=null;
    int day,month,year;
    String relOp;
    Calendar calendar;
    ArrayList<Integer> colors;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_stats);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Graph Statistics");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendar=Calendar.getInstance();
        day=calendar.get(Calendar.DATE);
        month=calendar.get(Calendar.MONTH)+1;
        year=calendar.get(Calendar.YEAR);

        myDB = new LedgerDBManager(this);

        int check=myDB.sumOfTxn("in")+myDB.sumOfTxn("ex");
        mchart=(PieChart)findViewById(R.id.chart);
        TextView tv=(TextView)findViewById(R.id.tvxt);
        ImageView im=(ImageView)findViewById(R.id.imgdat);

        if(check==0)
        {
            tv.setVisibility(View.VISIBLE);
            im.setVisibility(View.VISIBLE);
            mchart.setVisibility(View.INVISIBLE);
        }

        else
        {
            tv.setVisibility(View.INVISIBLE);
            im.setVisibility(View.INVISIBLE);
            mchart.setVisibility(View.VISIBLE);
            mchart.setDescription(null);
            mchart.setCenterText("Expense");
            mchart.setDrawHoleEnabled(true);
            mchart.setHoleColor(Color.WHITE);
            mchart.setHoleRadius(40f);
            mchart.setTransparentCircleRadius(44f);
            Legend l = mchart.getLegend();
            l.setEnabled(false);
        }

        filter=(FloatingActionButton)findViewById(R.id.filter);





        colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());

        inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" < 0 GROUP BY "+CATEGORY);
        sum=(-1)*myDB.sumOfTxn("ex");
        chartData(sum,inCur);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout linearLayout=(LinearLayout) getLayoutInflater().inflate(R.layout.graph_filter, null);
                final RadioGroup radioGroup=(RadioGroup)linearLayout.findViewById(R.id.newgroup);
                List<String> SpinnerArray1 = new ArrayList<String>();
                SpinnerArray1.add("Expense");
                SpinnerArray1.add("Income");
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(GraphStats.this,android.R.layout.simple_spinner_item, SpinnerArray1);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                final Spinner spinner_type = (Spinner)linearLayout.findViewById(R.id.spinner2);
                spinner_type.setAdapter(adapter);
                MaterialDialog materialDialog=new MaterialDialog.Builder(GraphStats.this)
                        .title("Filter By")
                        .customView(linearLayout,true)
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                selectedId=radioGroup.getCheckedRadioButtonId();
                                if(spinner_type.getSelectedItemPosition()==0)
                                {
                                    mchart.setCenterText("Expense");
                                    relOp="<";
                                    switch (selectedId)
                                    {
                                        case R.id.radioButton4:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 GROUP BY "+CATEGORY);
                                            sum=(-1)*myDB.sumOfTxn("ex");
                                            chartData(sum,inCur);
                                            break;
                                        case R.id.radioButton2:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 AND "+DAY+" = "+day+" GROUP BY "+CATEGORY);
                                            sum=(-1)*myDB.sumOfExpToday(String.valueOf(year),String.valueOf(month),String.valueOf(day));
                                            chartData(sum,inCur);
                                            break;
                                        case R.id.radioButton3:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 AND "+MONTH+" = "+month+" GROUP BY "+CATEGORY);
                                            sum=(-1)*myDB.sumOfExpThisMonth(String.valueOf(year),String.valueOf(month));
                                            chartData(sum,inCur);
                                            break;
                                        case R.id.radioButton:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 AND "+YEAR+" = "+year+" GROUP BY "+CATEGORY);
                                            sum=(-1)*myDB.sumOfExpThisYear(String.valueOf(year));
                                            chartData(sum,inCur);
                                            break;
                                    }
                                }

                                else if(spinner_type.getSelectedItemPosition()==1)
                                {
                                    mchart.setCenterText("Income");
                                    relOp=">" ;
                                    switch (selectedId)
                                    {
                                        case R.id.radioButton4:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 GROUP BY "+CATEGORY);
                                            sum=myDB.sumOfTxn("in");
                                            chartData(sum,inCur);
                                            break;
                                        case R.id.radioButton2:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 AND "+DAY+" = "+day+" GROUP BY "+CATEGORY);
                                            sum=myDB.sumOfInToday(String.valueOf(year),String.valueOf(month),String.valueOf(day));
                                            chartData(sum,inCur);
                                            break;
                                        case R.id.radioButton3:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 AND "+MONTH+" = "+month+" GROUP BY "+CATEGORY);
                                            sum=myDB.sumOfInThisMonth(String.valueOf(year),String.valueOf(month));
                                            chartData(sum,inCur);
                                            break;
                                        case R.id.radioButton:
                                            inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 AND "+YEAR+" = "+year+" GROUP BY "+CATEGORY);
                                            sum=myDB.sumOfInThisYear(String.valueOf(year));
                                            chartData(sum,inCur);
                                            break;
                                    }
                                }
                            }
                        })
                        .build();
                materialDialog.show();
            }
        });

    }

    public void chartData(int sum,Cursor inCur)
    {
        final List<String> discat = new ArrayList<>();
        final ArrayList<Entry> entries = new ArrayList<Entry>();

        while (inCur.moveToNext()){
            discat.add(inCur.getString(3));
            entries.add(new Entry(((float)inCur.getInt(8)*100)/sum,10));}

        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setSliceSpace(0);

        dataSet.setColors(colors);

        PieData data=new PieData(discat,dataSet);
        data.setValueFormatter(new PercentFormatter());

        mchart.setData(data);
        mchart.requestLayout();
    }
}