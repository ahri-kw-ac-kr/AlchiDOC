package com.example.jms.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;


public class WeekSleep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_sleep);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        StackedBarChart mStackedBarChart = (StackedBarChart) findViewById(R.id.stackedbarchart);

        StackedBarModel s1 = new StackedBarModel("월");
        s1.addBar(new BarModel(21, Color.parseColor("#7851C3")));
        s1.addBar(new BarModel(60, Color.parseColor("#AC89EB")));
        s1.addBar(new BarModel(19, Color.parseColor("#D7B6F9")));

        StackedBarModel s2 = new StackedBarModel("화");
        s2.addBar(new BarModel(10, Color.parseColor("#7851C3")));
        s2.addBar(new BarModel(25, Color.parseColor("#AC89EB")));
        s2.addBar(new BarModel(22, Color.parseColor("#D7B6F9")));

        StackedBarModel s3 = new StackedBarModel("수");
        s3.addBar(new BarModel(35, Color.parseColor("#7851C3")));
        s3.addBar(new BarModel(27, Color.parseColor("#AC89EB")));
        s3.addBar(new BarModel(38, Color.parseColor("#D7B6F9")));

        StackedBarModel s4 = new StackedBarModel("목");
        s4.addBar(new BarModel(21, Color.parseColor("#7851C3")));
        s4.addBar(new BarModel(60, Color.parseColor("#AC89EB")));
        s4.addBar(new BarModel(19, Color.parseColor("#D7B6F9")));

        StackedBarModel s5 = new StackedBarModel("금");
        s5.addBar(new BarModel(35, Color.parseColor("#7851C3")));
        s5.addBar(new BarModel(27, Color.parseColor("#AC89EB")));
        s5.addBar(new BarModel(38, Color.parseColor("#D7B6F9")));

        StackedBarModel s6 = new StackedBarModel("토");
        s6.addBar(new BarModel(21, Color.parseColor("#7851C3")));
        s6.addBar(new BarModel(60, Color.parseColor("#AC89EB")));
        s6.addBar(new BarModel(19, Color.parseColor("#D7B6F9")));

        StackedBarModel s7 = new StackedBarModel("일");
        s7.addBar(new BarModel(35, Color.parseColor("#7851C3")));
        s7.addBar(new BarModel(27, Color.parseColor("#AC89EB")));
        s7.addBar(new BarModel(38, Color.parseColor("#D7B6F9")));

        mStackedBarChart.addBar(s1);
        mStackedBarChart.addBar(s2);
        mStackedBarChart.addBar(s3);
        mStackedBarChart.addBar(s4);
        mStackedBarChart.addBar(s5);
        mStackedBarChart.addBar(s6);
        mStackedBarChart.addBar(s7);

        mStackedBarChart.startAnimation();
    }
}
