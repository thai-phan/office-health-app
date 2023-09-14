package com.sewon.officehealth.screen.demo;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sewon.officehealth.R;


public class DemoMain extends AppCompatActivity {
  BoundService mBoundService;
  boolean mServiceBound = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    final TextView timestampText = (TextView) findViewById(R.id.timestamp_text);
    Button printTimestampButton = (Button) findViewById(R.id.print_timestamp);
    Button stopServiceButon = (Button) findViewById(R.id.stop_service);
    printTimestampButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mServiceBound) {
          timestampText.setText(mBoundService.getTimestamp());
        }
      }
    });

    stopServiceButon.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mServiceBound) {
          unbindService(mServiceConnection);
          mServiceBound = false;
        }
        Intent intent = new Intent(DemoMain.this,
            BoundService.class);
        stopService(intent);
      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();
    Intent intent = new Intent(this, BoundService.class);
    startService(intent);
    bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (mServiceBound) {
      unbindService(mServiceConnection);
      mServiceBound = false;
    }
  }

  private ServiceConnection mServiceConnection = new ServiceConnection() {

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mServiceBound = false;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      BoundService.MyBinder myBinder = (BoundService.MyBinder) service;
      mBoundService = myBinder.getService();
      mServiceBound = true;
    }
  };
}