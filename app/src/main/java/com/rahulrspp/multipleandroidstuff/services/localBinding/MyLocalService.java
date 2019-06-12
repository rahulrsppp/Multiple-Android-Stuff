package com.rahulrspp.multipleandroidstuff.services.localBinding;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Random;

public class MyLocalService extends Service {

    private int randomNo=0;
    private boolean isNoGenerationOn =false;

    class MyServer extends Binder{

        MyLocalService getInstance(){
            return MyLocalService.this;
        }
    }

    MyServer myServer = new MyServer();

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(),"Service Bind", Toast.LENGTH_SHORT).show();
        return myServer;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getApplicationContext(),"Service UnBind", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Toast.makeText(getApplicationContext(),"Service ReBind", Toast.LENGTH_SHORT).show();
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),"Service Start", Toast.LENGTH_SHORT).show();


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
        Toast.makeText(getApplicationContext(),"Service Stop", Toast.LENGTH_SHORT).show();
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
