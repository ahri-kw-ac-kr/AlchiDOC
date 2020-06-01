package com.example.jms.home.statistic;

import android.annotation.SuppressLint;
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
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.home.UserDataModel;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;


public class WeekSleep extends Fragment {

    public WeekSleep(){}
    View view;

    int percent;
    int totalH;
    int totalM;
    int deepH;
    int deepM;
    int lightH;
    int lightM;
    String titleD;

    TextView titleDay;
    TextView titlePercent;
    TextView totalHT;
    TextView totalMT;
    TextView deepHT;
    TextView deepMT;
    TextView lightHT;
    TextView lightMT;
    TextView wakeT;
    TextView turnT;


    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.week_sleep, container, false);
        StackedBarChart mStackedBarChart = (StackedBarChart) view.findViewById(R.id.stackedbarchart);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];
        List<SleepDTO> weekList = user.getStatSleep().getWeekList();

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HHmm");
        String curr = transFormat.format(calendar.getTime());
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);

        ////////변수 초기화
        Integer[] totalSum = {0,0,0,0,0,0,0,0,0,0};
        Integer[] deepSum = {0,0,0,0,0,0,0,0,0,0};
        Integer[] lightSum = {0,0,0,0,0,0,0,0,0,0};
        Integer[] wakeSum = {0,0,0,0,0,0,0,0,0,0};
        Integer[] turnSum = {0,0,0,0,0,0,0,0,0,0};
        Integer[] turnHourSum = {0,0,0,0,0,0,0,0,0,0};
        int total = 0;
        int deep = 0;
        int light = 0;
        int wake = 0;
        int turn = 0;
        int turnHour = 0;

        /////////////////////주간통계//////////////////
        for(int i=0; i<weekList.size(); i++){
            totalSum[i] = weekList.get(i).getTotal();
            deepSum[i] = weekList.get(i).getDeep();
            lightSum[i] = weekList.get(i).getLight();
            wakeSum[i] = weekList.get(i).getWake();
            turnSum[i] = weekList.get(i).getTurn();
            turnHourSum[i] = weekList.get(i).getTurnHour();
        }

        for(int i=0; i<weekList.size(); i++){
            total += totalSum[i];
            deep += deepSum[i];
            light += lightSum[i];
            wake += wakeSum[i];
            turn += turnSum[i];
            turnHour += turnHourSum[i];
        }

        int totalA = total/thisDay;
        int deepA = deep/thisDay;
        int lightA = light/thisDay;
        int wakeA = wake/thisDay;
        int turnA = turn/thisDay;
        int turnHourA = turnHour/thisDay;
        percent = (int)(((totalA-((wakeA*10.0)+turnA))/totalA)*100);
        totalH = totalA / 60; //총수면시간 시
        totalM = totalA % 60; //총수면시간 분
        deepH = deepA / 60;
        deepM = deepA%60;
        lightH = lightA/60;
        lightM = lightA%60;


        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.weekSleepTitle);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        titleDay.setText(titleD);

        ///퍼센트
        titlePercent = (TextView)view.findViewById(R.id.weekSleepTitleP);
        String strP = "평균 수면량 "+percent+"%";
        titlePercent.setText(strP);

        //수면량 기록 카드뷰
        totalHT = (TextView)view.findViewById(R.id.weekSleepTotalH);
        totalMT = (TextView)view.findViewById(R.id.weekSleepTotalM);
        deepHT = (TextView)view.findViewById(R.id.weekSleepDeepH);
        deepMT = (TextView)view.findViewById(R.id.weekSleepDeepM);
        lightHT = (TextView)view.findViewById(R.id.weekSleepLightH);
        lightMT = (TextView)view.findViewById(R.id.weekSleepLightM);
        wakeT = (TextView)view.findViewById(R.id.weekSleepWake);
        turnT = (TextView)view.findViewById(R.id.weekSleepTurn);

        totalHT.setText(""+totalH);
        totalMT.setText(""+totalM);
        deepHT.setText(""+deepH);
        deepMT.setText(""+deepM);
        lightHT.setText(""+lightH);
        lightMT.setText(""+lightM);
        wakeT.setText(""+wakeA);
        turnT.setText(""+turnA);



        ///////그래프///////
        StackedBarModel[] data = new StackedBarModel[7];
        String[] day = {"일","월","화","수","목","금","토"};
        for(int i=0; i<7; i++){
            data[i] = new StackedBarModel(day[i]);
            data[i].addBar(new BarModel(deepSum[i], Color.parseColor("#7851C3")));
            data[i].addBar(new BarModel(lightSum[i], Color.parseColor("#AC89EB")));
            data[i].addBar(new BarModel(wakeSum[i], Color.parseColor("#D7B6F9")));
        }

        /*
        StackedBarModel s1 = new StackedBarModel("월");
        s1.addBar(new BarModel(21, Color.parseColor("#7851C3")));
        s1.addBar(new BarModel(60, Color.parseColor("#AC89EB")));
        s1.addBar(new BarModel(19, Color.parseColor("#D7B6F9")));

        StackedBarModel s2 = new StackedBarModel("화");
        s2.addBar(new BarModel(10, Color.parseColor("#7851C3")));
        s2.addBar(new BarModel(25, Color.parseColor("#AC89EB")));
        s2.addBar(new BarModel(22, Color.parseColor("#D7B6F9")));

        StackedBarModel s3 = new StackedBarModel("수");
        s3.addBar(new BarModel(35, Color.parseColor("#7851C3")));
        s3.addBar(new BarModel(27, Color.parseColor("#AC89EB")));
        s3.addBar(new BarModel(38, Color.parseColor("#D7B6F9")));

        StackedBarModel s4 = new StackedBarModel("목");
        s4.addBar(new BarModel(21, Color.parseColor("#7851C3")));
        s4.addBar(new BarModel(60, Color.parseColor("#AC89EB")));
        s4.addBar(new BarModel(19, Color.parseColor("#D7B6F9")));

        StackedBarModel s5 = new StackedBarModel("금");
        s5.addBar(new BarModel(35, Color.parseColor("#7851C3")));
        s5.addBar(new BarModel(27, Color.parseColor("#AC89EB")));
        s5.addBar(new BarModel(38, Color.parseColor("#D7B6F9")));

        StackedBarModel s6 = new StackedBarModel("토");
        s6.addBar(new BarModel(21, Color.parseColor("#7851C3")));
        s6.addBar(new BarModel(60, Color.parseColor("#AC89EB")));
        s6.addBar(new BarModel(19, Color.parseColor("#D7B6F9")));

        StackedBarModel s7 = new StackedBarModel("일");
        s7.addBar(new BarModel(35, Color.parseColor("#7851C3")));
        s7.addBar(new BarModel(27, Color.parseColor("#AC89EB")));
        s7.addBar(new BarModel(38, Color.parseColor("#D7B6F9")));

         */
        for(int i=0; i<7; i++){ mStackedBarChart.addBar(data[i]); }
        mStackedBarChart.startAnimation();


        /////////코멘트///////
        int deepPercent = 0;
        try{deepPercent = (int) (((double) (deepA / totalA)) * 100);}catch (Exception e){}


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
        if (Math.round(turnHourA) == 0) {//정상

        }
        else if(Math.round(turnHourA) == 1){//주의

        }
        else if(Math.round(turnHourA) == 2){//관리필요

        }


        return view;
    }
}
