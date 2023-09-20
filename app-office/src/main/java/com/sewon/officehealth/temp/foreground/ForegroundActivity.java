package com.sewon.officehealth.temp.foreground;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sewon.officehealth.R;

import timber.log.Timber;


public class ForegroundActivity extends AppCompatActivity {
  Button btnStartService, btnStopService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.foreground_main);
    btnStartService = findViewById(R.id.buttonStartService);
    btnStopService = findViewById(R.id.buttonStopService);
    btnStartService.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.tag("Timber").d("Start Service");
        startService();
      }
    });
    btnStopService.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.tag("Timber").d("Stop Service");
        stopService();
      }
    });
  }

  public void startService() {
    Intent serviceIntent = new Intent(this, ForegroundService.class);
    serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
    ContextCompat.startForegroundService(this, serviceIntent);
  }

  public void stopService() {
    Intent serviceIntent = new Intent(this, ForegroundService.class);
    stopService(serviceIntent);
  }
}