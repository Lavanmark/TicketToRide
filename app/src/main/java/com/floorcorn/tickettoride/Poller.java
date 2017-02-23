package com.floorcorn.tickettoride;

import android.app.job.JobParameters;
import android.app.job.JobService;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by mgard on 2/14/2017.
 */

public class Poller {
    //@Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    //@Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
//    @Override
//    public void pollPlayerList() {
//        scheduledExecutorService = Executors.newScheduledThreadPool(2);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//
//            @Override
//            public void run() {
//                PregameActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        presenter.requestPlayerList();
//                    }
//                });
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//    }
}
