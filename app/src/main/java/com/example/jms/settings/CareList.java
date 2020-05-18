package com.example.jms.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.viewmodel.APIViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class CareList extends AppCompatActivity {
    private final int dynamicIDp = 2000;
    private final int dynamicIDd = 5000;

    APIViewModel apiViewModel = new APIViewModel();
    UserDTO user;
    List<UserDTO> doctor;

    LinearLayout doctorList;
    LinearLayout careList;

    Button addPerson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_list);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow1_back_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        user = RestfulAPI.principalUser;
        doctor = RestfulAPI.myDoctor;

        doctorList = (LinearLayout)findViewById(R.id.doctor);
        careList = (LinearLayout)findViewById(R.id.patient);

        //추가하기 버튼
        addPerson = (Button) findViewById(R.id.add_person);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linear = (LinearLayout) View.inflate(CareList.this, R.layout.friends_plus, null);
                new AlertDialog.Builder(CareList.this)
                        .setView(linear)
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText id = (EditText) linear.findViewById(R.id.input_id);
                                String value = id.getText().toString();
                                apiViewModel.plusFriend(user.getId(),value)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(result ->{
                                            Toast.makeText(getApplicationContext(),
                                                    "친구가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                            apiViewModel.myDoctor(RestfulAPI.principalUser.getId(), "0")
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(body -> {
                                                        RestfulAPI.myDoctor = body.getContent();
                                                        updateDoctor();},Throwable::printStackTrace);
                                                },Throwable -> {if(Throwable instanceof HttpException) {
                                                    HttpException e = (HttpException) Throwable;
                                                    switch (e.code()) {
                                                        case 404:
                                                            Toast.makeText(getApplicationContext(),
                                                                    "해당 아이디를 가진 사용자가 없습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }});
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                invalidateOptionsMenu();
            }
        });

        updateDoctor();
        updatePatient();


    }

    public void updateDoctor(){
        doctorList.removeAllViews();
        doctor = RestfulAPI.myDoctor;
        if(doctor!=null) {
            Log.d("CareList","친구 수 "+doctor.size());
            CareListSub subD[] = new CareListSub[doctor.size()];
            for (int i = 0; i < doctor.size(); i++) {
                subD[i] = new CareListSub(getApplicationContext());
                subD[i].linearLayout.setId(dynamicIDd + i);
                String friendInfo = doctor.get(i).getFullname()
                        + " (" + doctor.get(i).getPhone().substring(0, 3)
                        + "-****-" + doctor.get(i).getPhone().substring(7) + ")";
                subD[i].textView.setText(friendInfo);
                Log.d("CareList","친구 "+friendInfo);
                doctorList.addView(subD[i]);

                ImageButton del = subD[i].imageButton;
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFilterPopup(v, "doctor");
                    }
                });
            }
        }
    }
    public void updatePatient(){
        careList.removeAllViews();
        if(user.getFriend() != null) {
            CareListSub subP[] = new CareListSub[user.getFriend().size()];
            for (int i = 0; i < user.getFriend().size(); i++) {
                subP[i] = new CareListSub(getApplicationContext());
                subP[i].linearLayout.setId(dynamicIDp + i);
                String friendInfo = user.getFriend().get(i).getFullname()
                        + " (" + user.getFriend().get(i).getPhone().substring(0, 3)
                        + "-****-" + user.getFriend().get(i).getPhone().substring(7) + ")";
                subP[i].textView.setText(friendInfo);
                careList.addView(subP[i]);

                ImageButton del = subP[i].imageButton;
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFilterPopup(v, "patient");
                    }
                });
            }
        }
    }

    // '삭제하기'를 누르면 실행되는 작업.
    public void showFilterPopup(View v,String s) {
        PopupMenu popup = new PopupMenu(this,v);
        popup.inflate(R.menu.delete);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        int id = ((View)v.getParent()).getId();
                        if(s.equals("doctor")){
                            Log.d("CareList","id: "+id+" id-daynamicID: "+Integer.toString(id-dynamicIDd));
                            apiViewModel.delFriend(doctor.get(id-dynamicIDd).getId(),user.getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(result -> {
                                                Log.d("CareList",user.getFriend().toString());
                                                Toast.makeText(CareList.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                                doctorList.removeView((View)v.getParent().getParent());
                                                doctor.remove(id-dynamicIDd);},
                                            Throwable::printStackTrace);
                        }
                        else if(s.equals("patient")){
                            careList.removeView((View)v.getParent().getParent());
                            Log.d("CareList","id: "+id+" id-daynamicID: "+Integer.toString(id-dynamicIDp));
                            apiViewModel.delFriend(user.getId(),user.getFriend().get(id-dynamicIDp).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(result -> {
                                                Log.d("CareList",user.getFriend().toString());
                                                Toast.makeText(CareList.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                                doctorList.removeView((View)v.getParent().getParent());
                                                RestfulAPI.principalUser = result;},
                                            Throwable::printStackTrace);
                        }

                        ((View) v.getParent().getParent()).invalidate();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }
}
