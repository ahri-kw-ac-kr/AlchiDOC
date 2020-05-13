package com.example.jms.etc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.home.MainActivity;
import com.example.jms.settings.PasswordFind1;

public class Login extends AppCompatActivity {
    TextView find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow2_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        /*find = findViewById(R.id.find);

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, PasswordFind1.class);
                startActivity(intent);
            }
        });*/
    }
    public void loginClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
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