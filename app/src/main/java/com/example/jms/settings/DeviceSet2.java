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
                })
                .subscribe(BleDeviceDTO -> {
                    Log.d("Device2","1차성공");
                    Log.d("Device2","1차성공 후 "+BleDeviceDTO.getName());
                    if (BleDeviceDTO.getName().equals("SleepDoc")) {
                        Log.d("Device2","슬립닥 찾음");
                        sleepDocViewModel.connectSleepDoc(BleDeviceDTO.getMacAddress())
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .doOnComplete(() -> Log.i("DeviceSet2", "on Complete"))
                                .subscribe(() -> {
                                    BleService.principalDevice = BleDeviceDTO;
                                    editor.putString("mac",BleDeviceDTO.getMacAddress());
                                    editor.putString("key",BleDeviceDTO.getKey());
                                    editor.putString("name",BleDeviceDTO.getName());
                                    editor.putInt("rssi",BleDeviceDTO.getRssi());
                                    editor.commit();
                                    Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                                    startActivity(intent);
                                    finish();
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
                                });
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