package com.star.apptest;

import android.util.Log;

import com.core.base.request.SRequestAsyncTask;

import java.util.concurrent.CountDownLatch;

/**
 * Created by gan on 2017/4/11.
 */

public class SignleTask extends SRequestAsyncTask {

    CountDownLatch countDownLatch;
    int threadId = 1;

    public SignleTask(int threadId, CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        this.threadId = threadId;
    }

    public void run() {

        try {
            Log.d(SignleTask.class.getCanonicalName(),"sleep start");
            Thread.sleep(this.threadId * 1000);
//            Log.d(SignleTask.class.getCanonicalName(),"sleep finish");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        run();
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (countDownLatch != null) {
            countDownLatch.countDown();
            Log.d(SignleTask.class.getCanonicalName(),"sleep finish " + threadId);

        }
    }
}
