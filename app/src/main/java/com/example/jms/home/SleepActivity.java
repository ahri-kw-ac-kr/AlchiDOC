package com.example.jms.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;

/*
* foreground service 참고링크
* https://www.youtube.com/watch?v=rxK_M9VfX2o
* https://www.youtube.com/watch?v=FbpD5RZtbCc
* https://snowdeer.github.io/android/2016/06/14/android-foreground-service-notification-customization/
* http://snowdeer.github.io/android/2018/08/02/android-foreground-service-after-oreo/
* */
public class SleepActivity extends AppCompatActivity {

    Button sleepEnd;
    Chronometer chronometer;
    long stopTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_activity); //승은이가 올려준 액티비티가 나오도록.


        chronometer = (Chronometer) findViewById(R.id.chronometer);
        sleepEnd = (Button) findViewById(R.id.sleep_end);
        Intent intent = new Intent(this, SleepService.class);
        intent.setAction("startForeground");

        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();

        sleepEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chronometer.setBase(SystemClock.elapsedRealtime());
                stopTime = 0;
                chronometer.stop();
                stopService(intent);
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
}
