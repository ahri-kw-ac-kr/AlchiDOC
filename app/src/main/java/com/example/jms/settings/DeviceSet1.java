package com.example.jms.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.BleDeviceDTO;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class DeviceSet1 extends AppCompatActivity {

    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    BleViewModel bleViewModel = new BleViewModel();
    LinearLayout deviceList;

    FloatingActionButton fab;
    FloatingActionButton refactor;
    DeviceSet3 myDevice;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static boolean refactorFlag;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device1);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        refactor = (FloatingActionButton)findViewById(R.id.bleRefactor);
        refactor.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("ble",0);
        editor = sharedPreferences.edit();

        deviceList = (LinearLayout)findViewById(R.id.deviceList);
        if(BleService.principalDevice!=null) {
            if(sleepDocViewModel.deviceCon()){
                myDevice = new DeviceSet3(getApplicationContext());
                deviceList.addView(myDevice);
                sleepDocViewModel.battery()
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            Log.i("DeviceSet1", "배터리 "+ data);
                            BleService.battery = "배터리: "+data+"%";
                            myDevice.textView.setText(BleService.battery);
                            }
                            ,Throwable->{
                            myDevice.textView.setText(""+BleService.battery);
                            Log.d("DeviceSet1","배터리 실패");
                        });
                fab.setVisibility(View.GONE);
                refactor.setVisibility(View.VISIBLE);
                Button btn = myDevice.button;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishButton(v);
                    }
                });
            }
        }

    }

    public void connectDevice(View view){
        Log.d("DeviceSet1","들어가기 시도");
        Intent intent = new Intent(getApplicationContext(), DeviceSet2.class);
        startActivity(intent);
        finish();
    }

    public void finishButton(View view){
        androidx.appcompat.app.AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("기기와 연결을 끊으시겠습니까?");
        ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BleService.principalDevice = null;
                BleService.battery = null;
                sleepDocViewModel.disconnect();
                deviceList.removeAllViews();
                editor.clear();
                editor.commit();
                dialog.dismiss();
                /*Activity act = (Activity)getApplicationContext();
                act.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {*/
                fab.setVisibility(View.VISIBLE);
            //}});
                Log.d("DeviceSet1","연결끊기 확인");
            }
        });

        ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.d("DeviceSet1","연결끊기 취소");
            }
        });
        ad.show();
    }

    public void refactorBle(View view){
        androidx.appcompat.app.AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("기기를 초기화 하시겠습니까?");
        ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "초기화 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                sleepDocViewModel.disconnect();
                refactorFlag = true;
                sleepDocViewModel.connectSleepDoc(BleService.principalDevice.getMacAddress())
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> { Log.i("DeviceSet1", "on Complete"); })
                        .subscribe(()->{
                            sleepDocViewModel.disconnect();
                            refactorFlag = false;
                            sleepDocViewModel.connectSleepDoc(BleService.principalDevice.getMacAddress())
                                    .observeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(()->{
                                        Toast.makeText(getApplicationContext(),
                                                "초기화가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    },Throwable -> {
                                        Toast.makeText(getApplicationContext(),
                                                "초기화를 실패했습니다.", Toast.LENGTH_SHORT).show(); });
                        },Throwable::printStackTrace);

                dialog.dismiss();
                fab.setVisibility(View.GONE);
                Log.d("DeviceSet1","공장 초기화");
            }
        });

        ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.d("DeviceSet1","공장 초기화 취소");
            }
        });
        ad.show();
    }
}