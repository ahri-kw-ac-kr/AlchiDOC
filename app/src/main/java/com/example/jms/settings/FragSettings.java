package com.example.jms.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
<<<<<<< HEAD
=======
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.viewmodel.APIViewModel;
>>>>>>> b13c555bf66f643c6df3f318a734b50b5d91b21f
import com.example.jms.etc.Login;
import com.example.jms.home.MainActivity;

public class FragSettings extends Fragment {

    private View view;
    MainActivity mainActivity;
    LinearLayout layButton1, layButton2, layButton3, layButton4, layButton5;
    TextView logoutButton;
    TextView myInfo;


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
        myInfo = (TextView)view.findViewById(R.id.myInfo);

        myInfo.setText(RestfulAPI.principalUser.getUsername());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                Intent intent = new Intent(getActivity(), Login.class);

=======
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setMessage("로그아웃 하시겠습니까?");
                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        RestfulAPI.logout();
                    }
                });

                ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
>>>>>>> b13c555bf66f643c6df3f318a734b50b5d91b21f
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
