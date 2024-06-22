package org.graphstream.tutorial.modularity;

import java.io.IOException;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.measure.Modularity;
import org.graphstream.graph.Edge;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.ui.view.Viewer;

public class LinLogLayout2 {

	private LinLog layout; // 2
	private double a = 0; // 3
	private double r = -1.3; // 3
	private double force = 3; // 3
	private double cutThreshold = 1; // 4

	private ProxyPipe fromViewer; // 1

	protected static String styleSheet =                       // 4
	"graph { padding: 60px; }" +
    "node { size: 7px; fill-color: rgb(150,150,150); }" +
    "edge { fill-color: rgb(255,50,50); size: 2px; }" +
    "edge.cut { fill-color: rgba(200,200,200,128); }" +
	"sprite {text-alignment: at-right;}"+
    "sprite#CC { size: 0px; text-color: rgb(150,100,100); text-size: 20; }" +
    "sprite#M  { size: 0px; text-color: rgb(100,150,100); text-size: 20; }";








	public static void main(String args[]) throws IOException, GraphParseException, InterruptedException {
		System.setProperty("org.graphstream.ui", "swing");
		(new LinLogLayout2()).findCommunities("data/karate.gml");
	}

	private Graph graph;
	private Viewer viewer;

	private ConnectedComponents cc;                           // 1
	private Modularity modularity;    
	private SpriteManager sm;                                  // 1
	private Sprite ccCount, modValue;                            // 1

	public void findCommunities(String fileName) throws IOException, GraphParseException, InterruptedException {
		graph = new SingleGraph("communities");
		viewer = graph.display(false);
		fromViewer = viewer.newThreadProxyOnGraphicGraph(); // 1
		layout = new LinLog(false);
		cc = new ConnectedComponents(graph); 
		sm = new SpriteManager(graph);                           // 1
		ccCount = sm.addSprite("CC");                            // 1
		modularity = new Modularity("module");                  // 2
		modValue = sm.addSprite("M");                              // 1
		modValue.setPosition(Units.PX, 20, 40, 0);                 // 2

		modularity.init(graph);                                 // 3
		layout.configure(a, r, true, force);
		layout.addSink(graph);
		graph.addSink(layout);
		fromViewer.addSink(graph); // 2
		cc.setCutAttribute("cut");                              // 3
		ccCount.setPosition(Units.PX, 20, 20, 0);                // 2
		cc.setCountAttribute("module");                         // 4

		graph.setAttribute("ui.stylesheet", styleSheet); // 1
		graph.setAttribute("ui.antialias"); // 2
		graph.read(fileName);
		while (!graph.hasAttribute("ui.viewClosed")) {
			fromViewer.pump();
			layout.compute();
			showCommunities(); // 3
			ccCount.setAttribute("ui.label",                       // 3
				String.format("Modules: %d", cc.getConnectedComponentsCount()));
			modValue.setAttribute("ui.label",                        // 3
				String.format("Modularity %.2f", modularity.getMeasure()));
			Thread.sleep(1);
			// Thread.yield();
		}

	}

	public void showCommunities() {
		int nEdges = graph.getEdgeCount();

		double averageLength = graph.edges().map(edge -> { // 1
			double length = GraphPosLengthUtils.edgeLength(edge);
			edge.setAttribute("length", length); // 2
			return length;
		}).reduce(0.0, (a, b) -> a + b) / nEdges; // 3


		graph.edges().forEach(edge ->{
			double length = edge.getNumber("length");
			if(length > averageLength * cutThreshold) {
				edge.setAttribute("ui.class", "cut");
				edge.setAttribute("cut");
			} else {
				edge.removeAttribute("ui.class");
				edge.removeAttribute("cut");
			}
		});

	}

	  
}
