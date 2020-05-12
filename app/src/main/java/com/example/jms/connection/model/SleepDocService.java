package com.example.jms.connection.model;

import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.Observable;
import com.example.jms.connection.sleep_doc.SleepDoc;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

public class SleepDocService {
    private SleepDoc sleepDoc;
    private static SleepDocService sleepDocService;

    public Observable<RawdataDTO> getRawdata() {
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
    private SleepDocService() {}
}
