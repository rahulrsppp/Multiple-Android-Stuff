package com.rahulrspp.multipleandroidstuff.thread.looper;

import android.os.Handler;
import android.os.Looper;

public class CustomLooperThread extends Thread {

     Handler handler;

    @Override
    public void run() {
        super.run();
       Looper.prepare();

       /* handler=new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                int count = (int)msg.obj;

                System.out.println("::: Looper Thread Id: "+Thread.currentThread().getId()+", Count: "+count);
            }
        };*/
        handler=new Handler();


       Looper.loop();
    }
}
