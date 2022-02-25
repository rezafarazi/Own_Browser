package com.sorapp.own.browser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class Splash_ac extends AppCompatActivity
{

    //Global Variables
    CountDownTimer Timer;



    //Main Function Start
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_ac);
        Get_TIMER();
    }
    //Main Function End



    //Timer Function Start
    public void Get_TIMER()
    {
        Timer=new CountDownTimer(3000,1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {

            }

            @Override
            public void onFinish()
            {
                startActivity(new Intent(getApplicationContext(),Main_ac.class));

                Timer.cancel();
            }
        }.start();
    }
    //Timer Function End



}