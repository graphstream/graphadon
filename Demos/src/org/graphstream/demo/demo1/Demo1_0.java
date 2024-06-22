package org.graphstream.demo.demo1;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import org.graphstream.graph.implementations.SingleGraph;

public class Demo1_0 {
	public static void main(String args[]) {
		System.setProperty("org.graphstream.ui", "swing");

        Graph graph = new SingleGraph("Tutorial 1");
        graph.display(false);

		Node a = graph.addNode("A");
		a.setAttribute("xy", 0, 0);

		graph.addNode("B").setAttribute("xy", 0, 10000);;

		graph.addEdge("AB", "A", "B");
		graph.addNode("C").setAttribute("xy", 10000, 10000);;
		graph.addEdge("BC", "B", "C", true); // Directed edge.
		graph.addEdge("CA", "C", "A");
	}
}