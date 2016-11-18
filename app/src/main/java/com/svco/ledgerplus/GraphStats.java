package com.svco.ledgerplus;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class GraphStats extends AppCompatActivity {

    PieChart mchart;
    Toolbar toolbar;
    LedgerDBManager myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_stats);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Graph Statistics");

        myDB=new LedgerDBManager(this);

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

        final List<String> discat = new ArrayList<>();  //Populate this list with Distinct Categories
        ArrayList<Entry> entries = new ArrayList<Entry>(); //Populate this list with sum of transactions in corresponding category

        //To be Edited from here ---->


        Cursor inCur=myDB.getExCategory(); //Cursor inCur=myDB.getAllData(myDB.TABLE_TRANSACTIONS);
        while (inCur.moveToNext()){
            discat.add(inCur.getString(1));
            entries.add(new Entry((float)Math.random(),10));}//entries.add(new Entry((float)inCur.getFloat(1),10));


        //------>upto here

        PieDataSet dataSet=new PieDataSet(entries,"");
        dataSet.setSliceSpace(2f);



        ArrayList<Integer> colors = new ArrayList<Integer>();
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

        dataSet.setColors(colors);

        PieData data=new PieData(discat,dataSet);
        data.setValueFormatter(new PercentFormatter());

        mchart.setData(data);


    }
}
