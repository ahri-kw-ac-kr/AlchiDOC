package com.example.jms.home.statistic;

import android.util.Log;

import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatSleep {
    private int deep; //깊은수면
    private int light; // 얕은수면
    private int turn; // 뒤척임 수
    private int wake; // 깨어남 수
    private int turnHour; // 1시간 당 뒤척인 정도가 정상, 주의, 관리 인지 구별... 0:정상, 1:주의, 2:관리필요
    private int total; // 총 수면시간
    private int[] level; //수면질 배열

    //일간
    int percentDay; //일간 퍼센트

    //주간
    private List<SleepDTO> weekList;

    //월간
    private List<SleepDTO> monthList;

    public StatSleep(){}
    public StatSleep(int deep, int light, int turn, int wake, int turnHour, int total, int[] level,
                     int percentDay, List<SleepDTO> weekList, List<SleepDTO> monthList){
        this.deep = deep;
        this.light = light;
        this.turn = turn;
        this.wake = wake;
        this.turnHour = turnHour;
        this.total = total;
        this.level = level;
        this.percentDay = percentDay;
        this.weekList = weekList;
        this.monthList = monthList;
    }

    public int getDeep() { return deep; }
    public void setDeep(int deep) { this.deep = deep; }

    public int getLight() { return light; }
    public void setLight(int light) { this.light = light; }

    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }

    public int getWake() { return wake; }
    public void setWake(int wake) { this.wake = wake; }

    public int getTurnHour() { return turnHour; }
    public void setTurnHour(int turnHour) { this.turnHour = turnHour; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int[] getLevel() { return level; }
    public void setLevel(int[] level) { this.level = level; }

    public int getPercentDay(){ return percentDay; }
    public void setPercentDay(int percentDay) { this.percentDay = percentDay; }

    public List<SleepDTO> getWeekList() { return weekList; }
    public void setMonthList(List<SleepDTO> monthList) { this.monthList = monthList; }

    public List<SleepDTO> getMonthList() { return monthList; }
    public void setWeekList(List<SleepDTO> weekList) { this.weekList = weekList; }


    //수면 분석
    public static SleepDTO analyze(List<RawdataDTO> data){
        SleepDTO sleepDTO = new SleepDTO();

        int[] levelA = new int[data.size()];

        int deepS = 0;
        int lightS = 0;
        int turnS = 0;
        int wakeS = 0;
        int totalS = 0;
        int deepFlag = 0;
        int turnHour = 0;
        int range = 0;

        for(int i=0; i<data.size(); i++){ levelA[i] = 3; }
        for(int i=0; i<data.size(); i++){
            try{
                for(int j=0; j<6; j++) {
                    int x = data.get(i+j).getVectorX();
                    int y = data.get(i+j).getVectorY();
                    int z = data.get(i+j).getVectorZ();
                    if(x==0 && y==0 && z==0){ deepFlag += 1; }
                    else if(x!=0 || y!=0 || z!=0){ break; }
                }//2nd for
            }catch(Exception e){}
            if(deepFlag == 6){
                range += 1;
                deepFlag = 0;
            }
            else if((deepFlag==0 && range != 0) ||(deepFlag==5 && range != 0)){
                System.out.print("range: "+range+", i: "+i+"\n");
                for(int j=i-range; j<i+5; j++){ levelA[j] = 1; }
                deepS = deepS+((range-1)*10)+60;
                range = 0;
            }
            else{
                deepFlag = 0;
                int isTurn = data.get(i).getVectorX()+data.get(i).getVectorY()+data.get(i).getVectorZ();
                int isWake = data.get(i).getSteps();
                if(isTurn>=2){
                    turnS = turnS + (isTurn/2);
                    if((isTurn/2)>10 && (isTurn/2) <16 && turnHour!=2){ turnHour = 1;}
                    else if((isTurn/2)>15){ turnHour = 2;}
                }
                if(isWake>=30){ wakeS += 1; levelA[i] = 5; } //깨어남의 기준... 스텝이 30넘으면...?? 잘 모르겠다
            }
        }
        totalS = data.size()*10;
        lightS = totalS - deepS;

        String str="";
        for(int i=0; i<data.size(); i++){ str=str+levelA[i]+","; }


        sleepDTO.setTurnHour(turnHour); //0은 정상, 1은 주의, 2는 관리필요
        sleepDTO.setDeep(deepS);
        sleepDTO.setLight(lightS);
        sleepDTO.setTurn(turnS);
        sleepDTO.setWake(wakeS);
        sleepDTO.setTotal(totalS);
        sleepDTO.setLevel(str);

        return sleepDTO;
    }

    //일간
    private void day(SleepDTO oneDay){
        int total = oneDay.getTotal(); //총 수면시간
        int wake =  oneDay.getWake(); // 깬 횟수
        int turn = oneDay.getTurn(); //뒤척인 횟수
        if(total == 0){ percentDay = 0; }
        else{ percentDay = (int)(((total-((wake*10.0)+turn))/total)*100); }
    }

    //날짜 파싱 : 주간, 월간
    private void selectDay(List<SleepDTO> sleepDTO) throws ParseException {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String date = transFormat.format(calendar.getTime());
        Date tod = transFormat.parse(date);

        calendar.setTime(tod); //오늘
        calendar.add(calendar.DATE,7-calendar.get(Calendar.DAY_OF_WEEK)+1); //이번 주 끝
        int weekE = Integer.parseInt(transFormat.format(calendar.getTime()));

        calendar.add(calendar.DATE,-7);//이번 주 시작
        int weekS = Integer.parseInt(transFormat.format(calendar.getTime()));

        String mstart = date.substring(0,6)+"01";
        calendar.setTime(transFormat.parse(mstart));
        int monthS = Integer.parseInt(transFormat.format(calendar.getTime()));//이번 달 시작

        calendar.add(calendar.MONTH,1);//이번 달 끝
        int monthE = Integer.parseInt(transFormat.format(calendar.getTime()));

        Log.d("StatSleep","이번주 시작 "+weekS+", 이번주 끝+1 "+weekE+", 이번달 시작 "+monthS+", 이번달 끝+1 "+monthE);

        List<SleepDTO> allList = sleepDTO;
        weekList = new ArrayList<>();
        monthList = new ArrayList<>();
        int weekFlag = 0;
        int monthFlag = 0;
        for (int i=0; i<allList.size(); i++){
            int thisDay = Integer.parseInt(allList.get(i).getWakeTime().substring(0,8));
            Log.d("StatSleep",i+" "+"디스데이: "+thisDay+", 위크플래그: "+weekFlag+", 먼스플래그: "+monthFlag);
            if(thisDay >= weekS && thisDay < weekE){
                if(weekFlag != thisDay) { weekList.add(allList.get(i)); weekFlag = thisDay; }
            }
            if(thisDay >= monthS && thisDay < monthE){
                if(monthFlag != thisDay) { monthList.add(allList.get(i)); monthFlag = thisDay; }
            }
        }
        Log.d("StatSleep","주간 사이즈: "+weekList.size()+", 월간 사이즈: "+monthList.size());
    }

    //UserDataModel과 연결
    public void parsing(List<SleepDTO> sleepDTO) throws ParseException {
        if(sleepDTO.size() != 0){
            day(sleepDTO.get(0));
            selectDay(sleepDTO);
        }
        else{ percentDay = 0; }
    }
}
