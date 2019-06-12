package com.rahulrspp.multipleandroidstuff.services.localBinding;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rahulrspp.multipleandroidstuff.R;

public class LocalActivity extends AppCompatActivity {

    TextView tvNo;
    Button btnStartService, btnStopService,btnGetNo, btnBindService, btnUnBindService;
    private ServiceConnection serviceConnection;
    private Intent intent;
    private MyLocalService myServerService;
    private int randomNo=0;
    private boolean isServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        setViewId();
        setListener();

    }

    private void setViewId() {
        tvNo=findViewById(R.id.tvNo);
        btnStartService=findViewById(R.id.btnStartService);
        btnStopService=findViewById(R.id.btnStopService);
        btnBindService=findViewById(R.id.btnBindService);
        btnUnBindService=findViewById(R.id.btnUnBindService);
        btnGetNo=findViewById(R.id.btnGetNo);

        intent =new Intent(LocalActivity.this, MyLocalService.class);

    }

    private void setListener() {
        btnStartService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startService(intent);
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(intent);
            }
        });

        btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setConnection();
            }
        });

        btnUnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServiceBound) {
                    isServiceBound= false;
                    unbindService(serviceConnection);
                }else{
                    Toast.makeText(LocalActivity.this, "Service is not Bound", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnGetNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isServiceBound){
                    randomNo = myServerService.getRandomNo();
                    tvNo.setText(String.valueOf(randomNo));
                }else{
                    Toast.makeText(LocalActivity.this, "Service is not Bound", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setConnection(){
        if(serviceConnection==null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    MyLocalService.MyServer myServer=  (MyLocalService.MyServer) iBinder;
                    myServerService = myServer.getInstance();
                    isServiceBound=true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound= false;
                }
            };
        }

        bindService(intent, serviceConnection,BIND_AUTO_CREATE);
    }

}
