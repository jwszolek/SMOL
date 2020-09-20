package main.groovy.dsl.engine


class SmolConfiguration extends ArrayList<String> {

}


class SmolConfigurationDsl {
    private final SmolConfiguration configItem

    SmolConfigurationDsl(SmolConfiguration item) {
        this.configItem = item
        configItem << "'type':'adapter'"
    }

    def ip(String ip) {
        configItem << "'ip':'${ip}'"
    }

    def generator_connected(String generator) {
        configItem << "'generator':'${generator}'"
    }

    def dst(String dst) {
        configItem << "'dst':'${dst}'"
    }
}


class TransferringConfigurationDsl {
    private final SmolConfiguration configItem

    TransferringConfigurationDsl(SmolConfiguration item) {
        this.configItem = item
        configItem << "'type':'adapter'"
    }

    def ip(String ip) {
        configItem << "'ip':'${ip}'"
    }

    def generator_connected(String generator) {
        configItem << "'generator':'${generator}'"
    }

    def dst(String dst) {
        configItem << "'dst':'${dst}'"
    }
}


class ConverterConfigurationDsl {
    private final SmolConfiguration configItem

    ConverterConfigurationDsl(SmolConfiguration item) {
        this.configItem = item
        configItem << "'type':'converter'"
    }

    def connect(String converter) {
        configItem << "'connect':'${converter}'"
    }

    def model(String model) {
        configItem << "'model':'${model}'"
    }

    def bridge(String bridge) {
        configItem << "'bridge':'${bridge}'"
    }
}

class SensorConfigurationDsl {
    private final SmolConfiguration configItem

    SensorConfigurationDsl(SmolConfiguration item) {
        this.configItem = item
        configItem << "'type':'sensor'"
    }

    def connect(String connect) {
        configItem << "'connect':'${connect}'"
    }

    def destAddress(String destAddress) {
        configItem << "'destAddress':'${destAddress}'"
    }

    def freq(String freq) {
        configItem << "'freq':'${freq}'"
    }

    def pubTopics(String pubTopics) {
        configItem << "'pubTopics':'${pubTopics}'"
    }

    def subTopics(String subTopics) {
        configItem << "'subTopics':'${subTopics}'"
    }
}

class MapConfigurationDsl {
    private final SmolConfiguration configItem

    MapConfigurationDsl(SmolConfiguration item) {
        this.configItem = item
        configItem << "map"
    }

    def fullmap(String dev) {
        configItem << dev
    }
}

class ActionConfigurationDsl {
    private final SmolConfiguration configItem

    ActionConfigurationDsl(SmolConfiguration item) {
        this.configItem = item
        configItem << "'type':'action'"
    }

    def fullmap(String dev) {
        configItem << "'fullmap':'${dev}'"
    }

    def stop(String dev) {
        configItem << "'stop':'${dev}'"
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

    RootConfigurationDsl(RootConfiguration root) {
        this.rootConfig = root
    }

    def getSim() {

    }

    //BEGIN of new naming convention

    SmolConfiguration tn(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = TransferringConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new TransferringConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration expander(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ConverterConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new ConverterConfigurationDsl(smol)
        script()
        return smol
    }


    SmolConfiguration san(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SensorConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new SensorConfigurationDsl(smol)
        script()
        return smol
    }


    //END of  new naming convention


    SmolConfiguration adapter(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SmolConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new SmolConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration converter(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ConverterConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new ConverterConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration sensor(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = SensorConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new SensorConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration map(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = MapConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new MapConfigurationDsl(smol)
        script()
        return smol
    }

    SmolConfiguration action(String name, @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = ActionConfigurationDsl) Closure script) {
        def smol = rootConfig.catalog[name]

        if (smol == null) {
            smol = rootConfig.catalog[name] = new SmolConfiguration()
        }

        script.resolveStrategy = Closure.DELEGATE_FIRST
        script.delegate = new ActionConfigurationDsl(smol)
        script()
        return smol
    }


}

class Root {
    static RootConfiguration create(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = RootConfigurationDsl) Closure script) {
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
        connect "eth4"
    }

    sensor "temperature1", {
        connect "eth3"
    }

    sensor "temperature2", {
        connect "rs485"
    }

    map "network-map", {
        fullmap "true"
    }

}


def testNetwork = Root.create {

    /*
 * Define a network adapter
 * Assign IP address and name
 * Set destination for TCP packages
 * Attach TCP packages generator
 */
    adapter "eth1", {
        ip "1"
        generator_connected "true"
        dst "Server"
    }

/*
 * Define a network adapter
 * Assign IP address and name
 */
    adapter "Server", {
        ip "2"
    }

/*
 * Define rs485 converter
 * Set destination IP or name
 */
    converter "rs485", {
        connect "Server"
    }

/*
 * Temp sensor definition
 * Connect sensor to rs485 bus
 */
    sensor "temperature", {
        connect "rs485"
        destAddress "Server"
    }

    action "draw", {
        fullmap "true"
    }

    action "sim", {
        stop "1s"
    }

}


//new GraphProducer().build(testNetwork)



