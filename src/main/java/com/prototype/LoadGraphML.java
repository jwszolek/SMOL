package main.java.com.prototype;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import main.java.com.utils.*;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.GraphMetadata.EdgeDefault;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class LoadGraphML {

	public static VisualizationViewer<NodeBase, CustomEdge> Read(String filepath) throws FileNotFoundException {
		BufferedReader fileReader = new BufferedReader(new FileReader(
				filepath));

		/* Create the Graph Transformer */
		Transformer<GraphMetadata, Graph<NodeBase, CustomEdge>> graphTransformer = new Transformer<GraphMetadata, Graph<NodeBase, CustomEdge>>() {

			public Graph<NodeBase, CustomEdge> transform(GraphMetadata metadata) {
				metadata.getEdgeDefault();
				if (metadata.getEdgeDefault().equals(EdgeDefault.UNDIRECTED)) {
					return new SparseMultigraph<NodeBase, CustomEdge>();
				} else {
					return new SparseMultigraph<NodeBase, CustomEdge>();
				}
			}
		};

		/* Create the Vertex Transformer */
		Transformer<NodeMetadata, NodeBase> vertexTransformer = new Transformer<NodeMetadata, NodeBase>() {
			public NodeBase transform(NodeMetadata metadata) {
				NodeBase v = new NodeBase();
				v.setX(Double.parseDouble(metadata.getProperty("x")));
				v.setY(Double.parseDouble(metadata.getProperty("y")));
				v.setType(NodeType.valueOf(metadata.getProperty("type")));
				v.setName(metadata.getProperty("id"));

				return v;
			}
		};

		/* Create the Edge Transformer */
		Transformer<EdgeMetadata, CustomEdge> edgeTransformer = new Transformer<EdgeMetadata, CustomEdge>() {
			public CustomEdge transform(EdgeMetadata metadata) {
				CustomEdge e = new CustomEdge();
				e.setConnSpeed(metadata.getProperty("connspeed"));
				return e;
			}
		};

		/* Create the Hyperedge Transformer */
		Transformer<HyperEdgeMetadata, CustomEdge> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, CustomEdge>() {
			public CustomEdge transform(HyperEdgeMetadata metadata) {
				CustomEdge e = new CustomEdge();
				return e;
			}
		};

		/* Create the graphMLReader2 */
		GraphMLReader2<Graph<NodeBase, CustomEdge>, NodeBase, CustomEdge> graphReader = new GraphMLReader2<Graph<NodeBase, CustomEdge>, NodeBase, CustomEdge>(
				fileReader, graphTransformer, vertexTransformer,
				edgeTransformer, hyperEdgeTransformer);

		Graph g = null;

		try {
			/* Get the new graph object from the GraphML file */
			g = graphReader.readGraph();
		} catch (GraphIOException ex) {
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		StaticLayout<NodeBase, CustomEdge> layout = new StaticLayout(g,
				new Transformer<NodeBase, Point2D>() {
					public Point2D transform(NodeBase v) {
						Point2D p = new Point2D.Double(v.getX(), v.getY());
						return p;
					}
				});

		float dash[] = { 10.0f };
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f);
		Transformer<CustomEdge, Stroke> edgeStrokeTransformer = new Transformer<CustomEdge, Stroke>() {
			public Stroke transform(CustomEdge s) {
				return edgeStroke;
			}
		};

		Transformer<CustomEdge, String> transEdge = new Transformer<CustomEdge, String>() {
			public String transform(CustomEdge myedge) {
				//CustomEdge myEdge = edges.get(myedge.getId());
				return myedge.getConnSpeed();
				//return "5GB";
			}
		};

		Transformer<NodeBase, String> vertexLabel = new Transformer<NodeBase, String>() {
			public String transform(NodeBase node) {
				if (node.getType() == NodeType.CN)
					return "CN";
				if (node.getType() == NodeType.SENSOR)
					return node.getType().toString();
				if (node.getType() == NodeType.PB)
					return node.getType().toString();
				if (node.getType() == NodeType.SA)
					return node.getType().toString();
				if (node.getType() == NodeType.CAN)
					return node.getType().toString();
				return null;
			}
		};

		Transformer<NodeBase, Shape> vertexSize = new Transformer<NodeBase, Shape>() {
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
		
		Transformer<NodeBase, Paint> vertexColor = new Transformer<NodeBase, Paint>() {
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

		VisualizationViewer<NodeBase, CustomEdge> vv = new VisualizationViewer<NodeBase, CustomEdge>(
				layout);
		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		vv.getRenderContext().setEdgeLabelTransformer(transEdge);

		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		vv.addKeyListener(gm.getModeKeyListener());
		vv.setGraphMouse(gm);

		return vv;
	}

}
