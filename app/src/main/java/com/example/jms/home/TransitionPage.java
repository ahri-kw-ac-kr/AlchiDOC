package com.example.jms.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;
import com.example.jms.home.statistic.StatAct;
import com.example.jms.home.statistic.StatSleep;
import com.example.jms.settings.DeviceSet1;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransitionPage extends AppCompatActivity {

    APIViewModel apiViewModel = new APIViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    BleViewModel bleViewModel = new BleViewModel();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_page); //승은이가 올려준 액티비티가 나오도록.

        SleepDTO sleepDTO = new SleepDTO();
        sleepDTO.setSleepTime(SleepActivity.start);
        sleepDTO.setWakeTime(SleepActivity.end);

        sharedPreferences = getSharedPreferences("ble",0);
        editor = sharedPreferences.edit();
        sharedPreferences2 = getSharedPreferences("sleep",0);
        editor2 = sharedPreferences2.edit();

        if (BleService.principalDevice != null && sleepDocViewModel.deviceCon()) {
            Log.d("분석중","기기 있다");
            /////////////////기기데이터 받아오기//////////////////
            sleepDocViewModel.getRawdataFromSleepDoc()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(()->{
                        /////////////////취침시간 동안의 데이터 갖고오기//////////////////
                        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", SleepActivity.start, SleepActivity.end)
                                .subscribeOn(Schedulers.io())
                                .doAfterTerminate(()->{
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(data2 -> {
                                    if (data2.getContent() != null) {
                                        try {
                                            Log.d("SleepActivity", "데이터 첫번째result" + data2.getContent().get(0).getStartTick());
                                        } catch (Exception e) {
                                            Log.d("SleepActivity", "데이터 첫번째result size" + data2.getContent().size());
                                        }
                                        if (data2.getContent().size() != 0) {
                                            UserDataModel.userDataModels[0].setSleepDataList(data2.getContent());
                                            Log.d("SleepActivity", "데이터 첫번째" + UserDataModel.userDataModels[0].getSleepDataList().size());
                                            SleepDTO sleepDTO1 = StatSleep.analyze(data2.getContent());
                                            sleepDTO1.setSleepTime(SleepActivity.start);
                                            sleepDTO1.setWakeTime(SleepActivity.end);
                                            sleepDTO1.setUser(RestfulAPI.principalUser);
                                            UserDataModel.userDataModels[0].getSleepDTOList().add(0,sleepDTO1);
                                            /////////////////분석결과 db에 저장//////////////////
                                            apiViewModel.postSleep(sleepDTO1)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(a -> {
                                                        Log.d("TransitionPage", "분석결과 저장");
                                                        editor2.putString("sleepTime","0");
                                                        editor2.putString("wakeTime","0");
                                                        editor2.apply();
                                                    }, Throwable::printStackTrace);
                                        }
                                    }
                                    Log.d("SleepActivity","데이터 "+data2.getContent());
                                }, Throwable::printStackTrace);/////api-getRawdataByID
                    })
                    .subscribe(data -> {
                        Log.d("SleepActivity","데이터 받아옴");
                        data.setUser(RestfulAPI.principalUser);
                        /////////////////받은 데이터 db로 전송//////////////////
                        apiViewModel.postRawdata(data)
                                .observeOn(Schedulers.io())
                                .doAfterTerminate(()->{
                                    /////////////////취침시간 동안의 데이터 갖고오기//////////////////
                                    /*apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", SleepActivity.start, SleepActivity.end)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(data2 -> {
                                                if (data2.getContent() != null) {
                                                    UserDataModel.userDataModels[0].setSleepDataList(data2.getContent());
                                                    Log.d("SleepActivity","데이터 첫번째"+UserDataModel.userDataModels[0].getSleepDataList().size());
                                                    SleepDTO sleepDTO1 = StatSleep.analyze(data2.getContent());
                                                    sleepDTO1.setSleepTime(SleepActivity.start);
                                                    sleepDTO1.setWakeTime(SleepActivity.end);
                                                    sleepDTO1.setUser(RestfulAPI.principalUser);
                                                    /////////////////분석결과 db에 저장//////////////////
                                                    apiViewModel.postSleep(sleepDTO1)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(a->Log.d("TransitionPage","분석결과 저장"),Throwable::printStackTrace);
                                                }
                                                Log.d("SleepActivity","데이터 "+data2.getContent());
                                            }, Throwable::printStackTrace);/////api-getRawdataByID  */
                                })//데이터 포스트 종료 후
                                .subscribe(result2 -> {
                                    Log.d("SleepActivity","raw데이터 포스트 완료");

                                }, Throwable -> {
                                    Log.d("SleeActivity", "집어넣기 오류 " + Throwable.getMessage());
                                });/////api = postRawdata
                    }, Throwable -> Log.d("SleeActivity", "데이터 불러오기 오류 " + Throwable.getMessage()));/////sleepDoc - getRawdataFromSleepDoc
        } //블루투스 연결 되어있을 때
        else {
            Log.d("분석중","기기 없다");
            BleManager.getInstance().init(getApplication());
            /////////////////기기검색//////////////////
            bleViewModel.scanBle()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(()->{
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    })
                    .subscribe(BleDeviceDTO->{
                        if (BleDeviceDTO.getName().equals("SleepDoc")) {
                            Log.d("SleepActivity","슬립닥 찾음");
                            /////////////////기기연결//////////////////
                            sleepDocViewModel.connectSleepDoc(BleDeviceDTO.getMacAddress())
                                    .observeOn(Schedulers.io())
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    .subscribe(()->{
                                        //BleService.principalDevice = BleDeviceDTO;
                                        editor.putString("mac",BleDeviceDTO.getMacAddress());
                                        editor.putString("key",BleDeviceDTO.getKey());
                                        editor.putString("name",BleDeviceDTO.getName());
                                        editor.putInt("rssi",BleDeviceDTO.getRssi());
                                        editor.commit();

                                        /////////////////기기데이터 받아오기//////////////////
                                        sleepDocViewModel.getRawdataFromSleepDoc()
                                                .observeOn(Schedulers.io())
                                                .subscribeOn(AndroidSchedulers.mainThread())
                                                .doOnComplete(()->{
                                                    /////////////////취침시간 동안의 데이터 갖고오기//////////////////
                                                    apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", SleepActivity.start, SleepActivity.end)
                                                            .subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(data2 -> {
                                                                if (data2.getContent() != null) {
                                                                    if (data2.getContent().size() != 0) {
                                                                        UserDataModel.userDataModels[0].setSleepDataList(data2.getContent());
                                                                        Log.d("SleepActivity", "데이터 첫번째" + UserDataModel.userDataModels[0].getSleepDataList().size());
                                                                        SleepDTO sleepDTO1 = StatSleep.analyze(data2.getContent());
                                                                        sleepDTO1.setSleepTime(SleepActivity.start);
                                                                        sleepDTO1.setWakeTime(SleepActivity.end);
                                                                        sleepDTO1.setUser(RestfulAPI.principalUser);
                                                                        UserDataModel.userDataModels[0].getSleepDTOList().add(0,sleepDTO1);
                                                                        /////////////////분석결과 db에 저장//////////////////
                                                                        apiViewModel.postSleep(sleepDTO1)
                                                                                .subscribeOn(Schedulers.io())
                                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                                .subscribe(a -> {
                                                                                    Log.d("TransitionPage", "분석결과 저장");
                                                                                    //editor2.putString("sleepTime","0");
                                                                                    //editor2.putString("wakeTime","0");
                                                                                    //editor2.apply();
                                                                                    }, Throwable::printStackTrace);
                                                                    }
                                                                }
                                                                Log.d("SleepActivity","데이터 "+data2.getContent());
                                                            }, Throwable::printStackTrace);/////api-getRawdataByID
                                                })
                                                .subscribe(data -> {
                                                    data.setUser(RestfulAPI.principalUser);
                                                    /////////////////받은 데이터 db로 전송//////////////////
                                                    apiViewModel.postRawdata(data)
                                                            .observeOn(Schedulers.io())
                                                            .subscribe(result2 -> { }, Throwable -> {
                                                                Log.d("SleeActivity", "집어넣기 오류 " + Throwable.getMessage());
                                                            });/////api = postRawdata
                                                }, Throwable -> Log.d("SleeActivity", "데이터 불러오기 오류 " + Throwable.getMessage()));/////sleepDoc - getRawdataFromSleepDoc
                                    },Throwable::printStackTrace);//연결 끝
                        }//검색 후 슬립닥 있을 경우 if
                    },Throwable -> {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() { Toast.makeText(getApplicationContext(), "블루투스와 위치가 켜져 있는지 확인해주세요.", Toast.LENGTH_SHORT).show(); }},0);
                        finish();
                    });//ble 검색 끝
        }//블루투스 연결 안되어있을 때 else

    }
}


