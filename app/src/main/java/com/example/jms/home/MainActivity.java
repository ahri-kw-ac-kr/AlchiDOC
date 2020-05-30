package com.example.jms.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.home.statistic.StatSleep;
import com.example.jms.map.FragMap;
import com.example.jms.settings.FragSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHome fragHome = new FragHome();
    private FragMap fragMap = new FragMap();
    private FragSettings fragSettings = new FragSettings();

    SharedPreferences sharedPreferences;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("sleep",0);
        String start = sharedPreferences.getString("sleepTime","");
        String end = sharedPreferences.getString("wakeTime","");

        APIViewModel apiViewModel = new APIViewModel();
        apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(),"0",start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data2 -> {
                    if (data2.getContent() != null) {
                        UserDataModel.userDataModels[0].setSleepDataList(data2.getContent());
                        Log.d("SleepActivity","데이터 첫번째"+UserDataModel.userDataModels[0].getSleepDataList().size());
                        SleepDTO sleepDTO1 = StatSleep.analyze(data2.getContent());
                        sleepDTO1.setSleepTime(start);
                        sleepDTO1.setWakeTime(end);
                        sleepDTO1.setUser(RestfulAPI.principalUser);
                        /////////////////분석결과 db에 저장//////////////////
                        apiViewModel.postSleep(sleepDTO1)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(a->Log.d("TransitionPage","분석결과 저장"),Throwable::printStackTrace);
                    }
                    Log.d("SleepActivity","데이터 "+data2.getContent());
                }, Throwable->Log.d("MainActivity-sleepData","분석결과 db저장 실패 "+Throwable.getMessage()));

        //하단바
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragHome).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.map:
                        setFrag(1);
                        break;
                    case R.id.settings:
                        setFrag(2);
                        break;
                }
                return false;
            }
        });
        fragHome = new FragHome();
        fragMap = new FragMap();
        fragSettings = new FragSettings();
    }


    //하단바를 통해 프레그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction(); //실제적인 프레그먼트 교체에서 사용
        switch (n) {
            case 0:
                ft.replace(R.id.frameLayout, fragHome);
                ft.commit(); //저장의미
                break;
            case 1:
                ft.replace(R.id.frameLayout, fragMap);
                ft.commit(); //저장의미
                break;
            case 2:
                ft.replace(R.id.frameLayout, fragSettings);
                ft.commit(); //저장의미
                break;
        }

    }


    ///권한 설정 하는 코드를 가져와봤는데... 실행이 안되네요
    ///https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions - 여기를 참조했습니다
}