package com.example.jms.home;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jms.connection.model.dto.GPSDTO;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class UserDataModel {
    public static UserDataModel[] userDataModels;
    public static int currentP;
    public static Context contextP;

    private List<RawdataDTO> dataList;
    private List<RawdataDTO> todayList;
    private List<RawdataDTO> weekList;
    private List<List<RawdataDTO>> perHour;
    private List<List<RawdataDTO>> perDay;
    private List<GPSDTO> gpsList;
    private String addresses;
    private int position;


    public UserDataModel(){}
    public UserDataModel(List<RawdataDTO> dataList, List<RawdataDTO> todayList, List<RawdataDTO> weekList, List<List<RawdataDTO>> perHour, List<List<RawdataDTO>> perDay, List<GPSDTO> gpsList, String addresses, int position){
        this.dataList = dataList;
        this.todayList = todayList;
        this.weekList = weekList;
        this.perHour = perHour;
        this.perDay = perDay;
        this.gpsList = gpsList;
        this.addresses = addresses;
        this.position = position;
    }

    public List<RawdataDTO> getDataList() { return dataList; }
    public List<RawdataDTO> getTodayList() { return todayList; }
    public List<RawdataDTO> getWeekList() { return weekList; }
    public List<List<RawdataDTO>> getPerHour() { return perHour; }
    public List<List<RawdataDTO>> getPerDay() { return perDay; }
    public List<GPSDTO> getGpsList() { return gpsList; }
    public String getAddresses() {return addresses;}
    public int getPosition() { return position; }

    public void setDataList(List<RawdataDTO> dataList) { this.dataList = dataList; }
    public void setTodayList(List<RawdataDTO> todayList) { this.todayList = todayList; }
    public void setWeekList(List<RawdataDTO> weekList) { this.weekList = weekList; }
    public void setPerHour(List<List<RawdataDTO>> perHour) { this.perHour = perHour;}
    public void setPerDay(List<List<RawdataDTO>> perDay) { this.perDay = perDay; }
    public void setGpsList(List<GPSDTO> gpsList) { this.gpsList = gpsList; }
    public void setAddresses(String addresses) { this.addresses = addresses; }
    public void setPosition(int position) { this.position = position; }


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

        Log.d("UserDataModel","오늘 "+today+", 내일 "+tomorrow+", 이번주 시작 "+weekS+", 이번주 끝+1 "+weekE);
        List<RawdataDTO> allList = userDataModels[pos].dataList;
        userDataModels[pos].todayList = new ArrayList<>();
        userDataModels[pos].weekList = new ArrayList<>();

        for(int i=0; i<allList.size(); i++){
            if(dataList.get(i).getStartTick() >= today && dataList.get(i).getStartTick() < tomorrow){
                userDataModels[pos].todayList.add(dataList.get(i));
            }
            if(dataList.get(i).getStartTick() >= weekS && dataList.get(i).getStartTick() < weekE){
                userDataModels[pos].weekList.add(dataList.get(i));
            }
        }
        parsingHour(pos,userDataModels[pos].todayList);
        parsingWeekDay(pos,userDataModels[pos].weekList);
        Log.d("UserDataModel","오늘데이터 길이: "+userDataModels[pos].getTodayList().size());
        Log.d("UserDataModel","일주일데이터 길이: "+userDataModels[pos].getWeekList().size());
    }

    public void parsingHour(int pos, List<RawdataDTO> data){
        userDataModels[pos].perHour = new ArrayList<>();
        for(int i=0; i<24; i++){ userDataModels[pos].perHour.add( new ArrayList<>()); }

        for(int i=0; i<data.size();i++){
            long time = (long) data.get(i).getStartTick()*1000;
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
            String date = transFormat.format(time);
            String hour = date.substring(9,11);
            int hourI = Integer.parseInt(hour);
            Log.d("UserDataModel","인티저로 바꾼 값: "+hourI);
            switch (hourI){
                case 0:
                    userDataModels[pos].perHour.get(0).add(data.get(i)); Log.d("UserDataModel","0으로 들어옴"); break;
                case 1:
                    userDataModels[pos].perHour.get(1).add(data.get(i)); Log.d("UserDataModel","1으로 들어옴"); break;
                case 2:
                    userDataModels[pos].perHour.get(2).add(data.get(i)); Log.d("UserDataModel","2으로 들어옴"); break;
                case 3:
                    userDataModels[pos].perHour.get(3).add(data.get(i)); Log.d("UserDataModel","3으로 들어옴"); break;
                case 4:
                    userDataModels[pos].perHour.get(4).add(data.get(i)); Log.d("UserDataModel","4으로 들어옴"); break;
                case 5:
                    userDataModels[pos].perHour.get(5).add(data.get(i)); Log.d("UserDataModel","5으로 들어옴"); break;
                case 6:
                    userDataModels[pos].perHour.get(6).add(data.get(i)); Log.d("UserDataModel","6으로 들어옴"); break;
                case 7:
                    userDataModels[pos].perHour.get(7).add(data.get(i)); Log.d("UserDataModel","7으로 들어옴"); break;
                case 8:
                    userDataModels[pos].perHour.get(8).add(data.get(i)); Log.d("UserDataModel","8으로 들어옴"); break;
                case 9:
                    userDataModels[pos].perHour.get(9).add(data.get(i)); Log.d("UserDataModel","9으로 들어옴"); break;
                case 10:
                    userDataModels[pos].perHour.get(10).add(data.get(i)); Log.d("UserDataModel","10으로 들어옴"); break;
                case 11:
                    userDataModels[pos].perHour.get(11).add(data.get(i)); Log.d("UserDataModel","11으로 들어옴"); break;
                case 12:
                    userDataModels[pos].perHour.get(12).add(data.get(i)); Log.d("UserDataModel","12으로 들어옴"); break;
                case 13:
                    userDataModels[pos].perHour.get(13).add(data.get(i)); Log.d("UserDataModel","13으로 들어옴"); break;
                case 14:
                    userDataModels[pos].perHour.get(14).add(data.get(i)); Log.d("UserDataModel","14으로 들어옴"); break;
                case 15:
                    userDataModels[pos].perHour.get(15).add(data.get(i)); Log.d("UserDataModel","15으로 들어옴"); break;
                case 16:
                    userDataModels[pos].perHour.get(16).add(data.get(i)); Log.d("UserDataModel","16으로 들어옴"); break;
                case 17:
                    userDataModels[pos].perHour.get(17).add(data.get(i)); Log.d("UserDataModel","17으로 들어옴"); break;
                case 18:
                    userDataModels[pos].perHour.get(18).add(data.get(i)); Log.d("UserDataModel","18으로 들어옴"); break;
                case 19:
                    userDataModels[pos].perHour.get(19).add(data.get(i)); Log.d("UserDataModel","19으로 들어옴"); break;
                case 20:
                    userDataModels[pos].perHour.get(20).add(data.get(i)); Log.d("UserDataModel","20으로 들어옴"); break;
                case 21:
                    userDataModels[pos].perHour.get(21).add(data.get(i)); Log.d("UserDataModel","21으로 들어옴"); break;
                case 22:
                    userDataModels[pos].perHour.get(22).add(data.get(i)); Log.d("UserDataModel","22으로 들어옴"); break;
                case 23:
                    userDataModels[pos].perHour.get(23).add(data.get(i)); Log.d("UserDataModel","23으로 들어옴"); break;
            }
        }
        Log.d("UserDataMdel","perHour");
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
                    perDay.get(1).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","월로 들어옴"); break;
                case "화":
                    perDay.get(2).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","화로 들어옴"); break;
                case "수":
                    perDay.get(3).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","수로 들어옴"); break;
                case "목":
                    perDay.get(4).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","목로 들어옴"); break;
                case "금":
                    perDay.get(5).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","금로 들어옴"); break;
                case "토":
                    perDay.get(6).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","토로 들어옴"); break;
                case "일":
                    perDay.get(0).add(userDataModels[pos].getWeekList().get(i)); Log.d("UserDataModel","일로 들어옴"); break;
                }
        }
        Log.d("UserDataMdel","perDay");
    }
}
