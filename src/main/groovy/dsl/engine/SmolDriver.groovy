package main.groovy.dsl.engine

class SmolDriver {

    static void main(String[] args) {
        def dslDef = new File('./src/main/groovy/dsl/engine/SmolEngine.groovy').text
        def smolFile = new File('./src/main/resources/network.smol').text

        def script = """
            ${dslDef}
            def out = Root.create {
                ${smolFile}
            }
            print out
        """
        new GroovyShell().evaluate(script)
    }

}
