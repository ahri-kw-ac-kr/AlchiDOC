package com.example.jms.etc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.jms.settings.PasswordFind1;

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

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else{
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
    }
    @SuppressLint("CheckResult")
    public void loginClick(View view){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        UserDTO user = new UserDTO();
        user.setUsername(email);
        user.setPassword(password);

        if(checkBox.isChecked()){
            editor.putString("id",email);
            editor.putString("pwd",password);
            editor.putString("isCheck","true");
            editor.commit();
        } else{
            editor.clear();
            editor.commit();
        }

        String ble = sharedPreferences2.getString("mac","");
        if(!ble.equals("")){
            BleManager.getInstance().init(getApplication());
            sleepDocViewModel.connectSleepDoc(ble)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete(()->myJobScheduler.setUpdateJob(this))
                    .subscribe(()->{
                        BleDeviceDTO bleDeviceDTO = new BleDeviceDTO();
                        bleDeviceDTO.setMacAddress(ble);
                        bleDeviceDTO.setKey(sharedPreferences2.getString("key",""));
                        bleDeviceDTO.setName(sharedPreferences2.getString("name",""));
                        bleDeviceDTO.setRssi(sharedPreferences2.getInt("rssi",0));
                        BleService.principalDevice = bleDeviceDTO;
                    },Throwable::printStackTrace);
            apiViewModel.postAuth(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        RestfulAPI.setToken(data);
                        Log.d("Login", data.getUser().getUsername() + "로그인 성공");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish(); }, Throwable -> { Toast.makeText(getApplicationContext(),
                            "이메일 또는 비밀번호가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                    });
        }else{
        apiViewModel.postAuth(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    RestfulAPI.setToken(data);
                    Log.d("Login", data.getUser().getUsername() + "로그인 성공");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    myJobScheduler.setUpdateJob(this);}, Throwable -> { Toast.makeText(getApplicationContext(),
                        "이메일 또는 비밀번호가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                });
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