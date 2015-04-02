package com.kennelbound.mc

import net.engio.mbassy.bus.MBassador
import org.springframework.context.support.GenericGroovyApplicationContext

/**
 * Created by samalsto on 3/27/15.
 */
class Application {
    public static void main(String... args) {
        def context = new GenericGroovyApplicationContext("classpath:config/context.groovy");
        def eventBus = context.getBean(MBassador)
    }
}
