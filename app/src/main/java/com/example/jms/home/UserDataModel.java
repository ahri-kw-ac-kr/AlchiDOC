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

public class UserDataModel {
    public static UserDataModel[] userDataModels;
    public static int currentP;
    public static Context contextP;

    private List<RawdataDTO> dataList;
    private List<RawdataDTO> todayList;
    private List<RawdataDTO> weekList;
    private List<List<RawdataDTO>> perHour;
    private List<GPSDTO> gpsList;
    private String addresses;
    private int position;


    public UserDataModel(){}
    public UserDataModel(List<RawdataDTO> dataList, List<RawdataDTO> todayList, List<RawdataDTO> weekList, List<List<RawdataDTO>> perHour, List<GPSDTO> gpsList, String addresses, int position){
        this.dataList = dataList;
        this.todayList = todayList;
        this.weekList = weekList;
        this.perHour = perHour;
        this.gpsList = gpsList;
        this.addresses = addresses;
        this.position = position;
    }

    public List<RawdataDTO> getDataList() { return dataList; }
    public List<RawdataDTO> getTodayList() { return todayList; }
    public List<RawdataDTO> getWeekList() { return weekList; }
    public List<List<RawdataDTO>> getPerHour() { return perHour; }
    public List<GPSDTO> getGpsList() { return gpsList; }
    public String getAddresses() {return addresses;}
    public int getPosition() { return position; }

    public void setDataList(List<RawdataDTO> dataList) { this.dataList = dataList; }
    public void setTodayList(List<RawdataDTO> todayList) { this.todayList = todayList; }
    public void setWeekList(List<RawdataDTO> weekList) { this.weekList = weekList; }
    public void setPerHour(List<List<RawdataDTO>> perHour) { this.perHour = perHour;}
    public void setGpsList(List<GPSDTO> gpsList) { this.gpsList = gpsList; }
    public void setAddresses(String addresses) { this.addresses = addresses; }
    public void setPosition(int position) { this.position = position; }


    public void parsingDay(int pos) throws ParseException {
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
        userDataModels[pos].todayList = userDataModels[pos].dataList;
        userDataModels[pos].weekList = userDataModels[pos].dataList;

        for(int i=0; i<allList.size(); i++){
            if(!(dataList.get(i).getStartTick() >= today && dataList.get(i).getStartTick() < tomorrow)){
                userDataModels[pos].todayList.remove(dataList.get(i));
            }
            if(!(dataList.get(i).getStartTick() >= weekS && dataList.get(i).getStartTick() < weekE)){
                userDataModels[pos].weekList.remove(dataList.get(i));
            }
        }
        parsingHour(pos,userDataModels[pos].todayList);
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
                    perHour.get(0).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","0으로 들어옴"); break;
                case 1:
                    perHour.get(1).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","1으로 들어옴"); break;
                case 2:
                    perHour.get(2).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","2으로 들어옴"); break;
                case 3:
                    perHour.get(3).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","3으로 들어옴"); break;
                case 4:
                    perHour.get(4).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","4으로 들어옴"); break;
                case 5:
                    perHour.get(5).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","5으로 들어옴"); break;
                case 6:
                    perHour.get(6).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","6으로 들어옴"); break;
                case 7:
                    perHour.get(7).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","7으로 들어옴"); break;
                case 8:
                    perHour.get(8).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","8으로 들어옴"); break;
                case 9:
                    perHour.get(9).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","9으로 들어옴"); break;
                case 10:
                    perHour.get(10).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","10으로 들어옴"); break;
                case 11:
                    perHour.get(11).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","11으로 들어옴"); break;
                case 12:
                    perHour.get(12).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","12으로 들어옴"); break;
                case 13:
                    perHour.get(13).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","13으로 들어옴"); break;
                case 14:
                    perHour.get(14).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","14으로 들어옴"); break;
                case 15:
                    perHour.get(15).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","15으로 들어옴"); break;
                case 16:
                    perHour.get(16).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","16으로 들어옴"); break;
                case 17:
                    perHour.get(17).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","17으로 들어옴"); break;
                case 18:
                    perHour.get(18).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","18으로 들어옴"); break;
                case 19:
                    perHour.get(19).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","19으로 들어옴"); break;
                case 20:
                    perHour.get(20).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","20으로 들어옴"); break;
                case 21:
                    perHour.get(21).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","21으로 들어옴"); break;
                case 22:
                    perHour.get(22).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","22으로 들어옴"); break;
                case 23:
                    perHour.get(23).add(userDataModels[pos].getTodayList().get(i)); Log.d("UserDataModel","23으로 들어옴"); break;
            }
        }
        Log.d("UserDataMdel","perHour");
    }
}
