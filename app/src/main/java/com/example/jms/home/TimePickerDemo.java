package com.example.jms.home;

import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;

import java.util.Calendar;

public class TimePickerDemo extends AppCompatActivity implements TimePicker.OnTimeChangedListener {

    TimePicker tp;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_time);
        c = Calendar.getInstance();
        int hourofDay = c.get(c.HOUR_OF_DAY);
        int minute = c.get(c.MINUTE);
        tp = (TimePicker) findViewById(R.id.tp);
        tp.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute){

    }
}