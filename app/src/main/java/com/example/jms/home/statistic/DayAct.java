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

    TextView dayActPlan1;
    TextView dayActPlan2;
    TextView dayActPlan3;
    TextView titleDay;
    TextView titlePercent;
    TextView totalT;
    TextView kalT;
    View view;
    int percent;
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
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HHmm");
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

        dayActPlan1 = (TextView) view.findViewById(R.id.dayActPlan1);
        dayActPlan2 = (TextView) view.findViewById(R.id.dayActPlan2);
        dayActPlan3 = (TextView) view.findViewById(R.id.dayActPlan3);

        //주간 코멘트
        if (percent < 100) {
            //부족
            dayActPlan1.setText(R.string.dayActComment2);
        } else {
            //충분
            dayActPlan1.setText(R.string.dayActComment1);
        }

        int currHour = Integer.parseInt(curr.substring(9,11)); //현재 시
        int dinnerFlag = 0;
        int eveningFlag = 0;

        //나 = 사용자
        if(pos == 0){
            int setHour4 = Integer.parseInt(RestfulAPI.principalUser.getSleep().substring(0,2))-4; // 취침 4시간 전 시
            int setHour2 = Integer.parseInt(RestfulAPI.principalUser.getSleep().substring(0,2))-2; // 취침 2시간 전 시
            //나의 저녁 코멘트
            if(Integer.parseInt(curr.substring(9))>=Integer.parseInt(RestfulAPI.principalUser.getSleep())-400)
            {
                for(int i=setHour4-1; i<currHour; i++){
                    for(int j=0; j<user.getPerHour().get(i).size(); j++){
                        if(user.getPerHour().get(i).get(j).getSteps()>400){
                            dinnerFlag = 1;
                            break; }
                    }
                    if(dinnerFlag == 1){ break; }
                }
                if(dinnerFlag == 1){
                    //////////////////////저녁 과다
                    dayActPlan2.setText(R.string.dayActComment3);
                }
                else{
                    ///////////////////////저녁 적정
                    dayActPlan2.setText(R.string.dayActComment4);
                }
            }
            else{ dayActPlan2.setText("\n"); }
            //나의 야간 코멘트
            if(Integer.parseInt(curr.substring(9))>=Integer.parseInt(RestfulAPI.principalUser.getSleep())-200)
            {
                for(int i=setHour2-1; i<currHour; i++){
                    for(int j=0; j<user.getPerHour().get(i).size(); j++){
                        if(user.getPerHour().get(i).get(j).getSteps()>200){
                            eveningFlag = 1;
                            break; }
                    }
                    if(eveningFlag == 1){ break; }
                }
                if(eveningFlag == 1){
                    ////////////////////야간 과다
                    dayActPlan3.setText(R.string.dayActComment5);
                }
                else{
                    //////////////////////야간 적정
                    dayActPlan3.setText(R.string.dayActComment6);
                }
            }
            else{ dayActPlan3.setText("\n"); }
        }
        else{ //친구코멘트
            int setHour4 = Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos-1).getSleep().substring(0,2))-4; // 취침 4시간 전 시
            int setHour2 = Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos-1).getSleep().substring(0,2))-2; // 취침 2시간 전 시
            //친구의 저녁 코멘트
            if(Integer.parseInt(curr.substring(9))>=Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos-1).getSleep())-400)
            {
                for(int i=setHour4-1; i<currHour; i++){
                    for(int j=0; j<user.getPerHour().get(i).size(); j++){
                        if(user.getPerHour().get(i).get(j).getSteps()>400){
                            dinnerFlag = 1;
                            break; }
                    }
                    if(dinnerFlag == 1){ break; }
                }
                if(dinnerFlag == 1){
                    //////////////////저녁 과다
                    dayActPlan2.setText(R.string.dayActComment3);
                }
                else{
                    //////////////////저녁 적정
                    dayActPlan2.setText(R.string.dayActComment4);
                }
            }
            else{ dayActPlan2.setText("\n"); }
            //친구의 야간 코멘트
            if(Integer.parseInt(curr.substring(9))>=Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos-1).getSleep())-200)
             {
                for(int i=setHour2-1; i<currHour; i++){
                    for(int j=0; j<user.getPerHour().get(i).size(); j++){
                        if(user.getPerHour().get(i).get(j).getSteps()>200){
                            eveningFlag = 1;
                            break; }
                    }
                    if(eveningFlag == 1){ break; }
                }
                if(eveningFlag == 1){
                    /////////////////야간 과다
                    dayActPlan3.setText(R.string.dayActComment5);
                }
                else{
                    /////////////////야간 적정
                    dayActPlan3.setText(R.string.dayActComment6);
                }
            }
            else{ dayActPlan3.setText("\n"); }
        }
        return view;
    }
}
