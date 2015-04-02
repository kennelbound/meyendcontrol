import com.kennelbound.mc.behaviors.BehaviorDefinition

//import org.codehaus.groovy.control.CompilerConfiguration
//
//def config = new CompilerConfiguration()
//config.scriptBaseClass = 'com.kennelbound.mc.behaviors.BehaviorDefinition'
//def shell = new GroovyShell(this.class.classLoader, config)
//shell.evaluate """
//    when I wink my right eye 500 ms click left button
//"""

def binding = new Binding()
def shell = new GroovyShell(binding)

BehaviorDefinition bd = new BehaviorDefinition()
binding.setVariable("when", bd.when())
shell.evaluate """
    when.I.wink.my.right eye 500 ms click left button
"""