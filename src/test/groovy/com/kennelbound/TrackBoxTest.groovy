package com.kennelbound

import com.kennelbound.components.TrackBox
import groovyx.javafx.GroovyFX
import javafx.stage.StageStyle

/**
 * Created by samalsto on 3/1/15.
 */
class TrackBoxTest {

    static void main(String... args) {
        GroovyFX.start { primaryStage ->
            registerBeanFactory "trackbox", TrackBox

            stage(title: 'GroovyFX Hello World', visible: true, style: StageStyle.UNDECORATED) {
                scene(id: 'main-scene', stylesheets: resource('/css/main.css')) {
                    trackbox(id: 'trackbox', width: 100, height: 100) {
                    }
                }
            }
        }
    }
}
