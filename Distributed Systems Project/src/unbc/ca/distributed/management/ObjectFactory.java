/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.management;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections15.Transformer;
import unbc.ca.distributed.GUI.Footer;
import unbc.ca.distributed.GUI.MainFrame;
import unbc.ca.distributed.GUI.MessagePanel;
import unbc.ca.distributed.GUI.NodeDetail;
import unbc.ca.distributed.GUI.SimulationPanel;
import unbc.ca.distributed.GUI.WorkloadGenerator;
import unbc.ca.distributed.animation.AnimationPanel;
import unbc.ca.distributed.animation.SideBar;
import unbc.ca.distributed.database.DatabaseConn;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.GetAllPaths;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.library.Simulation;
import unbc.ca.distributed.library.SimulationRun;
import unbc.ca.distributed.library.Workload;
import unbc.ca.distributed.performance.PerformancePanel;
import unbc.ca.distributed.performance.SidePanel;
import unbc.ca.distributed.trace.TraceObject;

/**
 *
 * @author behnish
 */
public class ObjectFactory {

    private static MainFrame mainFrame;
    private static Simulation sim;
    private static MessagePanel messagePanel;
    
    private static Map<Integer, Vertex> nodes = new ConcurrentHashMap<>();
    private static Map<Integer, Edge> edges = new ConcurrentHashMap<>();
    private static WorkloadGenerator workload;
    
    private static Map<Integer, Map<Integer, List<Edge>>> shortestPaths = new LinkedHashMap<>();
    private static Map<Integer, Map<Integer, List<Edge>>> lessHopPaths = new LinkedHashMap<>();    
    
    private static int nodeCount = 0;
    private static int edgeCount = 0;
    
    private static boolean multiple = false;
    private static Map<Integer, Workload> workloads = new ConcurrentHashMap<>();        
    private static Map<Integer, SimulationRun> simulationRuns = new LinkedHashMap<>();    
    
    private static SimulationPanel simPanel;
    
    private static Map<Integer, TraceObject> sendTrace = new LinkedHashMap<>();
    private static Map<Integer, TraceObject> receiverTrace = new LinkedHashMap<>();
    private static ArrayList<Integer> currentNetwork = new ArrayList<>();
   
    private static AnimationPanel animationPanel;
    private static SideBar sidebar;

    private static double animationClock = 1.0;
    private static int currentValue = 0;
    private static int animationClockDelay = 10;   
        
    private static Map<Integer,TraceObject> drawingList = new ConcurrentHashMap<>();
    
    private static PerformancePanel performancePanel;
    private static SidePanel sidePanel;         
    private static Footer footer;        
    
    private static DatabaseConn connection;     
    
    private static LinkedHashMap<Integer, LinkedHashMap<String, Generator>> distributionCollection = new LinkedHashMap<>();
    private static Map<Integer, Map<String, Generator>> workloadDistributions = new LinkedHashMap<>();
    private static Map<Integer, NodeDetail> nodesDetails  = new LinkedHashMap<>();
    private static boolean customizeNode = false;

    public static WorkloadGenerator getWorkload() {
        return workload;
    }

    public static void setWorkload(WorkloadGenerator workload) {
        ObjectFactory.workload = workload;
    }
    
    public static boolean isCustomizeNode() {
        return customizeNode;
    }

    public static void setCustomizeNode(boolean customizeNode) {
        ObjectFactory.customizeNode = customizeNode;
    }    

    public static Map<Integer, NodeDetail> getNodesDetails() {
        return nodesDetails;
    }    

    public static Map<Integer, Map<String, Generator>> getWorkloadDistributions() {
        return workloadDistributions;
    }

    public static void setDistributionCollection(LinkedHashMap<Integer, LinkedHashMap<String, Generator>> distributionCollection) {
        ObjectFactory.distributionCollection = distributionCollection;
    }
       
    public static LinkedHashMap<Integer, LinkedHashMap<String, Generator>> getDistributionCollection() {
        return distributionCollection;
    }
            
    public static DatabaseConn getConnection() {
        return connection;
    }

    public static void setConnection(DatabaseConn connection) {
        ObjectFactory.connection = connection;
    }

    public static Footer getFooter() {
        return footer;
    }

    public static void setFooter(Footer footer) {
        ObjectFactory.footer = footer;
    }    

    public static ArrayList<Integer> getCurrentNetwork() {
        return currentNetwork;
    }        
    
    
    public static PerformancePanel getPerformancePanel() {
        return performancePanel;
    }

    public static void setPerformancePanel(PerformancePanel performancePanel) {
        ObjectFactory.performancePanel = performancePanel;
    }

