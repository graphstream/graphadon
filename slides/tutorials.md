---
theme : ./
---
<p class="text-[70px] leading-[1.4] font-bold"> Dynamic Graphs </p>

<p class="text-[40px] "> Tutorials </p>

<p class="text-[40px] leading-[1.4] "> Graphadon Summer School </p>

---

## Outline

<Toc maxDepth=1 />

---


## Installation

- Download the tutorial sources
- Get an IDE (Eclipse, IntelliJ, NetBeans, etc.)
- Open the tutorial sources in the IDE

---


## Get the tutorial sources
- Go to the tutorial page at github:
[github.com/graphstream/graphadon](https://github.com/graphstream/graphadon)
- Get the code:
  - click on the green "Code" button on the right, then "Download ZIP"
  - or through git:
	```sh
	git clone https://github.com/graphstream/graphadon.git
	```

In that project, we want the `Demos/` folder.


---


## Get Eclipse Installed

- Go to the [Eclipse download page](https://www.eclipse.org/downloads/packages/release/2024-06/r/eclipse-ide-java-developers)
- On the right column, "Download Links", pick up your OS
- Click the download button
- Once downloaded, open the file. An eclipse folder is created
- Start Eclipse

---


## Open the workspace in the IDE

- When asked about your workspace, choose a new one or select an existing workspace.
  
- Navigate to `File > Import > Maven > Existing Maven Projects` in the menu.

- Locate your project in the `Demos/` folder. Eclipse will automatically recognize the project.

<!-- centered image,controle the width, keep the aspect ratio -->
  <img src="/img/eclipse-import-maven-projects.png" alt="Import Maven Projects in Eclipse" class="w-100" />


---

## Binaries and Compilation (with Maven)

```xml
<!-- ... -->
<groupId>org.graphstream</groupId>
<artifactId>gs-core</artifactId>
<version>2.0</version>
<!-- ... -->
<groupId>org.graphstream</groupId>
<artifactId>gs-algo</artifactId>
<version>2.0</version>
<!-- ... -->
<groupId>org.graphstream</groupId>
<artifactId>gs-ui-swing</artifactId>
<version>2.0</version>
```

- Eclipse or other IDE will compile the project automatically.
- Or on the command line : `mvn compile`

---

## Binaries and compilation (without Maven)

<p class="text-4xl text-centered">😫</p>

Binaries on the GraphStream Download page  [graphstream-project.org/download/](http://graphstream-project.org/download/) :

- `gs-algo-2.0.jar`, `gs-core-2.0.jar`, `gs-ui-swing-2.0.jar`
- Copy them in the `lib/` folder of the `Demo` project.
- On the command line:
	
	`cd /path/to/graphadon/Demo`

	`javac -cp lib/gs-core-2.0.jar:lib/gs-algo-2.0.jar:lib/gs-ui-swing-2.0.jar  src/org/graphstream/tutorial/tutorial1/Tutorial1.java`


---

# Tutorial 1

### Basic tasks with GraphStream

---

## Create and display

- Open
`src/org/graphstream/demo/tutorial1/Tutorial1.java`

```java
public class Tutorial1 {
	public static void main(String args[]) {
		Graph graph = new SingleGraph("Tutorial 1");
		graph.display();
		graph.addNode("A");
		graph.addNode("B");
		graph.addEdge("AB", "A", "B");
		graph.addNode("C");
		graph.addEdge("BC", "B", "C", true); // Directed edge.
		graph.addEdge("CA", "C", "A");
	}
}
```

- Maven : `mvn exec:java -Dexec.mainClass="org.graphstream.demo.tutorial1.Tutorial1"`
- command line : `java -cp lib/gs-core-2.0.jar:lib/gs-algo-2.0.jar:lib/gs-ui-swing-2.0.jar:src  org.graphstream.tutorial.tutorial1.Tutorial1`

---


## Change the Display with CSS

We can improve the display with some CSS:

```java{4-10}
...
graph.display();

graph.setAttribute("ui.quality");
graph.setAttribute("ui.antialias");
graph.setAttribute("ui.stylesheet", "" +
					"edge {" +
					"   size: 3px;" +
					"   fill-color: #D88;" +
					"}");

...
```
---


## Access Elements

- Each node, edge and attribute is identified by an unique string.
- The node and edge elements are created for you.
- You can access them however, when created: `Node n = graph.addNode("A");`
- Or after creation: `Node n = graph.getNode("A");`

---


## Constructive API vs. Events

- You can remove nodes and edges the same way: `graph.removeNode("A");`
- You can change the graph this way at any time. Each change is considered as an “event”.
- The sequence of changes is seen as the dynamics of the graph.
- There are many other ways to modify the graph.

---


## Attributes

Data stored in the graph, on nodes and edges, are called “attributes”.
An attribute is a pair (name,value).

```java
Edge ab = graph.getEdge("AB");
Edge bc = graph.getEdge("BC");
Edge ca = graph.getEdge("CA");

ab.setAttribute("ui.label", "AB");
bc.setAttribute("ui.label", "BC");
ca.setAttribute("ui.label", "CA");

for(String id : new String[]{"A", "B", "C"}){
    graph.getNode(id).setAttribute("ui.label", id);
}
graph.setAttribute("ui.stylesheet", "url(data/style.css);");
```

---


## Define Attributes

- But you can add any kind of data on each graph element.
- However not all attributes appear in the viewer.
- Notice the way you can add arrays with `setAttribute()` and a variable number of arguments:

```java
ab.setAttribute("aNumber", 10);
bc.setAttribute("anObject", new Double(10));
ca.setAttribute("anArrayOfThings", 1, 2, 3);
```


---



## Retrieve Attributes

Several ways to retrieve attributes:

```java
int value1 = ((Number) ab.getAttribute("aNumber")).intValue();
double value2 = bc.getAttribute("anObject");
Object[] value3 = ca.getAttribute("anArrayOfThings");

```
Special methods are here to simplify things:

```java
double value4 = ab.getNumber("aNumber");
double value5 = bc.getNumber("anObject");

```

---


## Traversing the graph

Iterating through all nodes of the graph is easy:

```java
graph.nodes().forEach(n -> System.out.println(n.getId()));
```

Equally for edges:

```java
graph.edges().forEach(e -> System.out.println(e.getId()));
```

---

## Index-based  access

Indices for nodes:

```java
int n = graph.getNodeCount();
for(int i=0; i<n; i++) {
	System.out.println(graph.getNode(i).getId());
}

```

Indices for edges:

```java
int n = graph.getEdgeCount();
for(int i=0; i<n; i++) {
	System.out.println(graph.getEdge(i).getId());
}

```
⚠ indices remain the same as long as the graph is unchanged. But as soon as an addition or removal occurs, indices are no longer tied to their old node or edge ⚠


---


## Traverse from nodes and edges

You can also travel the graph using nodes:

```java
import static org.graphstream.algorithm.Toolkit.*;
//...
Node node = randomNode(graph);

node.getEachLeavingEdge().forEach({
	System.out.printf("neighbor %s via %s%n",
		e.getOpposite(node).getId(),
		e.getId() );
});
```


- Each node and edge allow to iterate on their neighbourhood.
- `Toolkit` is set of often used functions and small algorithms (see the [API](http://graphstream-project.org/gs-algo/org/graphstream/algorithm/Toolkit.html)).


---


#  Tutorial 2


### A first dynamic graph

---

## Sinks
- A graph can receive events. It is a _sink_.
- A _sink_  is connected to a _source_ using the `Source.addSink(Sink)` method.
- Events are filtered by type (_Elements Events_ and _Attributes Events_) :
    - `addElementSink(ElementSink)`.  Nodes and edges are _Elements_.
    - `setAttributeSink(AttributeSink)`. Data attributes are stored on every element.
- A `Sink` is both an `ElementSink` and `AttributeSink`.


---


## ElementSink

ElementSink is an interface
```java
public interface ElementSink {
	void nodeAdded( ... );
	void nodeRemoved( ... );
	void edgeAdded( ... );
	void edgeRemoved( ... );
	void graphCleared( ... );
	void stepBegins( ... );
}
```

---


## AttributeSink

An attribute sink must follow the interface:

```java
public interface AttributeSink {
	void graphAttributeAdded( ... );
	void graphAttributeChanged( ... );
	void graphAttributeRemoved( ... );

	void nodeAttributeAdded( ... );
	void nodeAttributeChanged( ... );
	void nodeAttributeRemoved( ... );

	void edgeAttributeAdded( ... );
	void edgeAttributeChanged( ... );
	void edgeAttributeRemoved( ... );
}
```

---


## Source
A source is an interface that only defines methods to handle a set of sinks.

```java
public interface Source {
	void addSink(Sink sink);
	void removeSink(Sink sink);
	void setAttributeSink(AttributeSink sink);
	void removeAttributeSink(AttributeSink sink);
	void addElementSink(ElementSink sink);
	void removeElementSink(ElementSink sink);
	void clearElementSinks();
	void clearAttributeSinks();
	void clearSinks();
}

```

---


## A first dynamic graph

Since Graph is a _sink_ let's create a graph from a set of events generated by a _source_.

- A file with  information about graphs (in a proper file format) can be a source of events.
- A few graph file formats can handle dynamic.
- GraphStream provides a file format (DGS) that allows to store and load dynamic graphs.

![A graph from a file](/img/pipeline.svg)

---

## The GDS File Format

- `an` for "add node".
- `ae` for "add edge".
- `ae "AB" "A" > "B"` adds a directed edge between nodes `A` and `B`.
- `cn`, `ce` and `cg` change or add one or more attributes on a node, an edge or the graph.
- `dn` and `de` allow to remove nodes, edges.


Open the example DGS file in  `data/tutorial2.dgs`.

```c
DGS003
"Tutorial 2" 0 0
an "A"
an "B"
an "C"
ae "AB" "A" "B"
...
```


---

## How to handle dynamics
- Storing temporal information is tricky.
- Timestamps on events is a  good way to encode time
- But some events occur at the same time.
- Let's define _time steps_ within events
- `st <number>`

---


## Steps in DGS

The ability to remove nodes and edges make the format dynamic.

Add this to the  `data/tutorial2.dgs`  file:
```c
st 2
an "D" label="D"
an "E" label="E"
ae "BD" "B" "D" label="BD"
ae "CE" "C" "E" label="CE"
ae "DE" "D" "E" label="DE"
st 3
de "AB"
st 4
dn "A"
```
And save it.

---


## Read the whole file

The file can be read entirely :

```java
graph.read("tutorial2.dgs");
```

- However this will send all events as fast as possible.
- We have no control over the speed at which events occur.


---

## Read the file event by event
We can read the DGS file event by event using an input source:
```java
Graph graph = new SingleGraph("Tutorial2");
graph.display();
FileSource source = new FileSourceDGS();
source.addSink( graph );
source.begin("data/tutorial2.dgs");
while( source.nextEvents() );
source.end();
```

![FileSource Pipeline](/img/pipeline.svg)


---

## Read the file step by step
- We read the file event by event (line by line in the file), however it still does it as fast as it can.
- Note the line `while(source.nextEvents());`
- Also note that we have to call the `begin()` and `end()` methods before and after reading to cleanly open and close the file.
- Let's slow down the process :
``` java
while(source.nextEvents()) { Thread.sleep(1000); }
```
- We can also run it step by step so that events between two step appear together
```java
while(source.nextStep()) { Thread.sleep(1000); }
```

---


## Graph Layout

- Default: Node positions are automatically computed.
- Custom: Manually position nodes.
- Use `x` and `y` attributes to set positions.


```c {3-6,10-11}
an "C"
cn "C" label="C"
cn "A" x=0 y=0.86
cn "B" x=1 y=-1
cn "C" x=-1 y=-1
// ...
st 2
an "D" label="D"
an "E" label="E"
cn "D" x=1 y=1
cn "E" x=-1 y=1
```

- To disable automatic positioning:
  ```java
  graph.display(false);
  ```

  