package com.example.jms.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;

public class PasswordFind2 extends AppCompatActivity {

    EditText firstText, secondText;
    ImageView setImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_find2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow2_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        firstText = (EditText)findViewById(R.id.firstText);
        secondText = (EditText)findViewById(R.id.secondText);
        setImage = (ImageView)findViewById(R.id.setImage);

        // !!!수정 필요!!!
        // 첫 번째에 입력한 비밀번호와 두 번째로 입력한 비밀번호가 같은지 확인
        secondText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(firstText.getText().toString().equals(secondText.getText().toString())) {
                    setImage.setImageResource(R.drawable.ic_check_black_24dp); //같으면 체크 아이콘
                } else {
                    setImage.setImageResource(R.drawable.ic_wrong_black_24dp); //다르면 엑스 아이콘
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}