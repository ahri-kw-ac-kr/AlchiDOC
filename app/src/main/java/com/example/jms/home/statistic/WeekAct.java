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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;


public class WeekAct extends Fragment {

    View view;

    TextView titleDay;
    TextView titlePercent;
    TextView avgT;
    TextView avgK;
    String titleD;

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
        Integer[] sumDayMor = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDin = {0, 0, 0, 0, 0, 0, 0};
        Integer[][] day = new Integer[7][];
        for (int i = 0; i < user.getPerDay().size(); i++) {
            day[i] = new Integer[150];
            for (int j = 0; j < user.getPerDay().get(i).size(); j++) {
                day[i][j] = (int) user.getPerDay().get(i).get(j).getSteps();
                Log.d("WeekAct", "i: " + i + ", j: " + j + ", day: " + day[i][j]);
                if(j>=9 && j<18){sumDayMor[i]+=day[i][j];}
                if(j>=18 && j<21){sumDayDin[i]+=day[i][j];}
                sumDay[i] += day[i][j];
            }
            Log.d("WeekAct", i + ", " + sumDay[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("WeekAct","몇번째 요일: "+thisDay);


        //000님의 0월 0주차
        titleDay = (TextView) view.findViewById(R.id.weekActDate);
        if(pos == 0){
            titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차";
        }
        else{
            titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차";
        }
        titleDay.setText(titleD);

        //걸음수 구하는 곳
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumDay).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);

        int avg = Math.round(total.intValue()/thisDay);
        avgT = (TextView) view.findViewById(R.id.weekActAvgS);
        avgT.setText(Integer.toString(avg));

        //칼로리 구하는 곳
        int kal = avg / 30;
        avgK = (TextView) view.findViewById(R.id.weekActAvgK);
        avgK.setText(Integer.toString(kal));

        String[] str = {"일","월","화","수","목","금","토"};
        for(int i=0; i<7; i++){
            if(sumDayMor[i]<6000 && sumDayDin[i]<2000){ //주간부족, 야간적정 -> 부족
                mBarChart.addBar(new BarModel(str[i], sumDay[i], Color.parseColor("#CAEBA2")));
            }
            if(sumDayMor[i]>=6000 && sumDayDin[i]<2000){ //주간충분, 야간적정 -> 충분
                mBarChart.addBar(new BarModel(str[i], sumDay[i], Color.parseColor("#8CCA45")));
            }
            if(sumDayMor[i]>=6000 && sumDayDin[i]>=2000){ //주간충분, 야간과다 -> 과다
                mBarChart.addBar(new BarModel(str[i], sumDay[i], Color.parseColor("#5F9919")));
            }
        }

        //평균 활동량 00%
        titlePercent = (TextView) view.findViewById(R.id.weekActPercent);
        double percent = Math.round(avg / 6000.0 * 100);
        String dayP = "평균 활동량 " + percent + "%";
        titlePercent.setText(dayP);
        /*
        mBarChart.addBar(new BarModel("월", 0.5f, Color.parseColor("#CAEBA2")));
        mBarChart.addBar(new BarModel("화", 0.8f, Color.parseColor("#CAEBA2")));
        mBarChart.addBar(new BarModel("수", 1.2f, Color.parseColor("#8CCA45")));
        mBarChart.addBar(new BarModel("목", 2.1f, Color.parseColor("#8CCA45")));
        mBarChart.addBar(new BarModel("금", 3.3f, Color.parseColor("#5F9919")));
        mBarChart.addBar(new BarModel("토", 2.4f, Color.parseColor("#8CCA45")));
        mBarChart.addBar(new BarModel("일", 3.1f, Color.parseColor("#5F9919")));*/

        mBarChart.startAnimation();

        return view;

    }

}
