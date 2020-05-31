package com.example.jms.home.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.home.UserDataModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DaySleep extends Fragment {

    View view;

    TextView titleDay;
    TextView titlePercent;
    TextView totalH;
    TextView totalM;
    TextView deepH;
    TextView deepM;
    TextView lightH;
    TextView lightM;
    TextView wake;
    TextView turn;
    double percent;
    String titleD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_sleep, container, false);
        LineChart lineChart = (LineChart) view.findViewById(R.id.chart);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.daySleepDate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        titleDay.setText(titleD);

        // 수면효율 00%
        titlePercent = (TextView) view.findViewById(R.id.daySleepPercent);
        percent = user.getStatSleep().getPercentDay();
        String dayP = "수면효율 " + percent + "%";
        titlePercent.setText(dayP);

        //총수면시간
        totalH = (TextView)view.findViewById(R.id.daySleepTotalHour);
        totalM = (TextView)view.findViewById(R.id.daySleepTotalMinute);


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
