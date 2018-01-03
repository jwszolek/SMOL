package main.java.com.sample;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class SimpleGraphView {
	
	public static void main(String[] args)
	{
		SimpleGraphView sg = new SimpleGraphView();
		
	}
	
    public SimpleGraphView() {
        // Create a graph with Integer vertices and String edges
        Graph<Integer, String> g = new SparseGraph<Integer, String>();
    	//Graph<Integer, String> g = new DelegateForest<Integer, String>();
    	
        for(int i = 0; i < 20; i++) g.addVertex(i);
        
          //CAN
          g.addEdge("1Mb", 1, 2);
          g.addEdge("2Mb", 2, 9);
          g.addEdge("5Mb", 2, 10);
          
          g.addEdge("4Mb", 1, 3);
          g.addEdge("0.5Mb", 3, 11);
          g.addEdge("0.3Mb", 3, 12);
          g.addEdge("1.1Mb", 3, 13);
          
          g.addEdge("2.1Mb", 1, 7);
          g.addEdge("0.7Mb", 7, 4);
          g.addEdge("0.31Mb", 7, 6);
          g.addEdge("0.81Mb", 7, 5);
          g.addEdge("1.8Mb", 7, 8);
          g.addEdge("5.1Mb", 4, 18);
          g.addEdge("10Mb", 4, 15);
          g.addEdge("11.1Mb", 5, 17);
          g.addEdge("1.2Mb", 5, 14);
          g.addEdge("1.3Mb", 6, 19);
          g.addEdge("1.6Mb", 6, 16);
          g.addEdge("0.8Mb", 6, 0);
          
          
        
//        g.addEdge("Edge", 1, 2);
//        g.addEdge("Another Edge", 1, 4);
//        g.addEdge("Another Edge2", 1, 3);
//        g.addEdge("Another Edge3", 1, 5);
//        g.addEdge("Another Edge4", 1, 0);
//
//        g.addEdge("Another", 2, 6);
//        g.addEdge("sied", 2, 7);
//        g.addEdge("ose", 2, 8);
//        g.addEdge("dzie", 3, 9);
        
        // Layout implements the graph drawing logic
        Layout<Integer, String> layout = new ISOMLayout<Integer, String>(g);
        //Layout<Integer, String> layout = new TreeLayout<Integer, String>((Forest<Integer, String>)g);
        layout.setSize(new Dimension(600,600));
        //layout.setSize(new Dimension(600,123));

        // VisualizationServer actually displays the graph
        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size

        // Transformer maps the vertex number to a vertex property
        Transformer<Integer,Paint> vertexColor = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
            	//CAN
                if(i == 2) return Color.GREEN;
                if(i == 3) return Color.GREEN;
                if(i == 4) return Color.GREEN;
                //PROFIBUS
                if(i == 5) return Color.LIGHT_GRAY;
                if(i == 6) return Color.LIGHT_GRAY;
                //Transfer
                if(i == 7) return Color.MAGENTA;
                if(i == 8) return Color.WHITE;
                //ENd
                if(i == 9) return Color.WHITE;
                if(i == 10) return Color.WHITE;
                if(i == 11) return Color.WHITE;
                if(i == 12) return Color.WHITE;
                if(i == 13) return Color.WHITE;
                if(i == 14) return Color.WHITE;
                if(i == 15) return Color.WHITE;
                if(i == 16) return Color.WHITE;
                if(i == 17) return Color.WHITE;
                if(i == 18) return Color.WHITE;
                if(i == 19) return Color.WHITE;
                if(i == 20) return Color.WHITE;
                if(i == 0) return Color.WHITE;
                
                
                return Color.RED;
            }
        };
        
        Transformer<Integer,Shape> vertexSize = new Transformer<Integer,Shape>(){
            public Shape transform(Integer i){
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                // in this case, the vertex is twice as large
                if(i == 1) return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
                else return circle;
            }
        };
        
        
        Transformer<Integer,String> vertexLabel = new Transformer<Integer,String>(){
        	public String transform(Integer i){
				if (i == 1) return "CN";
				if (i == 2) return "CAN";
				if (i == 3) return "CAN";
				if (i == 4) return "CAN";
				if (i == 5) return "PB";
				
				if (i == 6) return "PB";
				if (i == 7) return "T(n)";
				
				if (i == 0) return "SAN";
				if (i == 8) return "SAN";
				if (i == 9) return "SAN";
				if (i == 10) return "SAN";
				if (i == 11) return "SAN";
				if (i == 12) return "SAN";
				if (i == 13) return "SAN";
				if (i == 14) return "SAN";
				if (i == 15) return "SAN";
				if (i == 16) return "SAN";
				if (i == 17) return "SAN";
				if (i == 18) return "SAN";
				if (i == 19) return "SAN";
				if (i == 20) return "SAN";
				
				return "node";
        		
        	}
        };
        
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
        
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
        
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);    
    }
}
