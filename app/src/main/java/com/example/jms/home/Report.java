package com.example.jms.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.home.statistic.StatSleep;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Report extends AppCompatActivity{

    APIViewModel apiViewModel = new APIViewModel();

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 dd일 달성률");
    ImageView backButton, frontButton;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    TextView mTextView;
    TextView reportActSun, reportActMoon;
    TextView reportLightSun, reportLightMoon;
    TextView reportSleep1, reportSleep2;
    ImageView face1, face2, face3, face4, face5, face6;
    TextView state1, state2, state3, state4, state5, state6;

    ArcProgress act;
    ArcProgress sleep;

    UserDataModel user = new UserDataModel();

    //현재시간
    Calendar calendar = Calendar.getInstance(Locale.KOREA);
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HHmm");
    String curr = transFormat.format(calendar.getTime());

    @SuppressLint("CheckResult")
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

        //시간
        Date date = new Date();
        try {
            date = transFormat.parse(curr);
        } catch (ParseException e) {
        }

        ////////////////////////////초기 : 어제//////////////////////////
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);
        Log.e("Report!",calendar+"");
        String yestserday = transFormat.format(calendar.getTime()).substring(0, 8) + " 00:00:00";
        mTextView = (TextView) findViewById(R.id.textView);
        String dateT = yestserday.substring(0,4)+"년 "+yestserday.substring(4,6)+"월 "+yestserday.substring(6,8)+"일 달성률";
        mTextView.setText(dateT); //파싱해서 세팅해줍니다.

        backButton = findViewById(R.id.goBack);
        frontButton = findViewById(R.id.goFront);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //표시되는 날짜를 하루 줄이는 것이 필요

            }
        });
        frontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //표시되는 날짜를 하루 늘리기
                //if()
            }
        });
        this.InitializeView();
        this.InitializeListener();



        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", yestserday, curr.substring(0, 8) + " 00:00:00")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.getContent() != null) {
                        user.setDataList(data.getContent());
                        user.parsingHour(-1, user.getDataList());
                        apiViewModel.getSleepByPeriod(RestfulAPI.principalUser.getId(), "0", yestserday, curr.substring(0, 8) + " 00:00:00")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(data2 -> {
                                    if (data.getContent() != null) {
                                        user.setSleepDTOList(data2.getContent());
                                        user.setStatSleep(new StatSleep());
                                        user.getStatSleep().parsing(user.getSleepDTOList());
                                        concon();
                                    }
                                });
                    }
                });


        //여기서부터 캘린더에서 고른날짜 받아와서 아래에 있는 changeDay 함수 쓰기.




    }//onCreate

    @SuppressLint("CheckResult")
    ////startDay는 캘린더에서 고른 날짜, finishDay는 그 다음 날짜, 날짜 형식은 무조건 yyyymmdd HH:mm:ss 로 해주기.
    public void changeDay(String startDay, String finishDay){
        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", startDay, finishDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.getContent() != null) {
                        user.setDataList(data.getContent());
                        user.parsingHour(-1, user.getDataList());
                        apiViewModel.getSleepByPeriod(RestfulAPI.principalUser.getId(), "0", startDay, finishDay)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(data2 -> {
                                    if (data.getContent() != null) {
                                        user.setSleepDTOList(data2.getContent());
                                        user.setStatSleep(new StatSleep());
                                        user.getStatSleep().parsing(user.getSleepDTOList());
                                        concon();
                                    }
                                });
                    }
                });
    }

    public void concon(){///내용물
        ////////////////////////////////활동량///////////////////////////////
        //활동량 그래프
        act = (ArcProgress) findViewById(R.id.arc_progress2);
        act.setProgress(user.getStatAct().getDayPercent());

        reportActSun = (TextView) findViewById(R.id.reportActSun);
        reportActMoon = (TextView) findViewById(R.id.reportActMoon);
        reportLightSun = (TextView) findViewById(R.id.reportLightSun);
        reportLightMoon = (TextView) findViewById(R.id.reportLightMoon);
        reportSleep1 = (TextView) findViewById(R.id.reportSleep1);
        reportSleep2 = (TextView) findViewById(R.id.reportSleep2);
        face1 = (ImageView) findViewById(R.id.face1);
        face2 = (ImageView) findViewById(R.id.face2);
        face3 = (ImageView) findViewById(R.id.face3);
        face4 = (ImageView) findViewById(R.id.face4);
        face5 = (ImageView) findViewById(R.id.face5);
        face6 = (ImageView) findViewById(R.id.face6);
        state1 = (TextView) findViewById(R.id.state1);
        state2 = (TextView) findViewById(R.id.state2);
        state3 = (TextView) findViewById(R.id.state3);
        state4 = (TextView) findViewById(R.id.state4);
        state5 = (TextView) findViewById(R.id.state5);
        state6 = (TextView) findViewById(R.id.state6);

        //활동량 코멘트
        int actSun = 0;
        int actMoon = 0;
        for(int i=9; i<18; i++){ actSun += user.getStatAct().getDaySumHour()[i]; }
        for(int i=18; i<21; i++){ actMoon += user.getStatAct().getDaySumHour()[i]; }

        if(user.getTodayList() == null){   }//데이터 없음
        else {//데이터 있음
            if (actSun >= 6000) {
                ///주간 충분
                reportActSun.setText(R.string.dayActComment1);
                face3.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state3.setText(R.string.good);
            } else {
                ///주간 부족
                reportActSun.setText(R.string.dayActComment2);
                face3.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state3.setText(R.string.bad);
            }
        }
        if(user.getTodayList()== null){    }//데이터 없음
        else {//데이터 있음
            if (actMoon <= 2000) {
                ///야간 적정
                reportActMoon.setText(R.string.dayActComment4);
                face4.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state4.setText(R.string.good);
            } else {
                ///야간 과다
                reportActMoon.setText(R.string.dayActComment3);
                face4.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state4.setText(R.string.good);
            }
        }

        ////////////////////////////////조도량///////////////////////////////

        act = (ArcProgress) findViewById(R.id.arc_progress1);
        act.setProgress(user.getStatLight().getDayPercent());

        int lightSun = 0;
        int lightMoon = 0;

        //가중치 먹인거
        for(int i=9; i<18; i++){ lightSun += user.getStatLight().getDaySumHourLuxWgt()[i]; }
        for(int i=18; i<21; i++){ lightMoon += user.getStatLight().getDaySumHourLuxWgt()[i]; }

        if(user.getTodayList()== null) {   }//데이터 없음
        else{
            if (lightSun >= 6000) {
                ///주간 충분
                reportLightSun.setText(R.string.dayLightComment1);
                face5.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state5.setText(R.string.good);
            } else {
                ///주간 부족
                reportLightSun.setText(R.string.dayLightComment2);
                face5.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state5.setText(R.string.bad);
            }
        }
        if(user.getTodayList()== null) {    }//데이터 없음
        else {
            if (lightMoon <= 2000) {
                ///야간 적정
                reportLightMoon.setText(R.string.dayLightComment4);
                face6.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state6.setText(R.string.good);
            } else {
                ///야간 과다
                reportLightMoon.setText(R.string.dayLightComment3);
                face6.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state6.setText(R.string.good);
            }
        }


        ////////////////////////////////수면량///////////////////////////////
        //수면량 그래프
        sleep = (ArcProgress) findViewById(R.id.arc_progress3);
        //sleep.setProgress(user.getStatSleep().getPercentDay());
        //Log.d("레포트",user.getSleepDTOList().get(0).getWakeTime().substring(0, 8));

        if(user.getSleepDTOList().size() == 0){////측정데이터 없음
            //reportSleep1.setText("\n");
            //reportSleep2.setText("\n");
            //sleep.setProgress(0);
        }
        else {/////측정데이터 존재
            sleep.setProgress(user.getStatSleep().getPercentDay());
            int deepPercent = 0;
            try {
                deepPercent = (int) ((((double) user.getStatSleep().getDeep() / (double) user.getStatSleep().getTotal())) * 100);
            } catch (Exception e) {
            }

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
            } else if (user.getStatSleep().getTurnHour() == 1) {//주의
                reportSleep2.setText(R.string.daySleepComment6);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.sleepState2);
            } else if (user.getStatSleep().getTurnHour() == 2) {//관리필요
                reportSleep2.setText(R.string.daySleepComment7);
                face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state2.setText(R.string.sleepState3);
            }
        }
    }
    public void InitializeView(){mTextView = (TextView) findViewById(R.id.textView);}
    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                Log.e("Report",""+year+":"+monthOfYear+":"+dayOfMonth);
                mTextView.setText(year + "년" + (monthOfYear+1) + "월" + dayOfMonth + "일 달성률");
            }
        };
    }

    final Calendar c = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);

        dialog.show();
    }

    ///현재시간///
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
