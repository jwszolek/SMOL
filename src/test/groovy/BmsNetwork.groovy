package test.groovy

import main.java.com.UI.DrawSmolEngine
import main.java.com.prototype.DrawSecond
import main.java.com.sample.DrawFirst
import main.java.com.utils.CustomEdge
import main.java.com.utils.NodeBase
import main.groovy.smoh.CanNode
import main.groovy.smoh.HBase
import main.groovy.smoh.MainNode
import main.groovy.smoh.SpeedUnit
import main.groovy.utils.Helper


DrawFirst df = new DrawFirst();
HashMap<HBase> varList = new HashMap();

//define central node
def(root) = [new MainNode("ROOT")]
varList.put("root",root);
	
// ===
// LAMP 1
// ===
//define profibus node
def (can1) = [new CanNode("LAMP_1")]
can1.connect(root,11,SpeedUnit.Mb,300)
varList.put("can1",can1);



List<NodeBase> lstBase = Helper.GenerateVertexList(varList)
List<CustomEdge> lstLink = Helper.GenerateEdgeList(lstBase, varList)


DrawSmolEngine dse = new DrawSmolEngine(DrawSecond.DrawDiagram(lstBase, lstLink));
dse.DrawUIGraph();


