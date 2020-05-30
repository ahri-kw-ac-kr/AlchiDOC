package com.example.jms.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.BleViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    Long calcTime;
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

        sleepEnd =  findViewById(R.id.sleep_end);
        chronometer = findViewById(R.id.ellapse);
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

                try {
                    //초단위로 계산되는 시간 차이
                    calcTime = (simpleDateFormat.parse(endTime).getTime()-simpleDateFormat.parse(startTime).getTime())/1000;
                    Log.e("SleepActivity",calcTime+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (calcTime < 3600 ) {
                    Toast.makeText(getApplicationContext(), "30분 미만의 수면기록은 기록되지 않습니다.", Toast.LENGTH_LONG).show();
                    stopService(intent);
                    Intent intent2 = new Intent(SleepActivity.this, MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent2);
                    finish();
                }
                //calctime이 30분 이상일경우
                else{
                    stopService(intent);
                    Intent intent1 = new Intent(SleepActivity.this, TransitionPage.class);
                    startActivity(intent1);
                    finish();
                }




                /*
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


                            //위에서 사용이 필요해 아래 6줄 복붙해서 위에 있음
                            stopService(intent);
                            Intent intent = new Intent(SleepActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }, Throwable::printStackTrace);
*/

                /*밑에 세줄 사용하니까 재시작됨 그래서 주석처리해뒀음 */
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   // startForegroundService(intent);
                //} else { startService(intent); }

            }//onClick
        });//ClickListener
    }//onCreate

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }//onBackPressed

}//class


