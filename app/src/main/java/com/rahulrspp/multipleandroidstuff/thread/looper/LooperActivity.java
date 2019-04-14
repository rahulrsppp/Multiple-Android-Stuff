package com.rahulrspp.multipleandroidstuff.thread.looper;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rahulrspp.multipleandroidstuff.R;
import com.rahulrspp.multipleandroidstuff.services.localBinding.MyLocalService;

public class LooperActivity extends AppCompatActivity {

    private TextView tvNo;
    private Button btnStartThread, btnStopThread;
    private ServiceConnection serviceConnection;
    private Intent intent;
    private MyLocalService myServerService;
    private int randomNo=0;
    private boolean isServiceBound;
    private boolean isThreadRunning=false;
    private int count=0;

    private CustomLooperThread customLooperThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper);
        setViewId();
        setListener();

    }

    private void setViewId() {
        tvNo=findViewById(R.id.tvNo);
        btnStartThread=findViewById(R.id.btnStartThread);
        btnStopThread=findViewById(R.id.btnStopThread);

        customLooperThread=new CustomLooperThread();
        customLooperThread.start();

    }

    private void setListener() {
        btnStartThread.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                isThreadRunning=true;
                executeOnCustomLooperWithCustomHandler();
            }
        });

        btnStopThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isThreadRunning=false;
            }
        });
    }


/*
        private void executeOnCustomLooper(){
            count=0;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    while(isThreadRunning){
                        try {
                            Thread.sleep(1000);
                            count++;
                            Message message=new Message();
                            message.obj = count;

                            customLooperThread.handler.sendMessage(message);

                            System.out.println("::: Counter Thread Id: "+Thread.currentThread().getId()+", Count: "+count);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
*/

    private void executeOnCustomLooperWithCustomHandler(){
        customLooperThread.handler.post(new Runnable() {
            @Override
            public void run() {
                count=0;
                System.out.println("::: Handler Thread Id: "+Thread.currentThread().getId()+", Count: "+count);

                while(isThreadRunning){
                    try {
                        Thread.sleep(1000);
                        count++;
                        System.out.println("::: Thread Id: "+Thread.currentThread().getId()+", Count: "+count);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("::: RunOnUiThread Thread Id: "+Thread.currentThread().getId()+", Count: "+count);

                                tvNo.setText(String.valueOf(count));
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

    }

}
