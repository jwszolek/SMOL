package main.groovy.dsl.engine


class SmolConfiguration extends ArrayList<String> {

}


class SmolConfigurationDsl {
    private final SmolConfiguration configItem

    SmolConfigurationDsl(SmolConfiguration item){
        this.configItem = item
        configItem << "'type':'adapter'"
    }

    def ip(String ip){
        configItem << "'ip':'${ip}'"
    }

    def generator_connected(String generator){
        configItem << "'generator':'${generator}'"
    }

    def dst(String dst){
        configItem << "'dst':'${dst}'"
    }
}


class ConverterConfigurationDsl {
    private final SmolConfiguration configItem

    ConverterConfigurationDsl(SmolConfiguration item){
        this.configItem = item
        configItem << "converter"
    }

    def dst(String dst){
        configItem << dst
    }
}

class SensorConfigurationDsl {
    private final SmolConfiguration configItem

    SensorConfigurationDsl(SmolConfiguration item){
        this.configItem = item
        configItem << "sensor"
    }

    def connect(String dev){
        configItem << dev
    }
}

class MapConfigurationDsl {
    private final SmolConfiguration configItem

    MapConfigurationDsl(SmolConfiguration item){
        this.configItem = item
        configItem << "map"
    }

    def fullmap(String dev){
        configItem << dev
    }
}


class RootConfiguration {
    final Map<String, SmolConfiguration> catalog = [:]

    String toString() {
        return "Adapters: " + catalog.toString()
    }
}

class RootConfigurationDsl {
    private final RootConfiguration rootConfig

    RootConfigurationDsl(RootConfiguration root){
        this.rootConfig = root
    }

    def getSim(){

    }

    SmolConfiguration adapter(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SmolConfigurationDsl) Closure script){
        def smol = rootConfig.catalog[name]

        if(smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new SmolConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration converter(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ConverterConfigurationDsl) Closure script){
        def smol = rootConfig.catalog[name]

        if(smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new ConverterConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration sensor(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SensorConfigurationDsl) Closure script){
        def smol = rootConfig.catalog[name]

        if(smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new SensorConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration map(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MapConfigurationDsl) Closure script){
        def smol = rootConfig.catalog[name]

        if(smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new MapConfigurationDsl(smol)
        script()
        return smol
    }


}

class Root {
    static RootConfiguration create(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RootConfigurationDsl) Closure script){
        def root = new RootConfiguration()

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new RootConfigurationDsl(root)
        script()

        return root
    }
}


def network = Root.create {
        adapter "eth1", {
            ip "1"
            generator_connected "true"
            dst "Server"
        }

        //comment test
        adapter "Server", {
            ip "2"
        }

        adapter "eth3", {
            ip "3"
        }

        adapter "eth4", {
            ip "4"
        }

        converter "rs485", {
            dst "Server"
        }

        sensor "temperature", {
            connect "rs485"
        }

        map "network-map", {
            fullmap "true"
        }

}


new GraphProducer().build(network)



