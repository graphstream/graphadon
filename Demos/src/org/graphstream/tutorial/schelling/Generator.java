package org.graphstream.tutorial.schelling;

import java.util.Random;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class Generator {



	// ================= RANDOM GEOMETRIC ====================

	/**
	 * a generator for a random geometric graph based on the 
	 * euclidean distance between nodes.
	 * 
	 * @param n the number of nodes
	 * @param d the maximum euclidean distance for 2 nodes to be connected
	 * @param envSize the size of the environment in which nodes are positioned
	 * @return rgg an instance of such a random euclidean graph
	 */
	public static SingleGraph randomEuclideanGraph(int n, double d, int envSize) {
		SingleGraph rgg = new SingleGraph("RGG: ("+n+","+d+","+envSize+")");
		Random alea = new Random(System.currentTimeMillis());
		// creation of nodes
		for(int u=0;u<n;u++) {
			Node v = rgg.addNode("v_"+u);
			v.setAttribute("x",alea.nextDouble()*envSize);
			v.setAttribute("y",alea.nextDouble()*envSize);
		}
		// creation of edges
		rgg.nodes().forEach(n1 -> rgg.nodes().forEach(n2 -> {
			if(n1 != n2 && !n1.hasEdgeBetween(n2) && distance(n1, n2) < d) {
				rgg.addEdge(n1.getId()+"-"+n2.getId(), n1, n2);
			}
		}));
		return rgg;
	}
	
	// =============== PLANAR ====================
	
	/**
	 * principle:
	 * start with one edge
	 * then add a new node w and randomly chose an edge (u,v)
	 * add edge (w,u) and (w,v) 
	 * @param n order of the graph
	 * @return
	 */
	public static SingleGraph planar(int n) {
		SingleGraph planargraph = new SingleGraph("planar "+n);
		// creation of the first edge
		Node v0 = planargraph.addNode("v_0");
		Node v1 = planargraph.addNode("v_1");
		planargraph.addEdge(v0.getId()+"-"+v1.getId(),v0.getId(),v1.getId());
		int currentOrder = 2;
		// addition of nodes
		while(currentOrder < n) {
			Node v = planargraph.addNode("v_"+currentOrder);
			Edge e = Toolkit.randomEdge(planargraph);
			v0 = e.getNode0();
			v1 = e.getNode1();
			planargraph.addEdge(v.getId()+"-"+v0.getId(),v.getId(),v0.getId());
			planargraph.addEdge(v.getId()+"-"+v1.getId(),v.getId(),v1.getId());
			currentOrder++;
		}
		return planargraph;
	}


	// =============== RANDOM ====================
	
	/**
	 * the graph is built in two steps: 
	 * 1) node creation
	 * 2) edge creation
	 * @param n order of the graph
 	 * @param proba ...bility of each edge to be present in the graph
	 * @return
	 */
	public static SingleGraph ErdosRenyi(int n, double proba) {
		SingleGraph randomgraph = new SingleGraph("G("+n+","+proba+")");
		Random alea = new Random(System.currentTimeMillis());
		// node creation
		for(int i=0;i<n;i++) {
			randomgraph.addNode("v_"+i);
		}
		// edge creation
		for(int i=0;i<n-1;i++) {
			for(int j=i+1;j<n;j++) {
				if(alea.nextDouble() < proba)
					randomgraph.addEdge(i+"-"+j,"v_"+i,"v_"+j);
			}
		}
		return randomgraph;
	}

	// =============== GRID and TORUS ====================

	/**
	 * Grid/Torus generator. n is the size of the grid
	 * a grid/torus of size n has n x n vertices
	 * by default the neighborhood is von Neumann neighborhood
	 * each vertex has neighbors located on the North, South, East and West
	 * (except of the borders for the grid)
	 * In a Moore neighborhood, each vertex has also neighbors located
	 * as South-East, South-West, North-East North-West.
	 * @param n
	 * @param torus
	 * @param moore
	 * @return
	 */
	public static SingleGraph grid(int n, boolean torus, boolean moore) {
		SingleGraph grid = new SingleGraph("grid "+n+"x"+n);
		// creation of nodes
		for(int line=0;line<n;line++) {
			for(int col=0;col<n;col++) {
				Node newNode = grid.addNode(line+","+col);
				newNode.setAttribute("x",col);
				newNode.setAttribute("y",line);
			}
		}
		// creation of links to the col+1 and line+1 neighbors
		for(int line=0;line<n;line++) {
			for(int col=0;col<n;col++) {
				Node currentNode = grid.getNode(line+","+col);	
				int colplusone = (col+1)%n;
				int colminusone = (col+n-1)%n;
				int lineplusone = (line+1)%n;
				Node eastNode = grid.getNode(line+","+colplusone);
				Node southNode = grid.getNode(lineplusone+","+col);
				if((lineplusone > 0) || torus) 
					grid.addEdge(currentNode.getId()+"-"+southNode.getId(),
							currentNode.getId(),southNode.getId());
				if((colplusone > 0) || torus)
					grid.addEdge(currentNode.getId()+"-"+eastNode.getId(),
							currentNode.getId(),eastNode.getId());
				if(moore) {
					Node southEastNode = grid.getNode(lineplusone+","+colplusone);
					Node southWestNode = grid.getNode(lineplusone+","+colminusone);
					if(((lineplusone > 0) && (colplusone > 0)) || torus) 
						grid.addEdge(currentNode.getId()+"-"+southEastNode.getId(),
								currentNode.getId(),southEastNode.getId());
					if(((lineplusone > 0) && (colminusone < n-1 )) || torus) 
						grid.addEdge(currentNode.getId()+"-"+southWestNode.getId(),
								currentNode.getId(),southWestNode.getId());
 				}
			}
		}
		return grid;
	}


	/**
	 * 
	 * @param n size of the grid
	 * @param dMax threshold for the distance between two neighbors
	 * @return
	 */
	public static SingleGraph gridFromDistance(int n,double dMax) {
		SingleGraph grid = new SingleGraph("grid "+n+"x"+n);
		// creation of nodes
		for(int line=0;line<n;line++) {
			for(int col=0;col<n;col++) {
				Node newNode = grid.addNode(line+","+col);
				newNode.setAttribute("x",(double)col);
				newNode.setAttribute("y",(double)line);
			}
		}
		
		// creation of links to the col+1 and line+1 neighbors
		grid.nodes().forEach(n1 -> grid.nodes().forEach(n2 -> {
			if(n1 != n2 && !n1.hasEdgeBetween(n2) && distance(n1, n2) <= dMax) {
				grid.addEdge(n1.getId()+"-"+n2.getId(), n1, n2);
			}
		}));
		return grid;
	}
	
	// =============== FULL-CONNECTED ====================
	
	/**
	 * the building algorithm consists in creating
	 * all the nodes in a first step then 
	 * for each node to link it to any other 
	 * node not linked yet
	 * @param n
	 * @return
	 */
	public static SingleGraph fullConnected(int n) {
		SingleGraph fullc = new SingleGraph("fc "+n);
		// creation vertices
		for(int i=0;i<n;i++) {
			fullc.addNode("v_"+i);
		}
		 
		// creation of edges 
		for(int i=0;i<n-1;i++) {
			for(int j=i+1;j<n;j++) {
				fullc.addEdge(i+"-"+j,"v_"+i,"v_"+j);
			}
		}
		return fullc;
	}
	

	// =============== RING ====================
	
	public static SingleGraph ring(int n) {
		SingleGraph rinGraph = new SingleGraph("ring_"+n);
		Node first = rinGraph.addNode("v_0");
		Node previous = first;
		Node next;
		for(int i=1;i<n;i++) {
			next = rinGraph.addNode("v_"+i);
			rinGraph.addEdge(previous.getId()+"-"+next.getId(),previous.getId(),next.getId());
			previous = next;
		}
		rinGraph.addEdge(previous.getId()+"-"+first.getId(),previous.getId(),first.getId());
		return rinGraph;
	}
	
	
	
	// =============== TREES ====================

	/**
	 * principle: add a node u and chose randomly another node v
	 * and link u and v 
	 * @param n
	 * @return
	 */
	public static SingleGraph tree(int n) {
		SingleGraph tree = new SingleGraph("tree "+n);
		// creation
		tree.addNode("root");
		for(int i=1;i<n;i++) {
			Node randomNode = Toolkit.randomNode(tree);
			Node newNode = tree.addNode("v_"+i);
			// link the two nodes
			tree.addEdge(randomNode.getId()+"- "+newNode.getId(),
					randomNode.getId(),newNode.getId());
		}
		 
		return tree;
	}

	public final static double distance(Node u, Node v) {
		double x1 = (double) u.getAttribute("x");
		double y1 = (double) u.getAttribute("y");
		double x2 = (double) v.getAttribute("x");
		double y2 = (double) v.getAttribute("y");
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
}
