package com.example.jms.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.viewmodel.APIViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PasswordFind1 extends AppCompatActivity {

    EditText emailT;
    EditText numT;
    Button  certifyB;

    APIViewModel apiViewModel = new APIViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_find);

        emailT = (EditText)findViewById(R.id.findEmail);
        numT = (EditText)findViewById(R.id.findNum);

        String email = emailT.getText().toString();
        String pwd = numT.getText().toString();

        apiViewModel.forget(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name->{},Throwable::printStackTrace);

    }
}