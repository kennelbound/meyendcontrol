package com.kennelbound.mc.behaviors.triggers

import com.kennelbound.eyelib.events.AbstractEvent

/**
 * Created by samalsto on 4/2/15.
 */
interface ITrigger<T, V extends AbstractEvent> {
    T check(V event)
}