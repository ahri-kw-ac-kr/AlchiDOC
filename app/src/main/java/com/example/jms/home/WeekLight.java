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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;


public class WeekLight extends Fragment {

    TextView titleWeek;
    TextView avgT;

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

        Integer[] sumWeekTemp = {0, 0, 0, 0, 0, 0, 0};
        Integer[][] weekTemp = new Integer[7][];

        for (int i = 0; i < user.getPerDay().size(); i++) {

            weekLux[i] = new Integer[150]; // 어째서 150칸짜리지
            weekTemp[i] = new Integer[150]; // 어째서 150칸짜리지

            for (int j = 0; j < user.getPerDay().get(i).size(); j++) {

                weekLux[i][j] = (int) user.getPerDay().get(i).get(j).getAvgLux();
                weekTemp[i][j] = (int) user.getPerDay().get(i).get(j).getAvgTemp();

                Log.d("WeekAct", "i: " + i + ", j: " + j + ", day: " + weekLux[i][j]);
                Log.d("WeekAct", "i: " + i + ", j: " + j + ", day: " + weekTemp[i][j]);

                sumWeekLux[i] += weekLux[i][j];
                sumWeekTemp[i] += weekTemp[i][j];

            }

            Log.d("WeekAct", i + ", " + sumWeekLux[i]);//합산 잘 되었는지 확인
            Log.d("WeekAct", i + ", " + sumWeekTemp[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);


        //000님의 0월 0주차
        titleWeek = (TextView) view.findViewById(R.id.weekLightPercent);
        String titleW = user.getDataList().get(0).getUser().getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차";
        titleWeek.setText(titleW);

        //조도량 구하는 곳
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumWeekLux).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);

        int avg = Math.round(total.intValue()/7)/60000*100;
        avgT = (TextView) view.findViewById(R.id.weekLightPercent);
        avgT.setText("조도량  "+Integer.toString(avg)+"%");

        String[] str = {"월","화","수","목","금","토","일"};
        for(int i=0; i<7; i++){

            if (sumWeekTemp[i]>6000) {
                //시간당 조도량이 6000K를 넘을 경우
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#d84315")));

            }

            else if (3000 < sumWeekTemp[i] || sumWeekTemp[i]<= 6000) {
                //시간당 조도량이 3000~6000K
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#fb8c00")));
            }

            else if(sumWeekTemp[i]<3000) {
                //시간당 조도량이 3000 미만
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#fb8c00")));
            }

            else{
                //이하밖에없겠지...?
                mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#5F9919")));
            }
            //mBarChart.addBar(new BarModel(str[i], sumWeekLux[i], Color.parseColor("#CAEBA2")));
        }
        //평균 활동량 00%
        titleWeek = (TextView) view.findViewById(R.id.weekLightPercent);
        int percent = avg / 6000 * 100;
        String weekP = "평균 활동량 " + percent + "%";
        titleWeek.setText(weekP);

        mBarChart.startAnimation();

        return view;
    }
}
