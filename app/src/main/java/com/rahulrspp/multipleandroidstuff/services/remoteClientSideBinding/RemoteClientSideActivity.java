    package com.rahulrspp.multipleandroidstuff.services.remoteClientSideBinding;

    import android.content.ComponentName;
    import android.content.Intent;
    import android.content.ServiceConnection;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.IBinder;
    import android.os.Message;
    import android.os.Messenger;
    import android.os.RemoteException;
    import android.support.v7.app.AppCompatActivity;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.rahulrspp.multipleandroidstuff.R;
    import com.rahulrspp.multipleandroidstuff.services.localBinding.MyLocalService;

    public class RemoteClientSideActivity extends AppCompatActivity {

        TextView tvNo;
        Button btnGetNo, btnBindService, btnUnBindService;
        private ServiceConnection serviceConnection;
        private Intent intent;
        private MyLocalService myServerService;
        private int randomNo=0;
        private boolean isServiceBound;
        private Messenger requestMessenger, responseMessenger;
        private final int GET_RANDOM_NO = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_remote_client_side);
            setViewId();
            setListener();

        }

        private void setViewId() {
            tvNo=findViewById(R.id.tvNo);

            btnBindService=findViewById(R.id.btnBindService);
            btnUnBindService=findViewById(R.id.btnUnBindService);
            btnGetNo=findViewById(R.id.btnGetNo);

            intent = new Intent();
            intent.setComponent(new ComponentName("com.testapplication.test","com.testapplication.test.MyRemoteServerSideService"));

        }

        private void setListener() {


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
                        Toast.makeText(RemoteClientSideActivity.this,"Service Unbound",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(RemoteClientSideActivity.this, "Service is not Bound", Toast.LENGTH_SHORT).show();

                    }
                }
            });

            btnGetNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRandomNoFromService();
                }
            });
        }

        private void getRandomNoFromService() {
            if(isServiceBound){
               Message message=Message.obtain(null, GET_RANDOM_NO);
               message.replyTo = responseMessenger;

                try {
                    requestMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(RemoteClientSideActivity.this, "Service is not Bound", Toast.LENGTH_SHORT).show();
            }
        }


        private void setConnection(){
            if(serviceConnection==null){
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        requestMessenger = new Messenger(iBinder);
                        responseMessenger = new Messenger(new ResponseHandler());
                        isServiceBound=true;
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        isServiceBound= false;
                        requestMessenger=null;
                        responseMessenger=null;
                    }
                };
            }

            bindService(intent, serviceConnection,BIND_AUTO_CREATE);
            Toast.makeText(this,"Service bound",Toast.LENGTH_SHORT).show();

        }

        class ResponseHandler extends Handler{

            @Override
            public void handleMessage(Message message) {
                randomNo=0;
                switch (message.what){
                    case GET_RANDOM_NO:
                        randomNo=message.arg1;
                        tvNo.setText(String.valueOf(randomNo));
                        break;
                    default:
                        break;
                }
                super.handleMessage(message);
            }


        }

    }
