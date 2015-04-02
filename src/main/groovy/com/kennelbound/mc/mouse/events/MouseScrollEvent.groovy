package com.kennelbound.mc.mouse.events

import com.kennelbound.eyelib.events.AbstractEvent

/**
 * Created by samalsto on 4/1/15.
 */
class MouseScrollEvent extends AbstractEvent {
    final double x, y

    MouseScrollEvent(double x, double y) {
        this.x = x
        this.y = y
    }
}
