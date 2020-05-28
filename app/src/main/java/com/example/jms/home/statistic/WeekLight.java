package com.example.jms.home.statistic;

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
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.home.UserDataModel;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class WeekLight extends Fragment {

    TextView titleWeek;
    TextView avgT;

    int dL,eL,eK,nL,nK = 0;

    String titleW;

    public WeekLight(){}
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.week_light, container, false);
        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);

        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //합산 준비
        Integer[] sumWeekLux = {0, 0, 0, 0, 0, 0, 0};
        Integer[][] weekLux = new Integer[7][];
        Integer[][] weekDayLux = new Integer[7][];
        Integer[][] weekEvenLux = new Integer[7][];
        Integer[][] weekNightLux = new Integer[7][];

        Integer[] sumWeekTemp = {0, 0, 0, 0, 0, 0, 0};
        Integer[][] weekTemp = new Integer[7][];
        Integer[][] weekDayTemp = new Integer[7][];
        Integer[][] weekEvenTemp = new Integer[7][];
        Integer[][] weekNightTemp = new Integer[7][];

        for (int i = 0; i < user.getPerDay().size(); i++) {

            weekLux[i] = new Integer[150];
            weekDayLux[i] = new Integer[150];
            weekTemp[i] = new Integer[150];


            for (int j = 0; j < user.getPerDay().get(i).size(); j++) {

                weekLux[i][j] = (int) user.getPerDay().get(i).get(j).getAvgLux();
                weekTemp[i][j] = (int) user.getPerDay().get(i).get(j).getAvgTemp();

                Log.d("WeekLightLux", "i: " + i + ", j: " + j + ", day: " + weekLux[i][j]);
                Log.d("WeekLightTemp", "i: " + i + ", j: " + j + ", day: " + weekTemp[i][j]);

                sumWeekLux[i] += weekLux[i][j]/user.getPerDay().get(i).size(); //size로 나눠서 하루동안 평균...
                sumWeekTemp[i] += weekTemp[i][j]/user.getPerDay().get(i).size(); //size로 나눠서 하루동안 평균..
            }

            Log.d("WeekLightLuxSum", i + ", " + sumWeekLux[i]);//합산 잘 되었는지 확인
            Log.d("WeekLightTempSum", i + ", " + sumWeekTemp[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);


        //000님의 0월 0주차
        titleWeek = (TextView) view.findViewById(R.id.weekLightDate);
        if(pos == 0){
            titleW = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차";
        }
        else{
            titleW = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차";
        }titleWeek.setText(titleW);

        //조도량 구하는
        //Log.e("total week",  ", " + total);//합산 잘 되었는지 확인
        //int avg = Math.round(total.intValue()/7)/60000*100;
        //Log.e("total week",  ", " + avg);//합산 잘 되었는지 확인
        avgT = (TextView) view.findViewById(R.id.weekLightPercent); // 상단 퍼센트
        avgT.setText("조도량  "+ sumWeekLux[thisWeek]/60000.0 +"%");



        String[] str = {"월","화","수","목","금","토","일"};
        for(int i=0; i<7; i++){

            if (sumWeekTemp[i]>6000) {
                //이 시간대 평균 조도량이 6000K를 넘을 경우
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#d84315")));

            }

            else if (3000 < sumWeekTemp[i] || sumWeekTemp[i]<= 6000) {
                //이 시간대 평균 조도량이 3000~6000K
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#fb8c00")));
            }

            else if(sumWeekTemp[i]<3000) {
                //이 시간대 평균 조도량이 3000 미만
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#fb8c00")));
            }

            else{
                //이하밖에없겠지...?
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#5F9919")));
            }

        }

        mBarChart.startAnimation();

        return view;
    }
}
