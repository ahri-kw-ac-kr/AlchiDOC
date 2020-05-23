package com.example.jms.etc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.clj.fastble.BleManager;
import com.example.jms.R;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.BleDeviceDTO;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;
import com.example.jms.home.MainActivity;
import com.example.jms.home.UserDataModel;
import com.example.jms.settings.PasswordFind1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Login extends AppCompatActivity {
    private static final int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    APIViewModel apiViewModel = new APIViewModel();
    SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();
    MyJobScheduler myJobScheduler = new MyJobScheduler();

    EditText txtEmail;
    EditText txtPassword;
    CheckBox checkBox;
    Button loginBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;

    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        UserDataModel.contextP = getApplicationContext();

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        txtEmail = (EditText)findViewById(R.id.login_email);
        txtPassword = (EditText)findViewById(R.id.login_password);
        checkBox = (CheckBox)findViewById(R.id.login_checkbox);
        loginBtn = (Button)findViewById(R.id.login);

        sharedPreferences = getSharedPreferences("boot",0);
        editor = sharedPreferences.edit();

        sharedPreferences2 = getSharedPreferences("ble",0);
        editor2 = sharedPreferences.edit();

        check = sharedPreferences.getString("isCheck","");
        if(check.equals("true")){
            checkBox.setChecked(true);
            txtEmail.setText(sharedPreferences.getString("id",""));
            txtPassword.setText(sharedPreferences.getString("pwd",""));
            loginBtn.performClick();
        }
    }

    @SuppressLint("CheckResult")
    public void loginClick(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if(email.equals("")||email.equals(null)) { Toast.makeText(getApplicationContext(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show(); }
        else if(password.equals("")||password.equals(null)) { Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show(); }
        else {
            UserDTO user = new UserDTO();
            user.setUsername(email);
            user.setPassword(password);

            if (checkBox.isChecked()) {
                editor.putString("id", email);
                editor.putString("pwd", password);
                editor.putString("isCheck", "true");
                editor.commit();
            } else {
                editor.clear();
                editor.commit();
            }

            String ble = sharedPreferences2.getString("mac", "");
            if (!ble.equals("")) {
                BleManager.getInstance().init(getApplication());
                sleepDocViewModel.connectSleepDoc(ble)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> myJobScheduler.setUpdateJob(this))
                        .subscribe(() -> {
                            BleDeviceDTO bleDeviceDTO = new BleDeviceDTO();
                            bleDeviceDTO.setMacAddress(ble);
                            bleDeviceDTO.setKey(sharedPreferences2.getString("key", ""));
                            bleDeviceDTO.setName(sharedPreferences2.getString("name", ""));
                            bleDeviceDTO.setRssi(sharedPreferences2.getInt("rssi", 0));
                            BleService.principalDevice = bleDeviceDTO;
                        }, Throwable::printStackTrace);
                apiViewModel.postAuth(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            RestfulAPI.setToken(data);
                            //UserDataModel[] userDataModel = new UserDataModel[RestfulAPI.principalUser.getFriend().size()+1];
                            //UserDataModel.userDataModels = getData(userDataModel);
                            Log.d("Login", data.getUser().getUsername() + "로그인 성공");
                            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //startActivity(intent);
                            //finish();
                            meAndyou();
                        }, Throwable -> {
                            Toast.makeText(getApplicationContext(),
                                    "이메일 또는 비밀번호가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                apiViewModel.postAuth(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            RestfulAPI.setToken(data);
                            //UserDataModel[] userDataModel = new UserDataModel[RestfulAPI.principalUser.getFriend().size()+1];
                            //UserDataModel.userDataModels = getData(userDataModel);
                            Log.d("Login", data.getUser().getUsername() + "로그인 성공");
                            //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            //startActivity(intent);
                            //finish();
                            meAndyou();
                            myJobScheduler.setUpdateJob(this);
                        }, Throwable -> {
                            Toast.makeText(getApplicationContext(),
                                    "이메일 또는 비밀번호가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                        });
            }

        }
    }

    public void findPassword(View view){
        Intent intent = new Intent(getApplicationContext(), PasswordFind1.class);
        startActivity(intent);
    }

    public void goSignUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    @SuppressLint("CheckResult")
    public void meAndyou() throws ParseException {
        UserDataModel[] userDataModel = new UserDataModel[RestfulAPI.principalUser.getFriend().size()+1];

        getData(userDataModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(()->{
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .subscribe();}

    @SuppressLint("CheckResult")
    public Completable getData(UserDataModel[] userDataModel) throws ParseException {
        return Completable.create(observer -> {
            APIViewModel apiViewModel = new APIViewModel();

            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
            String date = transFormat.format(calendar.getTime());
            Date today = transFormat.parse(date);
            calendar.setTime(today);
            String lastDate = String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastDate = date.substring(0,6)+lastDate;
            Log.d("MainActivity","오늘: "+date+"오늘1: "+today+", 이번달 마지막: "+lastDate);

            String month = date.substring(4,6);
            if(month == "01"){ month = "12"; }
            else{
                int monthI = Integer.parseInt(month)-1;
                if(monthI < 10){ month = "0"+ monthI; }
                else{ month = Integer.toString(monthI); }
            }
            String weekCause = date.substring(0,4)+month+"23";
            Log.d("Login","시작날짜: "+weekCause);

            AtomicInteger count = new AtomicInteger();
            //UserDataModel[] userDataModel = new UserDataModel[RestfulAPI.principalUser.getFriend().size()+1];
            for(int i=0; i< RestfulAPI.principalUser.getFriend().size()+1; i++){
                int finalI = i;
                userDataModel[i] = new UserDataModel();
                userDataModel[i].setPosition(i);
                if(i==0){
                    apiViewModel.getRawdataById(RestfulAPI.principalUser.getId(),"0",weekCause,lastDate)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(data -> {
                                if(data.getContent()!=null){ userDataModel[finalI].setDataList(data.getContent()); }
                                //userDataModel[finalI].setPosition(finalI);
                                Log.d("MainActivity","i 확인: "+finalI);
                                count.getAndIncrement();
                                if(count.get() == (RestfulAPI.principalUser.getFriend().size()+1)*2){
                                    UserDataModel.userDataModels = userDataModel;
                                    observer.onComplete();
                                }
                            },Throwable::printStackTrace);
                    apiViewModel.getGPSById(RestfulAPI.principalUser.getId(),"0")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(data-> {
                                if(data.getContent()!=null){
                                    userDataModel[finalI].setGpsList(data.getContent());
                                    if(data.getContent().size()>0){
                                    userDataModel[finalI].setAddresses(
                                            toAdd(Double.parseDouble(data.getContent().get(0).getLat()),
                                                    Double.parseDouble(data.getContent().get(0).getLon())));
                                    }
                                    Log.d("로그인","i 확인: "+finalI);
                                    count.getAndIncrement();
                                    Log.d("Login",finalI+"카운트 확인: "+count.get()+", "+RestfulAPI.principalUser.getFriend().size());
                                    if(count.get() == (RestfulAPI.principalUser.getFriend().size()+1)*2){
                                        UserDataModel.userDataModels = userDataModel;
                                        observer.onComplete();
                                    }
                                }
                            },Throwable::printStackTrace);
                }
                else{
                    apiViewModel.getRawdataById(RestfulAPI.principalUser.getFriend().get(i-1).getId(),"0",weekCause,lastDate)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(data -> {
                                if(data.getContent()!=null){ userDataModel[finalI].setDataList(data.getContent()); }
                                //userDataModel[finalI].setPosition(finalI);
                                Log.d("MainActivity","i 확인: "+finalI);
                                count.getAndIncrement();
                                if(count.get() == (RestfulAPI.principalUser.getFriend().size()+1)*2){
                                    UserDataModel.userDataModels = userDataModel;
                                    observer.onComplete();
                                }
                            },Throwable::printStackTrace);
                    apiViewModel.getGPSById(RestfulAPI.principalUser.getFriend().get(i-1).getId(),"0")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(data-> {
                                if(data.getContent()!=null){
                                    userDataModel[finalI].setGpsList(data.getContent());
                                    if(data.getContent().size()>0){
                                        userDataModel[finalI].setAddresses(
                                            toAdd(Double.parseDouble(data.getContent().get(0).getLat()),
                                                    Double.parseDouble(data.getContent().get(0).getLon())));
                                    }
                                    Log.d("로그인","i 확인: "+finalI);
                                    count.getAndIncrement();
                                    Log.d("Login",finalI+"카운트 확인: "+count.get()+", "+RestfulAPI.principalUser.getFriend().size());
                                    if(count.get() == (RestfulAPI.principalUser.getFriend().size()+1)*2){
                                        UserDataModel.userDataModels = userDataModel;
                                        observer.onComplete();
                                    }
                                }
                            },Throwable::printStackTrace);
                }
            }
            Log.d("Login","카운트 확인: "+count.get()+", "+(RestfulAPI.principalUser.getFriend().size()+1)*2);
        });
    }

    public String toAdd(double lat, double lon){
        Geocoder geocoder = new Geocoder(this);
        List<Address> add = null;
        try{ add = geocoder.getFromLocation(lat,lon,10); }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity","주소변환 불가 "+lat+", "+lon);
        }
        Address address = add.get(0);
        return address.getAddressLine(0).substring(5);
    }

    //로그인과 관련없은 권한 관련임.
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}