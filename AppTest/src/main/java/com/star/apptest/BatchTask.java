package com.star.apptest;

import android.os.Handler;

import com.core.base.utils.PL;

import java.util.concurrent.CountDownLatch;

/**
 * Created by gan on 2017/4/11.
 */

public class BatchTask extends Thread {

    CountDownLatch countDownLatch;
    Handler handler;

    public BatchTask() {
        this.countDownLatch = new CountDownLatch(10);
        this.handler = new Handler();
    }

    @Override
    public void run() {

        handler.post(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 10; i++) {
                    SignleTask s = new SignleTask(i + 1,countDownLatch);
                    s.asyncExcute();
                }
            }
        });


        try {
            countDownLatch.await();
            PL.i("await finish ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    PL.i("handle post main thread");
                }
            });


        }

    }
}
