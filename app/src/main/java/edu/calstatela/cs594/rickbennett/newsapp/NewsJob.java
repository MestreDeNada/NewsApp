package edu.calstatela.cs594.rickbennett.newsapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Rick on 8/6/17.
 */

//Homework 4 firebase: Added this class for firebase job dispatcher.
public class NewsJob extends JobService {

    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d("NewsJob", "onStartJob called");
        mBackgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("NewsJob", "onPreExecute");
            }
            @Override
            protected Object doInBackground(Object[] params) {
                DatabaseUtils.refreshDatabase(NewsJob.this);
                Log.d("NewsJob", "Refreshing database in background");
                return null;
            }
            @Override
            protected void onPostExecute(Object object) {
                super.onPostExecute(object);
                jobFinished(job, false);
                Log.d("NewsJob", "Database refreshed");
                Toast.makeText(NewsJob.this, "News refreshed", Toast.LENGTH_LONG).show();
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(false);
        return true;
    }
}
