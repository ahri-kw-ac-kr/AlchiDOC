package com.example.jms.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* foreground service 참고링크
* https://www.youtube.com/watch?v=rxK_M9VfX2o
* https://www.youtube.com/watch?v=FbpD5RZtbCc
* https://snowdeer.github.io/android/2016/06/14/android-foreground-service-notification-customization/
* http://snowdeer.github.io/android/2018/08/02/android-foreground-service-after-oreo/
* */
public class SleepActivity extends AppCompatActivity {

    Button sleepEnd;
    String startTime, endTime;
    Long calcTime;
    Chronometer chronometer;
    long stopTime = 0;

    public static String start;
    public static String end;


    APIViewModel apiViewModel = new APIViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    BleViewModel bleViewModel = new BleViewModel();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_activity); //승은이가 올려준 액티비티가 나오도록.

        //Calendar calendar = Calendar.getInstance();

        UserDataModel.contextP = getApplicationContext();
        sharedPreferences = getSharedPreferences("sleep",0);
        editor = sharedPreferences.edit();

        sleepEnd =  findViewById(R.id.sleep_end);
        chronometer = findViewById(R.id.ellapse);

        Intent intent = new Intent(this, SleepService.class);
        intent.setAction("startForeground"); //포그라운드 서비스 실행
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else { startService(intent); }

        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();

        Date sDate = new Date(System.currentTimeMillis());

        //calendar.setTime(sDate);
        //calendar.add(Calendar.HOUR,8);

        startTime = simpleDateFormat.format(sDate);
        start = startTime;
        Log.d("SleepActivity - start",startTime);

        sleepEnd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {

                Date eDate = new Date(System.currentTimeMillis());
                endTime = simpleDateFormat.format(eDate);
                end = endTime;
                Log.d("SleepActivity - end", endTime);
                chronometer.stop();

                editor.putString("sleepTime",start);
                editor.putString("wakeTime",end);
                editor.commit();

                try {
                    //초단위로 계산되는 시간 차이
                    calcTime = (simpleDateFormat.parse(endTime).getTime()-simpleDateFormat.parse(startTime).getTime())/1000;
                    Log.e("SleepActivity",calcTime+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (calcTime < 3600 ) {
                    Toast.makeText(getApplicationContext(), "30분 미만의 수면은 기록되지 않습니다.", Toast.LENGTH_LONG).show();
                    stopService(intent);
                    Intent intent2 = new Intent(SleepActivity.this, MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    finish();
                }
                //calctime이 30분 이상일경우
                else{
                    stopService(intent);
                    Intent intent1 = new Intent(SleepActivity.this, TransitionPage.class);
                    startActivity(intent1);
                    finish();
                }


                /*밑에 세줄 사용하니까 재시작됨 그래서 주석처리해뒀음 */
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //    startForegroundService(intent);
                //} else { startService(intent); }

            }//onClick
        });//ClickListener

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Date eDate = new Date(System.currentTimeMillis());
                endTime = simpleDateFormat.format(eDate);
                end = endTime;
                Log.d("SleepActivity - end", endTime);
                chronometer.stop();

                editor.putString("sleepTime", start);
                editor.putString("wakeTime", end);
                editor.commit();

                stopService(intent);
                Intent intent1 = new Intent(SleepActivity.this, TransitionPage.class);
                startActivity(intent1);
                finish();
            }
        }, 28800000);
    }//onCreate



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }//onBackPressed

}//class


