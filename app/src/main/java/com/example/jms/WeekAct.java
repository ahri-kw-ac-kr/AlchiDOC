package com.example.jms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;


public class WeekAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_act);

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
        series.addPoint(new ValueLinePoint("월", 2.4f));
        series.addPoint(new ValueLinePoint("화", 3.4f));
        series.addPoint(new ValueLinePoint("수", .4f));
        series.addPoint(new ValueLinePoint("목", 1.2f));
        series.addPoint(new ValueLinePoint("금", 2.6f));
        series.addPoint(new ValueLinePoint("토", 1.0f));
        series.addPoint(new ValueLinePoint("일", 3.5f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();




    }
}
