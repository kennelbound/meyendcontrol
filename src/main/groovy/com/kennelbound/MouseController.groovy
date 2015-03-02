package com.kennelbound

import com.theeyetribe.client.GazeManager
import com.theeyetribe.client.IGazeListener
import com.theeyetribe.client.data.GazeData

import java.awt.Robot

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
        if ((gazeData.state & GazeData.STATE_TRACKING_GAZE) == 0 &&
                (gazeData.state & GazeData.STATE_TRACKING_PRESENCE) == 0) return;

        def x = smooth ? gazeData.smoothedCoordinates.x : gazeData.rawCoordinates.x;
        def y = smooth ? gazeData.smoothedCoordinates.x : gazeData.rawCoordinates.x;

        setMousePos((int) x, (int) y);
    }

    void setMousePos(int x, int y) {
        robot.mouseMove(x, y);
    }
}
