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

//define SA node
def (sa1) = [new SANode("CD_L1")]
sa1.connect(can1,5,SpeedUnit.Mb,1)
varList.put("sa1",sa1);

//define SA node
def (sa2) = [new SANode("SW_L1")]
sa2.connect(can1,5,SpeedUnit.Mb,1)
varList.put("sa2",sa2);
// End of LAMP 1

// ===
// LAMP 2
// ===
//define profibus node
def (can2) = [new CanNode("LAMP_2")]
can2.connect(root,11,SpeedUnit.Mb,400)
varList.put("can2",can2);

//define SA node
def (sa3) = [new SANode("CD_L2")]
sa3.connect(can2,5,SpeedUnit.Mb,1)
varList.put("sa3",sa3);

//define SA node
def (sa4) = [new SANode("SW_L2")]
sa4.connect(can2,5,SpeedUnit.Mb,1)
varList.put("sa4",sa4);
// End of LAMP 2


// ===
// LAMP 3
// ===
//define profibus node
def (can3) = [new CanNode("LAMP_3")]
can3.connect(root,11,SpeedUnit.Mb,500)
varList.put("can3",can3);

//define SA node
def (sa5) = [new SANode("CD_L3")]
sa5.connect(can3,5,SpeedUnit.Mb,1)
varList.put("sa5",sa5);

//define SA node
def (sa6) = [new SANode("SW_L3")]
sa6.connect(can3,5,SpeedUnit.Mb,1)
varList.put("sa6",sa6);
// End of LAMP 3

// ===
// LAMP 4
// ===
//define profibus node
def (can4) = [new CanNode("LAMP_4")]
can4.connect(root,11,SpeedUnit.Mb,600)
varList.put("can4",can4);

//define SA node
def (sa7) = [new SANode("CD_L4")]
sa7.connect(can4,5,SpeedUnit.Mb,1)
varList.put("sa7",sa7);

//define SA node
def (sa8) = [new SANode("SW_L4")]
sa8.connect(can4,5,SpeedUnit.Mb,1)
varList.put("sa8",sa8);
// End of LAMP 4

// ===
// LAMP 5
// ===
//define profibus node
def (can5) = [new CanNode("LAMP_5")]
can5.connect(root,11,SpeedUnit.Mb,500)
varList.put("can5",can5);

//define SA node
def (sa9) = [new SANode("CD_L5")]
sa9.connect(can5,5,SpeedUnit.Mb,1)
varList.put("sa9",sa9);

//define SA node
def (sa10) = [new SANode("SW_L5")]
sa10.connect(can5,5,SpeedUnit.Mb,1)
varList.put("sa10",sa10);
// End of LAMP 5

// ===
// LAMP 6
// ===
//define profibus node
def (can6) = [new CanNode("LAMP_6")]
can6.connect(root,11,SpeedUnit.Mb,400)
varList.put("can6",can6);

//define SA node
def (sa11) = [new SANode("CD_L6")]
sa11.connect(can6,5,SpeedUnit.Mb,1)
varList.put("sa11",sa11);

//define SA node
def (sa12) = [new SANode("SW_L6")]
sa12.connect(can6,5,SpeedUnit.Mb,1)
varList.put("sa12",sa12);
// End of LAMP 6


// ===
// LAMP 7
// ===
//define profibus node
def (can7) = [new CanNode("LAMP_7")]
can7.connect(root,11,SpeedUnit.Mb,300)
varList.put("can7",can7);

//define SA node
def (sa13) = [new SANode("CD_L7")]
sa13.connect(can7,5,SpeedUnit.Mb,1)
varList.put("sa13",sa13);

//define SA node
def (sa14) = [new SANode("SW_L7")]
sa14.connect(can7,5,SpeedUnit.Mb,1)
varList.put("sa14",sa14);
// End of LAMP 7

// ===
// LAMP 8
// ===
//define profibus node
def (can8) = [new CanNode("LAMP_8")]
can8.connect(root,11,SpeedUnit.Mb,200)
varList.put("can8",can8);
//
//define SA node
def (sa15) = [new SANode("CD_L8")]
sa15.connect(can8,5,SpeedUnit.Mb,1)
varList.put("sa15",sa15);
//
////define SA node
def (sa16) = [new SANode("SW_L8")]
sa16.connect(can8,5,SpeedUnit.Mb,1)
varList.put("sa16",sa16);
// End of LAMP 8

// ===
// LAMP 9
// ===
//define profibus node
def (can9) = [new CanNode("LAMP_9")]
can9.connect(root,11,SpeedUnit.Mb,200)
varList.put("can9",can9);

////define SA node
def (sa17) = [new SANode("CD_L9")]
sa17.connect(can9,5,SpeedUnit.Mb,1)
varList.put("sa17",sa17);
//
////define SA node
def (sa18) = [new SANode("SW_L9")]
sa18.connect(can9,5,SpeedUnit.Mb,1)
varList.put("sa18",sa18);
// End of LAMP 9



List<NodeBase> lstBase = Helper.GenerateVertexList(varList)
List<CustomEdge> lstLink = Helper.GenerateEdgeList(lstBase, varList)


DrawSmolEngine dse = new DrawSmolEngine(DrawSecond.DrawDiagram(lstBase, lstLink));
dse.DrawUIGraph();
