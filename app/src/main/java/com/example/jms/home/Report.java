package com.example.jms.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    int trigger; //datepicker에 한번이라도 접근했을 경우를 확인하기 위한 변수
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 dd일 달성률");
    ImageView backButton, frontButton;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    int setyear,setmon,setday;

    TextView mTextView;
    TextView reportActSun, reportActMoon;
    TextView reportLightSun, reportLightMoon;
    TextView reportSleep1, reportSleep2;
    ImageView face1, face2, face3, face4, face5, face6;
    TextView state1, state2, state3, state4, state5, state6;

    ArcProgress act;
    ArcProgress light;
    ArcProgress sleep;

    UserDataModel user = new UserDataModel();

    //현재시간
    Calendar calendar = Calendar.getInstance(Locale.KOREA);
    Calendar calendar2 = Calendar.getInstance(Locale.KOREA);
    Calendar sCalendar = Calendar.getInstance(Locale.KOREA);
    Calendar fCalendar = Calendar.getInstance(Locale.KOREA);
    Calendar nCalendar = Calendar.getInstance(Locale.KOREA); //현재시간만 가르키는 캘린더. 건드리면 안됩니다
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
        calendar2.setTime(date);


        String yesterday = transFormat.format(calendar.getTime()).substring(0, 8) + " 00:00:00";
        mTextView = (TextView) findViewById(R.id.textView);
        String dateT = yesterday.substring(0,4)+"년 "+yesterday.substring(4,6)+"월 "+yesterday.substring(6,8)+"일 달성률";
        mTextView.setText(dateT); //파싱해서 세팅해줍니다.

        backButton = findViewById(R.id.goBack);
        frontButton = findViewById(R.id.goFront);

        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", yesterday, curr.substring(0, 8) + " 00:00:00")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.getContent() != null) {
                        user.setDataList(data.getContent());
                        user.parsingHour(-1, user.getDataList());
                        apiViewModel.getSleepByPeriod(RestfulAPI.principalUser.getId(), "0", yesterday, curr.substring(0, 8) + " 00:00:00")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(data2 -> {
                                    if (data.getContent() != null) {
                                        user.setSleepDTOList(data2.getContent());
                                        user.setStatSleep(new StatSleep());
                                        user.getStatSleep().parsing(user.getSleepDTOList());
                                        concon();
                                    }
                                },Throwable::printStackTrace);
                    }
                },Throwable::printStackTrace);


        //여기서부터 캘린더에서 고른날짜 받아와서 아래에 있는 changeDay 함수 쓰기.

        //버튼 우선 선언만 해둠
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //표시되는 날짜를 하루 줄이는 것이 필요
                if (trigger>0) { //만약 datepick dialogue에 한번이라도 접근했을 경우
                    sCalendar.add(Calendar.DATE,-1);
                    fCalendar.add(Calendar.DATE,-1);
                    String dayBefore = transFormat.format(sCalendar.getTime()).substring(0, 8) + " 00:00:00";
                    String dayBefore2 = transFormat.format(fCalendar.getTime()).substring(0, 8) + " 00:00:00";
                    mTextView.setText(transFormat.format(sCalendar.getTime()).substring(0,4)+"년 "+transFormat.format(sCalendar.getTime()).substring(4,6)+"월 "+transFormat.format(sCalendar.getTime()).substring(6,8)+"일 달성률");
                    Log.e("Report1", "" + dayBefore+"~"+dayBefore2);
                    changeDay(dayBefore,dayBefore2);
                }
                else{
                    //우선 현재 날짜를 cal2에 저장하고 calendar를 하나 빼준다,
                    calendar.add(Calendar.DATE,-1);
                    calendar2.add(Calendar.DATE,-1);
                    String dayBefore = transFormat.format(calendar.getTime()).substring(0, 8) + " 00:00:00";
                    String dayBefore2 = transFormat.format(calendar2.getTime()).substring(0, 8) + " 00:00:00";
                    if(calendar.compareTo(nCalendar)>0){
                        Toast.makeText(getApplicationContext(), "오늘 이후의 리포트는 열람할 수 없습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        mTextView.setText(transFormat.format(calendar.getTime()).substring(0,4)+"년 "+transFormat.format(calendar.getTime()).substring(4,6)+"월 "+transFormat.format(calendar.getTime()).substring(6,8)+"일 달성률");
                        Log.e("Report2", "" + dayBefore+"~"+dayBefore2);
                        changeDay(dayBefore,dayBefore2);}


                }
            }
        });
        frontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (trigger>0) {//만약 datepick dialogue에 한번이라도 접근했을 경우
                    sCalendar.add(Calendar.DATE,1);
                    fCalendar.add(Calendar.DATE,1);
                    String dayAfter = transFormat.format(sCalendar.getTime()).substring(0, 8) + " 00:00:00";
                    String dayAfter2 = transFormat.format(fCalendar.getTime()).substring(0, 8) + " 00:00:00";
                    mTextView.setText(transFormat.format(sCalendar.getTime()).substring(0,4)+"년 "+transFormat.format(sCalendar.getTime()).substring(4,6)+"월 "+transFormat.format(sCalendar.getTime()).substring(6,8)+"일 달성률");
                    Log.e("Report3", "" + dayAfter+"~"+dayAfter2);
                    changeDay(dayAfter,dayAfter2);
                }
                else{
                    calendar.add(Calendar.DATE,1);
                    calendar2.add(Calendar.DATE,1);
                    String dayAfter = transFormat.format(calendar.getTime()).substring(0, 8) + " 00:00:00";
                    String dayAfter2 = transFormat.format(calendar2.getTime()).substring(0, 8) + " 00:00:00";
                    if(calendar.compareTo(nCalendar)>0){
                        Toast.makeText(getApplicationContext(), "오늘 이후의 리포트는 열람할 수 없습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        mTextView.setText(transFormat.format(calendar.getTime()).substring(0,4)+"년 "+transFormat.format(calendar.getTime()).substring(4,6)+"월 "+transFormat.format(calendar.getTime()).substring(6,8)+"일 달성률");
                        Log.e("Report4", "" + dayAfter+"~"+dayAfter2);
                        changeDay(dayAfter,dayAfter2);}

                }
            }
        });

        //다이얼로그 뷰, 리스너 선언
        this.InitializeView();
        this.InitializeListener();

    }//onCreate

    @SuppressLint("CheckResult")
    ////startDay는 캘린더에서 고른 날짜, finishDay는 그 다음 날짜, 날짜 형식은 무조건 yyyymmdd HH:mm:ss 로 해주기.
    public void changeDay(String startDay, String finishDay){
        user = new UserDataModel();
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
                                },Throwable::printStackTrace);
                    }
                },Throwable::printStackTrace);
    }

    public void concon(){///내용물
        ////////////////////////////////활동량///////////////////////////////
        //활동량 그래프
        act = (ArcProgress) findViewById(R.id.arc_progress2);

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

        if(user.getTodayList().size() == 0){  //데이터 없음
            reportActSun.setText(R.string.noData);
            reportActMoon.setText(R.string.noData);
        }
        else {//데이터 있음
            int percent = user.getStatAct().getDayPercent();
            if(user.getStatAct().getDayPercent() > 100){ percent = 100; }
            act.setProgress(percent);
            for(int i=9; i<18; i++){ actSun += user.getStatAct().getDaySumHour()[i]; }
            for(int i=18; i<21; i++){ actMoon += user.getStatAct().getDaySumHour()[i]; }
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
        if(user.getTodayList().size() == 0){    }//데이터 없음
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
                state4.setText(R.string.exceed);
            }
        }

        ////////////////////////////////조도량///////////////////////////////

        light = (ArcProgress) findViewById(R.id.arc_progress1);

        int lightSun = 0;
        int lightMoon = 0;


        if(user.getTodayList().size() == 0) {  //데이터 없음
            reportLightSun.setText(R.string.noData);
            reportLightMoon.setText(R.string.noData);
        }
        else{
            int percent1 = user.getStatLight().getDayPercent();
            if(percent1 > 100){ percent1 = 100; }
            light.setProgress(percent1);
            //가중치 먹인거
            for(int i=9; i<18; i++){ lightSun += user.getStatLight().getDaySumHourLuxWgt()[i]; }
            for(int i=18; i<21; i++){ lightMoon += user.getStatLight().getDaySumHourLuxWgt()[i]; }
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
        if(user.getTodayList().size() == 0) {    }//데이터 없음
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

        //Log.d("레포트","수면디티오 길이: "+user.getSleepDTOList().size());
        if(user.getSleepDTOList().size() == 0){////측정데이터 없음
            reportSleep1.setText(R.string.noData);
            reportSleep2.setText(R.string.noData);
            sleep.setProgress(0);
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
                trigger +=1;
                //캘린더에서 설정한 날짜를 받아오기.
                setyear= year;
                setmon=monthOfYear;
                setday=dayOfMonth;

                //파싱형식 적용 위해 시작일, 종료일을 캘린더에 등록
                sCalendar.set(setyear,setmon,setday);
                fCalendar.set(setyear,setmon,setday);
                fCalendar.add(Calendar.DATE,1);
                mTextView.setText(transFormat.format(sCalendar.getTime()).substring(0,4)+"년 "+(monthOfYear+1)+"월 "+transFormat.format(sCalendar.getTime()).substring(6,8)+"일 달성률");
                String sday=transFormat.format(sCalendar.getTime()).substring(0, 8) + " 00:00:00";
                String fday=transFormat.format(fCalendar.getTime()).substring(0, 8) + " 00:00:00";

                Log.e("Report",""+sday+fday);
                changeDay(sday,fday);
            }
        };
    }

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);
        dialog.getDatePicker().setMaxDate(nCalendar.getTimeInMillis());
        dialog.show();
    }

    ///현재시간///
    //private String getTime(){
       // mNow = System.currentTimeMillis();
      // mDate = new Date(mNow);
       // return mFormat.format(mDate);
   // }
}
