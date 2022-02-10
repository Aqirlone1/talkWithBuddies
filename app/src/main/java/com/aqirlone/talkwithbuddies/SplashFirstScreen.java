package com.aqirlone.talkwithbuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Objects;


public class SplashFirstScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       Thread thread=new Thread() {
           @Override
           public void run() {
               try {
                sleep(1000);

               }catch (Exception e){
                   e.printStackTrace();

               }finally {
                   Intent intent=new Intent(SplashFirstScreen.this,MainActivity.class);
                   startActivity(intent);
               }
           }
       };thread.start();



    }
}