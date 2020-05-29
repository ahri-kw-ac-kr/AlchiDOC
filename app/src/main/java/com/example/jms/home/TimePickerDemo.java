package com.example.jms.home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;

import java.util.Calendar;

public class TimePickerDemo extends AppCompatActivity implements TimePicker.OnTimeChangedListener {

    TimePicker timePicker;
    Calendar calendar;
    int hourofDay;
    int minute;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_timepicker);

        calendar = Calendar.getInstance();
        timePicker = (TimePicker) findViewById(R.id.tp);

        button1 = (Button) findViewById(R.id.btn) ; //아래 설정 버튼
        //타임피커를 현재 시간으로 세팅해주기 위한 코드
        int hour1 = calendar.get(calendar.HOUR_OF_DAY);
        int minute1 = calendar.get(calendar.MINUTE);

        //버전따라 사용되는 함수가 다름.
        if (Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.M) {
            hourofDay = timePicker.getHour();
            minute = timePicker.getMinute();
        }
        else {
            hourofDay = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        timePicker.setOnTimeChangedListener(this);


        /*
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String strTime = hourOfDay + " : " + minute;
                Toast.makeText(TimePickerDemo.this, strTime, Toast.LENGTH_SHORT).show();
            }
        });
*/


        //timePicker.setOnTimeChangedListener(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), MainActivity.class);
                //startActivity(intent);
                Log.e("TimePickerDemo",hourofDay+","+minute);
                Log.e("TimePickerDemo",hour1+","+minute1);
                Toast.makeText(getApplicationContext(), "취침시간이 설정되었습니다. ", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute){
        Toast.makeText(this.getApplicationContext(), hourOfDay+","+minute, Toast.LENGTH_SHORT).show();

    }



}