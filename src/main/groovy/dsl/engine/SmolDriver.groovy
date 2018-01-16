package main.groovy.dsl.engine

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class SmolDriver {

    static void main(String[] args) {

        def smolFile = new File(args[0]).text
        def importCustomizer = new ImportCustomizer()
        importCustomizer.addImport( 'Root', 'main.groovy.dsl.engine.Root')
        importCustomizer.addImport( 'SimDriver', 'main.groovy.dsl.engine.SimDriver')

        def configuration = new CompilerConfiguration()
        configuration.addCompilationCustomizers(importCustomizer)

            def script = """
            def out = Root.create {
                ${smolFile}
            }
            
            SimDriver.run(out.toString())
            """
            new GroovyShell(configuration).evaluate(script)

    }

}
