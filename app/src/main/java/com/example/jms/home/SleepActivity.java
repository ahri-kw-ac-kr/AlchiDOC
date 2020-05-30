package com.example.jms.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.BleDeviceDTO;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
* foreground service 참고링크
* https://www.youtube.com/watch?v=rxK_M9VfX2o
* https://www.youtube.com/watch?v=FbpD5RZtbCc
* https://snowdeer.github.io/android/2016/06/14/android-foreground-service-notification-customization/
* http://snowdeer.github.io/android/2018/08/02/android-foreground-service-after-oreo/
* */
public class SleepActivity extends AppCompatActivity {

    Button sleepEnd;
    String startTime, endTime;
    Chronometer chronometer;
    long stopTime = 0;

    APIViewModel apiViewModel = new APIViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    BleViewModel bleViewModel = new BleViewModel();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_activity); //승은이가 올려준 액티비티가 나오도록.

        UserDataModel.contextP = getApplicationContext();
        sharedPreferences = getSharedPreferences("ble",0);
        editor = sharedPreferences.edit();

        sleepEnd = (Button) findViewById(R.id.sleep_end);
        chronometer = (Chronometer) findViewById(R.id.ellapse);
        Intent intent = new Intent(this, SleepService.class);
        intent.setAction("startForeground"); //포그라운드 서비스 실행

        chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronometer.start();

        Date sDate = new Date(System.currentTimeMillis());
        startTime = simpleDateFormat.format(sDate);
        Log.d("SleepActivity - start",startTime);

        sleepEnd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {

                Date eDate = new Date(System.currentTimeMillis());
                endTime = simpleDateFormat.format(eDate);
                Log.d("SleepActivity - end", endTime);
                chronometer.stop();

                UserDTO user = new UserDTO();
                user.setSleep(startTime);
                user.setWake(endTime);

                ///////취침, 기상시간 db저장///////
                apiViewModel.patchUser(RestfulAPI.principalUser.getId(), user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            RestfulAPI.principalUser = result;
                            if (BleService.principalDevice != null && sleepDocViewModel.deviceCon()) {
                                /////////////////기기데이터 받아오기//////////////////
                                sleepDocViewModel.getRawdataFromSleepDoc()
                                        .observeOn(Schedulers.io())
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .subscribe(data -> {
                                            data.setUser(RestfulAPI.principalUser);
                                            /////////////////받은 데이터 db로 전송//////////////////
                                            apiViewModel.postRawdata(data)
                                                    .observeOn(Schedulers.io())
                                                    .subscribe(result2 -> {
                                                        /////////////////취침시간 동안의 데이터 갖고오기//////////////////
                                                        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", startTime, endTime)
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(data2 -> {
                                                                    if (data2.getContent() != null) {
                                                                        UserDataModel.userDataModels[0].setSleepDataList(data2.getContent());
                                                                        Log.d("SleepActivity","데이터 첫번째"+UserDataModel.userDataModels[0].getSleepDataList().size());
                                                                    }
                                                                    Log.d("SleepActivity","데이터 "+data2.getContent());
                                                                    }, Throwable::printStackTrace);/////api-getRawdataByID
                                                    }, Throwable -> {
                                                        Log.d("SleeActivity", "집어넣기 오류 " + Throwable.getMessage());
                                                    });/////api = postRawdata
                                        }, Throwable -> Log.d("SleeActivity", "데이터 불러오기 오류 " + Throwable.getMessage()));/////sleepDoc - getRawdataFromSleepDoc

                            } //블루투스 연결 되어있을 때
                            else {
                                BleManager.getInstance().init(getApplication());
                                /////////////////기기검색//////////////////
                                bleViewModel.scanBle()
                                        .observeOn(Schedulers.io())
                                        .subscribeOn(AndroidSchedulers.mainThread())
                                        .subscribe(BleDeviceDTO->{
                                                    if (BleDeviceDTO.getName().equals("SleepDoc")) {
                                                        Log.d("SleepActivity","슬립닥 찾음");
                                                        /////////////////기기연결//////////////////
                                                        sleepDocViewModel.connectSleepDoc(BleDeviceDTO.getMacAddress())
                                                                .observeOn(Schedulers.io())
                                                                .subscribeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(()->{
                                                                    BleService.principalDevice = BleDeviceDTO;
                                                                    editor.putString("mac",BleDeviceDTO.getMacAddress());
                                                                    editor.putString("key",BleDeviceDTO.getKey());
                                                                    editor.putString("name",BleDeviceDTO.getName());
                                                                    editor.putInt("rssi",BleDeviceDTO.getRssi());
                                                                    editor.commit();

                                                                    /////////////////기기데이터 받아오기//////////////////
                                                                    sleepDocViewModel.getRawdataFromSleepDoc()
                                                                            .observeOn(Schedulers.io())
                                                                            .subscribeOn(AndroidSchedulers.mainThread())
                                                                            .subscribe(data -> {
                                                                                data.setUser(RestfulAPI.principalUser);
                                                                                /////////////////받은 데이터 db로 전송//////////////////
                                                                                apiViewModel.postRawdata(data)
                                                                                        .observeOn(Schedulers.io())
                                                                                        .subscribe(result2 -> {
                                                                                            /////////////////취침시간 동안의 데이터 갖고오기//////////////////
                                                                                            apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", startTime, endTime)
                                                                                                    .subscribeOn(Schedulers.io())
                                                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                                                    .subscribe(data2 -> {
                                                                                                        if (data2.getContent() != null) {
                                                                                                            UserDataModel.userDataModels[0].setSleepDataList(data2.getContent());
                                                                                                            Log.d("SleepActivity","데이터 첫번째"+UserDataModel.userDataModels[0].getSleepDataList().size());
                                                                                                        }
                                                                                                        Log.d("SleepActivity","데이터 "+data2.getContent());
                                                                                                    }, Throwable::printStackTrace);/////api-getRawdataByID
                                                                                        }, Throwable -> {
                                                                                            Log.d("SleeActivity", "집어넣기 오류 " + Throwable.getMessage());
                                                                                        });/////api = postRawdata
                                                                            }, Throwable -> Log.d("SleeActivity", "데이터 불러오기 오류 " + Throwable.getMessage()));/////sleepDoc - getRawdataFromSleepDoc
                                                                },Throwable::printStackTrace);//연결 끝
                                                        }//검색 후 슬립닥 있을 경우 if
                                                    },Throwable::printStackTrace);//ble 검색 끝
                            }//블루투스 연결 안되어있을 때 else



                            stopService(intent);
                            Intent intent = new Intent(SleepActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }, Throwable::printStackTrace);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else { startService(intent); }

            }//onClick
        });//ClickListener
    }//onCreate

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }//onBackPressed

}//class


