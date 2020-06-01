package com.example.jms.home.statistic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.home.UserDataModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    TextView weekSleepPlan1, weekSleepPlan2;
    ImageView face1, face2;
    TextView state1, state2;

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.week_sleep, container, false);
        StackedBarChart mStackedBarChart = (StackedBarChart) view.findViewById(R.id.stackedbarchart);
        //BarChart mStackedBarChart = (BarChart) view.findViewById(R.id.stackedbarchart);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];
        List<SleepDTO> weekList = user.getStatSleep().getWeekList();

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HHmm");
        String curr = transFormat.format(calendar.getTime());
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);

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

        int size = weekList.size()-1;
        /////////////////////주간통계//////////////////
        for(int i=0; i<weekList.size(); i++){
            totalSum[i] = weekList.get(size-i).getTotal();
            deepSum[i] = weekList.get(size-i).getDeep();
            lightSum[i] = weekList.get(size-i).getLight();
            wakeSum[i] = weekList.get(size-i).getWake();
            turnSum[i] = weekList.get(size-i).getTurn();
            turnHourSum[i] = weekList.get(size-i).getTurnHour();
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


        //000님의 0월 0주차
        titleDay = (TextView) view.findViewById(R.id.weekSleepTitle);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차"; }
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
        //int[] colorArray = new int[]{Color.parseColor("#7851C3"),Color.parseColor("#AC89EB"),Color.parseColor("#D7B6F9")};
        //ArrayList<BarEntry> dataSet = new ArrayList<>();
        StackedBarModel[] data = new StackedBarModel[7];
        String[] day = {"일","월","화","수","목","금","토"};
        for(int i=0; i<7; i++){
            data[i] = new StackedBarModel(day[i]);
            data[i].addBar(new BarModel(deepSum[i], Color.parseColor("#7851C3")));
            data[i].addBar(new BarModel(lightSum[i], Color.parseColor("#AC89EB")));
            data[i].addBar(new BarModel(wakeSum[i], Color.parseColor("#D7B6F9")));
            //dataSet.add(new BarEntry(0,new float[]{deepSum[i],lightSum[i],wakeSum[i]}));
            //dataSet.add(new BarEntry(0,new float[]{deepSum[i],lightSum[i],wakeSum[i]}));
        }
/*
        BarDataSet barDataSet = new BarDataSet(dataSet,"");
        barDataSet.setColors(colorArray);

        BarData barData = new BarData(barDataSet);
        mStackedBarChart.setData(barData);
        mStackedBarChart.animateY(3000);
*/


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
        weekSleepPlan1 = (TextView) view.findViewById(R.id.weekSleepPlan1);
        weekSleepPlan2 = (TextView) view.findViewById(R.id.weekSleepPlan2);
        face1 = (ImageView) view.findViewById(R.id.face1);
        face2 = (ImageView) view.findViewById(R.id.face2);
        state1 = (TextView) view.findViewById(R.id.state1);
        state2 = (TextView) view.findViewById(R.id.state2);
        int deepPercent = 0;
        try{deepPercent = (int) ((((double)deepA / (double)totalA)) * 100);}catch (Exception e){}

        Log.d("WeekSleep","weekList 길이 : "+user.getStatSleep().getWeekList().size()+", pos: "+pos);
        //////////////////////////////////총 수면시간 코멘트////////////////////////
        if(user.getStatSleep().getWeekList().size() == 0){///데이터 없음

        }
        else {
            if (percent >= 80) {// 수면효율 정상
                if (deepPercent >= 25) {//깊은잠 정상
                    weekSleepPlan1.setText(R.string.weekSleepComment2);
                    face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                    state1.setText(R.string.sleepState1);
                } else {//깊은잠 부족
                    weekSleepPlan1.setText(R.string.weekSleepComment1);
                    face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state1.setText(R.string.sleepState2);
                }
            } else {//수면효율 비정상
                if (deepPercent >= 25) {//깊은잠 정상
                    weekSleepPlan1.setText(R.string.weekSleepComment4);
                    face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state1.setText(R.string.sleepState2);
                } else {//깊은잠 부족
                    weekSleepPlan1.setText(R.string.weekSleepComment3);
                    face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state1.setText(R.string.sleepState2);
                }
            }

            ///////////////////////////////평균뒤척임 코멘트///////////////////////////
            if (Math.round(turnHourA) == 0) {//정상
                weekSleepPlan2.setText(R.string.weekSleepComment5);
                face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state2.setText(R.string.sleepState1);
            } else if (Math.round(turnHourA) == 1) {//주의
                weekSleepPlan2.setText(R.string.weekSleepComment6);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.sleepState2);
            } else if (Math.round(turnHourA) == 2) {//관리필요
                weekSleepPlan2.setText(R.string.weekSleepComment7);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.sleepState3);
            }

        }
        return view;
    }
}
