package com.example.jms.connection.model;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;

import java.util.List;

import io.reactivex.Observable;
import com.example.jms.connection.exceptions.BleScanFailException;
import com.example.jms.connection.model.dto.BleDeviceDTO;

public class BleService {
    private BleManager bleManager;
    private static BleService bleService;

    public static BleDeviceDTO principalDevice;
    public static String battery;

    public static synchronized BleService getInstance() {
        if (bleService == null) {
            bleService = new BleService();
            //principalDevice = null;
        }
        return bleService;
    }

    public Observable<BleDeviceDTO> scanBle() {
        return Observable.create(observer -> {
            BleManager.getInstance().scan(new BleScanCallback() {
                @Override
                public void onScanStarted(boolean success) {
                    if (!success) {
                        observer.onError(new BleScanFailException("Failed to start scanning"));
                    }
                }
                @Override
                public void onLeScan(BleDevice bleDevice) {
                    super.onLeScan(bleDevice);
                }

                @Override
                public void onScanning(BleDevice bleDevice) {
                    BleDeviceDTO bleDeviceDTO = new BleDeviceDTO(bleDevice);
                    //try{ if(bleDeviceDTO.getName().equals("SleepDoc")){ principalDevice = bleDeviceDTO; }
                    //}catch (Exception e){ }
                    if(bleDeviceDTO.getName()!=null && bleDeviceDTO.getName().equals("SleepDoc")){ observer.onNext(bleDeviceDTO); }

                }

                @Override
                public void onScanFinished(List<BleDevice> scanResultList) {
                    observer.onComplete();
                }
            });
        });
    }

    // Singleton
    private BleService() {
        this.bleManager = BleManager.getInstance();
    }
}
