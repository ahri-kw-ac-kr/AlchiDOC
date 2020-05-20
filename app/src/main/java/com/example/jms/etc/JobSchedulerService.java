package com.example.jms.etc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.clj.fastble.BleManager;
import com.example.jms.connection.model.BleService;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.model.dto.GPSDTO;
import com.example.jms.connection.viewmodel.APIViewModel;
import com.example.jms.connection.viewmodel.SleepDocViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// 실행될 작업에 대한 정의.
public class JobSchedulerService extends JobService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        JobTask jobTask = new JobTask(this);
        // 신규 Job 수행 조건이 만족되었을 때 호출됩니다.
        // onStartJob()의 종료 후에도 지속할 동작이 있다면 true, 여기에서 완료되면 false를 반환합니다.
        // true를 반환할 경우 finishJob()의 호출을 통해 작업 종료를 선언하거나,
        // 시스템이 필요 onStopJob()를 호출하여 작업을 중지할 수 있습니다.
        Toast.makeText(getApplicationContext(), "Job started ", Toast.LENGTH_LONG).show();
        jobTask.execute(jobParameters);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // 시스템에서 Job 종료 시 호출되며, 현재 처리 중인 동작들을 중지해야 합니다.
        // 갑작스러운 중지로 현재 실행하던 Job을 재실행해야 할 경우 true, 새로 스케쥴링을 할 필요가 없다면 false를 반환합니다.
        return false;
    }

    private class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;

        APIViewModel apiViewModel = new APIViewModel();
        SleepDocViewModel sleepDocViewModel = new SleepDocViewModel();

        public JobTask(JobService jobService) {
            this.jobService = jobService;
        }

        @SuppressLint("CheckResult")
        @Override
        protected JobParameters doInBackground(JobParameters... params) {

            //여기에 백그ㅏ운드에서 실행할 코드를 집어넣는다.
            /*여기에 코드코드코드*/

            if (BleService.principalDevice != null && RestfulAPI.principalUser != null) {
                sleepDocViewModel.getRawdataFromSleepDoc()
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(data -> {
                            data.setUser(RestfulAPI.principalUser);
                            apiViewModel.postRawdata(data)
                                    .observeOn(Schedulers.io())
                                    .subscribe(result -> {
                                    }, Throwable -> {
                                        Log.d("JobSchedulerService", "집어넣기 오류 " + Throwable.getMessage());
                                    });
                        }, Throwable -> Log.d("JobSchedulerService", "데이터 불러오기 오류 " + Throwable.getMessage()));
            }
            if (RestfulAPI.principalUser != null) {
                final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return TODO;
                    Log.d("JobSchedulerService","퍼미션 확인 막힘");
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                GPSDTO gpsdto = new GPSDTO();
                gpsdto.setLat(Double.toString(lat));
                gpsdto.setLon(Double.toString(lon));
                gpsdto.setUser(RestfulAPI.principalUser);
                Log.d("JobSchedulerService","현위치: "+gpsdto.getLat()+", "+gpsdto.getLon()+", "+gpsdto.getUser());
                apiViewModel.postGPS(gpsdto)
                        .observeOn(Schedulers.io())
                        .subscribe(result -> {},Throwable::printStackTrace); //-> Log.d("JobSchedulerService","GPS 집어넣기 오류 "+Throwable.getMessage()));
            }



            //밑에거 지워도 되나...?
            //지우자
            for(int i=1;i<=10;i++)
            {
                Log.e("number","num"+i);
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            jobService.jobFinished(jobParameters, false);
        }
    }
}
