package com.kennelbound.mc.behaviors

import com.kennelbound.eyelib.events.AbstractEvent
import com.kennelbound.mc.behaviors.actions.IAction
import com.kennelbound.mc.behaviors.triggers.ITrigger
import net.engio.mbassy.bus.MBassador
import net.engio.mbassy.listener.Handler
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct

/**
 * Created by samalsto on 4/1/15.
 */
class Behavior {
    @Autowired
    MBassador eventBus

    ITrigger trigger
    IAction action

    Class supportedEventType

    @PostConstruct
    void init() {
        eventBus.subscribe(this)
    }

    @Handler
    void handleEvent(AbstractEvent event) {
        if (event?.class?.name != supportedEventType?.name) {
            return;
        }

        def response = trigger.check(event);
        if (response) {
            action.execute(response, event)
        }
    }
}
