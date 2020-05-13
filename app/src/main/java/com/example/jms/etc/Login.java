package com.example.jms.etc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.home.MainActivity;
import com.example.jms.settings.PasswordFind1;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Login extends AppCompatActivity {
    APIViewModel apiViewModel = new APIViewModel();

    EditText txtEmail;
    EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtEmail = (EditText)findViewById(R.id.login_email);
        txtPassword = (EditText)findViewById(R.id.login_password);

    }
    public void loginClick(View view){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        UserDTO user = new UserDTO();
        user.setUsername(email);
        user.setPassword(password);

        apiViewModel.postAuth(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    RestfulAPI.setToken(data);
                    Log.d("Login",data.getUser().getUsername()+"로그인 성공");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish(); }, Throwable -> {
                    Toast.makeText(getApplicationContext(),
                            "이메일 또는 비밀번호가 잘못 되었습니다.", Toast.LENGTH_SHORT).show();
                });
    }

    public void findPassword(View view){
        Intent intent = new Intent(getApplicationContext(), PasswordFind1.class);
        startActivity(intent);
    }

    public void goSignUp(View view){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}