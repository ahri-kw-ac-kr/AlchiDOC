package com.example.jms.etc;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jms.R;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.settings.Terms;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignUp extends AppCompatActivity {

    private EditText birth;
    private DatePickerDialog.OnDateSetListener callbackMethod;

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

        this.InitializeView();
        this.InitializeListener();

        txtName = (EditText)findViewById(R.id.name);
        txtBirth = (EditText)findViewById(R.id.birth);
        txtEmail = (EditText)findViewById(R.id.email);
        txtPhone = (EditText)findViewById(R.id.phone);
        txtPassword = (EditText)findViewById(R.id.password);
        txtChkPassword = (EditText)findViewById(R.id.checkPassword);
        rg = (RadioGroup)findViewById(R.id.radioGroup);
    }

    public void InitializeView()
    {
        birth = (EditText)findViewById(R.id.birth);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                if((dayOfMonth < 10) && (monthOfYear < 10)){
                    birth.setText(year + "0" + (monthOfYear + 1) + "0" + dayOfMonth);}
                else if(dayOfMonth < 10){
                    birth.setText(year + "" + (monthOfYear + 1) + "0" + dayOfMonth);
                }
                else if(monthOfYear < 10){
                    birth.setText(year + "0" + (monthOfYear + 1) + "" + dayOfMonth);
                }
                else{
                    birth.setText(year + "" + (monthOfYear + 1) + "" + dayOfMonth);
                }
            }
        };
    }

    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, day);
        dialog.show();
    }

    public void goLogin(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void goTerms(View view){
        Intent intent = new Intent(getApplicationContext(), Terms.class);
        startActivity(intent);
    }

    @SuppressLint("CheckResult")
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
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    }},0);
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
                                else{Log.d("회원가입",Throwable.getMessage());}
                            });
        }

    }
}