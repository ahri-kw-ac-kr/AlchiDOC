package com.example.jms.etc;

import android.content.Intent;
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
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.settings.PasswordFind1;

public class SignUp extends AppCompatActivity {
    APIViewModel apiViewModel = new APIViewModel();

    EditText txtName;
    EditText txtBirth;
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
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String checkPassword = txtChkPassword.getText().toString();

        if(name.length()==0){ Toast.makeText(getApplicationContext(), "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(birth.length()==0){ Toast.makeText(getApplicationContext(), "생일을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(email.length()==0){ Toast.makeText(getApplicationContext(), "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(password.length()==0){ Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(checkPassword.length()==0){ Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(btnSex== null){ Toast.makeText(getApplicationContext(), "성별을 선택해 주세요.", Toast.LENGTH_SHORT).show(); }
        else if(!password.equals(checkPassword)){ Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show(); }
        else {
            String sex = btnSex.getText().toString();
            Log.d("SignUp",name+" , "+birth+" , "+email+" , "+password+" , "+checkPassword+" , "+sex);
        }

    }
}