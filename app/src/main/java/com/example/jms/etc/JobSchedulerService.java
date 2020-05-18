package com.example.jms.etc;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

// 실행될 작업에 대한 정의.
public class JobSchedulerService extends JobService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        // 신규 Job 수행 조건이 만족되었을 때 호출됩니다.
        // onStartJob()의 종료 후에도 지속할 동작이 있다면 true, 여기에서 완료되면 false를 반환합니다.
        // true를 반환할 경우 finishJob()의 호출을 통해 작업 종료를 선언하거나,
        // 시스템이 필요 onStopJob()를 호출하여 작업을 중지할 수 있습니다.
        Toast.makeText(getApplicationContext(), "Job started ", Toast.LENGTH_LONG).show();
        new JobTask(this).execute(jobParameters);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // 시스템에서 Job 종료 시 호출되며, 현재 처리 중인 동작들을 중지해야 합니다.
        // 갑작스러운 중지로 현재 실행하던 Job을 재실행해야 할 경우 true, 새로 스케쥴링을 할 필요가 없다면 false를 반환합니다.
        return false;
    }

    private static class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;

        public JobTask(JobService jobService) {
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {

        //여기에 백그ㅏ운드에서 실행할 코드를 집어넣는다.
            /*여기에 코드코드코드*/

            //밑에거 지워도 되나...?
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
