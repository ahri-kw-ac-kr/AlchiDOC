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
    int percent;
    String titleD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_sleep, container, false);
        LineChart lineChart = (LineChart) view.findViewById(R.id.chart);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.daySleepDate);
        try {
            if (pos == 0) {
                titleD = RestfulAPI.principalUser.getFullname() + "님의 " + user.getSleepDTOList().get(0).getWakeTime().substring(4, 6) + "월 " + user.getSleepDTOList().get(0).getWakeTime().substring(6, 8) + "일";
            } else {
                titleD = RestfulAPI.principalUser.getFriend().get(pos - 1).getFullname() + "님의 " + user.getSleepDTOList().get(0).getWakeTime().substring(4, 6) + "월 " + user.getSleepDTOList().get(0).getWakeTime().substring(6, 8) + "일";
            }
        } catch (Exception e) {
            if (pos == 0) {
                titleD = RestfulAPI.principalUser.getFullname() + "님의 0월 0일";
            } else {
                titleD = RestfulAPI.principalUser.getFriend().get(pos - 1).getFullname() + "님의 0월 0일";
            }
        }
        titleDay.setText(titleD);

        // 수면효율 00%
        titlePercent = (TextView) view.findViewById(R.id.daySleepPercent);
        percent = user.getStatSleep().getPercentDay();
        String dayP = "수면효율 " + percent + "%";
        titlePercent.setText(dayP);

        //총수면시간
        totalH = (TextView) view.findViewById(R.id.daySleepTotalHour);
        totalM = (TextView) view.findViewById(R.id.daySleepTotalMinute);
        try {
            int to = user.getSleepDTOList().get(0).getTotal(); //총 수면시간
            int toH = to / 60; //총수면시간 시
            int toM = to % 60; //총수면시간 분
            totalH.setText("" + toH);
            totalM.setText("" + toM);
        } catch (Exception e) {
            totalH.setText("0");
            totalM.setText("0");
        }

        //깊은수면
        deepH = (TextView) view.findViewById(R.id.daySleepDeepHour);
        deepM = (TextView) view.findViewById(R.id.daySleepDeepMinute);
        try {
            int de = user.getSleepDTOList().get(0).getDeep();
            int deH = de / 60;
            int deM = de % 60;
            deepH.setText("" + deH);
            deepM.setText("" + deM);
        } catch (Exception e) {
            deepH.setText("0");
            deepM.setText("0");
        }

        //얕은수면
        lightH = (TextView) view.findViewById(R.id.daySleepLightHour);
        lightM = (TextView) view.findViewById(R.id.daySleepLightMinute);
        try {
            int li = user.getSleepDTOList().get(0).getLight();
            int liH = li / 60;
            int liM = li % 60;
            lightH.setText("" + liH);
            lightM.setText("" + liM);
        } catch (Exception e) {
            lightH.setText("0");
            lightM.setText("0");
        }

        //깨어남
        wake = (TextView) view.findViewById(R.id.daySleepWake);
        try {
            wake.setText("" + user.getSleepDTOList().get(0).getWake());
        } catch (Exception e) {
            wake.setText("0");
        }


        //뒤척임
        turn = (TextView) view.findViewById(R.id.daySleepTurn);
        try {
            turn.setText("" + user.getSleepDTOList().get(0).getTurn());
        } catch (Exception e) {
            turn.setText("0");
        }


        //그래프
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            if (user.getSleepDTOList().get(0).getLevel() != null) {
                String[] levels = user.getSleepDTOList().get(0).getLevel().split(",");
                entries.add(new Entry(5, 0));
                for (int i = 0; i < levels.length; i++) {
                    entries.add(new Entry(Integer.parseInt(levels[i]), i+1));
                }
                entries.add(new Entry(5, levels.length+1));

            /*entries.add(new Entry(4f, 0));
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
        entries.add(new Entry(16f, 14));*/

                LineDataSet dataset = new LineDataSet(entries, "");

                ArrayList<String> labels = new ArrayList<String>();
                labels.add(user.getSleepDTOList().get(0).getSleepTime().substring(9,14));//잠든시간 HH:mm
                for (int i = 0; i < levels.length; i++) {
                    labels.add("");
                }
                labels.add(user.getSleepDTOList().get(0).getWakeTime().substring(9,14));//깬시간 HH:mm

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
                yAxisLeft.setAxisMaxValue(5);
                yAxisLeft.setAxisMinValue(0);

                YAxis yAxisRight = lineChart.getAxisRight();
                yAxisRight.setDrawLabels(false);
                yAxisRight.setDrawAxisLine(false);
                yAxisRight.setDrawGridLines(false);

                lineChart.setDescription(null);

                Legend legend = lineChart.getLegend();
                legend.setEnabled(false);

                lineChart.setData(data);
                lineChart.animateY(5000);
            }
        }catch (Exception e){ }



        ////////////////////////코멘트//////////////////////
        if(user.getSleepDTOList().size() == 0){////측정데이터 없음



        }
        else {/////측정데이터 존재
            int deepPercent = 0;
            try {
                deepPercent = (int) (((double) (user.getStatSleep().getDeep() / user.getStatSleep().getTotal())) * 100);
            } catch (Exception e) {
            }

            //////////////////////////////////총 수면시간 코멘트////////////////////////
            if (percent >= 80) {// 수면효율 정상
                if (deepPercent >= 25) {//깊은잠 정상

                } else {//깊은잠 부족

                }
            } else {//수면효율 비정상
                if (deepPercent >= 25) {//깊은잠 정상

                } else {//깊은잠 부족

                }
            }

            ///////////////////////////////평균뒤척임 코멘트///////////////////////////
            if (user.getStatSleep().getTurnHour() == 0) {//정상

            }
            else if(user.getStatSleep().getTurnHour() == 1){//주의

            }
            else if(user.getStatSleep().getTurnHour() == 2){//관리필요

            }
        }

            return view;
    }
}
