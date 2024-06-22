package org.graphstream.tutorial.tutorial2;

import java.io.IOException;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

public class Tutorial2 {
	public static void main(String args[]) throws IOException, InterruptedException {
		System.setProperty("org.graphstream.ui", "swing");

		Graph graph = new SingleGraph("Tutorial2");
		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.antialias");

		graph.setAttribute("ui.stylesheet", "url(data/style.css);");

		graph.display();
		FileSource source = new FileSourceDGS();
		source.addSink(graph);
		source.begin("data/tutorial2.dgs");
		while (source.nextEvents());
		source.end();
	}
}