package com.example.jms.home.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DaySleep extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_sleep, container, false);


        LineChart lineChart = (LineChart) view.findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(10f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));
        entries.add(new Entry(16f, 6));
        entries.add(new Entry(12f, 7));
        entries.add(new Entry(4f, 8));
        entries.add(new Entry(8f, 9));
        entries.add(new Entry(6f, 10));
        entries.add(new Entry(10f, 11));
        entries.add(new Entry(18f, 12));
        entries.add(new Entry(9f, 13));
        entries.add(new Entry(16f, 14));

        LineDataSet dataset = new LineDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("11시");
        labels.add("");
        labels.add("12시");
        labels.add("");
        labels.add("1시");
        labels.add("");
        labels.add("2시");
        labels.add("");
        labels.add("3시");
        labels.add("");
        labels.add("4시");
        labels.add("");
        labels.add("5시");
        labels.add("");
        labels.add("6시");

        LineData data = new LineData(labels, dataset);
        dataset.setDrawValues(false);
        dataset.setDrawCircles(false);
        dataset.setColor(Color.parseColor("#7610C0"));

        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        dataset.setFillColor(Color.parseColor("#EBD8FB"));
        dataset.setLineWidth(3f);

        /* 점 찍기
        dataset.setCircleColor(Color.parseColor("#7610C0"));
        dataset.setCircleColorHole(Color.parseColor("#7610C0"));
        dataset.setCircleSize(3f); */

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.parseColor("#707070"));

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        lineChart.setDescription(null);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        lineChart.setData(data);
        lineChart.animateY(5000);

        return view;
    }
}
