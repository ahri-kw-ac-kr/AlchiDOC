package com.example.jms.etc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.settings.PasswordFind1;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignUp extends AppCompatActivity {
    APIViewModel apiViewModel = new APIViewModel();

    EditText txtName;
    EditText txtBirth;
    EditText txtPhone;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtChkPassword;
    RadioGroup rg;
    RadioButton btnSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        txtName = (EditText)findViewById(R.id.name);
        txtBirth = (EditText)findViewById(R.id.birth);
        txtEmail = (EditText)findViewById(R.id.email);
        txtPhone = (EditText)findViewById(R.id.phone);
        txtPassword = (EditText)findViewById(R.id.password);
        txtChkPassword = (EditText)findViewById(R.id.checkPassword);
        rg = (RadioGroup)findViewById(R.id.radioGroup);
    }
    public void goLogin(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void signUp(View view){
        btnSex = (RadioButton)findViewById(rg.getCheckedRadioButtonId());

        String name = txtName.getText().toString();
        String birth = txtBirth.getText().toString();
        String phone = txtPhone.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String checkPassword = txtChkPassword.getText().toString();

        if(name.length()==0){ Toast.makeText(getApplicationContext(), "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(birth.length()==0){ Toast.makeText(getApplicationContext(), "생일을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(phone.length()==0){ Toast.makeText(getApplicationContext(), "전화번호를 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(email.length()==0){ Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(password.length()==0){ Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(checkPassword.length()==0){ Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(btnSex== null){ Toast.makeText(getApplicationContext(), "성별을 선택해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(!password.equals(checkPassword)){ Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show(); }
        else {
            String sex = btnSex.getText().toString();
            if(sex.equals("남자")){ sex = "m"; }
            else { sex = "f"; }

            Log.d("SignUp",name+" , "+birth+" , "+phone+" , "+email+" , "+password+" , "+checkPassword+" , "+sex);

            UserDTO user = new UserDTO();
            user.setFullname(name);
            user.setBirth(birth);
            user.setPhone(phone);
            user.setUsername(email);
            user.setPassword(password);
            user.setSex(sex);

            apiViewModel.postRegister(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(data -> {
                        Log.d("SignUp",data.getUsername()+" 회원가입 완료");
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                        },
                            Throwable -> {
                                if(Throwable instanceof HttpException){
                                    HttpException e = (HttpException) Throwable;
                                    switch (e.code()){
                                        case 409:
                                            Toast.makeText(getApplicationContext(),
                                                    "이미 존재하는 이메일입니다. 다른 이메일을 작성해 주세요.", Toast.LENGTH_SHORT).show();
                                        //case 500:
                                          //  Toast.makeText(getApplicationContext(),
                                            //        "연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
        }

    }
}