package com.example.jms.connection.sleep_doc;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Observer;
import java.util.TimeZone;
import java.util.UUID;

import io.reactivex.Completable;
import io.reactivex.Observable;

import com.clj.fastble.utils.HexUtil;
import com.example.jms.connection.exceptions.DataIsTooShortException;
import com.example.jms.connection.exceptions.ZeroLengthException;
import com.example.jms.connection.sleep_doc.command.Command;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;
import com.example.jms.connection.sleep_doc.dto.SyncDataDTO;
import com.example.jms.connection.sleep_doc.uuid.CharacteristicUUID;
import com.example.jms.connection.sleep_doc.uuid.DescriptorUUID;
import com.example.jms.connection.sleep_doc.uuid.ServiceUUID;
import com.example.jms.home.UserDataModel;
import com.example.jms.settings.DeviceSet1;

public class SleepDoc {
    private BleManager bleManager;
    private String macAddress;
    private BleDevice bleDevice;
    private BluetoothGatt gatt;
    private boolean isConnected = false;

    private Context contextDoc;
    private BluetoothGattService generalService;
    private BluetoothGattCharacteristic sysCmdChar;

    public SleepDoc(String macAddress) {
        this.macAddress = macAddress;
        bleManager = BleManager.getInstance();
    }

    public boolean deviceCon(){
        return isConnected;
    }

    public void disconnect(){
        Log.i("Sleepdoc", "연결끊기");
        bleManager.disconnect(bleDevice);
        bleDevice = null;
        isConnected = false;
    }

