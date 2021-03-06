package main.groovy.dsl.engine

import main.groovy.smoh.CanNode
import main.groovy.smoh.ExpNode
import main.groovy.smoh.HBase
import main.groovy.smoh.MainNode
import main.groovy.smoh.PbNode
import main.groovy.smoh.SANode
import main.groovy.smoh.SpeedUnit
import main.groovy.smoh.TNode
import main.groovy.utils.Helper
import main.java.com.UI.DrawSmolEngine
import main.java.com.prototype.DrawSecond
import main.java.com.sample.DrawFirst
import groovy.json.JsonSlurper
import main.java.com.utils.CustomEdge
import main.java.com.utils.NodeBase

class Adapter {
    String name
    String ip
    String generator
    String dst
    String bridge
}


class Sensor {
    String name
    String connect
    String destAddress
    String pubTopics
    String subTopics
    double freq
}

class Converter {
    String name
    String connect
    String model
    String bridge
}

class GraphProducer {

    private DrawFirst df
    private HashMap<String, HBase> varList

    private def components = [:]

    def GraphProducer() {
        df = new DrawFirst()
        varList = new HashMap()
    }


    def parseSimObject(def input) {

        def simItems = [:]

        input.catalog.each { k, v ->
            if (v.toString().contains("'type':'action'")) {

                def rawAdapter = v.toString().replace("[", "{").replace("]", "}").replace("\'", "\"")
                def valueMap = new JsonSlurper().parseText(rawAdapter)

                def simTime = ""
                valueMap.each { ak, av ->
                    if (ak.contains("stop")) simTime = av
                }

                if (!simTime.isEmpty()) {
                    simItems.put("simTime", simTime)
                }
            }
        }
        return simItems
    }


    def formatInput(def input) {

        //define central node
        def root = new MainNode("ROOT")
        varList.put("root", root)

        //scan collection for adapters
        input.catalog.each { k, v ->
            if (v.toString().contains("'type':'adapter'")) {

                def rawAdapter = v.toString().replace("[", "{").replace("]", "}").replace("\'", "\"")
                def valueMap = new JsonSlurper().parseText(rawAdapter)

                def adapter = new Adapter()
                adapter.name = k

                valueMap.each { ak, av ->
                    if (ak.contains("ip")) adapter.ip = av
                    if (ak.contains("generator")) adapter.generator = av
                    if (ak.contains("dst")) adapter.dst = av
                    if (ak.contains("bridge")) adapter.bridge = av
                }

                def newAdapter = new TNode(adapter.name, adapter.ip)
                newAdapter.connect(root, 1, SpeedUnit.Mb, 30)
                varList.put(adapter.name, newAdapter)

                components.put(adapter.name, newAdapter)
            }
        }

        if (components.size() > 0) {

            //scan collection for converters
            input.catalog.each { k, v ->
                if (v.toString().contains("'type':'converter'")) {
                    def rawConverter = v.toString().replace("[", "{").replace("]", "}").replace("\'", "\"")
                    def valueMap = new JsonSlurper().parseText(rawConverter)

                    def converter = new Converter()
                    converter.name = k

                    valueMap.each { ck, cv ->
                        if (ck.contains("connect")) converter.connect = cv
                        if (ck.contains("model")) converter.model = cv
                        if (ck.contains("bridge")) converter.bridge = cv
                    }

                    def parentName = components.find { it.key == converter.connect }?.value

                    if (parentName) {
                        def newConverter = new ExpNode(converter.name)
                        newConverter.bridge = converter.bridge
                        newConverter.model = converter.model
                        newConverter.connect(parentName, 1, SpeedUnit.Mb, 10)

                        varList.put(converter.name, newConverter)
                        components.put(converter.name, newConverter)
                    }
                }
            }

            //scan collection for sensors
            input.catalog.each { k, v ->
                if (v.toString().contains("'type':'sensor'")) {

                    def rawSensor = v.toString().replace("[", "{").replace("]", "}").replace("\'", "\"")
                    def valueMap = new JsonSlurper().parseText(rawSensor)

                    def sensor = new Sensor()
                    sensor.name = k

                    valueMap.each { sk, sv ->
                        if (sk.contains("connect")) sensor.connect = sv
                        if (sk.contains("destAddress")) sensor.destAddress = sv
                        if (sk.contains("freq")) sensor.freq = Double.parseDouble((String) sv)
                        if (sk.contains("pubTopics")) sensor.pubTopics = sv
                        if (sk.contains("subTopics")) sensor.subTopics = sv
                    }

                    def parentName = components.find { it.key == sensor.connect }?.value

                    if (parentName) {
                        def newSensor = new SANode(sensor.name)
                        newSensor.connect(parentName, 1, SpeedUnit.Mb, 10)
                        newSensor.destAddress = sensor.destAddress
                        newSensor.freq = sensor.freq
                        newSensor.pubTopics = sensor.pubTopics
                        newSensor.subTopics = sensor.subTopics

                        varList.put(sensor.name, newSensor)
                    }
                }
            }
        }

        varList
    }


    def build(def input) {
        def varList = formatInput(input)

        List<NodeBase> lstBase = Helper.GenerateVertexList(varList)
        List<CustomEdge> lstLink = Helper.GenerateEdgeList(lstBase, varList)

        DrawSmolEngine dse = new DrawSmolEngine(DrawSecond.DrawDiagram(lstBase, lstLink))
        dse.DrawUIGraph()
    }

}
