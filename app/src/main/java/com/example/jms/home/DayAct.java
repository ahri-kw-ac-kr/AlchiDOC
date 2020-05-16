package com.example.jms.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;


public class DayAct extends Fragment {

    View view;
    public DayAct(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_act, container, false);


        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);

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


        return view;
    }
}
