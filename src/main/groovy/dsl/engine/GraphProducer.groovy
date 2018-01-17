package main.groovy.dsl.engine

import main.groovy.smoh.CanNode
import main.groovy.smoh.HBase
import main.groovy.smoh.MainNode
import main.groovy.smoh.PbNode
import main.groovy.smoh.SANode
import main.groovy.smoh.SpeedUnit
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
}


class Sensor {
    String name
    String connect
}

class Converter {
    String name
    String connect
}

class GraphProducer {

    private DrawFirst df
    private HashMap<String,HBase> varList

    private def components = [:]

    def GraphProducer(){
        df = new DrawFirst()
        varList = new HashMap()
    }


    def build(def input) {

        //define central node
        def root = new MainNode("ROOT")
        varList.put("root",root)

        //scan collection for adapters
        input.catalog.each {k,v ->
            if(v.toString().contains("'type':'adapter'")) {

                def rawAdapter = v.toString().replace("[","{").replace("]","}").replace("\'","\"")
                def valueMap = new JsonSlurper().parseText(rawAdapter)

                def adapter = new Adapter()
                adapter.name = k

                valueMap.each { ak, av ->
                    if (ak.contains("ip")) adapter.ip = av
                    if (ak.contains("generator")) adapter.generator = av
                    if (ak.contains("dst")) adapter.dst = av
                }

                def newAdapter = new CanNode(adapter.name)
                newAdapter.connect(root,10,SpeedUnit.Mb,30)
                varList.put(adapter.name,newAdapter)

                components.put(adapter.name,newAdapter)
            }
        }

        if(components.size() > 0) {

            //scan collection for converters
            input.catalog.each { k, v ->
                if (v.toString().contains("'type':'converter'")) {
                    def rawConverter = v.toString().replace("[","{").replace("]","}").replace("\'","\"")
                    def valueMap = new JsonSlurper().parseText(rawConverter)

                    def converter = new Converter()
                    converter.name = k

                    valueMap.each { ck, cv ->
                        if(ck.contains("connect")) converter.connect = cv
                    }

                    def parentName = components.find { it.key == converter.connect}?.value

                    if(parentName) {
                        def newConverter = new PbNode(converter.name)
                        newConverter.connect(parentName, 10, SpeedUnit.Mb, 10)
                        varList.put(converter.name,newConverter)

                        components.put(converter.name,newConverter)
                    }
                }
            }

            //scan collection for sensors
            input.catalog.each { k, v ->
                if (v.toString().contains("'type':'sensor'")) {

                    def rawSensor = v.toString().replace("[","{").replace("]","}").replace("\'","\"")
                    def valueMap = new JsonSlurper().parseText(rawSensor)

                    def sensor = new Sensor()
                    sensor.name = k

                    valueMap.each {sk, sv ->
                        if(sk.contains("connect")) sensor.connect = sv
                    }

                    def parentName = components.find { it.key == sensor.connect}?.value

                    if(parentName) {
                        def newSensor = new SANode(sensor.name)
                        newSensor.connect(parentName, 10, SpeedUnit.Mb, 10)
                        varList.put(sensor.name, newSensor)
                    }
                }
            }
        }



        List<NodeBase> lstBase = Helper.GenerateVertexList(varList)
        List<CustomEdge> lstLink = Helper.GenerateEdgeList(lstBase, varList)


        DrawSmolEngine dse = new DrawSmolEngine(DrawSecond.DrawDiagram(lstBase, lstLink))
        dse.DrawUIGraph()
    }

}
