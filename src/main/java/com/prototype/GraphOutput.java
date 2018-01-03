package main.java.com.prototype;

import main.java.com.utils.CustomEdge;
import main.java.com.utils.NodeBase;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class GraphOutput {
	private Graph<NodeBase, CustomEdge> gx = new SparseMultigraph<NodeBase, CustomEdge>();
	
	public Graph<NodeBase, CustomEdge> getGx() {
		return gx;
	}
	public void setGx(Graph<NodeBase, CustomEdge> gx) {
		this.gx = gx;
	}
	
	public Layout<NodeBase, CustomEdge> getLayout_loc() {
		return layout_loc;
	}
	public void setLayout_loc(Layout<NodeBase, CustomEdge> layout_loc) {
		this.layout_loc = layout_loc;
	}
	
	public VisualizationViewer<NodeBase, CustomEdge> getVv() {
		return vv;
	}
	public void setVv(VisualizationViewer<NodeBase, CustomEdge> vv) {
		this.vv = vv;
	}

	private Layout<NodeBase, CustomEdge> layout_loc;
	
	private VisualizationViewer<NodeBase, CustomEdge> vv;
	
	
}
