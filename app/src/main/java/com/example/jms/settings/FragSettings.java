package com.example.jms.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.home.MainActivity;

public class FragSettings extends Fragment {

    private View view;
    MainActivity mainActivity;
    Profile profile;
    LinearLayout layButton1, layButton2, layButton3, layButton4, layButton5;
    TextView logoutButton;


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_settings, container, false);

        layButton1 = (LinearLayout) view.findViewById(R.id.setButton1);
        layButton2 = (LinearLayout) view.findViewById(R.id.setButton2);
        layButton3 = (LinearLayout) view.findViewById(R.id.setButton3);
        layButton4 = (LinearLayout) view.findViewById(R.id.setButton4);
        layButton5 = (LinearLayout) view.findViewById(R.id.setButton5);
        logoutButton = (TextView) view.findViewById(R.id.logout_button);


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
            }
        });

        layButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Profile.class);
                startActivity(intent);
            }
        });

        layButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeviceSet1.class);
                startActivity(intent);
            }
        });

        layButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CareList.class);
                startActivity(intent);
            }
        });

        layButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Guide.class);
                startActivity(intent);
            }
        });

        layButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terms.class);
                startActivity(intent);
            }
        });

        return view;
    }



}
