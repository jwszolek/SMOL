package test.groovy

import main.java.com.sample.DrawFirst
import main.java.com.utils.CustomEdge
import main.java.com.utils.NodeBase
import main.groovy.smoh.HBase
import main.groovy.smoh.MainNode
import main.groovy.smoh.PbNode
import main.groovy.smoh.SpeedUnit
import main.groovy.smoh.WiFi
import main.groovy.utils.Helper


DrawFirst df = new DrawFirst()
HashMap<HBase> varList = new HashMap();

//define central node  
def(root) = [new MainNode("ROOT")]
varList.put("root",root);

def sensorNames=["wifi10","wifi11","wifi12","wifi13", "wifi14"]
def sensorNames2=["wifi15","wifi16","wifi17","wifi19"]


def (wifi) = [new WiFi("Wifi1")]
wifi.connect(root,10,SpeedUnit.Mb)
varList.put("wifi",wifi);

def (wifi2) = [new WiFi("Wifi2")]
wifi2.connect(root,100,SpeedUnit.Mb)
varList.put("wifi2",wifi2)

def (pb1) = [new PbNode("Pb1")]
pb1.connect(root,10,SpeedUnit.Kb)
varList.put("Pb1",pb1)


//
//def (wifi3) = [new WiFi("Wifi3")]
//wifi3.name = 'Wifi access point'
//wifi3.connect(root)
//varList.put("wifi3",wifi3)
//
//def (wifi4) = [new WiFi("Wifi4")]
//wifi4.name = 'Wifi access point'
//wifi4.connect(root)
//varList.put("wifi4",wifi4)
//
//def (wifi5) = [new WiFi("Wifi5")]
//wifi5.name = 'Wifi access point'
//wifi5.connect(root)
//varList.put("wifi5",wifi5)
//
//def (wifi6) = [new WiFi("Wifi6")]
//wifi6.name = 'Wifi access point'
//wifi6.connect(root)
//varList.put("wifi6",wifi6)
//
//def (wifi7) = [new WiFi("Wifi7")]
//wifi7.name = 'Wifi access point'
//wifi7.connect(wifi6)
//varList.put("wifi7",wifi7)
//
//def (wifi8) = [new WiFi("Wifi8")]
//wifi8.name = 'Wifi access point'
//wifi8.connect(wifi6)
//varList.put("wifi8",wifi8)
//
//def (wifi9) = [new WiFi("Wifi9")]
//wifi9.name = 'Wifi access point'
//wifi9.connect(wifi6)
//varList.put("wifi9",wifi9)
//
//
//sensorNames.each {
//	String it ->
//	this[it] = new WiFi(it)
//	this[it].name = "Wifi"
//	this[it].connect(wifi9)
//	varList.put(it, this[it])
//}
//
//sensorNames2.each {
//	String it ->
//	this[it] = new WiFi(it)
//	this[it].name = "Wifi"
//	this[it].connect(wifi11)
//	varList.put(it, this[it])
//}


println "wielkosc listy=" + varList.size()


List<NodeBase> lstBase = Helper.GenerateVertexList(varList)
List<CustomEdge> lstLink = Helper.GenerateEdgeList(lstBase, varList)

//df.DrawDiagram(Helper.Parser(varList));

df.DrawDiagram(lstBase, lstLink);

//df.DrawDiagram(lstBase)

//varList.each { it.value.getProperties().each {println it.value}}

//run.drawSchema()
//run.exportXML()


//formal definition of central node
//def(centralNode) = [new CentralNode("CN")]
//elementsList.put(centralNode);
//
////formal definition of ProfiBus expander
//def (exp_can_1) = [new Expander("CAN")]
//exp_can_1.name = 'CAN Expander'
//exp_can_1.connect(centralNode.class, centralNode)
//elementsList.put(exp_can_1);
//
////formal definition of SAN node
//def (temp_san_1) = [new Expander("SAN")]
//temp_san_1.name = 'SAN  - temp sensor'
//temp_san_1.connect(exp_can_1.class, exp_can_1)
//elementsList.put(temp_san_1);
//
////formal definition of ProfiBus expander
//def (exp_pb_1) = [new Expander("PB")]
//exp_pb_1 = 'ProfiBus Expander'
//exp_pb_1.connect(centralNode.class, centralNode)
//elementsList.put(exp_pb_1);
//
////generate grafical representation of graph
//Utils.GenerateGraph("i_build.jpg", elementsList)
////export gml extract
//Utils.ExportGraphML("i_build.gml", elementsList)