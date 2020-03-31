package com.example.jms;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;


public class WeekSleep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_sleep);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        StackedBarChart mStackedBarChart = (StackedBarChart) findViewById(R.id.stackedbarchart);

        StackedBarModel s1 = new StackedBarModel("월");
        s1.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s1.addBar(new BarModel(2.3f, 0xFF56B7F1));
        s1.addBar(new BarModel(2.3f, 0xFFCDA67F));

        StackedBarModel s2 = new StackedBarModel("화");
        s2.addBar(new BarModel(1.1f, 0xFF63CBB0));
        s2.addBar(new BarModel(2.7f, 0xFF56B7F1));
        s2.addBar(new BarModel(0.7f, 0xFFCDA67F));

        StackedBarModel s3 = new StackedBarModel("수");
        s3.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s3.addBar(new BarModel(2.f, 0xFF56B7F1));
        s3.addBar(new BarModel(3.3f, 0xFFCDA67F));

        StackedBarModel s4 = new StackedBarModel("목");
        s4.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s4.addBar(new BarModel(2.f, 0xFF56B7F1));
        s4.addBar(new BarModel(3.3f, 0xFFCDA67F));

        StackedBarModel s5 = new StackedBarModel("금");
        s5.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s5.addBar(new BarModel(2.f, 0xFF56B7F1));
        s5.addBar(new BarModel(3.3f, 0xFFCDA67F));

        StackedBarModel s6 = new StackedBarModel("토");
        s6.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s6.addBar(new BarModel(2.f, 0xFF56B7F1));
        s6.addBar(new BarModel(3.3f, 0xFFCDA67F));

        StackedBarModel s7 = new StackedBarModel("일");
        s7.addBar(new BarModel(2.3f, 0xFF63CBB0));
        s7.addBar(new BarModel(2.f, 0xFF56B7F1));
        s7.addBar(new BarModel(3.3f, 0xFFCDA67F));

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
