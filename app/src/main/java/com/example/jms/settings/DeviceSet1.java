package com.example.jms.settings;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.model.dto.BleDeviceDTO;
import com.example.jms.connection.viewmodel.SleepDocViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeviceSet1 extends AppCompatActivity {

    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    LinearLayout deviceList;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device1);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        deviceList = (LinearLayout)findViewById(R.id.deviceList);
        if(BleManager.getInstance().getAllConnectedDevice() != null){
            DeviceSet3 myDevice = new DeviceSet3(getApplicationContext());
            deviceList.addView(myDevice);
            sleepDocViewModel.battery()
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        Log.i("MainActivity", "배터리 "+ data);
                        String battery = "배터리: "+data+"%";
                        myDevice.textView.setText(battery);},Throwable->{
                        Log.d("DeviceSet1","배터리 실패");
                    });
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    public void connectDevice(View view){
        Log.d("DeviceSet1","들어가기 시도");
        Intent intent = new Intent(getApplicationContext(), DeviceSet2.class);
        startActivity(intent);
        finish();
    }
}