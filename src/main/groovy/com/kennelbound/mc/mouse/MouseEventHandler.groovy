package com.kennelbound.mc.mouse

import com.kennelbound.mc.mouse.events.MouseClickEvent
import com.kennelbound.mc.mouse.events.MouseMoveEvent
import net.engio.mbassy.bus.MBassador
import net.engio.mbassy.listener.Handler
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct
import java.awt.*

/**
 * Created by samalsto on 4/2/15.
 */
class MouseEventHandler {
    @Autowired
    MBassador eventBus

    private Robot robot = new Robot()

    @PostConstruct
    void init() {
        eventBus.subscribe(this)
    }

    @Handler
    void handleClickEvent(MouseClickEvent event) {
        Thread.start {
            (0..event.count).each {
                robot.mousePress(event.button)
                Thread.sleep(event.duration / event.count)
                robot.mouseRelease(event.button)
            }
        }
    }

    @Handler
    void handleMoveEvent(MouseMoveEvent event) {
        robot.mouseMove(event.moveToX, event.moveToY)
    }
}
