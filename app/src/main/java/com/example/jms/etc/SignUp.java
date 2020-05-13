package com.example.jms.etc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.settings.PasswordFind1;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        EditText txtName = (EditText)findViewById(R.id.name);
        EditText txtBirth = (EditText)findViewById(R.id.birth);
        EditText txtEmail = (EditText)findViewById(R.id.email);
        EditText txtPassword = (EditText)findViewById(R.id.password);

        String name = txtName.getText().toString();
        String birth = txtBirth.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        APIViewModel apiViewModel = new APIViewModel();
    }
    public void goLogin(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }
}