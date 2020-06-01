package com.example.jms.etc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.viewmodel.APIViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PasswordFind1 extends AppCompatActivity {

    EditText emailT;
    EditText numT;
    EditText pwdT;
    Button  certifyB;
    Button okB;

    APIViewModel apiViewModel = new APIViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_find);

        /*emailT = (EditText)findViewById(R.id.findEmail);
        numT = (EditText)findViewById(R.id.findNum);
        pwdT = (EditText)findViewById(R.id.findPwd);
        certifyB = (Button)findViewById(R.id.email_send);
        okB = (Button)findViewById(R.id.findButton);

        String email = emailT.getText().toString();
        String num = numT.getText().toString();
        String pwd = pwdT.getText().toString();

        certifyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiViewModel.forget(email)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(name->{
                            Toast.makeText(getApplicationContext(), "입력하신 이메일로 인증번호를 발송하였습니다.\n5분 안으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                        },Throwable::printStackTrace);
            }
        });

        okB.setOnClickListener();*/
    }

    public void sendEmail(View view){
        emailT = (EditText)findViewById(R.id.findEmail);
        String email = emailT.getText().toString();
        apiViewModel.forget(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name->{
                    Toast.makeText(getApplicationContext(), R.string.password_find14, Toast.LENGTH_SHORT).show();
                },Throwable::printStackTrace);
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.209.225.252/api/v1/user/forget/?username="+email)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();*/
    }

    @SuppressLint("CheckResult")
    public void initPassword(View view){
        emailT = (EditText)findViewById(R.id.findEmail);
        numT = (EditText)findViewById(R.id.findNum);
        pwdT = (EditText)findViewById(R.id.findPwd);

        String email = emailT.getText().toString();
        String num = numT.getText().toString();
        String pwd = pwdT.getText().toString();

        apiViewModel.initPassword(email,num,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name->{
                    Toast.makeText(getApplicationContext(), R.string.password_find15, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                },Throwable -> {
                    if(Throwable instanceof HttpException){
                        HttpException e = (HttpException) Throwable;
                        switch (e.code()){
                            case 401:
                                Toast.makeText(getApplicationContext(),
                                        R.string.password_find15, Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getApplicationContext(),
                                        R.string.password_find16, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    else{
                        Log.d("비밀번호 찾기",Throwable.getMessage());}
                });
    }
}