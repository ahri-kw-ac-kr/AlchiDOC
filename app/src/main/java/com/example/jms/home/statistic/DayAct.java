package com.example.jms.home.statistic;

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
import com.example.jms.home.UserDataModel;

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
    String titleD;

    public DayAct(){}

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_act, container, false);
        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.dayActDate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        titleDay.setText(titleD);

        // 활동량 00%(무조건 기준은 주간)
        titlePercent = (TextView) view.findViewById(R.id.dayActPercent);
        percent = user.getStatAct().getDayPercent();
        String dayP = "활동량 " + percent + "%";
        titlePercent.setText(dayP);

        //걸음수
        totalT = (TextView) view.findViewById(R.id.dayActTotal);
        totalT.setText(""+user.getStatAct().getDaySum());

        //칼로리
        kalT = (TextView) view.findViewById(R.id.dayActKal);
        kalT.setText(""+user.getStatAct().getDayKal());

        //그래프
        for (int i = 9; i < 21; i++) { mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatAct().getDaySumHour()[i], Color.parseColor("#5F9919"))); }
        mBarChart.startAnimation();


        //코멘트
        if (percent < 100) {
            //부족
        } else {
            //충분
        }

        return view;
    }
}
