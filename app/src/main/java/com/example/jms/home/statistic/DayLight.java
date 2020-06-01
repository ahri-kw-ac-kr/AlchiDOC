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

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DayLight extends Fragment {

    TextView titleDay;
    TextView titlePercent;
    TextView totalT;
    TextView daySumLux;
    TextView evenSumLux;
    TextView evenAvgK;
    TextView nightSumLux;
    TextView nightAvgK;

    View view;
    int percent;
    double todayTotal;
    String titleD;

    //constructor
    public DayLight(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.day_light, container, false);
        BarChart mBarChart = view.findViewById(R.id.bar);

        //어떤 사람인지 (카드별로 버튼이 필요함)
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.dayLightDate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        titleDay.setText(titleD);


        //오늘동안의 리스트가 없을경우 트라이캐치 실행
        if(user.getTodayList()==null){
            try { user.parsingDay(pos); } catch (ParseException e) { e.printStackTrace(); }
        }

        String EvenP = Integer.toString(percent);
        daySumLux = view.findViewById(R.id.daylux1) ;
        evenSumLux = view.findViewById(R.id.daylux2);
        nightSumLux = view.findViewById(R.id.daylux3);
        evenAvgK = view.findViewById(R.id.dayK1);
        nightAvgK = view.findViewById(R.id.dayK2);
        totalT = view.findViewById(R.id.dayLightPercent);

        daySumLux.setText(""+user.getStatLight().getDayDaySumLux());
        evenSumLux.setText(""+user.getStatLight().getDayEvenSumLux());
        evenAvgK.setText(""+user.getStatLight().getDayEvenSumTemp()/4);
        nightSumLux.setText(""+user.getStatLight().getDayNightSumLux());
        nightAvgK.setText(""+user.getStatLight().getDayNightSumTemp()/10);
        totalT.setText("조도량 "+user.getStatLight().getDayPercent()+"%");

        //시간 상관없이 Y축높이는 Lux, 그래프 바 색상은 K
        //주간 - 기상 ~ 수면 4시간 전 - Lux총합

        for (int i = 0; i < 23; i++) {
            if (user.getStatLight().getDaySumHourTemp()[i]>6000) {
                //시간당 조도량이 6000K를 넘을 경우
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#d84315")));

            }

            else if (3000 < user.getStatLight().getDaySumHourTemp()[i] || user.getStatLight().getDaySumHourTemp()[i] <= 6000) {
                //시간당 조도량이 3000~6000K
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#fb8c00")));
            }

            else if(user.getStatLight().getDaySumHourTemp()[i]<3000) {
                //시간당 조도량이 3000 미만
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#fb8c00")));
            }

            else{
                //이하밖에없겠지...?
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#5F9919")));
            }

        }
        mBarChart.startAnimation();
        return view;
    }
}

