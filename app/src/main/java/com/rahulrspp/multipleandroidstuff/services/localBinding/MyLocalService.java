package com.rahulrspp.multipleandroidstuff.services.localBinding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class MyLocalService extends Service {

    private int randomNo=0;
    private boolean isNoGenerationOn =false;

    class MyServer extends Binder{

        MyLocalService getInstance(){
            return MyLocalService.this;
        }
    }

    MyServer myServer=new MyServer();

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println(":::: Bind");
        return myServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println(":::: UnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        System.out.println(":::: ReBind");

        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println(":::: Service Start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                isNoGenerationOn =true;
                startRandomNoGenerator();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println(":::: Service Stop");
         stopRandomNoGenerator();
    }

    private void startRandomNoGenerator(){
        while(isNoGenerationOn){
            try {
                Thread.sleep(2000);
                Random random=new Random();
                randomNo = random.nextInt(100);
                System.out.println(":::: Generated No: "+randomNo);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void stopRandomNoGenerator(){
        isNoGenerationOn=false;
    }


    public int getRandomNo() {
        return randomNo;
    }


}
