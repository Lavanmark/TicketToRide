package com.floorcorn.tickettoride;

import android.app.job.JobParameters;
import android.app.job.JobService;

/**
 * Created by mgard on 2/14/2017.
 */

public class Poller extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
