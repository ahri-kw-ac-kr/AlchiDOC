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
    int dL,nL,nK = 0; //dayLinght eveningLight eveningK nightLight nightK

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
        String titleW = user.getDataList().get(0).getUser().getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차";
        titleWeek.setText(titleW);

        //조도량 구하는
        avgT = view.findViewById(R.id.weekLightPercent); // 상단 퍼센트
        WeekDayLux = view.findViewById(R.id.weeklux1); //주 - 주간 평균 조도량 9~18
        WeekNightLux = view.findViewById(R.id.weeklux3); //주 - 야간 평균 조도량 18~21
        WeekNightTemp = view.findViewById(R.id.weekK2);
        avgT.setText("조도량  "+ user.getStatLight().getWeekAvg()/thisDay +"%");

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

        return view;
    }
}
