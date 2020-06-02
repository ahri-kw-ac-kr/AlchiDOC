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

import com.example.jms.R;

public class SleepService extends Service {

    public SleepService() { } // 생성자


    //이거 뭐였지
    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Sleepservice","intent");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Sleepservice","intent"+intent);
        if("startForeground".equals(intent.getAction())){ //해당 인텐트로 이와 같은 커맨드가 온다면
            startForegroundService(); // 아래의 함수를 실행하시오
        }
        Log.d("SleepService","수면 서비스 시작");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d("SleepService","수면서비스 종료");
        super.onDestroy();

    }


    private void startForegroundService() {
        // 노피티케이션컴팻 빌더 선언
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        // 상황에 따라 바꿔야 됨
        builder.setContentTitle("수면기록 측정 중");
        builder.setContentText("수면 기록을 측정 중입니다. 숙면을 위해 적절한 환경에서 수면을 취해주세요.");


        //클릭시 시작되는 인텐트 - 바로 일반인텐트 실행 불가 노티피케이션을 클릭했을 때 슬립액티비티로 돌아가도록.
        Intent notificationIntent = new Intent(this, SleepActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,0);

        builder.setContentIntent(pendingIntent); // 다엮여있음.


        //오레오에서는 다른 코드. - 채널을 등록해줘야한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("default", "SleepChannel", NotificationManager.IMPORTANCE_DEFAULT)); //위에 준 채널명이다
        }
            startForeground(1, builder.build()); // 실행이됩니다.

    }

    private void stopForegroundService(){
        Log.d("D","수면서비스 종료");
        stopForeground(true);
    }


}
