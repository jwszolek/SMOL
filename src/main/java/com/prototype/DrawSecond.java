package main.java.com.prototype;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.commons.collections15.Transformer;

import main.java.com.UI.DrawSmolEngine;
import main.java.com.utils.*;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class DrawSecond {
	
	private static float dash[] = { 10.0f };
	private static String test;
	private static String test2;
	
	private static final HashMap<Integer, CustomEdge> edges = new HashMap<Integer, CustomEdge>();
	
	private final static Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
	
	private static Transformer<CustomEdge, Stroke> edgeStrokeTransformer = new Transformer<CustomEdge, Stroke>() {
		public Stroke transform(CustomEdge s) {
			return edgeStroke;
		}
	};

	private static Transformer<NodeBase, String> vertexLabel = new Transformer<NodeBase, String>() {
		public String transform(NodeBase node) {
			if (node.getType() == NodeType.CN)
				return "CN";
			if (node.getType() == NodeType.SENSOR)
				return node.getName();
			if (node.getType() == NodeType.PB)
				return node.getName();
			if (node.getType() == NodeType.SA)
				return node.getName();
			if (node.getType() == NodeType.CAN)
				return node.getName();
			return null;
		}
	};

	private static Transformer<NodeBase, Shape> vertexSize = new Transformer<NodeBase, Shape>() {
		public Shape transform(NodeBase node) {
			Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
			// in this case, the vertex is twice as large
			if (node.getType() == NodeType.CN)
				return AffineTransform.getScaleInstance(2, 2)
						.createTransformedShape(circle);
			else
				return circle;
		}
	};


	private static Transformer<NodeBase, Paint> vertexColor = new Transformer<NodeBase, Paint>() {
		public Paint transform(NodeBase node) {
			if (node.getType() == NodeType.CN)
				return Color.RED;
			if (node.getType() == NodeType.SENSOR)
				return Color.WHITE;
			if (node.getType() == NodeType.CAN)
				return Color.GREEN;
			if (node.getType() == NodeType.PB)
				return Color.BLUE;
			if (node.getType() == NodeType.SA)
				return Color.WHITE;

			return Color.BLACK;
		}
	};
	
	private static Transformer<CustomEdge, String> transEdge = new Transformer<CustomEdge, String>() {
		public String transform(CustomEdge myedge) {
			CustomEdge myEdge = edges.get(myedge.getId());
			return myEdge.getConnSpeed() + " / " + myEdge.getLinkLength() + "m.";
		}
	};

	
	
	public static GraphOutput DrawDiagram(List<NodeBase> lstNode,
			List<CustomEdge> lstEdge) {
		Graph<NodeBase, CustomEdge> g2 = new SparseMultigraph<NodeBase, CustomEdge>();

		for (NodeBase node : lstNode) {
			System.out.println(node.getName());
			g2.addVertex(node);
		}

		int counter = 0;
		for (CustomEdge edge : lstEdge) {
			edges.put(counter, edge);
			edge.setId(counter);
			g2.addEdge(edge, edge.getNodeStart(), edge.getNodeStop(),
					EdgeType.UNDIRECTED);
			counter++;
		}

		final Layout<NodeBase, CustomEdge> layout = new FRLLLayout<NodeBase, CustomEdge>(
				g2);
		Dimension d = new Dimension();
		d.setSize(1024, 768);
		layout.setSize(d);

		VisualizationViewer<NodeBase, CustomEdge> vv = new VisualizationViewer<NodeBase, CustomEdge>(
				layout);

		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setEdgeLabelTransformer(transEdge);
		vv.getRenderContext().setLabelOffset(20);

		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);


		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		vv.addKeyListener(gm.getModeKeyListener());
		vv.setGraphMouse(gm);
		
		GraphOutput go = new GraphOutput();
		go.setGx(g2);
		go.setLayout_loc(layout);
		go.setVv(vv);
		
		return go;
	}
	
	public static VisualizationViewer<NodeBase, CustomEdge> TranformLayout(Graph<NodeBase, CustomEdge> gx, LayoutType layoutTp){
		
		Layout<NodeBase, CustomEdge> layout = null;
		
		switch(layoutTp){
		case Circle:
			layout = new CircleLayout<NodeBase, CustomEdge>(gx);
			break;
		case KKLayout:
			layout = new KKLayout<NodeBase, CustomEdge>(gx);
			break;
		case FRLayout:
			layout = new FRLayout<NodeBase, CustomEdge>(gx);
			break;
		case ISOMLayout:
			layout = new ISOMLayout<NodeBase, CustomEdge>(gx);
			break;
		case SpringLayout:
			layout = new SpringLayout<NodeBase, CustomEdge>(gx);
			break;
		case Own:
			layout = new FRLLLayout<NodeBase, CustomEdge>(gx);
			break;
		default:
			layout = new FRLLLayout<NodeBase, CustomEdge>(gx);
			break;
		}
		
		Dimension d = new Dimension();
		d.setSize(1024, 768);
		layout.setSize(d);

		VisualizationViewer<NodeBase, CustomEdge> vv = new VisualizationViewer<NodeBase, CustomEdge>(
				layout);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderContext().setEdgeLabelTransformer(transEdge);

		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);

		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		vv.addKeyListener(gm.getModeKeyListener());
		vv.setGraphMouse(gm);

		return vv;
	}
	
}

