package test.java;

import java.util.ArrayList;
import java.util.List;

import main.java.com.UI.DrawSmolEngine;
import main.java.com.prototype.DrawSecond;
import main.java.com.utils.*;

public class SmolExecutor {
	public static void main(String[] args) {
		List<NodeBase> lstBase = new ArrayList<NodeBase>();

		NodeBase nb_cn = new NodeBase(1, "CN", NodeType.CN, "2GB");
		lstBase.add(nb_cn);
		NodeBase nb_can1 = new NodeBase(2, "CAN1", NodeType.CAN, "2");
		lstBase.add(nb_can1);
		NodeBase nb_can2 = new NodeBase(3, "CAN2", NodeType.CAN, "3");
		lstBase.add(nb_can2);
		NodeBase nb_can3 = new NodeBase(4, "CAN3", NodeType.CAN, "4");
		lstBase.add(nb_can3);
		NodeBase nb_can4 = new NodeBase(5, "CAN4", NodeType.CAN, "5");
		lstBase.add(nb_can4);
		NodeBase nb_can5 = new NodeBase(6, "SA1", NodeType.SA, "6");
		lstBase.add(nb_can5);

		List<CustomEdge> lstLink = new ArrayList<CustomEdge>();
		CustomEdge ce = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can1,
				"DIRECTED", "1GB", 100);
		lstLink.add(ce);
		CustomEdge ce2 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can2,
				"DIRECTED", "2GB", 200);
		lstLink.add(ce2);
		CustomEdge ce3 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can3,
				"DIRECTED", "2GB", 300);
		lstLink.add(ce3);
		CustomEdge ce4 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can4,
				"DIRECTED", "3GB", 400);
		lstLink.add(ce4);
		CustomEdge ce5 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can5,
				"DIRECTED", "2GB", 300);
		lstLink.add(ce5);
		
		
		DrawSmolEngine dse = new DrawSmolEngine(DrawSecond.DrawDiagram(lstBase, lstLink));
		dse.DrawUIGraph();
		
	}
}
