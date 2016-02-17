package rupello.com.sumobotdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.shokai.firmata.ArduinoFirmata;
import org.shokai.firmata.ArduinoFirmataEventHandler;


import android.hardware.usb.*;
import android.util.*;
import android.widget.TextView;
import android.os.*;

import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    private String TAG = "SumoBotDemo";
    private ArduinoFirmata arduino;
    TextView tv ;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler = new Handler();

        setContentView(R.layout.activity_main);

        Log.v(TAG, "start");
        Log.v(TAG, "Firmata Lib Version : "+ArduinoFirmata.VERSION);

        tv = (TextView) findViewById(R.id.mainText);


        tv.setText("Connecting");
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
                while(arduino.isOpen()){
                    try{
                        Thread.sleep(100);
                        handler.post(new Runnable(){
                            public void run(){
                                try {
                                    for (int i = 0; i < 10; i++) {
                                        arduino.digitalWrite(13, true);
                                        Thread.sleep(500);
                                        arduino.digitalWrite(13, false);
                                        Thread.sleep(500);
                                    }
                                }
                                catch(InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
