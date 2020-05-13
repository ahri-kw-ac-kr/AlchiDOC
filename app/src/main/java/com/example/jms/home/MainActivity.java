package com.example.jms.home;

import android.os.Bundle;
import android.util.Log;
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
    private FragMap fragMap= new FragMap();
    private FragSettings fragSettings = new FragSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //하단바 메뉴 누르면 다른 페이지로 이동 ㄱ
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout,fragHome).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
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

    //프레그먼트 교체가 일어나는 실행문
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction(); //실제적인 프레그먼트 교체에서 사용
        switch (n) {
            case 0:
                Log.d("errror","error");
                //if(fragHome.isAdded()){ft.remove(fragHome);}
                ft.replace(R.id.frameLayout, fragHome);
                ft.commit(); //저장의미
                break;
            case 1:
                Log.d("errror","error1");
                //if(fragMap.isAdded()){ft.remove(fragMap);}
                ft.replace(R.id.frameLayout, fragMap);
                ft.commit(); //저장의미
                break;
            case 2:
                //if(fragSettings.isAdded()){ft.remove(fragSettings);}
                ft.replace(R.id.frameLayout, fragSettings);
                ft.commit(); //저장의미
                break;
        }
    }
}