package main.groovy.dsl.engine

import groovy.json.JsonSlurper
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class DrawAction {
    String fullmap
}

class SimAction {
    String stop
}

class SmolDriver {

    static void main(String[] args) {

        def smolFile = new File(args[0]).text
        def importCustomizer = new ImportCustomizer()
        importCustomizer.addImport( 'Root', 'main.groovy.dsl.engine.Root')
        importCustomizer.addImport( 'SimDriver', 'main.groovy.dsl.engine.SimDriver')

        def configuration = new CompilerConfiguration()
        configuration.addCompilationCustomizers(importCustomizer)


        def script = """Root.create{
            ${smolFile}
        }
        """
        def scriptOutput = new GroovyShell(configuration).evaluate(script)
        println scriptOutput

        Map actions = [:]

        scriptOutput.catalog.each { k, v ->
            if (v.toString().contains("'type':'action'")) {

                def rawAction = v.toString().replace("[", "{").replace("]", "}").replace("\'", "\"")
                def valueMap = new JsonSlurper().parseText(rawAction)

                if(k == "sim"){
                    def sim = new SimAction()
                    valueMap.each { ak, av ->
                        if (ak.contains("stop")) sim.stop = av
                    }
                    actions.put("sim", sim)
                }

                if(k == "draw"){
                    def draw = new DrawAction()
                    valueMap.each { ak, av ->
                        if (ak.contains("fullmap")) draw.fullmap = av
                    }
                    actions.put("draw", draw)
                }
            }
        }


        if(actions.getOrDefault("draw",null) != null){
            new GraphProducer().build(scriptOutput)
        }

        if(actions.getOrDefault("sim",null) != null){
            def simScript = """
            def out = Root.create {
                ${smolFile}
            }

            SimDriver.run(out.toString())
            """
            new GroovyShell(configuration).evaluate(simScript)
        }



                //println out




//            def script = """
//            def out = Root.create {
//                ${smolFile}
//            }
//
//            SimDriver.run(out.toString())
//            """
//            new GroovyShell(configuration).evaluate(script)

    }

}
