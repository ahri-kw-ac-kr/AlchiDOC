package com.example.jms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;


public class DayAct extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_act);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(Color.parseColor("#329035"));
        series.addPoint(new ValueLinePoint("09", 2.4f));
        series.addPoint(new ValueLinePoint("10", 3.4f));
        series.addPoint(new ValueLinePoint("11", .4f));
        series.addPoint(new ValueLinePoint("12", 1.2f));
        series.addPoint(new ValueLinePoint("13", 2.6f));
        series.addPoint(new ValueLinePoint("14", 1.0f));
        series.addPoint(new ValueLinePoint("15", 3.5f));
        series.addPoint(new ValueLinePoint("16", 2.4f));
        series.addPoint(new ValueLinePoint("17", 1.2f));
        series.addPoint(new ValueLinePoint("18", 1.8f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
    }
}
