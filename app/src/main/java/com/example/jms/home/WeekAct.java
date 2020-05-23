package com.example.jms.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class WeekAct extends Fragment {

    View view;

    TextView titleDay;
    TextView titlePercent;
    TextView avgT;
    TextView avgK;

    public WeekAct(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.week_act, container, false);

        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);

        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];


        //합산 준비
        Integer[] sumDay = {0, 0, 0, 0, 0, 0, 0};
        Integer[][] day = new Integer[7][];
        for (int i = 0; i < user.getPerHour().size(); i++) {
            day[i] = new Integer[150];
            for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                day[i][j] = (int) user.getPerHour().get(i).get(j).getSteps();
                Log.d("WeekAct", "i: " + i + ", j: " + j + ", day: " + day[i][j]);
                sumDay[i] += day[i][j];
            }
            Log.d("WeekAct", i + ", " + sumDay[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.weekActDate);
        String titleD = user.getDataList().get(0).getUser().getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일";
        titleDay.setText(titleD);


        mBarChart.addBar(new BarModel("월", 0.5f, Color.parseColor("#CAEBA2")));
        mBarChart.addBar(new BarModel("화", 0.8f, Color.parseColor("#CAEBA2")));
        mBarChart.addBar(new BarModel("수", 1.2f, Color.parseColor("#8CCA45")));
        mBarChart.addBar(new BarModel("목", 2.1f, Color.parseColor("#8CCA45")));
        mBarChart.addBar(new BarModel("금", 3.3f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("토", 2.4f, Color.parseColor("#8CCA45")));
        mBarChart.addBar(new BarModel("일", 3.1f, Color.parseColor("#5F9919")));

        mBarChart.startAnimation();

        return view;

    }

}
