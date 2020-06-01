package com.example.jms.home;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.jms.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Report extends AppCompatActivity{

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 dd일 달성률");
    TextView mTextView;
    TextView reportActSun, reportActMoon;
    TextView reportSleep1, reportSleep2;
    ImageView face1, face2, face3, face4;
    TextView state1, state2, state3, state4;

    ArcProgress act;
    ArcProgress sleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText(getTime());

        //사용자 정의
        UserDataModel user = UserDataModel.userDataModels[0];

        ////////////////////////////////활동량///////////////////////////////
        //활동량 그래프
        act = (ArcProgress) findViewById(R.id.arc_progress2);
        act.setProgress(user.getStatAct().getDayPercent());

        reportActSun = (TextView) findViewById(R.id.reportActSun);
        reportActMoon = (TextView) findViewById(R.id.reportActMoon);
        reportSleep1 = (TextView) findViewById(R.id.reportSleep1);
        reportSleep2 = (TextView) findViewById(R.id.reportSleep2);
        face1 = (ImageView) findViewById(R.id.face1);
        face2 = (ImageView) findViewById(R.id.face2);
        face3 = (ImageView) findViewById(R.id.face3);
        face4 = (ImageView) findViewById(R.id.face4);
        state1 = (TextView) findViewById(R.id.state1);
        state2 = (TextView) findViewById(R.id.state2);
        state3 = (TextView) findViewById(R.id.state3);
        state4 = (TextView) findViewById(R.id.state4);

        //활동량 코멘트
        int actSun = 0;
        int actMoon = 0;
        for(int i=9; i<18; i++){ actSun += user.getStatAct().getDaySumHour()[i]; }
        for(int i=18; i<21; i++){ actMoon += user.getStatAct().getDaySumHour()[i]; }

        if(actSun >= 6000){
            ///주간 충분
            reportActSun.setText(R.string.dayActComment1);
            face3.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
            state3.setText(R.string.good);
        }
        else{
            ///주간 부족
            reportActSun.setText(R.string.dayActComment2);
            face3.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            state3.setText(R.string.bad);
        }
        if(actMoon <= 2000){
            ///야간 적정
            reportActMoon.setText(R.string.dayActComment4);
            face4.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
            state4.setText(R.string.good);
        }
        else{
            ///야간 과다
            reportActMoon.setText(R.string.dayActComment3);
            face4.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            state4.setText(R.string.good);
        }

        ////////////////////////////////조도량///////////////////////////////


        ////////////////////////////////수면량///////////////////////////////
        //수면량 그래프
        sleep = (ArcProgress) findViewById(R.id.arc_progress3);
        sleep.setProgress(user.getStatSleep().getPercentDay());

        if(user.getSleepDTOList().size() == 0){////측정데이터 없음
            reportSleep1.setText("\n");
            reportSleep2.setText("\n");
        }
        else {/////측정데이터 존재
            int deepPercent = 0;
            try { deepPercent = (int) ((((double)user.getStatSleep().getDeep() /(double) user.getStatSleep().getTotal())) * 100); } catch (Exception e) { }

            //////////////////////////////////총 수면시간 코멘트////////////////////////
            if (user.getStatSleep().getPercentDay() >= 80) {// 수면효율 정상
                if (deepPercent >= 25) {//깊은잠 정상
                    reportSleep1.setText(R.string.daySleepComment2);
                    face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                    state1.setText(R.string.sleepState1);
                } else {//깊은잠 부족
                    reportSleep1.setText(R.string.daySleepComment1);
                    face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state1.setText(R.string.sleepState2);
                }
            } else {//수면효율 비정상
                if (deepPercent >= 25) {//깊은잠 정상
                    reportSleep1.setText(R.string.daySleepComment4);
                    face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state1.setText(R.string.sleepState2);
                } else {//깊은잠 부족
                    reportSleep1.setText(R.string.daySleepComment3);
                    face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state1.setText(R.string.sleepState2);
                }
            }

            ///////////////////////////////평균뒤척임 코멘트///////////////////////////
            if (user.getStatSleep().getTurnHour() == 0) {//정상
                reportSleep2.setText(R.string.daySleepComment5);
                face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state2.setText(R.string.sleepState1);
            }
            else if(user.getStatSleep().getTurnHour() == 1){//주의
                reportSleep2.setText(R.string.daySleepComment6);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.sleepState2);
            }
            else if(user.getStatSleep().getTurnHour() == 2){//관리필요
                reportSleep2.setText(R.string.daySleepComment7);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.sleepState3);
            }
        }

    }

    ///현재시간///
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
