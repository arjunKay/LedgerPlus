package com.svco.ledgerplus;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.svco.ledgerplus.LedgerDBManager.AMOUNT;
import static com.svco.ledgerplus.LedgerDBManager.CATEGORY;
import static com.svco.ledgerplus.LedgerDBManager.TABLE_TRANSACTIONS;

public class GraphStats extends AppCompatActivity {

    PieChart mchart;
    Toolbar toolbar;
    LedgerDBManager myDB;
    FloatingActionButton fil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_stats);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Graph Statistics");

        myDB=new LedgerDBManager(this);

        List<String> SpinnerArray1 = new ArrayList<String>();
        SpinnerArray1.add("Expense");
        SpinnerArray1.add("Income");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(GraphStats.this,android.R.layout.simple_spinner_item, SpinnerArray1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner_type = (Spinner)findViewById(R.id.spinner);
        spinner_type.setAdapter(adapter);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mchart=(PieChart)findViewById(R.id.chart);
        mchart.setDescription(null);
        mchart.setCenterText("Test");
        mchart.setDrawHoleEnabled(true);
        mchart.setHoleColor(Color.WHITE);
        mchart.setHoleRadius(40f);
        mchart.setTransparentCircleRadius(44f);
        Legend l=mchart.getLegend();
        l.setEnabled(false);

        final ArrayList<Integer> colors = new ArrayList<Integer>();
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

        fil=(FloatingActionButton)findViewById(R.id.filter);



        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final List<String> discat_in = new ArrayList<>();  //Populate this list with Distinct Categories
                final ArrayList<Entry> entries_in = new ArrayList<Entry>();

                if(position==1){
                    mchart.requestLayout();
                    mchart.setCenterText("Income");
                    String relOp=">";
                    Cursor inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 GROUP BY "+CATEGORY);
                    while (inCur.moveToNext()){
                        discat_in.add(inCur.getString(3));
                        entries_in.add(new Entry(((float)inCur.getInt(8)*100)/myDB.sumOfTxn("in"),10));}

                    PieDataSet dataSet=new PieDataSet(entries_in,"");
                    dataSet.setSliceSpace(0);

                    dataSet.setColors(colors);

                    PieData data=new PieData(discat_in,dataSet);
                    data.setValueFormatter(new PercentFormatter());

                    mchart.setData(data);



                }
                else if(position==0)
                {
                    mchart.requestLayout();
                    mchart.setCenterText("Expense");
                    final List<String> discat_ex = new ArrayList<>();  //Populate this list with Distinct Categories
                    final ArrayList<Entry> entries_ex = new ArrayList<Entry>();
                    String relOp="<";
                    Cursor inCur=myDB.executeQuery("SELECT *,SUM("+AMOUNT+") FROM "+ TABLE_TRANSACTIONS + " WHERE "+AMOUNT+" "+relOp+" 0 GROUP BY "+CATEGORY);
                    while (inCur.moveToNext()){
                        discat_ex.add(inCur.getString(3));
                        entries_ex.add(new Entry((((float)inCur.getInt(8)*-1)*100)/myDB.sumOfTxn("ex"),10));}

                    PieDataSet dataSet=new PieDataSet(entries_ex,"");
                    dataSet.setSliceSpace(0);

                    dataSet.setColors(colors);

                    PieData data=new PieData(discat_ex,dataSet);
                    data.setValueFormatter(new PercentFormatter());

                    mchart.setData(data);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
