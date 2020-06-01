package com.example.jms.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;

import java.util.Calendar;

public class SleepBroadcast extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    private final static int NOTICATION_ID4 = 4;
    private final static int NOTICATION_ID2 = 2;
    private String channelId = "channel";
    private String channelName = "channelName";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("SleepBroad","호출되었음");
        //알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //오레오 대응
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationmanager.createNotificationChannel(notificationChannel); }
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(),channelId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c = Calendar.getInstance();
        int test = c.get(Calendar.MINUTE);
        String aTime = RestfulAPI.principalUser.getSleep();
        int setHour = Integer.parseInt(aTime.substring(0,2));
        int setMin = Integer.parseInt(aTime.substring(2,4));
        //Notification.Builder builder = new Notification.Builder(context,"channel_alarm");
        //R.drawable.notification_icon
        if ((setMin - test)>4){
        builder.setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis()).setNumber(1)
                .setContentTitle("취침 4시간 전입니다.").setContentText("숙면을 위해 무리한 활동을 자제하고 조명을 낮춰주세요.")
                .setContentIntent(pendingIntent).setAutoCancel(true);
        Log.e("SleepBroad1", String.valueOf(builder));
        Log.e("TimePickernoti1",notificationmanager+",");
        notificationmanager.notify(NOTICATION_ID4, builder.build());
        }
        else {
            builder.setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis()).setNumber(1)
                    .setContentTitle("취침 2시간 전입니다.").setContentText("숙면을 위해 무리한 활동을 자제하고 조명을 낮춰주세요.")
                    .setContentIntent(pendingIntent).setAutoCancel(true);
            Log.e("SleepBroad2", String.valueOf(builder));
            Log.e("TimePickernoti2",notificationmanager+",");
            notificationmanager.notify(NOTICATION_ID2, builder.build());

        }
    }
}


