package com.example.jms.settings;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.example.jms.R;

public class LocationPermission extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_permission);

        Switch sw = (Switch)findViewById(R.id.LocationSwitch);

        //스위치 체크 이벤트를 위한 리스너 등록
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



            }
        });
    }
}
