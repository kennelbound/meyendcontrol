package com.kennelbound.mc.behaviors.actions

import com.kennelbound.eyelib.events.HeadLocationEvent
import com.kennelbound.mc.mouse.events.MouseClickEvent
import net.engio.mbassy.bus.MBassador
import org.springframework.beans.factory.annotation.Autowired

import java.awt.event.InputEvent

/**
 * Created by samalsto on 4/2/15.
 */
class MouseClickAction implements IAction<Boolean, HeadLocationEvent> {
    @Autowired
    MBassador eventBus;

    int count = 1
    int duration = 100
    int button = InputEvent.BUTTON1_DOWN_MASK

    @Override
    void execute(Boolean response, HeadLocationEvent event) {
        if (response) {
            eventBus.publish(new MouseClickEvent(button, duration, count))
        }
    }
}
