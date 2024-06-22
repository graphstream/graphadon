
---

# Static Graphs

---

##  Static Graphs Structures

- Nodes, Vertices
- Edges, Links (undirected) 
- Arcs (directed) 

![Simple Network](/img/simple-network.svg)


---


## Static Graphs Theory

- graph colouring problems
- routing problems
- flow problems
- covering problems
- sub-graphs problems


---

# Dynamic Graphs

---

## What kind of dynamics?

- values/weight on edges or nodes?
- nodes and/or edges added/removed?

![Weighted Network](/img/weighted-network.svg)

---

## What kind of algorithms?

- Computation time vs. Graph evolution?
  - As soon as it gets computed, the result has vanished
- Can we stop graph evolution and recompute?
  - It depends on the dynamic graph model

![Time-Varying Network](/img/varying-network.svg)


---

## Dynamic Graph Models

<div class="information">
Many Dynamic Graph models bounded to their application domain
</div>

- Is there a General model?
- What about a **Dynamic Graph Theory** (colouring, routing, flows, etc.)?

<!-- 
- Many graph models consider dynamics in some ways. But they are usually bounded to their application domain.
- Is there a general-enough model that can be used in a broad range of applications?
- What about a **Dynamic Graph Theory** with algorithms for colouring, routing, flows, etc.?
 
 -->
---

## Fields of Research

<!-- 3 columns with black vertical line as a separator -->
<div class="grid grid-cols-3">
  <div class="">
    <h3>Complex Networks</h3>
    <p>Analysis/Modeling of "real world" networks</p>
  </div>
 
  <div class="">
    <h3>Temporal Graphs</h3>
    <p>Time-labelled static graphs</p>
  </div>
  <div class="">
    <h3>Re-optimisation</h3>
    <p>Build and maintain structures</p>
  </div>
</div>



---

## Complex Networks

1. **Exploration**: Analysis of "real world" networks (web graphs, biological networks, social networks)

2. **Modelling**: Build artificial networks (Barabasi-Albert, Watts-Strogatz, Dorogovtsev-Mendes, Golomb
, etc.)

- Measures on graphs: community, distribution, dimensions, etc.
- Iterative Construction/Iteration: we see dynamic graphs here!

---

## Aggregative Methods

All the evolution is known **in advance**, the dynamic graph is aggregated into a static graph. (Temporal Networks, Evolving Graphs, Time-Varying Graphs, etc.)

Why? Because it allows the use of classical graph theory.

![Aggregative Methods](/img/temporal-network.svg)

---

## Re-optimisation

Build and maintain structures on dynamic graphs (e.g. spanning trees) **without** prior knowledge of the evolution.

**Hypothesis**: Updating an existing structure after some modifications is more effective that recomputing it from scratch.

![Re-optimization](/img/re-optimization.svg)

---

# GraphStream

**Study interaction networks and their dynamics**

- Dynamic Algorithms
- Dynamic Visualisation

A free and open-Source project supported by the University of Le Havre.


---

## In a nutshell

A Java library with a handy public API

```java
Graph g = new SingleGraph("MyGraph");
g.read("some-file.tlp");
System.out.println("Average Degree: "+g.getDegree());
g.display();
```

Based on an event model: Nodes and Edges are Added/Removed/Modified

Interaction with other tools

- Offline: several import / export file formats
- Online: through the API or through a network connection

---

## Architecture

### Public API <small>[graphstream-project.org/doc/API](http://graphstream-project.org/doc/API)</small>
- `org.graphstream`
    - `.graph`
    - `.stream`
    - `.ui`
    - `.algorithm`

### Organised into sub-projects <small>[github.com/graphstream](https://github.com/graphstream)</small>

`gs-core`, `gs-algo`, `gs-ui-swing`, `gs-ui-javafx`, `gs-ui-android`, `gs-netstream`, `gs-boids`, `gs-netlogo`, `gs-geography`, ...


---

## Get GraphStream!

