package main.java.com.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import main.java.com.utils.CustomEdge;
import main.java.com.utils.NodeBase;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.io.GraphMLWriter;

public class MenuActionListener implements ActionListener {
	Graph<NodeBase, CustomEdge> gx = new SparseMultigraph<NodeBase, CustomEdge>();
	Layout<NodeBase, CustomEdge> layout_loc;

	public MenuActionListener() {
	}

	public MenuActionListener(Graph<NodeBase, CustomEdge> graphToSave,
			Layout<NodeBase, CustomEdge> layout) {
		gx = graphToSave;
		layout_loc = layout;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "About") {
			JFrame frame = new JFrame("About");
			JOptionPane.showMessageDialog(frame, "SMOL Visualization Module v.1.0 \n \n Z.Kowalczuk - kova@eti.pg.gda.pl \n J.Wszolek - jwszolek@eti.pg.gda.pl",
					"About", JOptionPane.INFORMATION_MESSAGE);
		}

		if (e.getActionCommand() == "Load") {
			JFileChooser c = new JFileChooser();
			int rVal = c.showOpenDialog(null);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				System.out.println(c.getSelectedFile().getName());
				
			}			
		}

		if (e.getActionCommand() == "Save") {

			JFileChooser fileChooser = new JFileChooser();
			int rVal = fileChooser.showSaveDialog(null);

			if (rVal == JFileChooser.APPROVE_OPTION) {

				String filename = fileChooser.getSelectedFile().getName();
				String dirLoc = fileChooser.getCurrentDirectory().toString();
				String filePath = dirLoc + '\\' + filename; 
				
				System.out.println(filePath);
				
				System.out.println("Selected: " + e.getActionCommand());
				GraphMLWriter<NodeBase, CustomEdge> graphWriter = new GraphMLWriter<NodeBase, CustomEdge>();
				PrintWriter out = null;

				graphWriter.addVertexData("x", null, "0",
						new Transformer<NodeBase, String>() {
							@SuppressWarnings({ "unchecked", "rawtypes" })
							public String transform(NodeBase v) {
								return Double
										.toString(((AbstractLayout) layout_loc)
												.getX(v));
							}
						});

				graphWriter.addVertexData("y", null, "0",
						new Transformer<NodeBase, String>() {
							@SuppressWarnings({ "unchecked", "rawtypes" })
							public String transform(NodeBase v) {
								return Double
										.toString(((AbstractLayout) layout_loc)
												.getY(v));
							}
						});

				graphWriter.addVertexData("type", null, "none",
						new Transformer<NodeBase, String>() {
							public String transform(NodeBase v) {
								return v.getType().toString();
							}
						});

				graphWriter.addVertexData("connspeed", null, "none",
						new Transformer<NodeBase, String>() {
							public String transform(NodeBase v) {
								return v.getConnLabel();
							}
						});

				graphWriter.addEdgeData("connspeed", null, "none",
						new Transformer<CustomEdge, String>() {
							public String transform(CustomEdge v) {
								return v.getConnSpeed();
							}
						});

				try {
					out = new PrintWriter(new BufferedWriter(new FileWriter(
							filePath)));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					graphWriter.save(gx, out);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
