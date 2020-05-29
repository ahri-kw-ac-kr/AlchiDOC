package com.example.jms.settings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.etc.Login;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Profile extends AppCompatActivity {

    private EditText birth;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    APIViewModel apiViewModel = new APIViewModel();

    EditText nameT;
    EditText birthT;
    EditText phoneT;
    RadioGroup radioGroup;
    RadioButton sexBtn;
    RadioButton maleB;
    RadioButton femaleB;
    EditText currPwdT;
    EditText newPwdT;
    EditText chkPwdT;
    Button editBtn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        this.InitializeView();
        this.InitializeListener();

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nameT = (EditText)findViewById(R.id.editName);
        birthT = (EditText)findViewById(R.id.editBirth);
        phoneT = (EditText)findViewById(R.id.editPhone);
        radioGroup = (RadioGroup)findViewById(R.id.editRadioGroup);
        newPwdT = (EditText)findViewById(R.id.editNewPwd);
        chkPwdT = (EditText)findViewById(R.id.editChkPwd);
        editBtn = (Button)findViewById(R.id.editBtn);
        maleB = (RadioButton)findViewById(R.id.editMale);
        femaleB = (RadioButton)findViewById(R.id.editFemale);

        nameT.setHint(RestfulAPI.principalUser.getFullname());
        birthT.setHint(RestfulAPI.principalUser.getBirth());
        phoneT.setHint(RestfulAPI.principalUser.getPhone());
        if(RestfulAPI.principalUser.getSex().equals("m")) { maleB.setChecked(true); }
        else { femaleB.setChecked(true); }


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioGroup.isSelected()){ sexBtn = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()); }

                String sex;
                String name = nameT.getText().toString();
                String birth = birthT.getText().toString();
                String phone = phoneT.getText().toString();
                String newPwd = newPwdT.getText().toString();
                String chkPwd = chkPwdT.getText().toString();

                UserDTO user = new UserDTO();

                if(sexBtn != null){
                    sex = sexBtn.getText().toString();
                    if(sex.length()!=0){
                        if(sex.equals("남자")) { user.setSex("m"); }
                        if(sex.equals("여자")) { user.setSex("f"); }
                    }
                }

                if(name.length()!=0){ user.setFullname(name); }
                if(birth.length()!=0){ user.setBirth(birth); }
                if(phone.length()!=0){ user.setPhone(phone); }
                if(newPwd.length()!=0 && !newPwd.equals(chkPwd)){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                if((newPwd.length()!=0 && newPwd.equals(chkPwd)) || (newPwd.length()==0 && chkPwd.length()==0)){
                    user.setNewpassword(newPwd);

                    Log.d("Profile",""+user.getNewpassword());
                    sharedPreferences = getSharedPreferences("boot",0);
                    editor = sharedPreferences.edit();
                    check = sharedPreferences.getString("isCheck","");

                    apiViewModel.patchUser(RestfulAPI.principalUser.getId(),user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                        RestfulAPI.principalUser = result;
                                        Log.d("Profile",""+result.getFullname());
                                        if(check.equals("true")){
                                            editor.putString("id", result.getUsername());
                                            editor.putString("pwd", newPwd);
                                            editor.putString("isCheck", "true");
                                            editor.commit();
                                        }
                                        Toast.makeText(getApplicationContext(), "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    },
                                    Throwable::printStackTrace);
                }
            }
        });

    }
    public void InitializeView()
    {
        birth = (EditText)findViewById(R.id.editBirth);
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

}