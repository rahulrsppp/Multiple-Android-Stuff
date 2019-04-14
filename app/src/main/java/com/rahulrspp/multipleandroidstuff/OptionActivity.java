package com.rahulrspp.multipleandroidstuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rahulrspp.multipleandroidstuff.services.localBinding.LocalActivity;
import com.rahulrspp.multipleandroidstuff.thread.looper.LooperActivity;
import com.rahulrspp.multipleandroidstuff.services.remoteClientSideBinding.RemoteClientSideActivity;

public class OptionActivity extends AppCompatActivity {

    TextView tvNo;
    Button btnLocal, btnRemote, btnLooper;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        setViewId();
        setListener();

    }

    private void setViewId() {
        tvNo=findViewById(R.id.tvNo);
        btnRemote=findViewById(R.id.btnRemote);
        btnLocal=findViewById(R.id.btnLocal);
        btnLooper=findViewById(R.id.btnLooper);
    }

    private void setListener() {
        btnLocal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                intent =new Intent(OptionActivity.this, LocalActivity.class);
                startActivity(intent);
            }
        });

        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent =new Intent(OptionActivity.this, RemoteClientSideActivity.class);
                startActivity(intent);

            }
        });

        btnLooper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent =new Intent(OptionActivity.this, LooperActivity.class);
                startActivity(intent);
            }
        });


    }



}
