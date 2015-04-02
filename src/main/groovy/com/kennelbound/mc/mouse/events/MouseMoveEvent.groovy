package com.kennelbound.mc.mouse.events

import com.kennelbound.eyelib.events.AbstractEvent

/**
 * Created by samalsto on 4/1/15.
 */
class MouseMoveEvent extends AbstractEvent {
    final int moveToX, moveToY

    MouseMoveEvent(double moveToX, double moveToY) {
        this.moveToX = moveToX
        this.moveToY = moveToY
    }
}
