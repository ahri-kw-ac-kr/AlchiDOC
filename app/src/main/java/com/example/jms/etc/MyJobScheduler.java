package com.example.jms.etc;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.TimeUnit;


/*
*  스케줄러 참고 링크
* https://github.com/arunk7839/JobSchedulerExample
* https://www.vogella.com/tutorials/AndroidTaskScheduling/article.html
*
* */


//정의한 작업을 스케줄링해준다.
public class MyJobScheduler {
    private static final int JOB_ID = 0x1000;


    public void setUpdateJob(Context context) {
        final JobInfo jobInfo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(context, JobSchedulerService.class))
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(16))
                    .build();
            Log.d("D", "Scheduled JobA with version higher than N");

        }

        else {
            jobInfo = new JobInfo.Builder(JOB_ID, new ComponentName(context, JobSchedulerService.class))
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                    .build();
            Log.d("D", "Scheduled JobA with version lower than N");
        }

        // JobScheduler 서비스
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        // Job을 등록한다.
        jobScheduler.schedule(jobInfo);
    }
}

