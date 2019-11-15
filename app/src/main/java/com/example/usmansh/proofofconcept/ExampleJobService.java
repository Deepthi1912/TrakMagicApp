package com.example.usmansh.proofofconcept;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class ExampleJobService extends JobService {


    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");


        Intent goAlert = new Intent(getApplicationContext(),AlertDialogAct.class);
           goAlert.putExtra("userId",params.getExtras().getString("userId"));
           goAlert.putExtra("ProName",params.getExtras().getString("ProjectName"));
           goAlert.putExtra("remCode",0);
           goAlert.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goAlert);

        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}
