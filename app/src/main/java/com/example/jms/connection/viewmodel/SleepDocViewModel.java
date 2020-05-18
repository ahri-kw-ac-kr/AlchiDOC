package com.example.jms.connection.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.jms.connection.model.SleepDocService;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class SleepDocViewModel extends ViewModel {
    private SleepDocService sleepDocService;

    public SleepDocViewModel(){
        this.sleepDocService = SleepDocService.getInstance();
    }

    public boolean deviceCon(){ return sleepDocService.deviceCon(); }

    public Completable connectSleepDoc(String mac) {
        Log.i("Viewmodel", "connect start");
        return sleepDocService.connect(mac);
    }

    public Observable<RawdataDTO> getRawdataFromSleepDoc() {
        return sleepDocService.getRawdata();
    }

    public Observable battery(){ return sleepDocService.battery(); }

}
