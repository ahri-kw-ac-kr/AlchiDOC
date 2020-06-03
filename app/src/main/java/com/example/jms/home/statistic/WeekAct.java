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
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;

public class WeekAct extends Fragment {

    ImageView face1, face2;
    TextView state1, state2;
    View view;
    TextView weekActPlan1;
    TextView weekActPlan2;
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

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("WeekAct","몇번째 요일: "+thisDay);

        //000님의 0월 0주차
        titleDay = (TextView) view.findViewById(R.id.weekActDate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + thisWeek + "주차"; }
        titleDay.setText(titleD);

        //평균 활동량 00%
        titlePercent = (TextView) view.findViewById(R.id.weekActPercent);
        int percent = user.getStatAct().getWeekPercent();
        String dayP = "평균 활동량 " + percent + "%";
        titlePercent.setText(dayP);

        //걸음수
        avgT = (TextView) view.findViewById(R.id.weekActAvgS);
        avgT.setText(""+user.getStatAct().getWeekAvg());

        //칼로리
        avgK = (TextView) view.findViewById(R.id.weekActAvgK);
        avgK.setText(""+user.getStatAct().getWeekKal());

        //그래프
        String[] str = {"일","월","화","수","목","금","토"};
        for(int i=0; i<7; i++){
            if(user.getStatAct().getWeekSumSun()[i]<6000 && user.getStatAct().getWeekSumMoon()[i]<2000){ //주간부족, 야간적정 -> 부족
                mBarChart.addBar(new BarModel(str[i], user.getStatAct().getWeekSumDay()[i], Color.parseColor("#CAEBA2")));
                Log.d("weekAct",i+" 주간: "+user.getStatAct().getWeekSumSun()[i]+", 야간:"+user.getStatAct().getWeekSumMoon()[i]);
            }
            else if(user.getStatAct().getWeekSumSun()[i]>=6000 && user.getStatAct().getWeekSumMoon()[i]<2000){ //주간충분, 야간적정 -> 충분
                mBarChart.addBar(new BarModel(str[i], user.getStatAct().getWeekSumDay()[i], Color.parseColor("#8CCA45")));
                Log.d("weekAct",i+" 주간: "+user.getStatAct().getWeekSumSun()[i]+", 야간:"+user.getStatAct().getWeekSumMoon()[i]);
            }
            else if(user.getStatAct().getWeekSumSun()[i]>=6000 && user.getStatAct().getWeekSumMoon()[i]>=2000){ //주간충분, 야간과다 -> 과다
                mBarChart.addBar(new BarModel(str[i], user.getStatAct().getWeekSumDay()[i], Color.parseColor("#5F9919")));
                Log.d("weekAct",i+" 주간: "+user.getStatAct().getWeekSumSun()[i]+", 야간:"+user.getStatAct().getWeekSumMoon()[i]);
            }
            else if(user.getStatAct().getWeekSumSun()[i]<6000 && user.getStatAct().getWeekSumMoon()[i]>=2000){ //주간부족, 야간과다 -> 부족
                mBarChart.addBar(new BarModel(str[i], user.getStatAct().getWeekSumDay()[i], Color.parseColor("#CAEBA2")));
                Log.d("weekAct",i+" 주간: "+user.getStatAct().getWeekSumSun()[i]+", 야간:"+user.getStatAct().getWeekSumMoon()[i]);
            }
        }
        mBarChart.startAnimation();

        //////////코멘트///////////
        weekActPlan1 = (TextView) view.findViewById(R.id.weekActPlan1);
        weekActPlan2 = (TextView) view.findViewById(R.id.weekActPlan2);
        face1 = (ImageView) view.findViewById(R.id.face1);
        face2 = (ImageView) view.findViewById(R.id.face2);
        state1 = (TextView) view.findViewById(R.id.state1);
        state2 = (TextView) view.findViewById(R.id.state2);

        if(user.getWeekList().size()==0){//데이터 없음


        }
        else {
            int sun = 0;
            int moon = 0;
            for (int i = 0; i < thisDay; i++) {
                sun += user.getStatAct().getWeekSumSun()[i];
                moon += user.getStatAct().getWeekSumMoon()[i];
            }
            //주간코멘트
            if (sun >= 6000) {
                //충분
                weekActPlan1.setText(R.string.weekActComment1);
                face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state1.setText(R.string.good);
            } else {
                //부족
                weekActPlan1.setText(R.string.weekActComment2);
                face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state1.setText(R.string.bad);
            }

            //야간코멘트
            if (moon <= 2000) {
                //적정
                weekActPlan2.setText(R.string.weekActComment4);
                face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state2.setText(R.string.good);
            } else {
                //과다
                weekActPlan2.setText(R.string.weekActComment3);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.exceed);
            }
        }
        return view;
    }
}
