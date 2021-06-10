package com.example.p3l_apk_rastar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        shared = getSharedPreferences("getId", Context.MODE_PRIVATE);
        int user_id = shared.getInt("user_id", -1);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user_id == -1) { //Untuk Belum Scan Qr
                    Intent intent= new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else { //Untuk Sudah Scan QR
//                    Toast.makeText(SplashActivity.this, String.valueOf(user_id),
//                            Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(SplashActivity.this, MainAfterQrActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1500);
    }
}