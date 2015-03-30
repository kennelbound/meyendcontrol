package com.kennelbound.mc

import com.kennelbound.eyelib.AbstractEyesManager
import com.kennelbound.eyelib.events.ViewLocationEvent
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericGroovyApplicationContext

/**
 * Created by samalsto on 3/27/15.
 */
@Configuration
@ComponentScan
class Application {
    public static void main(String... args) {
        def context = new GenericGroovyApplicationContext("file:config/context.groovy");

        AbstractEyesManager em = context.getBean('eyesManager');
        em.fireViewEvent(new ViewLocationEvent(0, 0))
        em.start();
    }
}
