package com.kennelbound.mc.behaviors.triggers

import com.kennelbound.eyelib.events.HeadLocationEvent
import org.junit.Before
import org.junit.Test

import java.awt.*

import static junit.framework.Assert.assertEquals
import static junit.framework.Assert.assertNull

/**
 * Created by samalsto on 4/2/15.
 */
class LookTest {
    Look look;

    @Before
    void init() {
        look = new Look();
    }

    HeadLocationEvent getHeadLocationEvent(smoothedX, smoothedY) {
        HeadLocationEvent headLocationEvent = new HeadLocationEvent()
        headLocationEvent.smoothedViewX = smoothedX
        headLocationEvent.smoothedViewY = smoothedY
        return headLocationEvent
    }

    @Test
    void no_jitters() {
        look.antiJitterLevels = 0

        Point actual = look.check(getHeadLocationEvent(10, 10));
        assertEquals(10d, actual.x)
        assertEquals(10d, actual.y)
    }

    @Test
    void jitters_insufficient_events() {
        look.antiJitterLevels = 2
        Point actual = look.check(getHeadLocationEvent(10, 10))
        assertNull(actual)
    }

    @Test
    void jitters_sufficient_events() {
        look.antiJitterLevels = 2
        look.check(getHeadLocationEvent(10, 10))
        Point actual = look.check(getHeadLocationEvent(20, 20))

        assertEquals(15d, actual.x)
        assertEquals(15d, actual.y)

        actual = look.check(getHeadLocationEvent(20, 20))
        assertEquals(20d, actual.x)
        assertEquals(20d, actual.y)
    }

}
