package com.kennelbound

import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.IConnectionStateListener
import groovy.util.logging.Log

/**
 * Created by samalsto on 2/27/15.
 */
@Log
class TetManager implements IConnectionStateListener {
    boolean connected = false;
    CalibrationManager calibrationManager = new CalibrationManager();
    MouseController mouseController = new MouseController();
    GazeLogger gazeLogger = new GazeLogger();

    /*
        Implementation: IConnectionStateListener
     */

    @Override
    void onConnectionStateChanged(boolean connected) {
        this.connected = connected;
    }

    /*
        Public Methods:
     */

    void init() {
        GazeManager.instance.activate(GazeManager.ApiVersion.VERSION_1_0, GazeManager.ClientMode.PUSH, this);
        GazeManager.instance.addGazeListener(gazeLogger);
    }

    void calibrate() {
        if (calibrated) {
            return;
        }

        if (activated) {
            calibrationManager.calibrate();
        } else {
            throw new Exception("Please plug in before trying to calibrate.");
        }
    }

    boolean getMouseSmoothing() {
        mouseController.smooth;
    }

    void setMouseSmoothing(boolean smooth) {
        mouseController.smooth = smooth;
    }

    boolean enableMouse() {
        mouseController.enabled = true;
    }

    boolean disableMouse() {
        mouseController.enabled = false;
    }

    boolean isActivated() {
        return GazeManager.instance.activated;
    }

    boolean isCalibrated() {
        return GazeManager.instance.calibrated;
    }

    boolean isConnected() {
        return this.connected;
    }
}
