package com.rxn.compute;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    TextView responseText;

    LoadManager workThread;
    boolean networkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        createThread();
        createResponseText();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    1);
        }

        else {
            networkEnabled = true;
            startRoutine();
        }
    }

    private void createResponseText() {
        responseText = findViewById(R.id.response_text);
    }

    public void setResponseText(String text) {
        responseText.setText(text);
    }

    private void startRoutine() {
        workThread.setRunning(true);
        workThread.start();
    }

    private void createThread() {
        workThread = new LoadManager(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if(requestCode == 1) {
            int numGranted = 0;
            for(int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    numGranted++;
                    Log.e("AppPreferenceActivity", "Permission granted for: " + permissions[i]);
                } else {
                    Log.e("AppPreferenceActivity", "Permission NOT granted for: " + permissions[i]);
                }
            }
            if(numGranted == 0) {
                Log.e("AppPreferenceActivity", "No permissions granted.");
            }
        }
    }
}
