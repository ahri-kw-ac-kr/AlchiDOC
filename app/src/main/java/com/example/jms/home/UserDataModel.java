package com.example.jms.home;

import android.util.Log;

import com.example.jms.connection.model.dto.UserDTO;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserDataModel {
    public static UserDataModel[] userDataModels;
    public static int currentP;

    private List<RawdataDTO> dataList;
    private List<RawdataDTO> todayList;
    private List<RawdataDTO> weekList;
    private int position;


    public UserDataModel(){}
    public UserDataModel(List<RawdataDTO> dataList, List<RawdataDTO> todayList, List<RawdataDTO> weekList, int position){
        this.dataList = dataList;
        this.todayList = todayList;
        this.weekList = weekList;
        this.position = position;
    }

    public List<RawdataDTO> getDataList() { return dataList; }
    public List<RawdataDTO> getTodayList() { return todayList; }
    public List<RawdataDTO> getWeekList() { return weekList; }
    public int getPosition() { return position; }

    public void setDataList(List<RawdataDTO> dataList) { this.dataList = dataList; }
    public void setTodayList(List<RawdataDTO> todayList) { this.todayList = todayList; }
    public void setWeekList(List<RawdataDTO> weekList) { this.weekList = weekList; }
    public void setPosition(int position) { this.position = position; }


    public void getToday(int pos) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String date = transFormat.format(calendar.getTime());
        Date tod = transFormat.parse(date);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,0);
        int today = (int)(calendar.getTimeInMillis()/1000);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,1);
        int tomorrow = (int)(calendar.getTimeInMillis()/1000);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,1-calendar.get(Calendar.DAY_OF_WEEK));
        int weekS = (int)(calendar.getTimeInMillis()/1000);

        calendar.setTime(tod);
        calendar.add(calendar.DATE,8-calendar.get(Calendar.DAY_OF_WEEK));
        int weekE = (int)(calendar.getTimeInMillis()/1000);

        Log.d("UserDataModel","오늘 "+today+", 내일 "+tomorrow+", 이번주 시작 "+weekS+", 이번주 끝 "+weekE);
        List<RawdataDTO> allList = userDataModels[pos].dataList;
        for(int i=0; i<allList.size(); i++){
            if(dataList.get(i).getStartTick() >= today && dataList.get(i).getStartTick() < tomorrow){
                todayList.add(dataList.get(i));
            }
            if(dataList.get(i).getStartTick() >= weekS && dataList.get(i).getStartTick() < weekE){
                weekList.add(dataList.get(i));
            }
        }
    }
}
