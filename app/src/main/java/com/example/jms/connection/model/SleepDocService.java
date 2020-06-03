package com.example.jms.connection.model;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.Observable;

import com.clj.fastble.data.BleDevice;
import com.example.jms.connection.sleep_doc.SleepDoc;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.util.List;

public class SleepDocService {
    private SleepDoc sleepDoc;
    private static SleepDocService sleepDocService;

    public boolean deviceCon(){
        if(sleepDoc == null) { return false; }
        else{ return sleepDoc.deviceCon(); }
    }

    public void disconnect(){sleepDoc.disconnect();}

    public Observable<List<RawdataDTO>> getRawdata() {
        return sleepDoc.getRawdata();
    }

    public Observable battery(){
        return sleepDoc.battery();
    }

    public Completable connect(String macAddress) {
        Log.i("SleepdocService", "connect start");
        sleepDoc = new SleepDoc(macAddress);
        return sleepDoc.connect();
    }

    public static synchronized SleepDocService getInstance() {
        if (sleepDocService == null) {
            sleepDocService = new SleepDocService();
        }
        return sleepDocService;
    }

    // Singleton
    private SleepDocService() { }
}
