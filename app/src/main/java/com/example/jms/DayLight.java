package com.example.jms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;


public class DayLight extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_light);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        BarChart mBarChart = (BarChart) findViewById(R.id.bar);

        mBarChart.addBar(new BarModel("09", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("10", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("11", 1.2f, Color.parseColor("#F2EAB2")));
        mBarChart.addBar(new BarModel("12", 2.1f, Color.parseColor("#F7E082")));
        mBarChart.addBar(new BarModel("13", 3.3f, Color.parseColor("#D83909")));
        mBarChart.addBar(new BarModel("14", 3.8f, Color.parseColor("#B02E06")));
        mBarChart.addBar(new BarModel("15", 3.1f, Color.parseColor("#F8683C")));
        mBarChart.addBar(new BarModel("16", 2.4f, Color.parseColor("#F99678")));
        mBarChart.addBar(new BarModel("17", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("18", 0.0f, Color.parseColor("#ffffff")));



        mBarChart.startAnimation();





    }
}
