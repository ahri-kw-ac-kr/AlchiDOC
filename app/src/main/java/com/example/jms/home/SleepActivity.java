package com.example.jms.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;

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
    Chronometer chronometer;
    long stopTime = 0;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_activity); //승은이가 올려준 액티비티가 나오도록.

        sleepEnd = (Button) findViewById(R.id.sleep_end);
        chronometer = (Chronometer) findViewById(R.id.ellapse);
        Intent intent = new Intent(this, SleepService.class);
        intent.setAction("startForeground"); //포그라운드 서비스 실행

        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();

        Date sDate = new Date(System.currentTimeMillis());
        startTime = simpleDateFormat.format(sDate);
        Log.e("SleepActivity - start",startTime);

        sleepEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date eDate = new Date(System.currentTimeMillis());
                endTime = simpleDateFormat.format(eDate);
                Log.e("SleepActivity - end",endTime);
                chronometer.stop();
                stopService(intent);
                Intent intent = new Intent(SleepActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }

        else {
            startService(intent);

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
