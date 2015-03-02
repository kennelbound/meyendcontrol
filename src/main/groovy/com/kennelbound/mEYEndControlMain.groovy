package com.kennelbound

import groovy.util.logging.Log4j

import static groovyx.javafx.GroovyFX.start

/**
 * Created by samalsto on 2/27/15.
 */
@Log4j
class mEYEndControlMain {
    TetManager tetManager;

    /*
        Constructor
     */

    void init() {
        tetManager = new TetManager();
        tetManager.init();

        start {

        }

        tetManager.calibrate();

        if (tetManager.calibrated) {
            tetManager.enableMouse();
        }

        while (true) {
            // do nothing until the break
        }

        log.info "Closing";
        System.exit(0);
    }

    void logState() {
        log.info("calibration state is $tetManager.calibrated, activation state is $tetManager.activated and connection state is $tetManager.connected");
    }

    static void main(String... args) {
        new mEYEndControlMain().init();
    }
}