- docs :  [graphstream-project.org](http://graphstream-project.org)
- sources :  [github.com/graphstream](https://github.com/graphstream)
- libraries :  [maven.org](https://search.maven.org/search?q=graphstream)
    ```xml
    <groupId>org.graphstream</groupId>
    <artifactId>gs-core</artifactId>
    <version>2.0</version>
    ```

---

## GraphStream's Event Model

The dynamics of the graph is expressed by an **event model**

- Addition or removal of nodes
- Addition or removal of edge
- Addition, update or removal of data attributes
- Time steps

A **stream of events** modifies the structure of a graph.

---

## GraphStream's Event Model

<div  style = " display: grid; grid-template-columns: repeat(3, 1fr); grid-auto-rows: 1fr;">
  <div class="flex  p-5 flex-col items-center ">
    <h3>Sources</h3>
    <p>Produce a stream of events</p>
     <div >
    <img src="/img/source.svg" alt="Sources" class="mb-0" style="height: 12vh">
  </div>
    </div>
    <div class="flex p-5 flex-col items-center ">
    <h3>Sinks</h3>
    <p>Receive a stream of events</p>
    <div ><img src="/img/sink.svg" alt="Sinks" class="mb-0" style="height: 12vh"/></div>
    </div>
    <div class="flex p-5 flex-col items-center ">
    <h3>Pipes</h3>
    <p class="text-centered">Both source and sink</p>
    <div class=""><img src="/img/pipe.svg" alt="Pipes" class="mb-0" style="height: 12vh"/></div>
    <p>A <b>graph</b> is a <b>pipe</b></p>
    </div>
</div>

---


## Pipelining

Sources send events to sinks.

- Observer design pattern
- Publish / Subscribe messaging pattern
- Java Swing listeners

**Sources**, **pipes** and **sinks** can be connected to form **pipelines**.

<div class="flex items-center justify-center">
  <img src="/img/pipeline.svg" alt="File-Graph-Viewer Pipeline" style="height: 15vh" />
</div>

---

## Pipelining

```java
Graph graph = new SingleGraph("Some Graph");
graph.display();
FileSource source = new FileSourceDGS();
source.addSink( graph );
source.begin("someFile.dgs");
while( source.nextEvents() ){
  // Do whatever between two events
}
source.end();
```

<div class="flex items-center justify-center ">
  <img src="/img/pipeline.svg" alt="File-Graph-Viewer Pipeline" style="height: 15vh" />
</div>
---

## Pipelining

The stream of events can flow between sources and sinks:

- across the network,
- processes,
- threads.

For example a viewer can run in a distinct thread or machine, while a simulation on a graph runs on another.

<div class="flex items-center justify-center ">
  <img src="/img/proxy-pipe.svg" alt="Proxy Pipe" style="height: 12vh" />
</div>


---

## Pipelining

Receive events from some other process/thread/programme/machine

```java
Graph g = new SingleGraph("RWP");
ThreadProxyPipe pipe = getPipeFromElsewhere(); //fake function
pipe.addSink(g);
g.display(false);

while (true) {
  pipe.pump();
  Thread.sleep(1);
}
```

---

## Graph components

### Various graph structures

- Single graph (1-graph),
- Multi-graph (p-graph), graphs where several edges can connect two given nodes.
- Directed and/or undirected graphs.

### Several internal representations

- fast data retrieval,
- data compactness.

Representation of a graph at a given time (static). But this representation can evolve.

---

## Data Attributes

- Any number of data attributes can be associated with any element of the graph.
- An attribute is a **key**, **value** pair that can be any Java Object.
- Attributes can be placed on nodes, edges and on the graph itself.

```java
g.setAttribute("My attribute", aValue);
Node n = g.getNode("A");
n.setAttribute("xy", 23.4, 55.0);
Edge e = g.getEdge("AB");
e.removeAttribute("selected");
double w = e.getNumber("weight");
```

---

## Algorithms


<div class="flex flex-row items-center ">
<div class="flex  p-5 flex-col items-center" >
<h3>Searches</h3>
<p>random searches, shortest paths, spanning trees, etc.</p>
</div>

<div class="flex  p-5 flex-col items-center">
<h3>Metrics</h3>
<p>modularity, centrality, degree distributions, connectivity, density, etc.</p>
</div>

<div class="flex  p-5 flex-col items-center">
<h3>Generators</h3>
<p>random, regular, preferential attachment, small world, from GIS, from the web, etc.</p>
</div>
</div>



---

## Focus on Dynamic Connected Components

```java
import org.graphstream.algorithm.ConnectedComponents;
//...
ConnectedComponents cc = new ConnectedComponents();
cc.init(graph);
while(something) {
  cc.getConnectedComponentsCount();
  canDoSomethingWithGraph();
}
```

---

## Focus on Dynamic Shortest Paths

```java
import org.graphstream.algorithm
          .networksimplex.DynamicOneToAllShortestPath;
//...
DynamicOneToAllShortestPath algorithm =
                new DynamicOneToAllShortestPath(null);
algorithm.init(graph);
algorithm.setSource("0");
while(something) {
  algorithm.compute();
  canDoSomethingWithGraph();
}
```

---

## Algorithms

[graphstream-project.org/doc/Algorithms/](http://graphstream-project.org/doc/Algorithms/)



---

## Visualization

1. Dynamic Visualization: the graph is evolving, so does the visualization.
2. Get more information than the graph itself: sprites.

<div class="flex flex-row items-center ">

<div class="">
  <img src="/img/CSS.png" alt="CSS" />
</div>
<!-- <div>
 <video controls autoplay loop> <source data-src="/img/boids.mp4" type="video/mp4"></video>
</div> -->
</div>

---

## Extra visual information

### CSS

```css
graph { padding: 50px; }
node {
  size-mode: fit; shape: rounded-box;
  fill-color: white; stroke-mode: plain;
  padding: 5px, 4px; icon-mode: at-left;
  icon: url('data/Smiley_032.png');
}
```

<div class="flex flex-row items-center ">
<div class="">
  <img src="/img/size_mode2_.png" alt="CSS1" />
</div>
<div class="">
  <img src="/img/stroke_mode2.png" alt="CSS2" />
</div>
<div class="">
  <img src="/img/edge_shape1.png" alt="CSS3" />
</div>
<div class="">
  <img src="/img/node_shape6.png" alt="CSS4" />
</div>
</div>

<!-- ![css1](/img/size_mode2_.png)![css2](/img/stroke_mode2.png)![css3](/img/edge_shape1.png)![css4](/img/node_shape6.png) -->

---


## Extra visual information

### CSS classes

```java
graph.setAttribute("ui.stylesheet",
  "graph {padding : 50px;}"
  + "node {size: 100px; fill-mode: image-scaled;}"
  + "node.fun {fill-image: url('fun.gif');}"
  + "node.dull {fill-image: url('dull.png');}");
Node a = graph.addNode("A");
Node b = graph.addNode("B");
Node c = graph.addNode("C");
a.setAttribute("ui.class", "fun");
b.setAttribute("ui.class", "fun");
c.setAttribute("ui.class", "dull");

```

---


## Extra visual information

### CSS classes

<img src="/img/funordull.png" alt="...and of course it is dynamic" style="height:35vh"/>


---

## Extra visual information

### Sprites

Graphical objects that give extra information on the application you deal with.

```java
SpriteManager sman = new SpriteManager(graph);
Sprite pin = sman.addSprite("pin");
```

<p> Sprites are also customised with CSS <img src="/img/mapPinSmall.png" alt="Pin" style="display:inline ; height:5vh"/></p> 

```css
sprite#pin {
    shape: box;
    size: 32px, 52px;
    fill-mode: image-scaled;
    fill-image: url('mapPinSmall.png');
}
```

---

## Sprites

<section data-background=" white">
<img alt="LH" src="/img/leHavreStep2.png" class="max-h-100"/>
</section>


---

## Sprites

<section data-background=" white">
<img alt="LH" src="/img/leHavreStep7.png" class="max-h-100"/>
</section>


---

## Interactions with other Tools


---


## Offline interactions

### File formats

Tulip, Gephi, GML, Pajek, DOT, LGL, ncol, DGS

```java
DGS004
"graph.dgs" 0 0
an A x:1 y:2.3 label:"Node A"
an B x:0 y:0
an C xy:2.3,1
ae AB A B weight:23.3
ae AC A C weight:2
st 1.0
ae BC B > C
st 1.1
dn A
```

---

## OnLine interactions

### NetStream

- Export streams of events to other applications / machines / languages
- Both ways. From GS to other and from other to GS
- Binary network protocol
- TCP socket (and WebSocket) implementation
- Several languages (Java, C++, Python, JS)

---

## NetLogo Extension

- NetLogo agents (turtles, links and observer) send graph events to external application
- The external application maintains a dynamic graph and runs algorithms on it
- It sends the computed results back to NetLogo
- Agents can receive and use them to take their decisions

![NetLogo Extension](/img/netlogo.jpg)
