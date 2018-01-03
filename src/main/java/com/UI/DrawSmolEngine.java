package main.java.com.UI;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import main.java.com.UI.MenuActionListener;
import main.java.com.prototype.*;
import main.java.com.utils.*;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

public class DrawSmolEngine {

	private VisualizationViewer<NodeBase, CustomEdge> vv_loc;
	private Graph<NodeBase, CustomEdge> g2 = new SparseMultigraph<NodeBase, CustomEdge>();
	private Layout<NodeBase, CustomEdge> layout_loc;
	private JFrame frame = new JFrame("Simple Graph View");

	public DrawSmolEngine(GraphOutput go) {
		vv_loc = go.getVv();
		g2 = go.getGx();
		layout_loc = go.getLayout_loc();
	}
 
	public void DrawUIGraph() {
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem loadMenuItem = new JMenuItem("Load");
		fileMenu.add(saveMenuItem);
		fileMenu.add(loadMenuItem);

		loadMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {

					String filePath = c.getCurrentDirectory().toString() + "\\"
							+ c.getSelectedFile().getName();
					System.out.println(filePath);

					frame.getContentPane().removeAll();
					try {
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						
						frame.getContentPane().add(LoadGraphML.Read(filePath));
						frame.getContentPane().revalidate();
						frame.getContentPane().repaint();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		saveMenuItem.addActionListener(new MenuActionListener(g2, layout_loc));

		JMenu editMenu = new JMenu("Edit");
		JMenuItem dimMenuItem = new JMenuItem("LengthLayout");
		editMenu.add(dimMenuItem);

		dimMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().add(DrawSecond.TranformLayout(g2, LayoutType.Own));
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
		JMenuItem KKLayoutMenuItem = new JMenuItem("KKLayout");
		editMenu.add(KKLayoutMenuItem);
		KKLayoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().add(DrawSecond.TranformLayout(g2, LayoutType.KKLayout));
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
		JMenuItem FRLayoutMenuItem = new JMenuItem("FRLayout");
		editMenu.add(FRLayoutMenuItem);
		FRLayoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().add(DrawSecond.TranformLayout(g2, LayoutType.FRLayout));
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
		JMenuItem SpringLayoutMenuItem = new JMenuItem("SpringLayout");
		editMenu.add(SpringLayoutMenuItem);
		SpringLayoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().add(DrawSecond.TranformLayout(g2, LayoutType.SpringLayout));
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
		JMenuItem ISOMLayoutMenuItem = new JMenuItem("ISOMLayout");
		editMenu.add(ISOMLayoutMenuItem);
		ISOMLayoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().add(DrawSecond.TranformLayout(g2, LayoutType.ISOMLayout));
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
	
		JMenuItem circleLayoutMenuItem = new JMenuItem("CircleLayout");
		editMenu.add(circleLayoutMenuItem);
		
		circleLayoutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.getContentPane().removeAll();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().add(DrawSecond.TranformLayout(g2, LayoutType.Circle));
				
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
		editMenu.add(new JSeparator());
		
		JMenuItem clearMenuItem = new JMenuItem("Clear");
		editMenu.add(clearMenuItem);

		clearMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.getContentPane().removeAll();
				frame.getContentPane().revalidate();
				frame.getContentPane().repaint();
			}
		});
		
		JMenu simMenu = new JMenu("Simulation");
		JMenuItem runSimItem = new JMenuItem("Run");
		simMenu.add(runSimItem);
		
		JMenu helpMenu = new JMenu("Help");
		JMenuItem docMenuItem = new JMenuItem("Docs");
		JMenuItem helpMenuItem = new JMenuItem("About");
		helpMenu.add(docMenuItem);
		helpMenu.add(helpMenuItem);
		helpMenuItem.addActionListener(new MenuActionListener());

		
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(simMenu);
		menu.add(helpMenu);

		frame.setJMenuBar(menu);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.getContentPane().add(vv_loc);
		frame.pack();
		frame.setVisible(true);
	}

}
