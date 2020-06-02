package com.example.jms.settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        Log.d("DeviceSet1", "현재 디바이스 : "+ BleService.principalDevice);
        Log.d("DeviceSet1", "현재 커넥트 : "+ ""+sleepDocViewModel.deviceCon());
        if(BleService.principalDevice!=null) {
            if(sleepDocViewModel.deviceCon()){
                myDevice = new DeviceSet3(getApplicationContext());
                //deviceList.addView(myDevice);
                //TextView tt = myDevice.textView;
                AtomicReference<String> str = new AtomicReference<>("");
                sleepDocViewModel.battery()
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            BleService.battery = "배터리: "+data+"%";
                            Log.i("DeviceSet1", "배터리 "+ data);
                            //str.set("배터리: " + data + "%");
                            //myDevice.textView.setText(BleService.battery);
                            }
                            ,Throwable->{
                            //myDevice.textView.setText(""+BleService.battery);
                            Log.d("DeviceSet1","배터리 실패");
                        });

                //.setText(BleService.battery);
                //tt.setText(BleService.battery);
                myDevice.textView.setText(BleService.battery);
                deviceList.addView(myDevice);
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
                refactor.setVisibility(View.GONE);
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
                String mac = BleService.principalDevice.getMacAddress();
                Toast.makeText(getApplicationContext(), "초기화 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                sleepDocViewModel.disconnect();
                refactorFlag = true;
                Log.d("DeviceSet1", "공장 초기화 디바이스 : "+ BleService.principalDevice);
                sleepDocViewModel.connectSleepDoc(mac)
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> {
                            Log.i("DeviceSet1", "on Complete");
                                Log.d("DeviceSet1","공장 초기화 마침");
                            })
                        .subscribe(()->{
                            sleepDocViewModel.disconnect();
                            refactorFlag = false;
                            sleepDocViewModel.connectSleepDoc(mac)
                                    .observeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(()->{
                                        Handler mHandler = new Handler(Looper.getMainLooper());
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 사용하고자 하는 코드
                                                Toast.makeText(getApplicationContext(),
                                                        "초기화가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                                Log.d("DeviceSet1","공장 초기화 성공");
                                            }
                                        }, 0);

                                    },Throwable -> {
                                        Toast.makeText(getApplicationContext(),
                                                "초기화를 실패했습니다.", Toast.LENGTH_SHORT).show(); });
                        },Throwable::printStackTrace);

                dialog.dismiss();
                fab.setVisibility(View.GONE);
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