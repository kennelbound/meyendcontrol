package com.kennelbound.util

/**
 * Created by samalsto on 2/28/15.
 */
class GroovyTimerTask extends TimerTask {
    Closure closure;

    void run() {
        closure();
    }
}
