package com.kennelbound

import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.IGazeListener
import com.theeyetribe.client.data.GazeData

import java.awt.Robot
import java.awt.event.InputEvent

/**
 * Created by samalsto on 2/28/15.
 */
class MouseController implements IGazeListener {
    private Robot robot;

    boolean smooth = true;
    boolean enabled = true;

    void init() {
        robot = new Robot();
        GazeManager.instance.addGazeListener(this);
    }

    /*
        Implements: IGazeListener
     */

    @Override
    void onGazeUpdate(GazeData gazeData) {
        if (!enabled) return;

        // Don't do anything if you've lost the gaze
        if ((gazeData.state & GazeData.STATE_TRACKING_GAZE) == 1) {
            def x = smooth ? gazeData.smoothedCoordinates.x : gazeData.rawCoordinates.x;
            def y = smooth ? gazeData.smoothedCoordinates.y : gazeData.rawCoordinates.y;

            setMousePos((int) x, (int) y);
        }

        if ((gazeData.state & GazeData.STATE_TRACKING_PRESENCE) >= 1) {
            // Check if the right eye is closed.
            if(gazeData.rightEye.pupilCenterCoordinates.x <= 0 || gazeData.rightEye.pupilCenterCoordinates.y <= 0) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        }
    }

    void setMousePos(int x, int y) {
        robot.mouseMove(x, y);
    }
}
