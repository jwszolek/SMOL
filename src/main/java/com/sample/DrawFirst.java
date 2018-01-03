package main.java.com.sample;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.collections15.Transformer;

import main.java.com.utils.*;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;



public class DrawFirst {

	private static void DrawDiagram(List<NodeBase> lstNode, List<CustomEdge> lstEdge) {
		Graph<NodeBase, String> g2 = new SparseMultigraph<NodeBase, String>();
		//Graph<NodeBase, String> g2 = new UndirectedSparseGraph();
		for(NodeBase node : lstNode)
		{
			System.out.println(node.getName());
			g2.addVertex(node);
		}
		
		final HashMap<Integer,CustomEdge> edges = new HashMap();
		
		int counter = 0;
		for(CustomEdge edge : lstEdge){			
			edges.put(counter, edge);
			g2.addEdge(Integer.toString(counter), edge.getNodeStart(), edge.getNodeStop(),EdgeType.UNDIRECTED);
			counter++;
		}
		
		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String s) {
				return edgeStroke;
			}
		};
		
		Transformer<NodeBase,String> vertexLabel = new Transformer<NodeBase,String>(){
        	public String transform(NodeBase node) {
        		if (node.getType() == NodeType.CN) return "CN";
        		if (node.getType() == NodeType.SENSOR) return node.getName();
        		if (node.getType() == NodeType.PB) return node.getName();
        		if (node.getType() == NodeType.SA) return node.getName();
        		if (node.getType() == NodeType.CAN) return node.getName();
				return null;
			}
		};

		Transformer<NodeBase,Shape> vertexSize = new Transformer<NodeBase,Shape>(){
            public Shape transform(NodeBase node){
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                // in this case, the vertex is twice as large
                if(node.getType() == NodeType.CN) return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
                else return circle;
            }
        };
        
        Transformer<NodeBase,Paint> vertexColor = new Transformer<NodeBase,Paint>() {
            public Paint transform(NodeBase node) {
            	if (node.getType() == NodeType.CN) return Color.RED;
            	if (node.getType() == NodeType.SENSOR) return Color.WHITE;
            	if (node.getType() == NodeType.CAN) return Color.GREEN;
            	if (node.getType() == NodeType.PB) return Color.BLUE;
            	if (node.getType() == NodeType.SA) return Color.WHITE;
            	
            	return Color.BLACK;
            }
        };
		
        Transformer<String,String> transEdge = new Transformer<String,String>(){
        	public String transform(String myedge){
        		Integer idEdge = Integer.parseInt(myedge);
        		CustomEdge myEdge = edges.get(idEdge);
        		return myEdge.getConnSpeed();
        	}
        };
        
		//Layout<NodeBase, String> layout = new CircleLayout<NodeBase, String>(g2);
       //Layout<NodeBase, String> layout = new ISOMLayout<NodeBase, String>(g2);
        
        //final Layout<NodeBase, String> layout = new WireLengthLayout<NodeBase, String>(g2);
        final Layout<NodeBase, String> layout = new FRLLLayout<NodeBase, String>(g2);
        Dimension d = new Dimension();
        d.setSize(1024, 768);
        layout.setSize(d);
        
        //AbstractLayout<NodeBase, String> alayout = new WireLengthLayout<NodeBase, String>(g2);
        
		//BasicVisualizationServer<NodeBase, String> vv = new BasicVisualizationServer<NodeBase, String>(
		//		layout);

		VisualizationViewer<NodeBase,String> vv =  new VisualizationViewer<NodeBase,String>(layout);
		// vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		//vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeLabelTransformer(transEdge);
		//vv.getRenderContext().setEdgeLabelRenderer(arg0);
		
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		//vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		
		GraphMLWriter<NodeBase, String> graphWriter = new GraphMLWriter<NodeBase, String> ();
		PrintWriter out = null;
		
		//final AbstractLayout<NodeBase, String> l = new ISOMLayout<NodeBase, String>(g2);
		
		//String test = "asd";
		
		
		graphWriter.addVertexData("x", null, "0",
			    new Transformer<NodeBase, String>() {
			        @SuppressWarnings({ "unchecked", "rawtypes" })
					public String transform(NodeBase v) {
			            return Double.toString(((AbstractLayout)layout).getX(v));
			        }
			    }
			);
		
		graphWriter.addVertexData("y", null, "0",
			    new Transformer<NodeBase, String>() {
					@SuppressWarnings({ "unchecked", "rawtypes" })
			        public String transform(NodeBase v) {
			            return Double.toString(((AbstractLayout)layout).getY(v));
			       }
			    }
			);
		

		
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(Mode.PICKING);
		vv.setGraphMouse(gm); 
	
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem loadMenuItem = new JMenuItem("Load");
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);
		
		JMenu editMenu = new JMenu("Edit");
		JMenuItem dimMenuItem = new JMenuItem("DimScale");
		editMenu.add(dimMenuItem);
		
		JMenu helpMenu = new JMenu("Help");
		JMenuItem helpMenuItem = new JMenuItem("About");
		helpMenu.add(helpMenuItem);
		
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(helpMenu);
		
		JFrame frame = new JFrame("Simple Graph View");
		frame.setJMenuBar(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("output1.graphml")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			graphWriter.save(g2, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		List<NodeBase> lstBase = new ArrayList<NodeBase>();
		
		
		NodeBase nb_cn = new NodeBase(1,"CN",NodeType.CN,"2");
		lstBase.add(nb_cn);
		NodeBase nb_can1 = new NodeBase(2,"CAN",NodeType.CAN,"2");
		lstBase.add(nb_can1);
		NodeBase nb_can2 = new NodeBase(3,"CAN",NodeType.CAN,"3");
		lstBase.add(nb_can2);
		NodeBase nb_can3 = new NodeBase(4,"CAN",NodeType.CAN,"4");
		lstBase.add(nb_can3);
		NodeBase nb_can4 = new NodeBase(5,"CAN",NodeType.CAN,"5");
		lstBase.add(nb_can4);
		NodeBase nb_can5 = new NodeBase(6,"SA",NodeType.SA,"6");
		lstBase.add(nb_can5);
		NodeBase nb_can6 = new NodeBase(7,"SA",NodeType.SA,"7");
		lstBase.add(nb_can6);
		
		List<CustomEdge> lstLink = new ArrayList<CustomEdge>();
//		CustomEdge ce = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can1, "DIRECTED", "1GB","100");
//		lstLink.add(ce);
//		CustomEdge ce2 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can2, "DIRECTED", "2GB","200");
//		lstLink.add(ce2);
//		CustomEdge ce3 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can3, "DIRECTED", "2GB","300");
//		lstLink.add(ce3);
//		CustomEdge ce4 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can4, "DIRECTED", "2GB","400");
//		lstLink.add(ce4);
//		CustomEdge ce5 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can5, "DIRECTED", "2GB","500");
//		lstLink.add(ce5);
//		CustomEdge ce6 = new CustomEdge(new LinkBase(0, 0), nb_cn, nb_can6, "DIRECTED", "2GB","500");
//		lstLink.add(ce6);

		DrawDiagram(lstBase,lstLink);
	}

}
