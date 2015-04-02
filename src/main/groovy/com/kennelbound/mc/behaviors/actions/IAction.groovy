package com.kennelbound.mc.behaviors.actions

import com.kennelbound.eyelib.events.AbstractEvent

/**
 * Created by samalsto on 4/2/15.
 */
interface IAction<T, V extends AbstractEvent> {
    void execute(T response, V event);
}