package com.example.jms.home;

import android.content.Context;
import android.util.Log;

import com.example.jms.connection.model.dto.GPSDTO;
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;
import com.example.jms.home.statistic.StatAct;
import com.example.jms.home.statistic.StatLight;
import com.example.jms.home.statistic.StatSleep;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserDataModel {
    public static UserDataModel[] userDataModels;
    public static int currentP;
    public static Context contextP;

    private List<RawdataDTO> dataList;
    private List<RawdataDTO> sleepDataList;
    private List<RawdataDTO> todayList;
    private List<RawdataDTO> weekList;
    private List<RawdataDTO> monthList;
    private List<List<RawdataDTO>> perHour; //0시~23시
    private List<List<RawdataDTO>> perDay;  //일~토
    private List<List<RawdataDTO>> perMonthDay; //1일~31일
    private List<GPSDTO> gpsList;
    private List<SleepDTO> sleepDTOList;
    private String addresses;
    private int position;
    private StatAct statAct;
    private StatLight statLight;
    private StatSleep statSleep;


    public UserDataModel(){}
    public UserDataModel(List<RawdataDTO> dataList, List<RawdataDTO> sleepDataList, List<RawdataDTO> todayList, List<RawdataDTO> weekList, List<RawdataDTO> monthList,
                         List<List<RawdataDTO>> perHour, List<List<RawdataDTO>> perDay, List<List<RawdataDTO>> perMonthDay,
                         List<GPSDTO> gpsList, List<SleepDTO> sleepDTOList, String addresses, int position,
                         StatAct statAct, StatLight statLight, StatSleep statSleep){
        this.dataList = dataList;
        this.sleepDataList = sleepDataList;
        this.todayList = todayList;
        this.weekList = weekList;
        this.weekList = monthList;
        this.perHour = perHour;
        this.perDay = perDay;
        this.perMonthDay = perMonthDay;
        this.gpsList = gpsList;
        this.sleepDTOList = sleepDTOList;
        this.addresses = addresses;
        this.position = position;
        this.statAct = statAct;
        this.statLight = statLight;
        this.statSleep = statSleep;
    }

    public List<RawdataDTO> getDataList() { return dataList; }
    public List<RawdataDTO> getSleepDataList() { return sleepDataList; }
    public List<RawdataDTO> getTodayList() { return todayList; }
    public List<RawdataDTO> getWeekList() { return weekList; }
    public List<RawdataDTO> getMonthList() { return monthList; }
    public List<List<RawdataDTO>> getPerHour() { return perHour; }
    public List<List<RawdataDTO>> getPerDay() { return perDay; }
    public List<List<RawdataDTO>> getPerMonthDay() { return perMonthDay; }
    public List<GPSDTO> getGpsList() { return gpsList; }
    public String getAddresses() {return addresses;}
    public int getPosition() { return position; }
    public StatAct getStatAct() { return statAct; }
    public StatLight getStatLight() {return statLight;}
    public StatSleep getStatSleep() {return statSleep;}
    public List<SleepDTO> getSleepDTOList() { return sleepDTOList; }

    public void setDataList(List<RawdataDTO> dataList) { this.dataList = dataList; }
    public void setSleepDataList(List<RawdataDTO> sleepDataList) { this.sleepDataList = sleepDataList; }
    public void setTodayList(List<RawdataDTO> todayList) { this.todayList = todayList; }
    public void setWeekList(List<RawdataDTO> weekList) { this.weekList = weekList; }
    public void setMonthList(List<RawdataDTO> monthList) { this.monthList = monthList; }
    public void setPerHour(List<List<RawdataDTO>> perHour) { this.perHour = perHour;}
    public void setPerDay(List<List<RawdataDTO>> perDay) { this.perDay = perDay; }
    public void setPerMonthDay(List<List<RawdataDTO>> perMonthDay) { this.perMonthDay = perMonthDay; }
    public void setGpsList(List<GPSDTO> gpsList) { this.gpsList = gpsList; }
    public void setAddresses(String addresses) { this.addresses = addresses; }
    public void setPosition(int position) { this.position = position; }
    public void setStatAct(StatAct statAct) { this.statAct = statAct; }
    public void setStatLight(StatLight statLight){ this.statLight = statLight;}
    public void setStatSleep(StatSleep statSleep){ this.statSleep = statSleep;}
    public void setSleepDTOList(List<SleepDTO> sleepDTOList) { this.sleepDTOList = sleepDTOList; }

    public void parsingDay(int pos) throws ParseException {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String date = transFormat.format(calendar.getTime());
        Date tod = transFormat.parse(date);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,0);
        int today = (int)(calendar.getTimeInMillis()/1000);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,1);
        int tomorrow = (int)(calendar.getTimeInMillis()/1000);

        //calendar.setTime(tod);//transFormat.parse(transFormat.format(calendar.getTime())));
        //Log.d("UserDataModel","오늘은 "+tod+"일주일 중에 "+calendar.get(Calendar.DAY_OF_WEEK)+"번째");
        //calendar.add(calendar.DATE,2-calendar.get(Calendar.DAY_OF_WEEK));
        //int weekS = (int)(calendar.getTimeInMillis()/1000);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,7-calendar.get(Calendar.DAY_OF_WEEK)+1);
        int weekE = (int)(calendar.getTimeInMillis()/1000);

        int weekS = weekE-604800;

        String mstart = date.substring(0,6)+"01";
        calendar.setTime(transFormat.parse(mstart));
        calendar.add(calendar.DATE,0);
        int monthS = (int)(calendar.getTimeInMillis()/1000);

        calendar.add(calendar.MONTH,1);
        int monthE = (int)(calendar.getTimeInMillis()/1000);

        Log.d("UserDataModel","오늘 "+today+", 내일 "+tomorrow+", 이번주 시작 "+weekS+", 이번주 끝+1 "+weekE+", 이번달 시작 "+monthS+", 이번달 끝+1 "+monthE);
        List<RawdataDTO> allList = userDataModels[pos].dataList;
        userDataModels[pos].todayList = new ArrayList<>();
        userDataModels[pos].weekList = new ArrayList<>();
        userDataModels[pos].monthList = new ArrayList<>();

        for(int i=0; i<allList.size(); i++){
            if(dataList.get(i).getStartTick() >= today && dataList.get(i).getStartTick() < tomorrow){
                userDataModels[pos].todayList.add(dataList.get(i));
            }
            if(dataList.get(i).getStartTick() >= weekS && dataList.get(i).getStartTick() < weekE){
                userDataModels[pos].weekList.add(dataList.get(i));
            }
            if(dataList.get(i).getStartTick() >= monthS && dataList.get(i).getStartTick() < monthE){
                userDataModels[pos].monthList.add(dataList.get(i));
            }
        }
        parsingHour(pos,userDataModels[pos].todayList);
        parsingWeekDay(pos,userDataModels[pos].weekList);
        parsingMonthDay(pos,userDataModels[pos].monthList);
        Log.d("UserDataModel","오늘데이터 길이: "+userDataModels[pos].getTodayList().size());
        Log.d("UserDataModel","일주일데이터 길이: "+userDataModels[pos].getWeekList().size());
        Log.d("UserDataModel","한달데이터 길이: "+userDataModels[pos].getMonthList().size());

        userDataModels[pos].setStatAct(new StatAct());
        userDataModels[pos].getStatAct().parsing(userDataModels[pos].getPerHour(),userDataModels[pos].getPerDay(),userDataModels[pos].getPerMonthDay());
        userDataModels[pos].setStatLight(new StatLight());
        userDataModels[pos].getStatLight().parsing(userDataModels[pos].getPerHour(),userDataModels[pos].getPerDay(),userDataModels[pos].getPerMonthDay());
        userDataModels[pos].setStatSleep(new StatSleep());
        userDataModels[pos].getStatSleep().parsing(userDataModels[pos].getSleepDTOList());
    }

    public void parsingHour(int pos, List<RawdataDTO> data) throws ParseException {
        if(pos >=0 ) {
            userDataModels[pos].perHour = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                userDataModels[pos].perHour.add(new ArrayList<>());
            }

            for (int i = 0; i < data.size(); i++) {
                long time = (long) data.get(i).getStartTick() * 1000;
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
                String date = transFormat.format(time);
                String hour = date.substring(9, 11);
                int hourI = Integer.parseInt(hour);
                Log.d("UserDataModel", "인티저로 바꾼 값: " + hourI);
                switch (hourI) {
                    case 0:
                        userDataModels[pos].perHour.get(0).add(data.get(i));
                        Log.d("UserDataModel", "0으로 들어옴");
                        break;
                    case 1:
                        userDataModels[pos].perHour.get(1).add(data.get(i));
                        Log.d("UserDataModel", "1으로 들어옴");
                        break;
                    case 2:
                        userDataModels[pos].perHour.get(2).add(data.get(i));
                        Log.d("UserDataModel", "2으로 들어옴");
                        break;
                    case 3:
                        userDataModels[pos].perHour.get(3).add(data.get(i));
                        Log.d("UserDataModel", "3으로 들어옴");
                        break;
                    case 4:
                        userDataModels[pos].perHour.get(4).add(data.get(i));
                        Log.d("UserDataModel", "4으로 들어옴");
                        break;
                    case 5:
                        userDataModels[pos].perHour.get(5).add(data.get(i));
                        Log.d("UserDataModel", "5으로 들어옴");
                        break;
                    case 6:
                        userDataModels[pos].perHour.get(6).add(data.get(i));
                        Log.d("UserDataModel", "6으로 들어옴");
                        break;
                    case 7:
                        userDataModels[pos].perHour.get(7).add(data.get(i));
                        Log.d("UserDataModel", "7으로 들어옴");
                        break;
                    case 8:
                        userDataModels[pos].perHour.get(8).add(data.get(i));
                        Log.d("UserDataModel", "8으로 들어옴");
                        break;
                    case 9:
                        userDataModels[pos].perHour.get(9).add(data.get(i));
                        Log.d("UserDataModel", "9으로 들어옴");
                        break;
                    case 10:
                        userDataModels[pos].perHour.get(10).add(data.get(i));
                        Log.d("UserDataModel", "10으로 들어옴");
                        break;
                    case 11:
                        userDataModels[pos].perHour.get(11).add(data.get(i));
                        Log.d("UserDataModel", "11으로 들어옴");
                        break;
                    case 12:
                        userDataModels[pos].perHour.get(12).add(data.get(i));
                        Log.d("UserDataModel", "12으로 들어옴");
                        break;
                    case 13:
                        userDataModels[pos].perHour.get(13).add(data.get(i));
                        Log.d("UserDataModel", "13으로 들어옴");
                        break;
                    case 14:
                        userDataModels[pos].perHour.get(14).add(data.get(i));
                        Log.d("UserDataModel", "14으로 들어옴");
                        break;
                    case 15:
                        userDataModels[pos].perHour.get(15).add(data.get(i));
                        Log.d("UserDataModel", "15으로 들어옴");
                        break;
                    case 16:
                        userDataModels[pos].perHour.get(16).add(data.get(i));
                        Log.d("UserDataModel", "16으로 들어옴");
                        break;
                    case 17:
                        userDataModels[pos].perHour.get(17).add(data.get(i));
                        Log.d("UserDataModel", "17으로 들어옴");
                        break;
                    case 18:
                        userDataModels[pos].perHour.get(18).add(data.get(i));
                        Log.d("UserDataModel", "18으로 들어옴");
                        break;
                    case 19:
                        userDataModels[pos].perHour.get(19).add(data.get(i));
                        Log.d("UserDataModel", "19으로 들어옴");
                        break;
                    case 20:
                        userDataModels[pos].perHour.get(20).add(data.get(i));
                        Log.d("UserDataModel", "20으로 들어옴");
                        break;
                    case 21:
                        userDataModels[pos].perHour.get(21).add(data.get(i));
                        Log.d("UserDataModel", "21으로 들어옴");
                        break;
                    case 22:
                        userDataModels[pos].perHour.get(22).add(data.get(i));
                        Log.d("UserDataModel", "22으로 들어옴");
                        break;
                    case 23:
                        userDataModels[pos].perHour.get(23).add(data.get(i));
                        Log.d("UserDataModel", "23으로 들어옴");
                        break;
                }
            }
            Log.d("UserDataMdel", "perHour");
        }
        else if(pos == -1){//레포트에서 사용
            perHour = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                perHour.add(new ArrayList<>());
            }
            for (int i = 0; i < data.size(); i++) {
                long time = (long) data.get(i).getStartTick() * 1000;
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
                String date = transFormat.format(time);
                String hour = date.substring(9, 11);
                int hourI = Integer.parseInt(hour);
                Log.d("UserDataModel", "인티저로 바꾼 값: " + hourI);
                perHour.get(hourI).add(data.get(i));
            }
            statAct = new StatAct();
            statAct.parsing(perHour);
            statLight = new StatLight();
            statLight.parsing(perHour);
        }
    }

    public void parsingWeekDay(int pos, List<RawdataDTO> data){
        userDataModels[pos].perDay = new ArrayList<>();
        for(int i=0; i<7; i++){ userDataModels[pos].perDay.add( new ArrayList<>()); }

        for(int i=0; i<data.size();i++){
            long time = (long) data.get(i).getStartTick()*1000;
            SimpleDateFormat transFormat = new SimpleDateFormat("EEE yyyyMMdd", Locale.KOREAN);
            String date = transFormat.format(time);
            String day = date.substring(0,1);
            Log.d("UserDataModel","요일로 바꾼 값: "+day);
            switch (day){
                case "월":
                    userDataModels[pos].perDay.get(1).add(data.get(i)); Log.d("UserDataModel","월로 들어옴"); break;
                case "화":
                    userDataModels[pos].perDay.get(2).add(data.get(i)); Log.d("UserDataModel","화로 들어옴"); break;
                case "수":
                    userDataModels[pos].perDay.get(3).add(data.get(i)); Log.d("UserDataModel","수로 들어옴"); break;
                case "목":
                    userDataModels[pos].perDay.get(4).add(data.get(i)); Log.d("UserDataModel","목로 들어옴"); break;
                case "금":
                    userDataModels[pos].perDay.get(5).add(data.get(i)); Log.d("UserDataModel","금로 들어옴"); break;
                case "토":
                    userDataModels[pos].perDay.get(6).add(data.get(i)); Log.d("UserDataModel","토로 들어옴"); break;
                case "일":
                    userDataModels[pos].perDay.get(0).add(data.get(i)); Log.d("UserDataModel","일로 들어옴"); break;
                }
        }
        Log.d("UserDataMdel","perDay");
    }

    public void parsingMonthDay(int pos, List<RawdataDTO> data){
        userDataModels[pos].perMonthDay = new ArrayList<>();
        for(int i=0; i<31; i++){ userDataModels[pos].perMonthDay.add( new ArrayList<>()); }

        for(int i=0; i<data.size();i++){
            long time = (long) data.get(i).getStartTick()*1000;
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
            String date = transFormat.format(time);
            String day = date.substring(6,8);
            int dayI = Integer.parseInt(day);
            Log.d("UserDataModel","Month - 일로 바꾼 값: "+day);
            userDataModels[pos].perMonthDay.get(dayI-1).add(data.get(i)); Log.d("UserDataModel","Month - "+dayI+"로 들어옴");
            /*switch (day){
                case "01":
                    perDay.get(dayI).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
                case "02":
                    perDay.get(dayI).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
                case "03":
                    perDay.get(3).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
                case "04":
                    perDay.get(4).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
                case "05":
                    perDay.get(5).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
                case "06":
                    perDay.get(6).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
                case "07":
                    perDay.get(0).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","로 들어옴"); break;
            }*/
        }
    }
}
