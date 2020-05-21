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


public class WeekLight extends Fragment {

    public WeekLight(){}
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.week_light, container, false);


        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);

        mBarChart.addBar(new BarModel("월", 0.5f, Color.parseColor("#ffd54f")));
        mBarChart.addBar(new BarModel("화", 0.8f, Color.parseColor("#ffd54f")));
        mBarChart.addBar(new BarModel("수", 1.2f, Color.parseColor("#fb8c00")));
        mBarChart.addBar(new BarModel("목", 2.1f, Color.parseColor("#fb8c00")));
        mBarChart.addBar(new BarModel("금", 3.3f, Color.parseColor("#d84315")));
        mBarChart.addBar(new BarModel("토", 2.4f, Color.parseColor("#fb8c00")));
        mBarChart.addBar(new BarModel("일", 3.1f, Color.parseColor("#d84315")));

        mBarChart.startAnimation();

        return view;
    }
}
