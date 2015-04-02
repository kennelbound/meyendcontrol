package com.kennelbound.mc.behaviors

import com.kennelbound.eyelib.events.EyeSelection

import java.awt.event.InputEvent

/*

when I wink my left eye 500 ms click left button
when I wink my left eye 2000 ms doubleclick left button
when I wink my right eye 500 ms click right button
when I look at the screen move the mouse there with 10 antijitter levels

when I say "stop watching" stop watching
when I say "start watching" start watching
when I say "start skype" run skype

*/

class BehaviorDefinition {
    EyeSelection eyeSelection
    long duration
    int ajl

    int btn = -1
    int clickCount = 1

    String sentence
    String appPath

    String action

    def register() {
        // TODO: Save the behavior into the configuration
    }

    def when() {
        [I: {
            [look: {
                this.action = "look"
                [at: {
                    [the: {
                        [screen: {
                            [move: {
                                [mouse: {
                                    [with: { levels ->
                                        this.ajl = levels
                                        [antijitter: {
                                            [levels: {
                                                register()
                                            }]
                                        }]
                                    }]
                                }]
                            }]
                        }]
                    }]
                }]
            },
             wink: {
                 this.action = "wink"
                 [my: { eyeChoice ->
                     def eyeC = [eye { duration ->
                         this.duration = duration
                         [ms: {
                             [click        : {
                                 [left   : {
                                     this.btn = InputEvent.BUTTON1_DOWN_MASK
                                     [button: {
                                         register()
                                     }]
                                 }, right: {
                                     this.btn = InputEvent.BUTTON2_DOWN_MASK
                                     [button: {
                                         register()
                                     }]
                                 }]
                             }, doubleclick: {
                                 this.clickCount = 2
                                 [left   : {
                                     this.btn = InputEvent.BUTTON1_DOWN_MASK
                                     [button: {
                                         register()
                                     }]
                                 }, right: {
                                     this.btn = InputEvent.BUTTON2_DOWN_MASK
                                     [button: {
                                         register()
                                     }]
                                 }]
                             }]
                         }]
                     }]

                     [left : {

                         return eyeC
                     },
                      right: {
                          return eyeC
                      }]
                 }]
             },
             say : {
                 this.action = "say"
             }]
        }]
    }


    @Override
    public String toString() {
        return "BehaviorDefinition{" +
                "eyeSelection=" + eyeSelection +
                ", duration=" + duration +
                ", ajl=" + ajl +
                ", btn=" + btn +
                ", clickCount=" + clickCount +
                ", sentence='" + sentence + '\'' +
                ", appPath='" + appPath + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