    public static SidePanel getSidePanel() {
        return sidePanel;
    }

    public static void setSidePanel(SidePanel sidePanel) {
        ObjectFactory.sidePanel = sidePanel;
    }
    
    

     public static int getAnimationClockDelay() {
        return animationClockDelay;
    }

    public static Map<Integer, TraceObject> getReceiverTrace() {
        return receiverTrace;
    }

    public static void setReceiverTrace(Map<Integer, TraceObject> receiverTrace) {
        ObjectFactory.receiverTrace = receiverTrace;
    }

     
    public static void loadNodes(Map<Integer, Vertex> needToBeClone)
    {
        ObjectFactory.nodes.clear();
        for (Map.Entry<Integer, Vertex> entry : needToBeClone.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();
            ObjectFactory.nodes.put(integer, vertex);            
        }
    }
    public static void loadEdges(Map<Integer, Edge> needToBeCloneEdge)
    {
        ObjectFactory.edges.clear();
        for (Map.Entry<Integer, Edge> entry : needToBeCloneEdge.entrySet()) {
            Integer integer = entry.getKey();
            Edge edge = entry.getValue();
            ObjectFactory.edges.put(integer, edge);            
        }
    }
    public static void setAnimationClockDelay(int animationClockDelay) {
        ObjectFactory.animationClockDelay = animationClockDelay;
    }
    
    public synchronized static void updateClock(double clock) 
    {
        if(ObjectFactory.getAnimationClock() <  clock)
        {
            ObjectFactory.setAnimationClock(clock);     
//            if(clock > Configuration.simulationLength)
//            {
//                clock = Configuration.simulationLength;
//            }
            ObjectFactory.getSidebar().clock.setText(String.valueOf(clock));
        }
    }
    public static SideBar getSidebar() {
        return sidebar;
    }

    public static void setSidebar(SideBar sidebar) {
        ObjectFactory.sidebar = sidebar;
    }

    public static int getCurrentValue() {
        return currentValue;
    }

    public static void setCurrentValue(int currentValue) {
        ObjectFactory.currentValue = currentValue;
    }
    
    

    public static Map<Integer,TraceObject> getDrawingList() {
        return drawingList;
    }
   
    public static double getAnimationClock() {
        return animationClock;
    }

    public static double incAnimationClock() {
        return animationClock++;
    }
    public static void setAnimationClock(double animationClock) {
        ObjectFactory.animationClock = animationClock;
    }           
    
    public static AnimationPanel getAnimationPanel() {
        return animationPanel;
    }

    public static void setAnimationPanel(AnimationPanel animationPanel) {
        ObjectFactory.animationPanel = animationPanel;
    }
    
    

    public static Map<Integer, TraceObject> getTrace() {
        return sendTrace;
    }    
    public static SimulationPanel getSimPanel() {
        return simPanel;
    }

    public static void setSimPanel(SimulationPanel simPanel) {
        ObjectFactory.simPanel = simPanel;
    }        
    
    public static MessagePanel getMessagePanel() {
        return messagePanel;
    }

    public static void setMessagePanel(MessagePanel messagePanel) {
        ObjectFactory.messagePanel = messagePanel;
    }    

    public static Map<Integer, Workload> getWorkloads() {
        return workloads;
    }

    public static boolean isMultiple() {
        return multiple;
    }
    public static void setMultiple(boolean b) {
        ObjectFactory.multiple = b;
    }

    public static Map<Integer, SimulationRun> getSimulationRuns() {
        return simulationRuns;
    }
    
    public static void setNodeCount(int nodeCount) {
        ObjectFactory.nodeCount = nodeCount;
    }

    public static void setEdgeCount(int edgeCount) {
        ObjectFactory.edgeCount = edgeCount;
    }

    public static void setEdges(Map<Integer, Edge> edges) {
        ObjectFactory.edges = edges;
    }

    public static Simulation getSim() {
        return sim;
    }

    public static void setSim(Simulation sim) {
        ObjectFactory.sim = sim;
    }

    public static Map<Integer, Map<Integer, List<Edge>>> getLessHopPaths() {
        return lessHopPaths;
    }

    public static Map<Integer, Map<Integer, List<Edge>>> getShortestPaths() {
        return shortestPaths;
    }

    public static void intializeMap() {
        for (Map.Entry<Integer, Vertex> entry : nodes.entrySet()) {
            Integer integer = entry.getKey();
            shortestPaths.put(integer, giveMap());
        }
    }

    public static void intializeLessMap() {
        for (Map.Entry<Integer, Vertex> entry : nodes.entrySet()) {
            Integer integer = entry.getKey();
            lessHopPaths.put(integer, giveMap());
        }
    }

