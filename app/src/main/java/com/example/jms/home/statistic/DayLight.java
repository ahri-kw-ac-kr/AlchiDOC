package com.example.jms.home.statistic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.home.UserDataModel;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DayLight extends Fragment {

    TextView titleDay;
    TextView titlePercent;
    TextView totalT;
    TextView daySumLux;
    TextView evenSumLux;
    TextView evenAvgK;
    TextView nightSumLux;
    TextView nightAvgK;

    TextView dayLightPlan1;
    TextView dayLightPlan2;
    TextView dayLightPlan3;
    ImageView face1;
    ImageView face2;
    ImageView face3;
    TextView state1;
    TextView state2;
    TextView state3;



    View view;
    int percent;
    double todayTotal;
    String titleD;

    //constructor
    public DayLight(){}

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.day_light, container, false);
        BarChart mBarChart = view.findViewById(R.id.bar);

        //어떤 사람인지 (카드별로 버튼이 필요함)
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HHmm");
        String curr = transFormat.format(calendar.getTime());


        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.dayLightDate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일"; }
        titleDay.setText(titleD);


        //오늘동안의 리스트가 없을경우 트라이캐치 실행
        if(user.getTodayList()==null){
            try { user.parsingDay(pos); } catch (ParseException e) { e.printStackTrace(); }
        }

        String EvenP = Integer.toString(percent);
        daySumLux = view.findViewById(R.id.daylux1) ;
        evenSumLux = view.findViewById(R.id.daylux2);
        nightSumLux = view.findViewById(R.id.daylux3);
        evenAvgK = view.findViewById(R.id.dayK1);
        nightAvgK = view.findViewById(R.id.dayK2);
        totalT = view.findViewById(R.id.dayLightPercent);

        daySumLux.setText(""+user.getStatLight().getDayDaySumLux());
        evenSumLux.setText(""+user.getStatLight().getDayEvenSumLux());
        evenAvgK.setText(""+user.getStatLight().getDayEvenSumTemp()/4);
        nightSumLux.setText(""+user.getStatLight().getDayNightSumLux());
        nightAvgK.setText(""+user.getStatLight().getDayNightSumTemp()/10);
        totalT.setText("조도량 "+user.getStatLight().getDayPercent()+"%");

        //시간 상관없이 Y축높이는 Lux, 그래프 바 색상은 K
        //주간 - 기상 ~ 수면 4시간 전 - Lux총합

        for (int i = 0; i < 23; i++) {
            if (user.getStatLight().getDaySumHourTemp()[i]>6000) {
                //시간당 조도량이 6000K를 넘을 경우
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#d84315")));

            }

            else if (3000 < user.getStatLight().getDaySumHourTemp()[i] || user.getStatLight().getDaySumHourTemp()[i] <= 6000) {
                //시간당 조도량이 3000~6000K
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#fb8c00")));
            }

            else if(user.getStatLight().getDaySumHourTemp()[i]<3000) {
                //시간당 조도량이 3000 미만
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#fb8c00")));
            }

            else{
                //이하밖에없겠지...?
                mBarChart.addBar(new BarModel(Integer.toString(i), user.getStatLight().getDaySumHourLuxWgt()[i], Color.parseColor("#5F9919")));
            }

        }
        mBarChart.startAnimation();

        dayLightPlan1 = (TextView) view.findViewById(R.id.dayLightPlan1);
        dayLightPlan2 = (TextView) view.findViewById(R.id.dayLightPlan2);
        dayLightPlan3 = (TextView) view.findViewById(R.id.dayLightPlan3);
        face1 = (ImageView) view.findViewById(R.id.dayLightFace1);
        face2 = (ImageView) view.findViewById(R.id.dayLightFace2);
        face3 = (ImageView) view.findViewById(R.id.dayLightFace3);
        state1 = (TextView) view.findViewById(R.id.dayLightState1);
        state2 = (TextView) view.findViewById(R.id.dayLightState2);
        state3 = (TextView) view.findViewById(R.id.dayLightState3);

        if(user.getTodayList().size()==0){


        }
        else {
            //주간 코멘트
            if (user.getStatLight().getDayPercent() < 100) {
                //부족 멘트
                dayLightPlan1.setText(R.string.dayLightComment2);
                face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                state1.setText(R.string.bad);
            } else {
                //충분 멘트
                dayLightPlan1.setText(R.string.dayLightComment1);
                face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                state1.setText(R.string.good);

            }

            int currHour = Integer.parseInt(curr.substring(9, 11)); //현재 시
            int dinnerFlag = 0;
            int eveningFlag = 0;

            //나 = 사용자
            if (pos == 0) {
                //내 수면시간 가져와서 저녁, 야간 먹이기
                int setHour4 = Integer.parseInt(RestfulAPI.principalUser.getSleep().substring(0, 2)) - 4; // 취침 4시간 전 hour
                int setHour2 = Integer.parseInt(RestfulAPI.principalUser.getSleep().substring(0, 2)) - 2; // 취침 2시간 전 hour

                //나의 저녁 코멘트
                if (Integer.parseInt(curr.substring(9)) >= Integer.parseInt(RestfulAPI.principalUser.getSleep()) - 400) //4시간 빼줬습니다
                {
                    for (int i = setHour4 - 1; i < currHour; i++) { //0부터 받아오더라..
                        for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                            if (user.getPerHour().get(i).get(j).getAvgLux() > 400 || user.getPerHour().get(i).get(j).getAvgTemp() > 3000) {
                                dinnerFlag = 1;
                                break;
                            }
                        }
                        if (dinnerFlag == 1) {
                            break;
                        }
                    }
                    if (dinnerFlag == 1) {
                        //////////////////////저녁 과다
                        dayLightPlan2.setText(R.string.dayLightComment3);
                        face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state2.setText(R.string.exceed);
                    } else {
                        ///////////////////////저녁 적정
                        dayLightPlan2.setText(R.string.dayLightComment4);
                        face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        state2.setText(R.string.good);
                    }
                } else {
                    dayLightPlan2.setText(R.string.noData);
                } //그 시간대가 아닐때 비워두기.


                //나의 야간 코멘트 - 200빼기
                if (Integer.parseInt(curr.substring(9)) >= Integer.parseInt(RestfulAPI.principalUser.getSleep()) - 200) {
                    for (int i = setHour2 - 1; i < currHour; i++) {
                        for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                            if (user.getPerHour().get(i).get(j).getAvgLux() > 300 || user.getPerHour().get(i).get(j).getAvgTemp() > 2000) {
                                eveningFlag = 1;
                                break;
                            }
                        }
                        if (eveningFlag == 1) {
                            break;
                        }
                    }
                    if (eveningFlag == 1) {
                        ////////////////////야간 과다
                        dayLightPlan3.setText(R.string.dayLightComment5);
                        face3.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state3.setText(R.string.exceed);
                    } else {
                        //////////////////////야간 적정
                        dayLightPlan3.setText(R.string.dayLightComment6);
                        face3.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        state3.setText(R.string.good);
                    }
                } else {
                    dayLightPlan3.setText(R.string.noData);
                } //그 시간대가 아닐때 비워두기.

            }

            //친구 수면시간 가져오기
            else {
                // 친구 수면시간 가져와서 저녁, 야간 먹이기
                int setHour4 = Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos - 1).getSleep().substring(0, 2)) - 4; // 취침 4시간 전 시
                int setHour2 = Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos - 1).getSleep().substring(0, 2)) - 2; // 취침 2시간 전 시

                //친구의 저녁 코멘트
                if (Integer.parseInt(curr.substring(9)) >= Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos - 1).getSleep()) - 400) {
                    for (int i = setHour4 - 1; i < currHour; i++) {
                        for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                            if (user.getPerHour().get(i).get(j).getAvgLux() > 400 || user.getPerHour().get(i).get(j).getAvgTemp() > 3000) {
                                dinnerFlag = 1;
                                break;
                            }
                        }
                        if (dinnerFlag == 1) {
                            break;
                        }
                    }
                    if (dinnerFlag == 1) {
                        //////////////////저녁 과다
                        dayLightPlan2.setText(R.string.dayLightComment3);
                        face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state2.setText(R.string.exceed);
                    } else {
                        //////////////////저녁 적정
                        dayLightPlan2.setText(R.string.dayLightComment4);
                        face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        state2.setText(R.string.good);
                    }
                } else {
                    dayLightPlan2.setText(R.string.noData);
                } //그 시간대가 아닐때 비워두기.


                //친구의 야간 코멘트
                if (Integer.parseInt(curr.substring(9)) >= Integer.parseInt(RestfulAPI.principalUser.getFriend().get(pos - 1).getSleep()) - 200) {
                    for (int i = setHour2 - 1; i < currHour; i++) {
                        for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                            if (user.getPerHour().get(i).get(j).getAvgLux() > 300 || user.getPerHour().get(i).get(j).getAvgTemp() > 2000) {
                                eveningFlag = 1;
                                break;
                            }
                        }
                        if (eveningFlag == 1) {
                            break;
                        }
                    }
                    if (eveningFlag == 1) {
                        /////////////////야간 과다
                        dayLightPlan3.setText(R.string.dayLightComment5);
                        face3.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state3.setText(R.string.exceed);
                    } else {
                        /////////////////야간 적정
                        dayLightPlan3.setText(R.string.dayLightComment6);
                        face3.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        state3.setText(R.string.good);
                    }
                } else {
                    dayLightPlan3.setText(R.string.noData);
                }


            }

        }


        return view;
    }
}

