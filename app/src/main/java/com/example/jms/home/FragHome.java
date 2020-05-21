package com.example.jms.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;

import cn.nightcode.sliderIndicator.SliderIndicator;

public class FragHome extends Fragment {

    MainActivity mainActivity;
    private ViewPager mainViewPager;
    private SliderIndicator indicator;
    private com.example.jms.home.SamplePagerAdapter pagerAdapter;
    private View view;

    Button button1, button2, button3;
    ImageButton sleepStart, report;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //이 메소드가 호출될떄는 프래그먼트가 엑티비티위에 올라와있는거니깐 getActivity메소드로 엑티비티참조가능
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //이제 더이상 엑티비티 참초가안됨
        mainActivity = null;
    }

    public FragHome(){    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);

        //각 버튼 누르면 다른 페이지로 이동하는 역할!!
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button)view.findViewById(R.id.button3);
        report = (ImageButton) view.findViewById(R.id.report);
        sleepStart = (ImageButton) view.findViewById(R.id.sleepStart);

        //메인화면 상단에 사용자 옆으로 넘겨서 볼 수 있게 하는 역할
        mainViewPager = (ViewPager) view.findViewById(R.id.main_view_pager);
        //pagerAdapter = new com.example.jms.SamplePagerAdapter(container.getContext());
        pagerAdapter = new com.example.jms.home.SamplePagerAdapter(getActivity());
        mainViewPager.setAdapter(pagerAdapter);

        indicator = (SliderIndicator) view.findViewById(R.id.main_slide_indicator);
        if(RestfulAPI.principalUser.getFriend()!=null){
            pagerAdapter.setCount(RestfulAPI.principalUser.getFriend().size()+1);//나중에 이 부분을 보호자 숫자대로 바꿔야함..
        }
        else{
            pagerAdapter.setCount(1);
        }
        indicator.setupWithViewPager(mainViewPager);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LightButtonActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActButtonActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SleepButtonActivity.class);
                startActivity(intent);
            }
        });

        sleepStart.setOnClickListener(new View.OnClickListener() {
            //https://loveiskey.tistory.com/206
            //취침 중 서비스 해당 게시글 참조해서 구현
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setMessage("취침을 시작하시겠습니까?");
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //확인버튼을 누르면 다이얼로그가 사라지고 SleepActivity가 실행되도록
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), SleepActivity.class);
                        startActivity(intent);
                        Log.d("D","버튼클릭됨");
                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.example.jms.home.Report.class);
                startActivity(intent);
            }
        });

        return view;

    }


}





