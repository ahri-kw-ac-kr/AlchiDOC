package com.example.jms.home;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.jms.R;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Report extends AppCompatActivity{

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 dd일 달성률");
    TextView mTextView;

    ArcProgress act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView.setText(getTime());

        //사용자 정의
        UserDataModel user = UserDataModel.userDataModels[0];

        ////////////////////////////////활동량///////////////////////////////
        //활동량 그래프
        act = (ArcProgress) findViewById(R.id.arc_progress2);
        act.setProgress(user.getStatAct().getDayPercent());

        //활동량 코멘트
        int actSun = 0;
        int actMoon = 0;
        for(int i=9; i<18; i++){ actSun += user.getStatAct().getDaySumHour()[i]; }
        for(int i=18; i<21; i++){ actMoon += user.getStatAct().getDaySumHour()[i]; }
        if(actSun >= 6000){
            ///주간 충분
        }
        else{
            ///주간 부족
        }
        if(actMoon <= 2000){
            ///야간 적정
        }
        else{
            ///야간 과다
        }

        ////////////////////////////////조도량///////////////////////////////


        ////////////////////////////////수면량///////////////////////////////


    }


    ///현재시간///
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
