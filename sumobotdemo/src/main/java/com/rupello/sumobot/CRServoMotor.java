/**
 * Created by rupello on 2/26/2016.
 */

package com.rupello.sumobot;

import org.shokai.firmata.ArduinoFirmata;


public class CRServoMotor {
    static private final int STOP = 94;        // stop value
    private ArduinoFirmata mArduino ;          // the arduino (firmata) object
    private int mPin;                          // which pin ios this servo on?
    private boolean mReversed = false ;        // reverse direction (usually done for one side)

    public CRServoMotor(ArduinoFirmata arduino, int pin, boolean reversed ){
        this.mArduino = arduino;
        this.mPin = pin;
        this.mReversed = reversed;
    }

    public void stop() {
        this.mArduino.servoWrite(this.mPin,CRServoMotor.STOP);
    }

    // setSpeed at speed between -1 and +1
    public void setSpeed(double speed) {
        if (this.mReversed) {
            speed *= -1.0;
        }
        this.mArduino.servoWrite(this.mPin,CRServoMotor.STOP+(int)(speed*30.0));
    }
}
