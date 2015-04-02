package com.kennelbound.mc.behaviors.triggers

import com.kennelbound.eyelib.events.EyeSelection
import com.kennelbound.eyelib.events.HeadLocationEvent

/**
 * Created by samalsto on 4/1/15.
 */
class Wink implements ITrigger<Boolean, HeadLocationEvent> {
    EyeSelection eyeSelection
    long requiredDuration = 500

    HeadLocationEvent last

    Boolean check(HeadLocationEvent event) {
        if (!last) {
            last = event
        }

        if (event.isOpen(eyeSelection)) { // Eye is open
            if (!last.isOpen(eyeSelection)) {    // Eye was closed
                last = event;
                if (last.timestamp.time + requiredDuration <= event.timestamp.time) {
                    // Eye was closed longer than the duration
                    return true;
                }
                return false
            }
        } else { // Eye is closed
            if (last.isOpen(eyeSelection)) { // Eye was open
                last = event;
            }
        }

        return false
    }
}
