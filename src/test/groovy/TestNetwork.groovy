package test.groovy


import main.java.com.UI.DrawSmolEngine;
import main.java.com.prototype.DrawSecond;
import main.java.com.sample.DrawFirst
import main.java.com.utils.CustomEdge
import main.java.com.utils.NodeBase
import main.groovy.smoh.CanNode
import main.groovy.smoh.HBase
import main.groovy.smoh.MainNode
import main.groovy.smoh.PbNode
import main.groovy.smoh.SANode
import main.groovy.smoh.SpeedUnit
import main.groovy.utils.Helper


// wykorzystanie przyciskow na schemacie
// przycisk P - lapanie wezla z rysuku
// przycis K - poruszanie sie po schemacie


DrawFirst df = new DrawFirst()
HashMap<HBase> varList = new HashMap();

//define central node
def(root) = [new MainNode("ROOT")]
varList.put("root",root);

//define profibus node
def (pb1) = [new PbNode("PB1")]
pb1.connect(root,10,SpeedUnit.Mb,100)
varList.put("pb1",pb1);

//define SA node
def (sa1) = [new SANode("sa1")]
sa1.connect(pb1,10,SpeedUnit.Mb,200)
varList.put("sa1",sa1);

//define CAN node
def (can1) = [new CanNode("CAN1")]
can1.connect(root,11,SpeedUnit.Mb,300)
varList.put("can1",can1);

//define SA node
def (sa2) = [new SANode("sa2")]
sa2.connect(can1,5,SpeedUnit.Mb,400)
varList.put("sa2",sa2);

//list of SA sensors

def canSensors=["sa3","SA4","SA5","SA6","SA7","SA8","SA9","SA10"]

//application loop 
canSensors.each {
	String it ->
	this[it] = new SANode(it)
	this[it].connect(can1,4,SpeedUnit.Gb,200)
	varList.put(it, this[it])
}



//define CAN2 node
def (can2) = [new CanNode("CAN2")]
can2.connect(root,12,SpeedUnit.Mb,200)
varList.put("can2",can2);

//list of SA sensors
def canSensors2=["SA11","SA12","SA13","SA14","SA15"]

//aplication loop
canSensors2.each {
	String it ->
	this[it] = new SANode(it)
	this[it].connect(can2,4,SpeedUnit.Gb,350)
	varList.put(it, this[it])
}

//define CAN3 node
def (can3) = [new CanNode("CAN3")]
can3.connect(root,12,SpeedUnit.Mb,400)
varList.put("can3",can3);

//list of SA sensors
def canSensors3=["SA16","SA17","SA18","SA19","SA20"]

//aplication loop
canSensors3.each {
	String it ->
	this[it] = new SANode(it)
	this[it].connect(can3,4,SpeedUnit.Gb,250)
	varList.put(it, this[it])
}

//define CAN4 node
def (can4) = [new CanNode("CAN4")]
can4.connect(root,12,SpeedUnit.Mb,300)
varList.put("can4",can4); 

//list of SA sensors
def canSensors4=["SA21","SA22","SA23","SA24","SA25"]

//aplication loop
canSensors4.each {
	String it ->
	this[it] = new SANode(it)
	this[it].connect(can4,1,SpeedUnit.Gb,400)
	varList.put(it, this[it])
}


println "wielkosc listy=" + varList.size()

List<NodeBase> lstBase = Helper.GenerateVertexList(varList)
List<CustomEdge> lstLink = Helper.GenerateEdgeList(lstBase, varList)


DrawSmolEngine dse = new DrawSmolEngine(DrawSecond.DrawDiagram(lstBase, lstLink));
dse.DrawUIGraph();
