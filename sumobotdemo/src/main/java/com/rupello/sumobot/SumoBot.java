/**
 * Created by rupello on 2/26/2016.
 */

package com.rupello.sumobot;

import org.shokai.firmata.ArduinoFirmata;

public class SumoBot {
    private CRServoMotor mLeftWheel ;
    private CRServoMotor mRightWheel ;
    private ArduinoFirmata mArduino ;

    public SumoBot(ArduinoFirmata arduino) {
        this.mArduino = arduino ;
        this.mLeftWheel = new CRServoMotor(arduino,10,false);
        this.mRightWheel = new CRServoMotor(arduino,9,true);
    }

    public void setSpeed(double speed) {
        this.mLeftWheel.setSpeed(speed);
        this.mRightWheel.setSpeed(speed);
    }

    public void setLeftSpeed(double speed) {
        this.mLeftWheel.setSpeed(speed);
    }

    public void setRightSpeed(double speed) {
        this.mRightWheel.setSpeed(speed);
    }

    public void setTurnSpeed(double speed) {
        // speed is +1.0(fast left) to -1.0(fast right)
        this.mLeftWheel.setSpeed(-1.0 * speed);
        this.mRightWheel.setSpeed(speed);
    }

}

