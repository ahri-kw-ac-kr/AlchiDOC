package com.example.jms.home.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    View view;
    TextView titleWeek;
    TextView avgT;
    TextView WeekDayLux;
    TextView WeekNightLux;
    TextView WeekNightTemp;
    TextView weekLightPlan1, weekLightPlan2;
    ImageView face1, face2;
    TextView state1,state2;
    String titleW;

    //constructor
    public WeekLight(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.week_light, container, false);
        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);

        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);

        //000님의 0월 0주차
        titleWeek = view.findViewById(R.id.weekLightDate);
        if(pos == 0){ titleW = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차"; }
        else{ titleW = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차"; }
        titleWeek.setText(titleW);

        //조도량 구하는
        avgT = view.findViewById(R.id.weekLightPercent); // 상단 퍼센트
        WeekDayLux = view.findViewById(R.id.weeklux1); //주 - 주간 평균 조도량 9~18
        WeekNightLux = view.findViewById(R.id.weeklux3); //주 - 야간 평균 조도량 18~21
        WeekNightTemp = view.findViewById(R.id.weekK2);
        Log.e("WeekLight",thisDay+"");
        avgT.setText("조도량  "+ user.getStatLight().getWeekPercent() +"%");

        WeekDayLux.setText(user.getStatLight().getWeekSunAvg()+"");
        WeekNightLux.setText(user.getStatLight().getWeekMoonLuxAvg()+"");
        WeekNightTemp.setText(user.getStatLight().getWeekMoonTempAvg()/3+"");


        String[] str = {"일","월","화","수","목","금","토",};
        for(int i=0; i<7; i++){

            if (user.getStatLight().getWeekSumDay()[i]>6000) {
                //이 시간대 평균 조도량이 6000K를 넘을 경우
                mBarChart.addBar(new BarModel(str[i], user.getStatLight().getWeekSumDay()[i], Color.parseColor("#d84315")));

            }

            else if (3000 < user.getStatLight().getWeekSumDay()[i] || user.getStatLight().getWeekSumDay()[i] <= 6000) {
                //이 시간대 평균 조도량이 3000~6000K
                mBarChart.addBar(new BarModel(str[i], user.getStatLight().getWeekSumDay()[i], Color.parseColor("#fb8c00")));
            }

            else if(user.getStatLight().getWeekSumDay()[i] < 3000) {
                //이 시간대 평균 조도량이 3000 미만
                mBarChart.addBar(new BarModel(str[i], user.getStatLight().getWeekSumDay()[i], Color.parseColor("#fb8c00")));
            }

            else{
                //이하밖에없겠지...?
                mBarChart.addBar(new BarModel(str[i], user.getStatLight().getWeekSumDay()[i], Color.parseColor("#5F9919")));
            }

        }

        mBarChart.startAnimation();

        //////////코멘트///////////
        weekLightPlan1 = (TextView) view.findViewById(R.id.weekLightPlan1);
        weekLightPlan2 = (TextView) view.findViewById(R.id.weekLightPlan2);
        face1 = (ImageView) view.findViewById(R.id.weekLightFace1);
        face2 = (ImageView) view.findViewById(R.id.weekLightFace2);
        state1 = (TextView) view.findViewById(R.id.weekLightState1);
        state2 = (TextView) view.findViewById(R.id.weekLightState2);

        if(user.getWeekList().size() == 0){

        }
        else {
            int sun = 0;
            int moonLux = 0;
            int moonTemp = 0;

            for (int i = 0; i < thisDay; i++) {
                sun += user.getStatLight().getWeekSumSun()[i];
                moonLux += user.getStatLight().getWeekSumMoonLux()[i];
                moonTemp += user.getStatLight().getWeekSumMoonTemp()[i];
            }
            //주간코멘트
            if (sun >= 60000) {
                //충분
                weekLightPlan1.setText(R.string.weekLightComment1);
                face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state1.setText(R.string.good);
            } else {
                //부족
                weekLightPlan1.setText(R.string.weekLightComment2);
                face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state1.setText(R.string.bad);
            }

            //야간코멘트
            if (moonLux <= 400 || moonTemp < 2500) {
                //적정
                weekLightPlan2.setText(R.string.weekLightComment4);
                face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state2.setText(R.string.good);
            } else {
                //과다
                weekLightPlan2.setText(R.string.weekLightComment3);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.exceed);
            }
        }

        return view;
    }
}
