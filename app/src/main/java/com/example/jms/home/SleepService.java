package com.example.jms.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class SleepService extends Service {

    public SleepService() { } // 생성자

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //startForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if("startForeground".equals(intent.getAction())){
            startForegroundService();
        }

        Log.d("D","수면 서비스 시작");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("D","수면서비스 종료");
        super.onDestroy();

    }

    private void startForegroundService() {
        // 노피티케이션컴팻 빌더 선언
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        //builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("수면중입니다");
        builder.setContentText("수면중 내용입니다");

        //클릭시 시작되는 인텐트 - 바로 일반인텐트 실행 불가 노티피케이션을 클릭했을 때 슬립액티비티로 돌아가도록.
        Intent notificationIntent = new Intent(this, SleepActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pendingIntent); // 다엮여있음.


        //오레오에서는 다른 코드. - 채널을 등록해줘야한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "SleepChannel", NotificationManager.IMPORTANCE_DEFAULT)); //위에 준 채널명이다
        }
            startForeground(1, builder.build()); // 실행이됩니다.

    }


}