    public Completable connect() {
        return Completable.create(observer -> {
            Log.i("Sleepdoc", "connect start");
            bleManager.connect(macAddress, new BleGattCallback() {
                @Override
                public void onStartConnect() {
                    Log.i("SleepDoc", "연결시작");
                }

                @Override
                public void onConnectFail(BleDevice _bleDevice, BleException exception) {
                    Log.i("SleepDoc", "연결실패");
                    observer.onError(new Exception(String.format("Connect fail: %s", macAddress)));
                }

                @Override
                public void onConnectSuccess(BleDevice _bleDevice, BluetoothGatt _gatt, int status) {
                    Log.i("SleepDoc", "연결성공");
                    bleDevice = _bleDevice;
                    isConnected = true;
                    gatt = bleManager.getBluetoothGatt(bleDevice);

                    //sysCmdChar는 아래처럼 초기 연결시 가져오고 있습니다.
                    generalService = gatt.getService(ServiceUUID.GENERAL);
                    if (generalService == null) { Log.d("SleepDoc","General service does not exists on the device"); }
                    sysCmdChar = generalService.getCharacteristic(CharacteristicUUID.SYS_CMD);

                    //공장초기화
                    if(DeviceSet1.refactorFlag){
                        byte[] op = new byte[] {(byte)0x06};
                        sysCmdChar.setValue(op);
                        gatt.writeCharacteristic(sysCmdChar);
                    }

                    setTimeAndZone(bleDevice);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            observer.onComplete();
                        }},500);

                }

                @Override
                public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                    Log.i("SleepDoc", "연결해제");
                    isConnected = false;
                }
            });
        });
    }

    public Observable<RawdataDTO> getRawdata() {
        if (!isConnected) {
            Log.d("SleepDoc", "이거 나오면 안됨");
        }
        refreshDeviceCache(gatt);
        BluetoothGattCharacteristic syncControlChar = gatt.getService(ServiceUUID.SYNC).getCharacteristic(CharacteristicUUID.SYNC_CONTROL);
        BluetoothGattDescriptor descriptor = syncControlChar.getDescriptor(DescriptorUUID.CLIENT_CHARACTERISTIC_CONFIGURATION);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        gatt.writeDescriptor(descriptor);

        return Observable.create(observer -> {
            bleManager.notify(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_CONTROL.toString(), new BleNotifyCallback() {
                //Sync Notify ON Success
                @Override
                public void onNotifySuccess() {
                    Log.i("SleepDoc", "notify SYNC_CONTROL Success");
                    bleManager.write(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_CONTROL.toString(), new byte[]{Command.SYNC_CONTROL_START}, logWriteCallback);
                }

                @Override
                public void onNotifyFailure(BleException exception) {
                    Log.i("SleepDoc", "notify SYNC_CONTROL Fail");
//                    observer.onError(new Exception(exception.getDescription()));
                }
                @Override
                public void onCharacteristicChanged(byte[] data) {
                    Log.i("SleepDoc", "Characteristic Changed");
                    if(data[0] == Command.SYNC_NOTI_DONE) {
                        Log.i("SleepDoc", "Sync Noti Done");
                        observer.onComplete();
                        return;
                    }
                    bleManager.read(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_DATA.toString(), new BleReadCallback() {
                        @Override
                        public void onReadSuccess(byte[] values) {
                            Log.i("SleepDoc", "Read from SleepDoc");
                            try {
                                Log.i("SleepDoc", "Sync data is arrived");
                                SyncDataDTO syncDataDTO = SyncDataDTO.ParseByteArray(values);
                                /*for (final RawdataDTO rawdataDTO : syncDataDTO.rawdataDTOArray) {
                                    Log.i("SleepDoc", "Sync data is parsed into rawdata");
                                    Log.i("스텝값", Integer.toString(i)+"_"+Integer.toString(rawdataDTO.getSteps()));
                                    //observer.onNext(rawdataDTO);
                                    i++;
                                }*/
                                int count = 0;
                                for (int i=0;i<6;i++){
                                    RawdataDTO rawdataDTO = syncDataDTO.rawdataDTOArray[i];
                                    Log.d("슬립닥 raw디티오 "+Integer.toString(i)+"번째",String.format("  \t%d\t\t%d\t\t%d\t%d\t%d\t%d\t%d\t%d\t%d",
                                            rawdataDTO.getStartTick(),
                                            rawdataDTO.getEndTick(),
                                            rawdataDTO.getSteps(),
                                            rawdataDTO.getTotalLux(),
                                            rawdataDTO.getAvgLux(),
                                            rawdataDTO.getAvgTemp(),
                                            rawdataDTO.getVectorX(),
                                            rawdataDTO.getVectorY(),
                                            rawdataDTO.getVectorZ()));
                                    if(rawdataDTO.getStartTick()==0){ break; }
                                    else{
                                    observer.onNext(syncDataDTO.rawdataDTOArray[i]);
                                    count += 1;}
                                }
                                if(count == 0){ observer.onNext(syncDataDTO.rawdataDTOArray[count]); }
                                //else { observer.onNext(syncDataDTO.rawdataDTOArray[count-1]); }
                                bleManager.write(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_CONTROL.toString(), new byte[]{Command.SYNC_CONTROL_PREPARE_NEXT}, logWriteCallback);
                            } catch (ZeroLengthException e) {
                                Log.i("SleepDoc", "Sync data has 0 length, Sync is done.");
                                bleManager.write(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_CONTROL.toString(), new byte[]{Command.SYNC_CONTROL_DONE}, logWriteCallback);
                            } catch (DataIsTooShortException e) {
                                Log.i("SleepDoc", "Request next data");
                                bleManager.write(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_CONTROL.toString(), new byte[]{Command.SYNC_CONTROL_PREPARE_NEXT}, logWriteCallback);
                            } catch(IllegalAccessException e){
                                Log.i("SleepDoc","It is not raw data.");
                                bleManager.write(bleDevice, ServiceUUID.SYNC.toString(), CharacteristicUUID.SYNC_CONTROL.toString(), new byte[]{Command.SYNC_CONTROL_PREPARE_NEXT}, logWriteCallback);
                            }
                        }
                        @Override
                        public void onReadFailure(BleException exception) {
                            Log.i("SleepDoc", "BleRead Callback fail");
                            observer.onError(new Exception(exception.getDescription()));
                        }
                    });
                }
            });
        });
    }

    public void setTimeAndZone(final BleDevice _bleDevice) {
        Calendar c = Calendar.getInstance();
        TimeZone tz = c.getTimeZone();
        int time = (int) (c.getTimeInMillis() / 1000);
        int gmtOffset = (int) (tz.getRawOffset() / 1000);
        Log.i("타임셋", "time: " + time + ", gmt : " + gmtOffset);

        byte[] op = new byte[9];
        op[0] = (byte)0x06;

        ByteBuffer bb1 = ByteBuffer.wrap(new byte[4]);
        bb1.order(ByteOrder.LITTLE_ENDIAN);
        bb1.putInt(time);
        ByteBuffer bb2 = ByteBuffer.wrap(new byte[4]);
        bb2.order(ByteOrder.LITTLE_ENDIAN);
        bb2.putInt(gmtOffset);

        System.arraycopy(bb1.array(), 0, op, 1, 4);
        System.arraycopy(bb2.array(), 0, op, 5, 4);

        sysCmdChar.setValue(op);
        gatt.writeCharacteristic(sysCmdChar);
        contextDoc = UserDataModel.contextP;
        setUUID(_bleDevice, contextDoc);



        /*bleManager.write(_bleDevice, ServiceUUID.GENERAL.toString(), CharacteristicUUID.SYS_CMD.toString(), op,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.i("타임셋", "timeSet: 성공");
                        contextDoc = UserDataModel.contextP;
                        if(isFactory){
                            //setUUID(_bleDevice, contextDoc);
                            //bleDevice = _bleDevice;
                        }
                        else{
                            //getUUID(_bleDevice, contextDoc);
                            //bleDevice = _bleDevice;
                        }
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        Log.i("타임셋", "timeSet: 실패");
                    }
                });*/

    }

    public Observable battery(){
        return Observable.create(observer -> {
            bleManager.read(bleDevice, ServiceUUID.BATTERY.toString(), CharacteristicUUID.BATTERY.toString(), new BleReadCallback() {
                @Override
                public void onReadSuccess(byte[] data) {
                    observer.onNext(data[0]);
                    Log.i("SleepDoc", "배터리 "+ data[0]);
                    //setTimeAndZone(bleDevice);
                }
                @Override
                public void onReadFailure(BleException exception) {
                    Log.d("SleepDoc","배터리 실패");
                }
            });
        });
    }

    private synchronized void refreshDeviceCache(BluetoothGatt bluetoothGatt) {
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null && bluetoothGatt != null) {
                boolean success = (Boolean) refresh.invoke(bluetoothGatt);
                Log.i("SleepDoc", "refreshDeviceCache, is success:  " + success);
            }
        } catch (Exception e) {
            Log.i("SleepDoc", "exception occur while refreshing device: " + e.getMessage());
            e.printStackTrace();
        }
    }

    BleWriteCallback logWriteCallback = new BleWriteCallback() {
        @Override
        public void onWriteSuccess(int current, int total, byte[] justWrite) {
            Log.i("SleepDoc",  "write success, current: " + current
                    + " total: " + total
                    + " justWrite: " + justWrite.toString());
        }
        @Override
        public void onWriteFailure(BleException exception) {
            Log.i("SleepDoc", "Write Fail\n"+exception.toString());
        }
    };

    public void setUUID(final BleDevice bleDevice, Context context) {
        Log.d("SleepDoc","셋유유아이디 들어옴");
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID uuid = new UUID(androidId.hashCode(), androidId.hashCode());
        Log.i("SleepDoc", "setUUID "+ uuid.toString());
        byte[] op = new byte[17];

        // uuid to bytes
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        // must be little-endian
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        op[0] = (byte)0x0A;
        System.arraycopy(bb.array(), 0, op, 1, 16);
        bleManager.write(bleDevice, ServiceUUID.GENERAL.toString(), CharacteristicUUID.SYS_CMD.toString(), op,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.i("SleepDoc", "setUUID : " + "current : " + current + " total : " + total + " justWrite :" + HexUtil.formatHexString(justWrite));
                    }
                    @Override
                    public void onWriteFailure(BleException exception) {
                        Log.d("SleepDoc","셋유유아이디 실패");
                    }
                });
    }

    public void getUUID(final BleDevice bleDevice, final Context c) {
        Log.d("SleepDoc","겟유유아이디 들어옴");
        bleManager.write(bleDevice, ServiceUUID.GENERAL.toString(), CharacteristicUUID.SYS_CMD.toString(), new byte[]{(byte)0x0B},
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.i("SleepDoc", "getUUID : " + "current : " + current + " total : " + total + " justWrite :" + HexUtil.formatHexString(justWrite));
                        bleManager.read(bleDevice, ServiceUUID.GENERAL.toString(), CharacteristicUUID.SYS_CMD.toString(), new BleReadCallback() {
                            @Override
                            public void onReadSuccess(byte[] data) {
                                String uData = HexUtil.formatHexString(data);
                                Log.i("SleepDoc", "getUUID : " + "data : " + uData);
                                if(uData.contains("00000000000000000")){
                                    Log.d("SleepDoc","0만 나온다.");
                                    setUUID(bleDevice, c);
                                }
                            }
                            @Override
                            public void onReadFailure(BleException exception) {
                                Log.d("SleepDoc","겟유유아이디 실패");
                            }
                        });
                    }
                    @Override
                    public void onWriteFailure(BleException exception) { Log.d("SleepDoc","겟유유아이디 쓰기 실패");}
                });
    }
}
