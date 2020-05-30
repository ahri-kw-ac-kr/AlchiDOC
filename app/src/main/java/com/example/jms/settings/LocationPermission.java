package com.example.jms.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LocationPermission extends Activity {
    APIViewModel apiViewModel = new APIViewModel();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_permission);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Switch sw = (Switch)findViewById(R.id.LocationSwitch);
        /*if(RestfulAPI.principalUser.getShareGPS() == null){
            sw.setChecked(true);
            UserDTO user = new UserDTO();
            user.setShareGPS("true");
            apiViewModel.patchUser(RestfulAPI.principalUser.getId(),user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> RestfulAPI.principalUser=result,Throwable::printStackTrace);
        }*/
        if(RestfulAPI.principalUser.getShareGPS().equals("true")){ sw.setChecked(true); }
        else if(RestfulAPI.principalUser.getShareGPS().equals("false")){ sw.setChecked(false); }

        //스위치 체크 이벤트를 위한 리스너 등록
        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UserDTO user = new UserDTO();
                    user.setShareGPS("true");
                    apiViewModel.patchUser(RestfulAPI.principalUser.getId(),user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> RestfulAPI.principalUser=result,Throwable::printStackTrace);
                }
                else{
                    UserDTO user = new UserDTO();
                    user.setShareGPS("false");
                    apiViewModel.patchUser(RestfulAPI.principalUser.getId(),user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> RestfulAPI.principalUser=result,Throwable::printStackTrace);
                }


            }
        });
    }
}
