package com.kennelbound

import groovyx.javafx.GroovyFX
import javafx.stage.StageStyle

/**
 * Created by samalsto on 3/1/15.
 */
class TestWindow {

    static void main(String ... args) {
        GroovyFX.start {
            stage(title: 'GroovyFX Hello World', visible: true, style: StageStyle.UNDECORATED) {

                scene(fill: PALEGREEN, width: 200, height: 200) {
                }
                scene(fill: BLACK, width:100, height: 100){}
            }
        }
    }
}
