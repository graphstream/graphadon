package org.graphstream.tutorial.schelling;

import java.util.ArrayList;
import java.util.Random;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Schelling {

	String defaultStyleSheet = "graph {padding:40px;fill-color:white;}"
			+ " node {shape:box; fill-color:black; size:0.9gu;}"
			+ " edge {fill-color:gray;size:1px;}"
			+ " node.satisfied {shape:box;}"
			+ " node.unsatisfied {shape:cross;}";
	
	
	// ------------  styles and states 
	String redStyle = "fill-color:red;";
	String blueStyle = "fill-color:blue;";
	String freeStyle = "fill-color:white;";

	String satisfiedClass = "satisfied";
	String unsatisfiedClass = "unsatisfied";


	// -------------states of the cells
	public final static int FREE = 0;
	public final static int RED = 1;
	public final static int BLUE = 2;
	
	
	// ---------- parameters and objects for Schelling 
	// we keep an arraylist of all 
	// - the free spaces in the automata
	// - the unsatisfied cells
	ArrayList<Node>freeSpaces = new ArrayList<>();
	ArrayList<Node> unsatisfiedCells = new ArrayList<>();
	
	// threshold for the satisfaction of a cell
	double thresholdMaxPercentDifferent = 0.3;
	
	
	Random random;
	SingleGraph ca; // ca like cellular automaton
	int delay = 10;
	int gridSize = 40;
	int redPercentage = 45;
	int bluePercentage = 45;
	
	
	
	/**
	 * Constructor 
	 * @param args
	 */
	public Schelling(String[] args) {
		random = new Random(System.currentTimeMillis());
		ca = Generator.grid(gridSize,false,true);
		// ca = Generator.randomEuclideanGraph(500, 10, 100);
		ca.setAttribute("ui.stylesheet",defaultStyleSheet);
		ca.setAttribute("ui.quality");
		ca.setAttribute("ui.antialias");
		ca.display(false);
		compute();
		
	}
	

	

	/**
	 * Compute the Schelling segregation model.
	 * We get a given percentage of people of each color and 
	 * a given percentage of free spaces. 
	 * Initially red and blue people are randomly distributed over the space
	 * Then depending on a given threshold, a person 
	 * is comfortable if the proportion of neighbors of the other
	 * color is not greater than this threshold. 
	 */
	private void compute() {

		// Set the initial distribution of the population, red and blues. 
		// The sum of the two percentages should be less than 100 to leave some free spaces
		setInitialDistribution(redPercentage,bluePercentage);
		// list all the unsatisfied cells
		listUnsatisfiedCells();
		
		
		// loop while there are unsatisfied cells
	 	boolean everybodyIsSatisfied = false;
		while(!everybodyIsSatisfied) {
			// randomly choose one unsatisfied cell and a free space
			// remove them from the list of unsatisfied cells and free spaces
			System.out.println("nb of unsatisfied cells: "+unsatisfiedCells.size());
			System.out.println("nb of free spaces: "+freeSpaces.size());
			Node fromPosition = unsatisfiedCells.remove(random.nextInt(unsatisfiedCells.size()));			
			Node toPosition = freeSpaces.remove(random.nextInt(freeSpaces.size()));
			// swap their characteristics
			swap(fromPosition,toPosition);
			
			// update the satisfaction of the new cell with the 'ui.class' attribute
			// add the cell to the list of unsatisfied cells if it is not satisfied
			if(isSatisfied(toPosition)) {
				toPosition.setAttribute("ui.class",satisfiedClass);


			} else {
				toPosition.setAttribute("ui.class",unsatisfiedClass);
				unsatisfiedCells.add(toPosition);
			}

			// Update the satisfaction of neighbors of the moving cell
			// first for its old neighbors
			updateSatisfactionNeighbors(fromPosition);
			// then for its new neighbors
			updateSatisfactionNeighbors(toPosition);

			
			// pause for a while
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(unsatisfiedCells.isEmpty()) 
				everybodyIsSatisfied = true;
			else 
				System.out.println("nb of unsatisfied cells: "+unsatisfiedCells.size());
		}
		System.out.println("process is finished");
	}
	
	
	/**
	 * swap the characteristics of the cells the unsatisfied red or blue 
	 * with the free one. 
	 * @param fromPosition
	 * @param toPosition
	 */
	public void swap(Node fromPosition, Node toPosition) {
		freeSpaces.add(fromPosition);
		if((int)fromPosition.getAttribute("state") == RED) {
			toPosition.setAttribute("ui.style",redStyle);
			toPosition.setAttribute("state",RED);
		} else if((int)fromPosition.getAttribute("state") == BLUE){
			toPosition.setAttribute("ui.style",blueStyle);
			toPosition.setAttribute("state",BLUE);
		} 
		fromPosition.setAttribute("ui.style",freeStyle);
		fromPosition.setAttribute("state",FREE);
	}
	
	/**
	 * Update the satisfaction of the neighbors of a given node
	 * @param u
	 */
	public void updateSatisfactionNeighbors(Node u) {
		// for each neighbor of u
		// if the neighbor is satisfied and was in the list of unsatisfied cells
		// remove it from the list of unsatisfied cells
		// if the neighbor is not satisfied and was not in the list of unsatisfied cells
		// add it to the list of unsatisfied cells
		// update the 'ui.class' attribute of the neighbor
		u.neighborNodes().forEach(v -> {
			if(isSatisfied(v)) {
				if(unsatisfiedCells.contains(v)) {
					unsatisfiedCells.remove(v);
					v.setAttribute("ui.class",satisfiedClass);
				}
			} else {
				if(!unsatisfiedCells.contains(v)) {
					unsatisfiedCells.add(v);
					v.setAttribute("ui.class",unsatisfiedClass);
				}
			}
		});
	}
	
	
	
	/**
	 * List all the unsatisfied cells in the cellular automaton
	 */
	public void listUnsatisfiedCells() {
		ca.nodes().forEach(u -> {
			if(!isSatisfied(u)) {
				unsatisfiedCells.add(u);
				u.setAttribute("ui.class",unsatisfiedClass);
			}
		});
	}
	
	
	/**
	 * Check if a given node is satisfied.
	 * A node is unsatisfied if its neighborhood (excluding empty spaces)
	 * is composed by more than threshold% of nodes of a color different than its
	 * own color. 
	 * @param u the node to check
	 * @return
	 */
	public boolean isSatisfied(Node u) {
		int myState = (int)u.getAttribute("state");
		if(myState == FREE) {
			return true;
		}
		// Compute the number of neighbors of the other color
		// Compute the number of neighbors of the same color
		// if the number of neighbors of the other color is greater than the threshold
		// the node is unsatisfied (return false)
		// TODO
		return random.nextBoolean();
	}
	
	/**
	 * set the initial configuration of the population
	 * with given percentage of red/blue and free space.
	 * @param pred
	 * @param pblue
	 */
	public void setInitialDistribution(int pred, int pblue) {
		int nbOfRed = (ca.getNodeCount()*pred)/100;
		int nbOfBlue = (ca.getNodeCount()*pblue)/100;
		
		System.out.println("nb of red/blue:"+nbOfRed+"/"+nbOfBlue);
		
		for(int k=0;k<nbOfRed;k++) {
			Node u = Toolkit.randomNode(ca);
			u.setAttribute("state",RED);
			u.setAttribute("ui.style",redStyle);
		}

		for(int k=0;k<nbOfBlue;k++) {
			Node u = Toolkit.randomNode(ca);
			// I cannot choose a node that was already colored in red or blue
			while(u.hasAttribute("state")) u = Toolkit.randomNode(ca);
			u.setAttribute("state",BLUE);
			u.setAttribute("ui.style",blueStyle);
		}
		
		ca.nodes().forEach(v -> {
			if(!v.hasAttribute("state")) {
				v.setAttribute("state",FREE);
				v.setAttribute("ui.class","");
				v.setAttribute("ui.style",freeStyle);
				
				freeSpaces.add(v);
			}
		});
	}
	
	
	// ================ MAIN ==================
	
	public static void main(String[] args) {

        System.setProperty("org.graphstream.ui", "swing");
        new Schelling(args);
	}

}
