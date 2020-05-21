package com.example.jms.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.jms.R;
import com.example.jms.map.FragMap;
import com.example.jms.settings.FragSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FragHome fragHome = new FragHome();
    private FragMap fragMap = new FragMap();
    private FragSettings fragSettings = new FragSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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