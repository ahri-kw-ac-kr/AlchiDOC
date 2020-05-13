package com.example.jms.etc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.jms.R;


public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent (getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        },1000); //1초 후에 메인화면 나타남(인트로 화면을 몇 초 동안 보여줄 것인지)
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}