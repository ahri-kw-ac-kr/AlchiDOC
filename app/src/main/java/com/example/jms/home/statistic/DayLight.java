package com.example.jms.home.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.home.UserDataModel;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;


public class DayLight extends Fragment {

    TextView titleDay;
    TextView titlePercent;
    TextView totalT;
    TextView daySumLux;
    TextView evenSumLux;
    TextView evenAvgK;
    TextView nightSumLux;
    TextView nightAvgK;

    int dL,eL,eK,nL,nK = 0;

    View view;
    int percent;
    double todayTotal;

    //constructor
    public DayLight(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.day_light, container, false);
        BarChart mBarChart = (BarChart) view.findViewById(R.id.bar);


        //어떤 사람인지 (카드별로 버튼이 필요함)
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //오늘동안의 리스트가 없을경우 트라이캐치 실행
        if(user.getTodayList()==null){
            try { user.parsingDay(pos); } catch (ParseException e) { e.printStackTrace(); }
        }

        //else {
        //합산 준비
        Integer[] sumHourLux = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourLux = new Integer[24][]; //10분마다
        Integer[] avgHourTemp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourTemp = new Integer[24][]; //10분마다

        for (int i = 0; i < user.getPerHour().size(); i++) {

            hourLux[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            hourTemp[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};

            for (int j = 0; j < user.getPerHour().get(i).size(); j++) {

                int raw = user.getPerHour().get(i).get(j).getAvgLux();

                // 2000이상이면 가중치 1, 노출시간 10분.
                if (raw>=2000) {hourLux[i][j]= raw*1*10;}
                else if(1800<=raw&&raw<2000){hourLux[i][j] = raw*9;} //가중치 0.9, 노출시간 10분
                else if(1600<=raw&&raw<1800){hourLux[i][j]= raw*7;} //이하 동문.
                else if(1400<=raw&&raw<1600){hourLux[i][j]= raw*4;}
                else if(1000<=raw&&raw<1400){hourLux[i][j]= raw*1;}
                else{hourLux[i][j]= raw*0*10;}

                 // 시간마다 누적 - > 따라서 해당 시간의 최종 누적량을 가져와야한다.
                hourTemp[i][j] = (int) user.getPerHour().get(i).get(j).getAvgTemp();
                Log.d("DayLight for in Lux", "i: " + i + ", j: " + j + ", hour: " + hourLux[i][j]);
                Log.d("DayLight for in Temp", "i: " + i + ", j: " + j + ", hour: " + hourTemp[i][j]);

                //60분의 기록이 더해져서 1시간으로 저장된다.
                sumHourLux[i] += hourLux[i][j]; // 위의 가중치 반영한 것을 더해준다.
                avgHourTemp[i] += hourTemp[i][j];

                if(j+1 == user.getPerHour().get(i).size()){
                    avgHourTemp[i] = avgHourTemp[i]/(j+1);
                } //이건 전체 평균 K인데
            }
            Log.d("DayLightLux", i + ", " + sumHourLux[i]);//합산 잘 되었는지 확인
            Log.d("DayLightTemp", i + ", " + avgHourTemp[i]);//합산 잘 되었는지 확인
        }

        //Log.e("SumEvenLux", ", " + sumEvenLux);//합산 잘 되었는지 확인
        //Log.e("SumEvenLux", ", " + sumEvenTemp);//합산 잘 되었는지 확인

        String EvenP = Integer.toString(percent);
        daySumLux = (TextView) view.findViewById(R.id.daylux1) ;
        evenSumLux = (TextView) view.findViewById(R.id.daylux2);
        nightSumLux = (TextView) view.findViewById(R.id.daylux3);
        evenAvgK = (TextView) view.findViewById(R.id.dayK1);
        nightAvgK = (TextView) view.findViewById(R.id.dayK2);

        for (int a = 0;a<9;a++){
            nL += sumHourLux[a];
            nK += avgHourTemp[a];

        }
        for (int a=8;a<19;a++){
            dL += sumHourLux[a];
        }
        for (int a=19;a<23;a++){
            eL += sumHourLux[a];
            eK += avgHourTemp[a];
        }
        for (int a = 22;a<24;a++){
            nL += sumHourLux[a];
            nK += avgHourTemp[a];
        }

        Log.e("dL",  Integer.toString(dL));//합산 잘 되었는지 확인
        Log.e("eL", Integer.toString(eL));//합산 잘 되었는지 확인
        Log.e("eK", Integer.toString(eK));//합산 잘 되었는지 확인
        Log.e("nL", Integer.toString(nL));//합산 잘 되었는지 확인
        Log.e("nK", Integer.toString(nK));//합산 잘 되었는지 확인

        daySumLux.setText(Integer.toString(dL));
        evenSumLux.setText(Integer.toString(eL));
        evenAvgK.setText(Integer.toString(eK/4));
        nightSumLux.setText(Integer.toString(nL));
        nightAvgK.setText(Integer.toString(nK/10));

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.dayLightDate);
        String titleD = user.getDataList().get(0).getUser().getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일";
        titleDay.setText(titleD);