    public static void addShortPath(int nodeId, int destinationNode, List<Edge> path) {
        Map<Integer, List<Edge>> internalPaths = shortestPaths.get(nodeId);
        internalPaths.put(destinationNode, path);
        shortestPaths.put(nodeId, internalPaths);
    }

    public static void calculateShortPathsForNodes() {
        intializeMap();

        for (Map.Entry<Integer, Vertex> entry : nodes.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();

            for (Map.Entry<Integer, Vertex> entry1 : nodes.entrySet()) {
                Integer integer1 = entry1.getKey();
                Vertex vertex1 = entry1.getValue();

                if (integer != integer1) {
                    List<Edge> path = ObjectFactory.getShortestPath(vertex, vertex1);
                    addShortPath(integer, integer1, path);
                }
            }
        }

    }
    public static List<Edge> getShortestPath(Vertex n1, Vertex n2) {

        Transformer<Edge, Integer> wtTransformer = new Transformer<Edge, Integer>() {
            @Override
            public Integer transform(Edge link) {
                return link.getWeight();
            }
        };

        DijkstraShortestPath alg = new DijkstraShortestPath(mainFrame.getGraphObject(), wtTransformer);
        
        List<Edge> list = alg.getPath(n1, n2);
        
        return list;
    }
    
    public static List<Edge> getLessHopPath(Vertex n1, Vertex n2) 
    {
        List<Edge> list = null;        
        int depth = getShortestPath(n1, n2).size() + 1 ;
        
        GetAllPaths flood = new GetAllPaths(mainFrame.getGraphObject(), depth);
        
        list = flood.firstPathBest(flood.getAllCycles(n1, n2));
        
        if(list.size()<1)
        {
            System.out.println("I am chooseing shortest path because no less hop count is available");
            return getShortestPath(n1, n2);
        }
        
        return list;
    }

    public static void calculateShortHopPathsForNodes() {
        intializeLessMap();

        for (Map.Entry<Integer, Vertex> entry : nodes.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();

            for (Map.Entry<Integer, Vertex> entry1 : nodes.entrySet()) {
                Integer integer1 = entry1.getKey();
                Vertex vertex1 = entry1.getValue();

                if (integer != integer1) {
                    List<Edge> path = ObjectFactory.getLessHopPath(vertex, vertex1);
                    addLessHopPath(integer, integer1, path);
                }
            }
        }

    }

    public static void addLessHopPath(int nodeId, int destinationNode, List<Edge> path) {
        Map<Integer, List<Edge>> internalPathsHop = lessHopPaths.get(nodeId);
        internalPathsHop.put(destinationNode, path);
        lessHopPaths.put(nodeId, internalPathsHop);
    }

    public static void printAllPath(Map<Integer, Map<Integer, List<Edge>>> pathsPrint, String type) {
        for (Map.Entry<Integer, Map<Integer, List<Edge>>> entry : pathsPrint.entrySet()) {
            Integer integer = entry.getKey();
            Map<Integer, List<Edge>> map = entry.getValue();

            System.out.println("For node " + integer + " all nodes " + type + " paths are");

            for (Map.Entry<Integer, List<Edge>> entry1 : map.entrySet()) {
                Integer integer1 = entry1.getKey();
                List<Edge> list = entry1.getValue();

                System.out.println(type + " path to node " + integer1);

                System.out.println(list.toString());
            }
        }
    }

    private static Map<Integer, List<Edge>> giveMap() {
        Map<Integer, List<Edge>> internalPaths = new LinkedHashMap<>();
        return internalPaths;
    }

    public static Map<Integer, Edge> getEdges() {
        return edges;
    }

    public static int getNodeCount() {
        return nodeCount;
    }

    public static void incNodeCount() {
        ObjectFactory.nodeCount++;
    }

    public static int getEdgeCount() {
        return edgeCount;
    }

    public static void incEdgeCount() {
        ObjectFactory.edgeCount++;
    }

    public static void decNodeCount() {
        ObjectFactory.nodeCount--;
    }

    public static void decEdgeCount() {
        ObjectFactory.edgeCount--;
    }

    public static Map<Integer, Vertex> getNodes() {
        return nodes;
    }

    public static void setNodes(Map<Integer, Vertex> nodes) {
        ObjectFactory.nodes = nodes;
    }

    public static MainFrame getMainFrame() {
        return mainFrame;
    }

    public static void setMainFrame(MainFrame mainFrame) {
        ObjectFactory.mainFrame = mainFrame;
    }  
}
