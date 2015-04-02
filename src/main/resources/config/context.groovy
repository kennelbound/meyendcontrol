import com.kennelbound.eyelib.events.EyeSelection
import com.kennelbound.eyelib.events.HeadLocationEvent
import com.kennelbound.eyelib.eyetribe.EyeTribeEventDispatcher
import com.kennelbound.eyelib.logging.EyeLibEventLogger
import com.kennelbound.mc.behaviors.Behavior
import com.kennelbound.mc.behaviors.actions.MouseClickAction
import com.kennelbound.mc.behaviors.actions.MouseMoveAction
import com.kennelbound.mc.behaviors.triggers.Look
import com.kennelbound.mc.behaviors.triggers.Wink
import com.kennelbound.mc.mouse.MouseEventHandler
import net.engio.mbassy.bus.MBassador

import java.awt.event.InputEvent

beans {
    xmlns([ctx: 'http://www.springframework.org/schema/context'])
    ctx.'component-scan'('base-package': 'com.kennelbound')

    eventLogger(EyeLibEventLogger)

    // Event Handlers
    mbassador(MBassador)

    // Behaviors
    bh1(Behavior) { bean ->
        supportedEventType = HeadLocationEvent
        trigger = { Wink w ->
            eyeSelection = EyeSelection.RIGHT
            requiredDuration = 250
        }
        action = { MouseClickAction a ->
            count = 1
            button = InputEvent.BUTTON1_DOWN_MASK
            duration = 200
        }
    }
    bh2(Behavior) { bean ->
        supportedEventType = HeadLocationEvent
        trigger = { Look l ->
            antiJitterLevels = 15
        }
        action = { MouseMoveAction a ->
        }
    }

    mouseEventHandler(MouseEventHandler)

    // EyeTribe specific configuration
    eyesManager(EyeTribeEventDispatcher)
}