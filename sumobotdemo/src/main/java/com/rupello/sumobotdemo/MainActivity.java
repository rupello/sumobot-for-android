package com.rupello.sumobotdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.shokai.firmata.ArduinoFirmata;
import org.shokai.firmata.ArduinoFirmataEventHandler;


import android.util.*;
import android.widget.TextView;
import android.os.*;

import com.MobileAnarchy.Android.Widgets.Joystick.DualJoystickView;
import com.MobileAnarchy.Android.Widgets.Joystick.JoystickMovedListener;
import com.rupello.sumobot.SumoBot;

import java.io.IOException;

public class MainActivity extends Activity {

    private String TAG = "SumoBotDemo";
    private ArduinoFirmata arduino;
    private Handler handler;
    private DualJoystickView mDualJoystickView;
    private double mLeftSpeed = 0.0 ;
    private double mRightSpeed = 0.0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.handler = new Handler();

        setContentView(R.layout.activity_main);

        mDualJoystickView = (DualJoystickView) findViewById(R.id.joysticks);
        mDualJoystickView.setOnJoystickMovedListener(_listenerLeft, _listenerRight);

        Log.v(TAG, "start");
        Log.v(TAG, "Firmata Lib Version : " + ArduinoFirmata.VERSION);


        this.arduino = new ArduinoFirmata(this);
        this.arduino.setEventHandler(new ArduinoFirmataEventHandler(){
            public void onError(String errorMessage){
                Log.e(TAG, errorMessage);
            }
            public void onClose(){
                Log.v(TAG, "arduino closed");
            }
        });

        Thread thread = new Thread(new Runnable(){
            public void run(){
                if(arduino.isOpen()) {
                    try {
                            SumoBot bot = new SumoBot(arduino);
                            Thread.sleep(500);
                            bot.setSpeed(0.0);
                            Thread.sleep(500);
                            while (arduino.isOpen()) {
                                try {
                                    Thread.sleep(100);
                                    bot.setLeftSpeed(mLeftSpeed);
                                    bot.setRightSpeed(mRightSpeed);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.v(TAG, "exited bot control loop");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.v(TAG, "exception in+ bot control loop");
                    }
                }
            }
        });

        try{
            Log.v(TAG, "connecting...");
            arduino.connect();
            Log.v(TAG, "connected");
            Log.v(TAG, "Board Version : "+arduino.getBoardVersion());

            thread.start();
        }
        catch(IOException e){
            e.printStackTrace();
            finish();
        }
        catch(InterruptedException e){
            e.printStackTrace();
            finish();
        }
    }

    private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

        @Override
        public void OnMoved(float pan, float tilt) {
            Log.v(TAG, "OnMoved Left" + pan + " " + tilt);
            mLeftSpeed = tilt ;
        }

        @Override
        public void OnReleased() {
        }

        public void OnReturnedToCenter() {
        };
    };

    private JoystickMovedListener _listenerRight = new JoystickMovedListener() {

        @Override
        public void OnMoved(float pan, float tilt) {
            Log.v(TAG, "OnMoved Right" + pan + " " + tilt);
            mRightSpeed = tilt ;
        }

        @Override
        public void OnReleased() {
        }

        public void OnReturnedToCenter() {
        };
    };


}
