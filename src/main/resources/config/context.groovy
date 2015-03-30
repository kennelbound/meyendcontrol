import com.kennelbound.eyelib.eyetribe.EyeTribeEyesManager
import com.kennelbound.eyelib.logging.EyeLibEventLogger

beans {
    eventLogger(EyeLibEventLogger)

    eyesManager(EyeTribeEyesManager) {
        viewLocationListeners = [eventLogger]
        calibrationCompleteListeners = [eventLogger]
        eyesLocationListeners = [eventLogger]
        hardwareConnectionStateListeners = [eventLogger]
    }
}