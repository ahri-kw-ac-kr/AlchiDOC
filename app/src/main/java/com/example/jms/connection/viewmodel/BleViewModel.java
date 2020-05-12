package com.example.jms.connection.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.dto.BleDeviceDTO;

import io.reactivex.Observable;

public class BleViewModel extends ViewModel {
    private BleService bleService;

    public BleViewModel(){
        this.bleService = BleService.getInstance();
    }

    public Observable<BleDeviceDTO> scanBle() {
        return bleService.scanBle();
    }
}
