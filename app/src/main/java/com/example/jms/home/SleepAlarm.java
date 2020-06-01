package com.example.jms.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jms.connection.model.RestfulAPI;

import java.util.Calendar;

public class SleepAlarm {

    private Context context;

    public SleepAlarm(Context context) {
        this.context = context;
    }

    public void Alarm() {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SleepBroadcast.class);

        PendingIntent sender = PendingIntent.getBroadcast(context, 4, intent, 0);
        PendingIntent sender1 = PendingIntent.getBroadcast(context, 2, intent, 0);
        PendingIntent sender2 = PendingIntent.getBroadcast(context, 0, intent, 0);
        String aTime = RestfulAPI.principalUser.getSleep();
        Calendar c = Calendar.getInstance();
        //알람시간 calendar에 set해주기

        int Nhour=c.get(c.HOUR_OF_DAY);
        int Nminute=c.get(c.MINUTE);
        int setHour = Integer.parseInt(aTime.substring(0,2));
        int setMin = Integer.parseInt(aTime.substring(2,4));
        int AHour = setHour-4; // 기본 4시간 전
        int AMin = setMin;
        Log.e("sleepalarm 현재 시간",Nhour+" : "+Nminute);
        Log.e("sleepalarm 맞춘 시간",setHour+" : "+setMin);
        //1시 59분에 2시 10분 알람을 맞추면 이상해지니까 우선 setting해둠.


        if((Nhour-setHour)<4){ //4시간 이내로 취침예정일 때
            AHour = setHour-Nhour;
            AMin = setMin-Nminute;
            if (AMin < 0){
                AHour = AHour-1;
                AMin = 60+AMin;
            }
            if (AHour==0) {
                //그냥 직전알림 보냅니다.
                Log.e("SleepA", "취침시간 : 직전알람 - 예약시간 " + AHour + ":" + AMin + "전");
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), setHour, setMin, 0);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender2);
                Log.e("TimePickeralarm1",am+""+c.getTimeInMillis());}
            else {
                //2시간전+직전
                Log.e("SleepA","취침시간 : 직전알람 - 예약시간 "+AHour+":"+AMin+"전");
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), setHour-2, setMin, 0);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender1);
                Log.e("TimePickeralarm1",am+""+c.getTimeInMillis());
                c.add(c.HOUR,2);
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender2);
                Log.e("TimePickeralarm1",am+""+c.getTimeInMillis());
            }
        }
        else{ //4시간 이상일때
            Log.e("SleepA","취침시간 : 당일 / 알림 4시간 전, 2시간 전 "+AHour+":"+AMin);
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), AHour, AMin, 0);
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
            Log.e("TimePickeralarm1",am+""+c.getTimeInMillis());

            c.add(c.HOUR,2);
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender1);
            Log.e("TimePickeralarm2",am+""+c.getTimeInMillis());
        }



        /*if (Nhour>setHour){
          //  Log.e("SleepA","취침시간 : 익일새벽 / 알림 4시간 전, 2시간 전 "+AHour+":"+AMin);
           // c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE+1), AHour, AMin, 0);
        //} 새벽알림 막아버렸음.
        else {

        }
        //알람 예약
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
        Log.e("TimePickeralarm1",am+""+c.getTimeInMillis());

        c.add(c.HOUR,2);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender1);
        Log.e("TimePickeralarm2",am+""+c.getTimeInMillis());
        */
    }
}