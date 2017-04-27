package UTest

import SMOH.*
import Utils.*

import com.SMOF.*
import com.UI.DrawSmolEngine;
import com.prototype.DrawSecond;
import com.sample.DrawFirst
import com.utils.CustomEdge
import com.utils.INode
import com.utils.NodeBase


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


