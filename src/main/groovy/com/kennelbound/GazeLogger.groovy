package com.kennelbound

import com.theeyetribe.client.IGazeListener
import com.theeyetribe.client.data.GazeData
import groovy.util.logging.Log4j

/**
 * Created by samalsto on 2/28/15.
 */
@Log4j
class GazeLogger implements IGazeListener {
    @Override
    void onGazeUpdate(GazeData gazeData) {
        log.debug("Received gaze x:$gazeData.rawCoordinates.x,y:$gazeData.rawCoordinates.y,sx:$gazeData.smoothedCoordinates.x,sy:$gazeData.smoothedCoordinates.y,state:${gazeData.stateToString()},lp:$gazeData.leftEye.pupilSize,rp:$gazeData.rightEye.pupilSize,lx:$gazeData.leftEye.rawCoordinates.x,ly:$gazeData.leftEye.rawCoordinates.y,rx:$gazeData.rightEye.rawCoordinates.x,ry:$gazeData.rightEye.rawCoordinates.y");
    }
}
