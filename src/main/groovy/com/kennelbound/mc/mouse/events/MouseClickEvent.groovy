package com.kennelbound.mc.mouse.events

import com.kennelbound.eyelib.events.AbstractEvent

/**
 * Created by samalsto on 4/1/15.
 */
class MouseClickEvent extends AbstractEvent {
    final int button
    final int duration
    final int count

    MouseClickEvent(int button, int duration, int count) {
        this.button = button
        this.duration = duration
        this.count = count
    }
}
