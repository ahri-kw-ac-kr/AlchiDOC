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


public class DayLight extends Fragment {

    TextView titleDay;
    TextView titlePercent;
    View view;
    int percent;

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
        Integer[] sumHourTemp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourTemp = new Integer[24][];
        for (int i = 0; i < user.getPerHour().size(); i++) {
            hourLux[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            hourTemp[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            for (int j = 0; j < user.getPerHour().get(i).size(); j++) {
                hourLux[i][j] = (int) user.getPerHour().get(i).get(j).getTotalLux();
                hourTemp[i][j] = (int) user.getPerHour().get(i).get(j).getAvgTemp();
                Log.d("DayLight", "i: " + i + ", j: " + j + ", hour: " + hourLux[i][j]);
                Log.d("DayLight", "i: " + i + ", j: " + j + ", hour: " + hourTemp[i][j]);
                sumHourLux[i] += hourLux[i][j];
                sumHourTemp[i] += hourTemp[i][j];
            }
            Log.d("DayLightLux", i + ", " + sumHourLux[i]);//합산 잘 되었는지 확인
            Log.d("DayLightTemp", i + ", " + sumHourTemp[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());

        //000님의 0월 0일
        titleDay = (TextView) view.findViewById(R.id.dayActDate);
        String titleD = user.getDataList().get(0).getUser().getFullname() + "님의 " + curr.substring(4, 6) + "월 " + curr.substring(6, 8) + "일";
        titleDay.setText(titleD);

        //주간/야간 조도량 00% 60000Lux 중 얼마?
        titlePercent = (TextView) view.findViewById(R.id.dayActPercent);

        //시간 상관없이 Y축높이는 Lux, 그래프 바 색상은 K

        //주간 - 기상 ~ 수면 4시간 전
        if (9 <= Integer.parseInt(curr.substring(9, 11)) || Integer.parseInt(curr.substring(9, 11)) < 18) { //9시~18시 일 때
            int sumD = 0;
            for (int i = 9; i < 21; i++) { //아침9시~ 저녁6시
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHourLux[i], Color.parseColor("#5F9919")));
                    sumD += sumHourLux[i];
            }
            percent = (sumD / 60000) * 100;
            String dayP = "주간 조도량(Lux) " + percent + "%";
            titlePercent.setText(dayP);
        }

        //야간 - 수면 4시간전 ~ 수면 전
        else if (18 <= Integer.parseInt(curr.substring(9, 11)) || Integer.parseInt(curr.substring(9, 11)) < 22) { //18시~22시일 떄떄
            int sumD = 0;
            for (int i = 9; i < 21; i++) {
                if (9 <= i && i < 18) {//지금은 야간인데 이건 주간이니까 연하게
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
                } else {//야간이므로 여기를 진하게
                    mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
                    sumD += sumHour[i];
                }
            }
            percent = sumD / 2000 * 100;
            String dayP = "야간 활동량 " + percent + "%";
            titlePercent.setText(dayP);
        }
        // 수면중
        else {
            for (int i = 9; i < 21; i++) {
                mBarChart.addBar(new BarModel(Integer.toString(i), sumHour[i], Color.parseColor("#5F9919")));
            }
            percent = total.intValue() / 8000 * 100;
            String dayP = "오늘 활동량 " + percent + "%";
            titlePercent.setText(dayP);
        }

        mBarChart.startAnimation();




        mBarChart.addBar(new BarModel("09", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("10", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("11", 1.2f, Color.parseColor("#ffd54f")));
        mBarChart.addBar(new BarModel("12", 2.1f, Color.parseColor("#ffd54f")));
        mBarChart.addBar(new BarModel("13", 3.3f, Color.parseColor("#fb8c00")));
        mBarChart.addBar(new BarModel("14", 3.8f, Color.parseColor("#d84315")));
        mBarChart.addBar(new BarModel("15", 3.1f, Color.parseColor("#fb8c00")));
        mBarChart.addBar(new BarModel("16", 2.4f, Color.parseColor("#fb8c00")));
        mBarChart.addBar(new BarModel("17", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("18", 0.0f, Color.parseColor("#ffffff")));
        mBarChart.addBar(new BarModel("19", 0.0f, Color.parseColor("#ffffff")));


        mBarChart.startAnimation();

        return view;
    }
}
