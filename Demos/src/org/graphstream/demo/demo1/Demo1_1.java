package org.graphstream.demo.demo1;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class Demo1_1 {
	public static void main(String args[]) {
		System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Tutorial 1");
        graph.display();

		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.antialias");
		graph.setAttribute("ui.stylesheet", "" +
				"edge {" +
				"   size: 3px;" +
				"	arrow-size: 5px, 5px;" +
				"   fill-color: #2c7fb8;" +
				"}");

		graph.addNode("A");
		graph.addNode("B");
		graph.addEdge("AB", "A", "B");
		graph.addNode("C");
		graph.addEdge("BC", "B", "C", true); // Directed edge.
		graph.addEdge("CA", "C", "A");
	}
}