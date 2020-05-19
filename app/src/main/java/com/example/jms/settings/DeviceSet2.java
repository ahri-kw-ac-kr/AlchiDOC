package com.example.jms.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceSet2 extends AppCompatActivity {

    BleViewModel bleViewModel = new BleViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device2);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        // 1. 블루투스 검색이 된 경우: device3 페이지로 이동
        // 2. 블루투스 검색이 안 된 경우: device1 페이지로 다시 이동한 후 토스트 메세지로 "기기를 찾지 못했습니다" 띄우기

        Log.d("DeviceSet2","들어옴");

        BleManager.getInstance().init(getApplication());
        //try {
            bleViewModel.scanBle()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(() -> Log.i("MainActivity", "블루투스 기기 스캔"))
                    .subscribe(BleDeviceDTO -> {
                        Log.d("Device2","1차성공");
                        Log.d("Device2","1차성공 후 "+BleDeviceDTO.getName());
                            if (BleDeviceDTO.getName().equals("SleepDoc")) {
                                Log.d("Device2","슬립닥 찾음");
                                sleepDocViewModel.connectSleepDoc(BleDeviceDTO.getMacAddress())
                                        .observeOn(Schedulers.io())
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .doOnComplete(() -> Log.i("MainActivity", "on Complete"))
                                        .subscribe(() -> {
                                            Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                                            startActivity(intent);
                                            finish();
                                        }, Throwable -> {
                                            Toast.makeText(getApplicationContext(),
                                                    "기기 연결 실패", Toast.LENGTH_SHORT).show();
                                        });
                            }
                            else {
                                Log.d("Device2","뭐야");
                                Toast.makeText(getApplicationContext(), "검색된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                                startActivity(intent);
                                finish();
                            }

                    });//,Throwable -> {Log.d("Device2","혹시 여기");
                        //Log.d("Device2","에러: "+Throwable.getMessage());});
           /* }catch (Exception e){
                Log.d("Device2","스캔했지만 기기없음.");
                Toast.makeText(getApplicationContext(), "검색된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DeviceSet1.class);
                startActivity(intent);
                finish();
            }*/
    }
}