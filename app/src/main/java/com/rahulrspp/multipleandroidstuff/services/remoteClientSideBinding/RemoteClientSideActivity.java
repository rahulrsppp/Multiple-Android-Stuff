    package com.rahulrspp.multipleandroidstuff.services.remoteClientSideBinding;

    import android.content.ComponentName;
    import android.content.Intent;
    import android.content.ServiceConnection;
    import android.graphics.Color;
    import android.net.Uri;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.IBinder;
    import android.os.Message;
    import android.os.Messenger;
    import android.os.RemoteException;
    import android.support.v7.app.AppCompatActivity;
    import android.text.Spannable;
    import android.text.SpannableString;
    import android.text.Spanned;
    import android.text.method.LinkMovementMethod;
    import android.text.style.ClickableSpan;
    import android.text.style.ForegroundColorSpan;
    import android.text.style.UnderlineSpan;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.rahulrspp.multipleandroidstuff.R;
    import com.rahulrspp.multipleandroidstuff.services.localBinding.MyLocalService;

    public class RemoteClientSideActivity extends AppCompatActivity {

        TextView tvNo, tvClick;
        Button btnGetNo, btnBindService, btnUnBindService;
        private ServiceConnection serviceConnection;
        private Intent intent;
        private MyLocalService myServerService;
        private int randomNo = 0;
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
            tvClick=findViewById(R.id.tvClick);

            btnBindService=findViewById(R.id.btnBindService);
            btnUnBindService=findViewById(R.id.btnUnBindService);
            btnGetNo=findViewById(R.id.btnGetNo);

            intent = new Intent();
            intent.setComponent(new ComponentName("com.rahulrspp.serviceserverside","com.rahulrspp.serviceserverside.MyRemoteServerSideService"));


            final String data = tvClick.getText().toString();
            final int position = data.indexOf("http");
            System.out.println("::: Position: "+position);
            SpannableString   spannableString=new SpannableString(data);
            spannableString.setSpan(new UnderlineSpan(),position, tvClick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), position, tvClick.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    String getUrl = data.substring(position, tvClick.length()-1);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl));
                    startActivity(browserIntent);
                }};

            spannableString.setSpan(clickableSpan, position, tvClick.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvClick.setText(spannableString);
            tvClick.setMovementMethod(LinkMovementMethod.getInstance());

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
