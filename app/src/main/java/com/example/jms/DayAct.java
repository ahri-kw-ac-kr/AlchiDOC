package com.example.jms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
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

        BarChart mBarChart = (BarChart) findViewById(R.id.bar);

        mBarChart.addBar(new BarModel("09", 0.0f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("10", 0.0f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("11", 1.2f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("12", 2.1f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("13", 3.3f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("14", 3.8f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("15", 3.1f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("16", 2.4f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("17", 0.0f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("18", 0.0f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("19", 0.0f, Color.parseColor("#5F9919")));


        mBarChart.startAnimation();

    }
}
