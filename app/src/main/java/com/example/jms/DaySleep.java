package com.example.jms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;


public class DaySleep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_sleep);

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
        series.setColor(Color.parseColor("#7476B3"));
        series.addPoint(new ValueLinePoint("23", 0.5f));
        series.addPoint(new ValueLinePoint("24", 1.9f));
        series.addPoint(new ValueLinePoint("01", 3.1f));
        series.addPoint(new ValueLinePoint("02", 1.7f));
        series.addPoint(new ValueLinePoint("03", 2.3f));
        series.addPoint(new ValueLinePoint("04", 1.2f));
        series.addPoint(new ValueLinePoint("05", 2.3f));
        series.addPoint(new ValueLinePoint("06", 1.9f));
        series.addPoint(new ValueLinePoint("07", 1.5f));


        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();




    }
}
