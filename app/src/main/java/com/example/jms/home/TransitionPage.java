package com.example.jms.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransitionPage extends AppCompatActivity {

    Button triggerButton;
    TextView tv;

    APIViewModel apiViewModel = new APIViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    BleViewModel bleViewModel = new BleViewModel();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_page); //승은이가 올려준 액티비티가 나오도록.

        triggerButton = findViewById(R.id.trigger);
        tv = findViewById(R.id.textView3);

        SleepDTO sleepDTO = new SleepDTO();
        sleepDTO.setSleepTime(SleepActivity.start);
        sleepDTO.setWakeTime(SleepActivity.end);

        ///////취침, 기상시간 db저장///////
        /*apiViewModel.patchUser(RestfulAPI.principalUser.getId(), user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    RestfulAPI.principalUser = result;*/
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
                                    apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", SleepActivity.start, SleepActivity.end)
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
                                                                apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(), "0", SleepActivity.start, SleepActivity.end)
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

/*
                    //위에서 사용이 필요해 아래 6줄 복붙해서 위에 있음
                    stopService(intent);
                    Intent intent = new Intent(SleepActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }, Throwable::printStackTrace);*/





        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


    }
}


