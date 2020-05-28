package com.example.jms.home;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;


public class DayAct extends Fragment {

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 dd일");
    TextView titleDay;
    TextView titlePercent;
    TextView totalT;
    TextView kalT;
    View view;
    double percent;

    public DayAct(){}

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_act, container, false);

        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);

        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        if(user.getTodayList()==null){
            try { user.parsingDay(pos); } catch (ParseException e) { e.printStackTrace(); }
        }

        //else {
        //합산 준비
        Integer[] sumHour = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hour = new Integer[24][];
        for (int i = 0; i < user.getPerHour().size(); i++) {
            hour[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                hour[i][j] = (int) user.getPerHour().get(i).get(j).getSteps();
                Log.d("DayAct", "i: " + i + ", j: " + j + ", hour: " + hour[i][j]);
                sumHour[i] += hour[i][j];
            }
            Log.d("DayAct", i + ", " + sumHour[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.dayActDate);
        String titleD = user.getDataList().get(0).getUser().getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일";
        titleDay.setText(titleD);

        //주간/야간 활동량 00%
        titlePercent = (TextView) view.findViewById(R.id.dayActPercent);

        //걸음수 구하는 곳
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumHour).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        totalT = (TextView) view.findViewById(R.id.dayActTotal);
        totalT.setText(total.toString());

        //칼로리 구하는 곳
        int kal = total.intValue() / 30;
        kalT = (TextView) view.findViewById(R.id.dayActKal);
        kalT.setText(Integer.toString(kal));

        //주간일때
        /*if (9 <= Integer.parseInt(curr.substring(9, 11)) || Integer.parseInt(curr.substring(9, 11)) < 18) {
            int sumD = 0;
            for (int i = 9; i < 21; i++) {
                if (9 <= i && i < 18) {//주간이므로 여기를 진하게
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
                    sumD += sumHour[i];
                } else {//지금은 주간인데 야간이니까 여기를 연하게인데 생각해보니까 0이니까 안나올듯
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
                }
            }
            percent = Math.round((sumD / 6000.0) * 100);
            String dayP = "주간 활동량 " + percent + "%";
            titlePercent.setText(dayP);
        }
        //야간일때
        else if (18 <= Integer.parseInt(curr.substring(9, 11)) || Integer.parseInt(curr.substring(9, 11)) < 21) {
            int sumD = 0;
            for (int i = 9; i < 21; i++) {
                if (9 <= i && i < 18) {//지금은 야간인데 이건 주간이니까 연하게
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
                } else {//야간이므로 여기를 진하게
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
                    sumD += sumHour[i];
                }
            }
            percent = (sumD / 2000.0) * 100;
            String dayP = "야간 활동량 " + percent + "%";
            titlePercent.setText(dayP);
        }*/
        //주간도 야간도 아닌 여기는 그냥 모두 진하게
        //else {
            for (int i = 9; i < 21; i++) {
                mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
            }
            //Log.d("DayAct","토탈밸류 소수: "+total.doubleValue());
            percent = (total.doubleValue() / 6000.0) * 100;
            String dayP = "활동량 " + percent + "%";
            titlePercent.setText(dayP);
        //}

        mBarChart.startAnimation();

        if (percent < 100) {
            //부족
        } else {
            //충분
        }
        //}
        return view;
    }
}