        // 시간당 전체 Lux를 더하는 atomicinteger total
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumHourLux).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);

        Log.e("check now",Integer.toString(Integer.parseInt(curr.substring(9, 11))));
        totalT = (TextView) view.findViewById(R.id.dayLightPercent);
        todayTotal = (total.intValue() / 60000.0) * 100;
        totalT.setText("조도량  "+ todayTotal +"%");

        //시간 상관없이 Y축높이는 Lux, 그래프 바 색상은 K
        //주간 - 기상 ~ 수면 4시간 전 - Lux총합
        if (9 <= Integer.parseInt(curr.substring(9, 11)) && Integer.parseInt(curr.substring(9, 11)) < 18) { //9시~18시 일 때
            Log.e("check","9~18 in!");
            int sumDayLux = 0;

            for (int i = 0; i < 23; i++) {

                if (avgHourTemp[i]>6000) {
                    //시간당 조도량이 6000K를 넘을 경우
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#d84315")));

                }

                else if (3000 < avgHourTemp[i] || avgHourTemp[i]<= 6000) {
                    //시간당 조도량이 3000~6000K
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#fb8c00")));
                }

                else if(avgHourTemp[i]<3000) {
                    //시간당 조도량이 3000 미만
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#fb8c00")));
                }

                else{
                    //이하밖에없겠지...?
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#5F9919")));
                }

            }

        }

        //저녁 - 수면 4시간전 ~ 수면 전 - Lux총합 / K평균
        if (18 <= Integer.parseInt(curr.substring(9, 11)) && Integer.parseInt(curr.substring(9, 11)) < 22) { //18시~22시일 떄
            Log.e("check","18~22 in!");
            int sumEvenLux = 0;
            int sumEvenTemp = 0;
            for (int i = 0; i < 23; i++) {
                if (avgHourTemp[i]>6000) {
                    //시간당 조명온도가 6000K를 넘을 경우
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#d84315")));
                }

                else if (3000 < avgHourTemp[i] || avgHourTemp[i]<= 6000) {
                    //시간당 조명온도가 3000~6000K
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#fb8c00")));
                }

                else if(avgHourTemp[i]<3000) {
                    //시간당 조명온도가 3000 미만
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#fb8c00")));
                }

                else{
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#5F9919")));
                }
                if (18 <= i && i < 22) {//주간이므로 여기를 진하게
                    sumEvenLux += sumHourLux[i];
                    sumEvenTemp += avgHourTemp[i];
                }
            }
        }

        // 야간 - 수면중 Lux총합 / K평균
        if(23 <= Integer.parseInt(curr.substring(9, 11)) || (0<=Integer.parseInt(curr.substring(9, 11)) &&Integer.parseInt(curr.substring(9, 11))<= 8)) { //23시 ~ 오전 8시
            Log.e("check","else in!");
            int sumNightLux = 0;
            int sumNightTemp = 0;
            for (int i = 0; i < 23; i++) {
                mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#5F9919")));
                if (22 <= i && i < 24 && i >= 0 && i < 8) {//주간이므로 여기를 진하게
                    sumNightLux += sumHourLux[i];
                    sumNightTemp += avgHourTemp[i];
                }
            }
        }

        mBarChart.startAnimation();


        return view;
    }
}
