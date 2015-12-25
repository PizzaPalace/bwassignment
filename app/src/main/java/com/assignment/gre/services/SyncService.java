package com.assignment.gre.services;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import com.assignment.gre.backgroundtasks.DataFetcherTask;
import com.assignment.gre.listeners.GREListListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 25/12/15.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SyncService extends JobService implements GREListListener {

    private JobParameters mJobParams;

    @Override
    public boolean onStartJob(JobParameters params) {

        this.mJobParams = params;
        new DataFetcherTask(this,getApplicationContext()).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onWordListDownloaded(ArrayList<HashMap<String, Object>> wordlist) {
        //Log.v("HI4",wordlist.toString());
        jobFinished(mJobParams, false);
    }
}
