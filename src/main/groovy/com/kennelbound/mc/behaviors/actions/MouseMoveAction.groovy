package com.kennelbound.mc.behaviors.actions

import com.kennelbound.eyelib.events.HeadLocationEvent
import com.kennelbound.mc.mouse.events.MouseMoveEvent
import net.engio.mbassy.bus.MBassador
import org.springframework.beans.factory.annotation.Autowired

import java.awt.*

/**
 * Created by samalsto on 4/2/15.
 */
class MouseMoveAction implements IAction<Point, HeadLocationEvent> {
    @Autowired
    MBassador eventBus

    void execute(Point response, HeadLocationEvent event) {
        eventBus.publish(new MouseMoveEvent(response.x, response.y))
    }
}
