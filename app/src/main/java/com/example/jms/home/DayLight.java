package com.example.jms.home;

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

    View view;
    int percent;
    int todayTotal;

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
        Integer[][] hourLux = new Integer[24][];
        Integer[] avgHourTemp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourTemp = new Integer[24][];

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

                sumHourLux[i] += hourLux[i][j];
                avgHourTemp[i] += hourTemp[i][j];
            }
            Log.d("DayLightLux", i + ", " + sumHourLux[i]);//합산 잘 되었는지 확인
            Log.d("DayLightTemp", i + ", " + avgHourTemp[i]);//합산 잘 되었는지 확인
        }

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


        totalT = (TextView) view.findViewById(R.id.dayLightPercent);
        todayTotal = (total.intValue() / 60000) * 100;
        totalT.setText("조도량  "+Integer.toString(todayTotal)+"%");

        //맨 위 일간 총합.
        //titlePercent = (TextView) view.findViewById(R.id.dayLightPercent);



        //시간 상관없이 Y축높이는 Lux, 그래프 바 색상은 K
        //주간 - 기상 ~ 수면 4시간 전 - Lux총합
        if (9 <= Integer.parseInt(curr.substring(9, 11)) || Integer.parseInt(curr.substring(9, 11)) < 18) { //9시~18시 일 때
            int sumD = 0;
            int sumTemp = 0;

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

                //주간 합산
                if (9 <= i && i < 18) {
                    sumD += sumHourLux[i];
                }

            }
            //일간 조도량 60000Lux이상이 목표이므로
            percent = (sumD / 60000) * 100;
            String dayP = Integer.toString(sumD);
            daySumLux = (TextView) view.findViewById(R.id.daylux1) ;
            daySumLux.setText(dayP);
        }

        //저녁 - 수면 4시간전 ~ 수면 전 - Lux총합 / K평균
        else if (18 <= Integer.parseInt(curr.substring(9, 11)) || Integer.parseInt(curr.substring(9, 11)) < 22) { //18시~22시일 떄
            int sumD = 0;
            int sumTemp = 0;
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
                    sumD += sumHourLux[i];
                    sumTemp += avgHourTemp[i];
                }
            }
            percent = (sumD / 60000) * 100; // 전체 받은 조도량 나타내는 것 같은데... 안사용해도 될것 같음
            sumTemp = sumTemp/4;
            String dayP = Integer.toString(percent);
            evenSumLux = (TextView) view.findViewById(R.id.daylux2);
            evenAvgK = (TextView) view.findViewById(R.id.dayK1);
            evenSumLux.setText(Integer.toString(sumD));
            evenAvgK.setText(Integer.toString(sumTemp));
        }

        // 야간 - 수면중 Lux총합 / K평균
        else { //23시 ~ 오전 8시
            int sumD = 0;
            int sumTemp = 0;
            for (int i = 0; i < 23; i++) {
                mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#5F9919")));
                if (22 <= i && i < 24 && i >= 0 && i < 8) {//주간이므로 여기를 진하게
                    sumD += sumHourLux[i];
                    sumTemp += avgHourTemp[i];
                }
            }
            percent = total.intValue() / 8000 * 100;
            nightSumLux = (TextView) view.findViewById(R.id.daylux3);
            nightAvgK = (TextView) view.findViewById(R.id.dayK2);
            nightSumLux.setText(Integer.toString(sumD));
            nightAvgK.setText(Integer.toString(sumTemp/4));
        }

        mBarChart.startAnimation();


        return view;
    }
}
