package com.example.jms.settings;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;

public class DeviceSet3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 1. '연결하기' 버튼 누른 경우: 페이지는 device4.xml로 이동
        // => 연결되면 메인 화면으로 이동과 동시에 '연결 완료'와 같은 토스트 메세지 띄울 것(이거는 DeviceSet4.java 에서!)
    }
}