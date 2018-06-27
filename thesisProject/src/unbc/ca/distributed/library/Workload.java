/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.MyGraph;
import unbc.ca.distributed.graph.elements.CreateHyperCube;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class Workload {

    private int noOfProcessor;
    private String name;
    private int id;

    private Generator weightDistribution;

    private String topology;
    private int simulationTime;

    private int chords;
    private int rows;
    private int columns;
    private LinkedHashMap<Integer, LinkedHashMap<String, Generator>> workLoaddistributionCollection = new LinkedHashMap<>();
    private boolean constant = false;
    
    private boolean constantForHopProcessing = false;
    
    
    private String inter,Cs, delay, interM,CsM, delayM, interV,CsV, delayV;
    
    private double interQ;

    public boolean isConstantForHopProcessing() {
        return constantForHopProcessing;
    }

    public void setConstantForHopProcessing(boolean constantForHopProcessing) {
        this.constantForHopProcessing = constantForHopProcessing;
    }        

    public double getInterQ() {
        return interQ;
    }

    public void setInterQ(double interQ) {
        this.interQ = interQ;
    }        

    public Workload()
    {}
    public Workload(String inter, String interM, String interV, String Cs, String CsM, String CsV, String delay, String delayM, String delayV)
    {
        this.inter = inter;
        this.Cs = Cs;
        this.delay = delay;
        
        this.interV = interV;
        this.CsV = CsV;
        this.delayV = delayV;
        
        this.interM = interM;
        this.CsM = CsM;
        this.delayM = delayM;

        
     
    }
    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public LinkedHashMap<Integer, LinkedHashMap<String, Generator>> getWorkLoaddistributionCollection() {
        return workLoaddistributionCollection;
    }

    public Generator getWeightDistribution() {
        return weightDistribution;
    }

    public int getWeightValue() {
        if (isConstant()) {
            return 1;
        } else {
            return (int) Math.round(weightDistribution.generate());
        }
    }

    public void setWeightDistribution(Generator weightDistribution) {
        this.weightDistribution = weightDistribution;
    }

    public int getChords() {
        return chords;
    }

    public void setChords(int chords) {
        this.chords = chords;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public String getTopology() {
        return topology;
    }

    public void setTopology(String topology) {
        this.topology = topology;
    }

    public int getNoOfProcessor() {
        return noOfProcessor;
    }

    public void setNoOfProcessor(int noOfProcessor) {
        this.noOfProcessor = noOfProcessor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private MyGraph<Vertex, Edge> graphObject;
    private AbstractLayout<Vertex, Edge> layout;
    private Map<Integer, Vertex> simulationNodes = new ConcurrentHashMap<>();
    private Map<Integer, Edge> simulationEdges = new ConcurrentHashMap<>();
    private int simulationNodeCount = 0;
    private int simulationEdgeCount = 0;

    public int getSimulationNodeCount() {
        return simulationNodeCount;
    }

    public void incSimulationNodeCount() {
        this.simulationNodeCount++;
    }

    public int getSimulationEdgeCount() {
        return simulationEdgeCount;
    }

    public void incSimulationEdgeCount() {
        this.simulationEdgeCount++;
    }

    public Map<Integer, Vertex> getSimulationNodes() {
        Map<Integer, Vertex> clone = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();
            clone.put(integer, vertex);
        }
        return clone;
    }

    public Map<Integer, Vertex> getSimulationNodesClone() {
        Map<Integer, Vertex> clone = new ConcurrentHashMap<>();
        for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();
            clone.put(integer, vertex);
        }
        return clone;
    }

    public void setSimulationNodes(Map<Integer, Vertex> simulationNodes) {
        this.simulationNodes = simulationNodes;
    }

    public Map<Integer, Edge> getSimulationEdges() {
        Map<Integer, Edge> clone = new ConcurrentHashMap<>();

        for (Map.Entry<Integer, Edge> entry : simulationEdges.entrySet()) {
            Integer integer = entry.getKey();
            Edge edge = entry.getValue();
            clone.put(integer, edge);
        }

        return clone;
    }

    public Map<Integer, Edge> getSimulationEdgesClone() {
        Map<Integer, Edge> clone = new ConcurrentHashMap<>();

        for (Map.Entry<Integer, Edge> entry : simulationEdges.entrySet()) {
            Integer integer = entry.getKey();
            Edge edge = entry.getValue();
            clone.put(integer, edge);
        }

        return clone;
    }

    public void setSimulationEdges(Map<Integer, Edge> simulationEdges) {
        this.simulationEdges = simulationEdges;
    }

    public AbstractLayout<Vertex, Edge> getLayout() {
        return layout;
    }

    public MyGraph getGraphObject() {
        if (simulationNodeCount == 0) {
            switch (getTopology()) {
                case "Ring":
                    graphObject = circleGraph();
                    break;
                case "Full Connected":
                    graphObject = fullGraph();
                    break;
                case "Star":
                    graphObject = starGraph();
                    updateNodePositions();

                    Point2D p = ObjectFactory.getMainFrame().getCenterPoint(new Point((int) simulationNodes.get(1).getX(),
                            (int) simulationNodes.get(1).getY()),
                            new Point((int) simulationNodes.get(2).getX(),
                                    (int) simulationNodes.get(2).getY()),
                            new Point((int) simulationNodes.get(3).getX(),
                                    (int) simulationNodes.get(3).getY()));

                    updateModelNodePositions(simulationNodes.get(getNoOfProcessor()), p);

                    for (int i = 1; i <= getNoOfProcessor() - 1; i++) {
                        Edge e = new Edge(getWeightValue());
                        simulationEdgeCount++;
                        e.setId(simulationEdgeCount);

                        simulationEdges.put(simulationEdgeCount, e);

                        graphObject.addEdge(e, simulationNodes.get(getNoOfProcessor()), simulationNodes.get(i));

                    }
                    break;
                case "Tree":
                    graphObject = treeGraph();
                    layout = getLayoutCircle(graphObject);
                    for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
                        Vertex vertex = entry.getValue();
                        layout.setLocation(vertex, vertex.getX(), vertex.getY());
                    }
                    break;
                case "Chord":
                    graphObject = chord();
                    break;
                case "Grid":
                    graphObject = grid();
                    layout = getLayoutCircle(graphObject);
                    for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
                        Vertex vertex = entry.getValue();
                        layout.setLocation(vertex, vertex.getX(), vertex.getY());
                    }
                    break;
                case "Random":
                    graphObject = random();
                    layout = getLayoutCircle(graphObject);
                    for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
                        Vertex vertex = entry.getValue();
                        layout.setLocation(vertex, vertex.getX(), vertex.getY());
                    }
                    break;
                case "Hypercube":
                    graphObject = hypercubeGraph();
                    layout = getLayoutCircle(graphObject);
                    /* Updating the coordinates in the layout */
                    for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
                        Vertex vertex = entry.getValue();
                        layout.setLocation(vertex, vertex.getX(), vertex.getY());
                    }
                    break;
                case "Bus":
                    graphObject = busGraph();
                    break;
            }
        }

        return graphObject;
    }

    private MyGraph<Vertex, Edge> hypercubeGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        CreateHyperCube m = new CreateHyperCube(getNoOfProcessor(), g, getWeightDistribution(), simulationNodes, simulationNodeCount, simulationEdges, simulationEdgeCount, constant);
        int numberOF = simulationNodeCount = m.createCube();
        
        
        for (int i = 1; i <= numberOF; i++) {            
            Generator ir = Utilites.returnDistribution(inter,
                    Integer.parseInt(interM), Double.parseDouble(interV));
            Generator cs = Utilites.returnDistribution(Cs,
                    Integer.parseInt(CsM), Double.parseDouble(CsV));
            Generator dp = Utilites.returnDistribution(delay,
                    Integer.parseInt(delayM), Double.parseDouble(delayV));
            LinkedHashMap<String, Generator> workD = new LinkedHashMap<>();

            workD.put("Critical Section", cs);
            workD.put("Inter Request", ir);
            workD.put("Hop Processing", dp);

            getWorkLoaddistributionCollection().put(i, workD);
        }

        return g;
    }

    public MyGraph returnMeGraph() {
        return graphObject;
    }

    private MyGraph<Vertex, Edge> circleGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int numberOfNodes = getNoOfProcessor();
        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            simulationNodeCount++;

            n1.setLabel(String.valueOf(simulationNodeCount));
            simulationNodes.put(simulationNodeCount, n1);
            g.addVertex(n1);
        }

        for (int i = 1; i <= numberOfNodes; i++) {

            Edge e = new Edge(getWeightValue());
            simulationEdgeCount++;

            e.setId(simulationEdgeCount);
            simulationEdges.put(simulationEdgeCount, e);

            if (i != numberOfNodes) {
                g.addEdge(e, simulationNodes.get(i), simulationNodes.get(i + 1));
            } else if (i == numberOfNodes) {
                g.addEdge(e, simulationNodes.get(numberOfNodes), simulationNodes.get(1));
            }
        }

        layout = getLayoutCircle(g);

        return g;
    }
    private MyGraph<Vertex, Edge> busGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int numberOfNodes = getNoOfProcessor();
        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            simulationNodeCount++;

            n1.setLabel(String.valueOf(simulationNodeCount));
            simulationNodes.put(simulationNodeCount, n1);
            g.addVertex(n1);
        }

        for (int i = 1; i < numberOfNodes; i++) {

            Edge e = new Edge(getWeightValue());
            simulationEdgeCount++;

            e.setId(simulationEdgeCount);
            simulationEdges.put(simulationEdgeCount, e);

            if (i != numberOfNodes) {
                g.addEdge(e, simulationNodes.get(i), simulationNodes.get(i + 1));
            }
        }

        layout = getLayoutCircle(g);

        return g;
    }

    private MyGraph<Vertex, Edge> fullGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int numberOfNodes = getNoOfProcessor();
        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            simulationNodeCount++;

            n1.setLabel(String.valueOf(simulationNodeCount));
            simulationNodes.put(simulationNodeCount, n1);
            g.addVertex(n1);
        }

        for (Vertex n1 : g.getVertices()) {
            for (Vertex n2 : g.getVertices()) {
                if (n1 != n2) {
                    if (g.findEdge(n2, n1) == null) {
                        Edge e = new Edge(getWeightValue());
                        simulationEdgeCount++;

                        e.setId(simulationEdgeCount);
                        simulationEdges.put(simulationEdgeCount, e);

                        g.addEdge(e, n1, n2);
                    }
                }
            }

        }
        layout = getLayoutCircle(g);
        return g;
    }

    public MyGraph<Vertex, Edge> starGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int numberOfNodes = getNoOfProcessor();
        for (int i = 1; i < numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            simulationNodeCount++;
            n1.setLabel(String.valueOf(simulationNodeCount));
            simulationNodes.put(simulationNodeCount, n1);

            g.addVertex(n1);
        }

        Vertex center = new Vertex();
        simulationNodeCount++;
        center.setLabel(String.valueOf(numberOfNodes));
        simulationNodes.put(numberOfNodes, center);
        g.addVertex(center);

        layout = getLayoutCircle(g);

        return g;
    }

    private void updateModelNodePositions(Vertex nodeChanged, Point2D pointL) {
        layout.setLocation(nodeChanged, pointL);
    }

    public MyGraph<Vertex, Edge> treeGraph() {
        int distant = 150;
        int width = ObjectFactory.getMainFrame().widthOfDrawPanel;
        int middle = width / 2;
        int high = 100;
        int diameter = 100;
        int number = getNoOfProcessor();

        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 0; i < number; i++) {
            int amount = (int) Math.pow(2, i);

            for (int j = 0; j < amount; j++) {
                Vertex n1 = new Vertex();
                simulationNodeCount++;

                int x = middle - diameter / 2 - distant * ((int) Math.pow(2, number - i - 1)) * (amount - 2 * j - 1) / 2;
                int y = high * (i + 1);

                n1.setPosition((double) x, (double) y);

                n1.setLabel(String.valueOf(simulationNodeCount));
                simulationNodes.put(simulationNodeCount, n1);
                g.addVertex(n1);
            }
        }

        for (int i = 1; i < (int) Math.pow(2, number - 1); i++) {
            int index = i - 1;

            /* Creating left edge for nodes */
            Edge leftEdge = new Edge(getWeightValue());
            simulationEdgeCount++;
            leftEdge.setId(simulationEdgeCount);
            simulationEdges.put(simulationEdgeCount, leftEdge);

            int left = (index * 2 + 1) + 1;
            g.addEdge(leftEdge, simulationNodes.get(index + 1), simulationNodes.get(left));

            /* Creating Right edge for nodes */
            Edge rightEdge = new Edge(getWeightValue());
            simulationEdgeCount++;
            rightEdge.setId(simulationEdgeCount);
            simulationEdges.put(simulationEdgeCount, rightEdge);

            int right = (index * 2 + 2) + 1;

            g.addEdge(rightEdge, simulationNodes.get(index + 1), simulationNodes.get(right));
        }

        return g;
    }

    private CircleLayout<Vertex, Edge> getLayoutCircle(Graph<Vertex, Edge> graph) {

        CircleLayout<Vertex, Edge> localLayout = new CircleLayout<>(graph);
        localLayout.setSize(new Dimension(ObjectFactory.getMainFrame().widthOfDrawPanel, ObjectFactory.getMainFrame().heightOfDrawPanel));
        return localLayout;
    }

    private void updateNodePositions() {
        for (Vertex node : graphObject.getVertices()) {

            double x = layout.getX(node);
            double y = layout.getY(node);

            node.setPosition(x, y);
        }
    }

    private MyGraph<Vertex, Edge> chord() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int noOfProcessorLocal = getNoOfProcessor();
        int chordsLocal = getRows();

        for (int i = 1; i <= noOfProcessorLocal; i++) {
            Vertex n1 = new Vertex();
            simulationNodeCount++;

            n1.setLabel(String.valueOf(simulationNodeCount));
            simulationNodes.put(simulationNodeCount, n1);
            g.addVertex(n1);
        }

        System.out.println("Number of nodes added " + simulationNodeCount);

        Vector chordLengths = new Vector();
        chordLengths.add(new Integer(1)); // outer ring always present

        StringTokenizer st = new StringTokenizer(String.valueOf(chordsLocal), ",");
        while (st.hasMoreTokens()) {
            chordLengths.add(new Integer(st.nextToken()));
        }

        int mirrors = 0;
        if (Math.IEEEremainder((double) noOfProcessorLocal, 2) == 0 && chordLengths.contains(new Integer(noOfProcessorLocal / 2))) {
            mirrors = noOfProcessorLocal / 2;
        }

        // Create links
        Vertex targetNode, anchorNode;
        int size = chordLengths.size();
        int index;
        for (int i = 1; i <= noOfProcessorLocal; i++) {
            anchorNode = simulationNodes.get(i);
            for (int j = 0; j < size; j++) {
                if (mirrors > 0 && (i >= noOfProcessorLocal / 2) && ((Integer) chordLengths.get(j)).intValue() == noOfProcessorLocal / 2) {
                } else {
                    index = i + ((Integer) chordLengths.get(j)).intValue();
                    if (index >= noOfProcessorLocal) {
                        index = index - noOfProcessorLocal;
                    }
                    targetNode = simulationNodes.get(index + 1);

                    Edge e = new Edge(getWeightValue());
                    simulationEdgeCount++;
                    e.setSource(anchorNode);
                    e.setDestination(targetNode);

                    e.setId(simulationEdgeCount);
                    simulationEdges.put(simulationEdgeCount, e);
                    g.addEdge(e, anchorNode, targetNode);
                }
            }
        }
        System.out.println("Number of nodes added " + simulationEdgeCount);
        layout = getLayoutCircle(g);
        return g;
    }

    private MyGraph<Vertex, Edge> grid() {
        int rowsLocal = getRows();
        int cols = getColumns();

        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int totalNodes = rowsLocal * cols;

        for (int i = 1; i <= totalNodes; i++) {
            Vertex n1 = new Vertex();
            simulationNodeCount++;

            n1.setLabel(String.valueOf(simulationNodeCount));
            simulationNodes.put(simulationNodeCount, n1);

            g.addVertex(n1);
        }

        double dif_x = (3 * ObjectFactory.getMainFrame().widthOfDrawPanel) / (4 * (cols - 1));
        double dif_y = (3 * ObjectFactory.getMainFrame().heightOfDrawPanel) / (4 * (rowsLocal - 1));

        double x = ObjectFactory.getMainFrame().widthOfDrawPanel / 8 - Configuration.radius / 2;
        double y = ObjectFactory.getMainFrame().heightOfDrawPanel / 8 - Configuration.radius / 2;

        //double this_x, this_y;       
        double this_x, this_y;
        int number = 1;
        for (int i = 0; i < rowsLocal; i++) {
            for (int j = 0; j < cols; j++) {
                this_x = x + j * dif_x;
                this_y = y + i * dif_y;
                simulationNodes.get(number).setPosition(this_x, this_y);
                number++;
            }
        }

        for (int i = 1; i <= totalNodes; i++) {
            Edge e;
            if (i <= totalNodes - cols) {
                e = new Edge(getWeightValue());
                simulationEdgeCount++;
                e.setId(simulationEdgeCount);

                Vertex n1 = simulationNodes.get(i);
                Vertex n2 = simulationNodes.get(i + cols);

                e.setSource(n1);
                e.setDestination(n2);

                g.addEdge(e, n1, n2);
                simulationEdges.put(simulationEdgeCount, e);
            }
            if (i + 1 % cols != 0) {
                if (i % cols != 0) {
                    e = new Edge(getWeightValue());
                    simulationEdgeCount++;
                    e.setId(simulationEdgeCount);

                    Vertex n1 = simulationNodes.get(i);
                    Vertex n2 = simulationNodes.get(i + 1);

                    e.setSource(n1);
                    e.setDestination(n2);

                    g.addEdge(e, n1, n2);
                    simulationEdges.put(simulationEdgeCount, e);
                }
            }
        }
        return g;
    }

    private MyGraph<Vertex, Edge> random() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int x, y;
        Vertex newNode, newNeighbour;

        for (int i = 1; i <= getNoOfProcessor(); i++) {
            newNode = new Vertex();
            x = (int) Math.round((ObjectFactory.getMainFrame().widthOfDrawPanel + 30 - Configuration.radius) * Math.random());
            y = (int) Math.round((ObjectFactory.getMainFrame().heightOfDrawPanel - 30 - Configuration.radius) * Math.random());
            newNode.setPosition(x, y);
            if (!tooCloseToOtherNodes(newNode)) {
                simulationNodeCount++;

                newNode.setLabel(String.valueOf(simulationNodeCount));
                simulationNodes.put(simulationNodeCount, newNode);
                g.addVertex(newNode);
            }
        }
        int z, i, m;
        for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();

            if (integer == 1) {
                z = 0;
            } else {
                z = (int) Math.round((integer - 2) * Math.random() + 1);
            }
            Vector neighbours = new Vector(z);
            i = 0;
            while (i < z) { // haven't reached full # neighbours yet
                m = (int) Math.round((integer - 1) * Math.random()); // generate a random index between 0 and n to get the next neighbour out of the nodes vector
                newNeighbour = simulationNodes.get(m);
                if (!neighbours.contains(newNeighbour) && newNeighbour != vertex) {
                    newLink(vertex, newNeighbour, g);
                    neighbours.add(newNeighbour);
                    i++;
                }
            }
        }
        return g;
    }

    private void newLink(Vertex n1, Vertex n2, MyGraph g) {
        if (n1 != null && n2 != null) {
            Edge e = new Edge(getWeightValue());
            simulationEdgeCount++;

            e.setId(simulationEdgeCount);
            simulationEdges.put(simulationEdgeCount, e);

            g.addEdge(e, n1, n2);
        }
    }

    public boolean tooCloseToOtherNodes(Vertex nodeToCheck) {
        Point centre1, centre2;
        for (Map.Entry<Integer, Vertex> entry : simulationNodes.entrySet()) {
            Vertex nodeInternal = entry.getValue();

            if (nodeInternal != nodeToCheck) {
                centre1 = nodeInternal.getCentre();
                centre2 = nodeToCheck.getCentre();
                if (centre1.distance(centre2) < Configuration.diameter + Configuration.radius) {
                    return true;
                }
            }
        }
        return false;
    }
}
