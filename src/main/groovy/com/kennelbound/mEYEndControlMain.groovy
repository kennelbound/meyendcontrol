package com.kennelbound

import com.kennelbound.components.TrackBox
import com.theeyetribe.client.GazeManager
import groovy.util.logging.Log4j
import javafx.stage.StageStyle

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

        def mouseControl = new MouseController();
        mouseControl.init();

        def tb = null;

        start {
            registerBeanFactory "trackbox", TrackBox

            stage(title: 'GroovyFX Hello World', visible: true, style: StageStyle.UNDECORATED) {
                scene(id: 'main-scene', stylesheets: resource('/css/main.css'), width: 200, height: 200) {
                    tb = trackbox(id: 'trackbox', width: 200, height: 200) { }
                    GazeManager.instance.addGazeListener(tb);

                }
            }
        }

//        tetManager.calibrate();
//
//        if (tetManager.calibrated) {
//        }

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
