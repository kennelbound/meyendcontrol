package com.kennelbound.mc.behaviors.triggers

import com.kennelbound.eyelib.events.EyeSelection
import com.kennelbound.eyelib.events.HeadLocationEvent
import groovy.util.logging.Log4j

import java.awt.*
import java.util.Queue

/**
 * Created by samalsto on 4/1/15.
 */
@Log4j
class Look implements ITrigger<Point, HeadLocationEvent> {
    EyeSelection selection = EyeSelection.BOTH

    int antiJitterLevels = 0
    def antiJitterEvents = [] as Queue

    synchronized Point check(HeadLocationEvent event) {
        antiJitterEvents << event;
        if (antiJitterEvents.size() < antiJitterLevels)
            return null

        if (antiJitterLevels && antiJitterEvents.size() > antiJitterLevels) {
            antiJitterEvents.poll()
        }

        def x = (antiJitterEvents.sum { it.getViewX(selection) }) / antiJitterEvents.size()
        def y = (antiJitterEvents.sum { it.getViewY(selection) }) / antiJitterEvents.size()

        log.info("Moving mouse to $x, $y")

        return new Point((int) x, (int) y)
    }
}
