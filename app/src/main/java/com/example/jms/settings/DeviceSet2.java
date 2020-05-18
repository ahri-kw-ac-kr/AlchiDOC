package com.example.jms.settings;

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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceSet2 extends AppCompatActivity {

    BleViewModel bleViewModel = new BleViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();

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
        bleViewModel.scanBle()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.i("MainActivity", "블루투스 기기 스캔"))
                .subscribe(BleDeviceDTO -> {
                    if (BleDeviceDTO.getName().equals("SleepDoc")) {
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
                                            "검색된 기기가 없습니다.", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }
}