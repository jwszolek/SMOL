package main.java.com.utils;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class FRLLLayout<V, E> extends AbstractLayout<V, E> implements
		IterativeContext {

	
	public FRLLLayout(Graph<V,E> graph, Transformer<V,Point2D> initializer) {
		super(graph, initializer);
	}
	
	public FRLLLayout(Graph<V, E> graph) {
		super(graph);
		minMaxValue(graph);
		this.gr = graph;
	}


	private double forceConstant;

	private double temperature;

	private int currentIteration;

	private int mMaxIterations = 700;

	private Map<V, FRVertexData> frVertexData = LazyMap.decorate(
			new HashMap<V, FRVertexData>(), new Factory<FRVertexData>() {
				public FRVertexData create() {
					return new FRVertexData();
				}
			});

	private double attraction_multiplier = 0.75;

	private double attraction_constant;

	private double repulsion_multiplier = 0.75;

	private double repulsion_constant;

	private double max_dimension;
	
	private Graph gr;

	/**
	 * Creates an instance for the specified graph.
	 */
	/**
	 * Creates an instance of size {@code d} for the specified graph.
	 */
	public FRLLLayout(Graph<V, E> g, Dimension d) {
		super(g, new RandomLocationTransformer<V>(d), d);
		initialize();
		max_dimension = Math.max(d.height, d.width);
	}

	private double minEdgeLength;
	private double maxEdgeLength;
	
	@SuppressWarnings("unchecked")
	private void minMaxValue(Graph g){
		Collection<CustomEdge> edgeLst = g.getEdges();
		@SuppressWarnings("rawtypes")
		List list = new ArrayList(edgeLst);
		Collections.sort(list, CustomEdge.LengthComparator);
		
		int listSize = list.size();
		minEdgeLength = ((CustomEdge)list.get(0)).getLinkLength();
		maxEdgeLength = ((CustomEdge)list.get(listSize-1)).getLinkLength();
	}
	
	private double getLinkLength(NodeBase n1, NodeBase n2){
		Collection<CustomEdge> edgeLst = gr.getEdges();
		
		for(CustomEdge ce : edgeLst){
			if (ce.getNodeStart() == n1 && ce.getNodeStop() == n2){
				return ce.getLinkLength();
			}
			
			if(ce.getNodeStart() == n2 && ce.getNodeStop() == n1){
				return ce.getLinkLength();
			}
		}
		return 0;
	}

	private double minMaxNor(double LinkLength){
		double dmin = 0.1;
		double dmax = 10;
		
		double out = ((LinkLength - minEdgeLength)/(maxEdgeLength - minEdgeLength)) * (dmax - dmin) + dmin;
		
		return 10 - out;
	}
	
	
	@Override
	public void setSize(Dimension size) {
		if (initialized == false) {
			setInitializer(new RandomLocationTransformer<V>(size));
		}
		super.setSize(size);
		max_dimension = Math.max(size.height, size.width);
	}

	/**
	 * Sets the attraction multiplier.
	 */
	public void setAttractionMultiplier(double attraction) {
		this.attraction_multiplier = attraction;
	}

	/**
	 * Sets the repulsion multiplier.
	 */
	public void setRepulsionMultiplier(double repulsion) {
		this.repulsion_multiplier = repulsion;
	}

	public void reset() {
		doInit();
	}

	public void initialize() {
		doInit();
	}

	private void doInit() {
		Graph<V, E> graph = getGraph();
		Dimension d = getSize();
		if (graph != null && d != null) {
			currentIteration = 0;
			temperature = d.getWidth() / 10;

			forceConstant = Math.sqrt(d.getHeight() * d.getWidth()
					/ graph.getVertexCount());

			attraction_constant = attraction_multiplier * forceConstant;
			repulsion_constant = repulsion_multiplier * forceConstant;
		}
	}

	private double EPSILON = 0.000001D;

	/**
	 * Moves the iteration forward one notch, calculation attraction and
	 * repulsion between vertices and edges and cooling the temperature.
	 */
	public synchronized void step() {
		currentIteration++;

		/**
		 * Calculate repulsion
		 */
		while (true) {

			try {
				for (V v1 : getGraph().getVertices()) {
					calcRepulsion(v1);
				}
				break;
			} catch (ConcurrentModificationException cme) {
			}
		}

		/**
		 * Calculate attraction
		 */
		while (true) {
			try {
				for (E e : getGraph().getEdges()) {

					calcAttraction(e);
				}
				break;
			} catch (ConcurrentModificationException cme) {
			}
		}

		while (true) {
			try {
				for (V v : getGraph().getVertices()) {
					if (isLocked(v))
						continue;
					calcPositions(v);
				}
				break;
			} catch (ConcurrentModificationException cme) {
			}
		}
		cool();
	}

	protected synchronized void calcPositions(V v) {
		FRVertexData fvd = getFRData(v);
		if (fvd == null)
			return;
		Point2D xyd = transform(v);
		double deltaLength = Math.max(EPSILON, fvd.norm());

		double newXDisp = fvd.getX() / deltaLength
				* Math.min(deltaLength, temperature);

		if (Double.isNaN(newXDisp)) {
			throw new IllegalArgumentException(
					"Unexpected mathematical result in FRLayout:calcPositions [xdisp]");
		}

		double newYDisp = fvd.getY() / deltaLength
				* Math.min(deltaLength, temperature);
		xyd.setLocation(xyd.getX() + newXDisp, xyd.getY() + newYDisp);

		double borderWidth = getSize().getWidth() / 50.0;
		double newXPos = xyd.getX();
		if (newXPos < borderWidth) {
			newXPos = borderWidth + Math.random() * borderWidth * 2.0;
		} else if (newXPos > (getSize().getWidth() - borderWidth)) {
			newXPos = getSize().getWidth() - borderWidth - Math.random()
					* borderWidth * 2.0;
		}

		double newYPos = xyd.getY();
		if (newYPos < borderWidth) {
			newYPos = borderWidth + Math.random() * borderWidth * 2.0;
		} else if (newYPos > (getSize().getHeight() - borderWidth)) {
			newYPos = getSize().getHeight() - borderWidth - Math.random()
					* borderWidth * 2.0;
		}

		xyd.setLocation(newXPos, newYPos);
	}

	protected void calcAttraction(E e) {
		Pair<V> endpoints = getGraph().getEndpoints(e);
		V v1 = endpoints.getFirst();
		V v2 = endpoints.getSecond();
		boolean v1_locked = isLocked(v1);
		boolean v2_locked = isLocked(v2);

		if (v1_locked && v2_locked) {
			// both locked, do nothing
			return;
		}
		Point2D p1 = transform(v1);
		Point2D p2 = transform(v2);
		if (p1 == null || p2 == null)
			return;
		double xDelta = p1.getX() - p2.getX();
		double yDelta = p1.getY() - p2.getY();

		double deltaLength = Math.max(EPSILON,
				Math.sqrt((xDelta * xDelta) + (yDelta * yDelta)));

		double force = (deltaLength * deltaLength) / attraction_constant;

		if (Double.isNaN(force)) {
			throw new IllegalArgumentException(
					"Unexpected mathematical result in FRLayout:calcPositions [force]");
		}

		double dx = 0;
		double linkLength = this.getLinkLength((NodeBase)v1, (NodeBase)v2);
		double lengthFactor = this.minMaxNor(linkLength);

		
		System.out.println(lengthFactor);
		System.out.println(((NodeBase)v2).getName());
		
//		if (((NodeBase)v2).getType() == NodeType.SA){
//			dx =  (0.1 * xDelta / deltaLength) * force;	
//		}
//		else
//			dx =  (xDelta / deltaLength) * force;
		if (!Double.isNaN(lengthFactor)) {
			dx =  (lengthFactor * xDelta / deltaLength) * force;
		} else {
			dx =  (xDelta / deltaLength) * force;
		}
		
		
		double dy = 0;
//		if (((NodeBase)v2).getType() == NodeType.SA)
//			dy =  (0.1 * yDelta / deltaLength) * force;
//		else
//			dy =  (yDelta / deltaLength) * force;

		if (!Double.isNaN(lengthFactor)) {
			dy =  (lengthFactor * yDelta / deltaLength) * force;
		} else {
			dy =  (yDelta / deltaLength) * force;
		}
		
		if (v1_locked == false) {
			FRVertexData fvd1 = getFRData(v1);
			fvd1.offset(-dx, -dy);
		}
		if (v2_locked == false) {
			FRVertexData fvd2 = getFRData(v2);
			fvd2.offset(dx, dy);
		}
	}

	private double normalizeLength(Integer LinkLength){
		
		return 0;
	}
	
	protected void calcRepulsion(V v1) {
		FRVertexData fvd1 = getFRData(v1);
		if (fvd1 == null)
			return;
		fvd1.setLocation(0, 0);

		try {
			for (V v2 : getGraph().getVertices()) {

				// if (isLocked(v2)) continue;
				if (v1 != v2) {
					Point2D p1 = transform(v1);
					Point2D p2 = transform(v2);
					if (p1 == null || p2 == null)
						continue;
					double xDelta = p1.getX() - p2.getX();
					double yDelta = p1.getY() - p2.getY();

					double deltaLength = Math.max(EPSILON,
							Math.sqrt((xDelta * xDelta) + (yDelta * yDelta)));

					double force = (repulsion_constant * repulsion_constant)
							/ deltaLength;

					if (Double.isNaN(force)) {
						throw new RuntimeException(
								"Unexpected mathematical result in FRLayout:calcPositions [repulsion]");
					}

					fvd1.offset((xDelta / deltaLength) * force,
							(yDelta / deltaLength) * force);
				}
			}
		} catch (ConcurrentModificationException cme) {
			calcRepulsion(v1);
		}
	}

	private void cool() {
		temperature *= (1.0 - currentIteration / (double) mMaxIterations);
	}

	/**
	 * Sets the maximum number of iterations.
	 */
	public void setMaxIterations(int maxIterations) {
		mMaxIterations = maxIterations;
	}

	protected FRVertexData getFRData(V v) {
		return frVertexData.get(v);
	}

	/**
	 * This one is an incremental visualization.
	 */
	public boolean isIncremental() {
		return true;
	}

	/**
	 * Returns true once the current iteration has passed the maximum count,
	 * <tt>MAX_ITERATIONS</tt>.
	 */
	public boolean done() {
		if (currentIteration > mMaxIterations
				|| temperature < 1.0 / max_dimension) {
			return true;
		}
		return false;
	}

	protected static class FRVertexData extends Point2D.Double {
		protected void offset(double x, double y) {
			this.x += x;
			this.y += y;
		}

		protected double norm() {
			return Math.sqrt(x * x + y * y);
		}
	}

}
