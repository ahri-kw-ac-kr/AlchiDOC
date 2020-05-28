package com.example.jms.home;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.example.jms.R;
import com.google.android.material.tabs.TabLayout;


/*아직 ACT 버튼만 test로 만든 상태입니다.*/
public class LightButtonActivity extends AppCompatActivity {

    String code = "Light";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightbutton_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
        tabs.addTab(tabs.newTab().setText("일간"));
        tabs.addTab(tabs.newTab().setText("주간"));
        tabs.addTab(tabs.newTab().setText("월간"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final MyLightPagerAdapter myPagerAdapter = new MyLightPagerAdapter(getSupportFragmentManager(),3);
        viewPager.setAdapter(myPagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));


    }
}


//http://blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=221347183776&categoryNo=0&parentCategoryNo=56&viewDate=&currentPage=1&postListTopCurrentPage=1&from=section&userTopListOpen=true&userTopListCount=10&userTopListManageOpen=false&userTopListCurrentPage=1
//해당 링크 참고