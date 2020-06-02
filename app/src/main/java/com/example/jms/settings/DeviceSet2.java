package com.example.jms.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;
import com.example.jms.home.UserDataModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceSet2 extends AppCompatActivity {

    BleViewModel bleViewModel = new BleViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    APIViewModel apiViewModel = new APIViewModel();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device2);

        UserDataModel.contextP = getApplicationContext();
        sharedPreferences = getSharedPreferences("ble",0);
        editor = sharedPreferences.edit();

        Log.d("DeviceSet2","들어옴");

        AtomicInteger flag = new AtomicInteger(0);

        BleManager.getInstance().init(getApplication());
        bleViewModel.scanBle()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    Log.i("DeviceSet2", "블루투스 기기 스캔");
                    if(BleService.principalDevice == null){
                        Log.d("Device2","스캔했지만 기기없음.1");
                        Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                        startActivity(intent);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() { Toast.makeText(getApplicationContext(), "검색된 기기가 없습니다.", Toast.LENGTH_SHORT).show(); }},0);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                        startActivity(intent);
                        finish();

                        Calendar calendar = Calendar.getInstance();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
                        String date = transFormat.format(calendar.getTime());
                        Date today = transFormat.parse(date);
                        calendar.setTime(today);
                        String lastDate;
                        if(Integer.parseInt(date.substring(4,6))==12){
                            int year = Integer.parseInt(date.substring(0,4))+1;
                            lastDate = year+"0101 00:00:00";
                        }else{
                            int month = Integer.parseInt(date.substring(4,6))+1;
                            lastDate = date.substring(0,4)+month+"01 00:00:00";
                        }

                        String weekCause;
                        String month = date.substring(4,6);
                        if(month == "01"){
                            int year = Integer.parseInt(date.substring(0,4))-1;
                            weekCause = year+"1223"; }
                        else{
                            int monthI = Integer.parseInt(month)-1;
                            if(monthI < 10){
                                month = "0"+ monthI;
                                weekCause = date.substring(0,4)+month+"23 00:00:00"; }
                            else{
                                month = Integer.toString(monthI);
                                weekCause = date.substring(0,4)+month+"23 00:00:00"; }
                        }

                        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(),"0",weekCause,lastDate)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(data -> {
                                    if(data.getContent()!=null){
                                        UserDataModel.userDataModels[0].setDataList(data.getContent());
                                        UserDataModel.userDataModels[0].parsingDay(0);
                                    }
                                },Throwable::printStackTrace);
                    }
                })
                .subscribe(BleDeviceDTO -> {
                    flag.getAndIncrement();
                    Log.d("Device2","1차성공");
                    Log.d("Device2","1차성공 후 "+BleDeviceDTO.getName());
                    if (BleDeviceDTO.getName().equals("SleepDoc") && flag.get() ==1) {
                        Log.d("Device2","슬립닥 찾음");
                        DeviceSet1.refactorFlag = false;
                        sleepDocViewModel.connectSleepDoc(BleDeviceDTO.getMacAddress())
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .doOnComplete(() -> {
                                    Log.i("DeviceSet2", "on Complete");
                                    /*sleepDocViewModel.battery()
                                            .observeOn(Schedulers.io())
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .subscribe(batt -> {
                                                Log.i("DeviceSet2", "배터리 "+ batt);
                                                BleService.battery = "배터리: "+batt+"%";
                                            },Throwable->{Log.d("DeviceSet2","배터리 실패"); });*/
                                })
                                .subscribe(() -> {
                                    BleService.principalDevice = BleDeviceDTO;
                                    editor.putString("mac",BleDeviceDTO.getMacAddress());
                                    editor.putString("key",BleDeviceDTO.getKey());
                                    editor.putString("name",BleDeviceDTO.getName());
                                    editor.putInt("rssi",BleDeviceDTO.getRssi());
                                    editor.apply();
                                    sleepDocViewModel.battery()
                                            .observeOn(Schedulers.io())
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .doOnComplete(()->{
                                                sleepDocViewModel.getRawdataFromSleepDoc()
                                                        .observeOn(Schedulers.io())
                                                        .subscribeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(data -> {
                                                            data.setUser(RestfulAPI.principalUser);
                                                            apiViewModel.postRawdata(data)
                                                                    .observeOn(Schedulers.io())
                                                                    .subscribe(result -> {
                                                                    }, Throwable -> { Log.d("DeviceSet2", "집어넣기 오류 " + Throwable.getMessage()); });
                                                        }, Throwable -> Log.d("DeviceSet2", "데이터 불러오기 오류 " + Throwable.getMessage()));
                                            })
                                           .subscribe(batt -> {
                                                Log.i("DeviceSet2", "배터리 "+ batt);
                                                BleService.battery = "배터리: "+batt+"%";
                                          },Throwable->{Log.d("DeviceSet2","배터리 실패"); });
                                    }, Throwable -> {
                                    Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                                    startActivity(intent);
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "기기 연결 실패", Toast.LENGTH_SHORT).show();
                                             }},0);
                                    finish();
                                }
                                );
                    }
                    /*else{
                        Log.d("Device2","스캔했지만 기기없음.1");
                        Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                        startActivity(intent);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() { Toast.makeText(getApplicationContext(), "검색된 기기가 없습니다.", Toast.LENGTH_SHORT).show(); }},0);
                        finish();

                    }*/},Throwable::printStackTrace);

    }
}