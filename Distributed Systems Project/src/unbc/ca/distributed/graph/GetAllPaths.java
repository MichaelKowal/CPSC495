/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph;

import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;

/**
 *
 * @author behnish orginal idea from Aaron (Changed to work on mine)
 */
public class GetAllPaths {

    private Graph<Vertex, Edge> graphObject;
    private int depth;

    public GetAllPaths(Graph<Vertex, Edge> graph, int value) {
        graphObject = graph;
        depth = value;
    }

    /**
     * Uses the 'flooding' algorithm to find all cycles in a graph. Excludes so
     * called 'super cycles' which contain all the nodes of two smaller cycles.
     *
     * start with packet at every node at every opportunity, send packet to all
     * your neighbors except pipe you already sent it down if at any point you
     * reach node you started at, add to cycles if at any point, packet length
     * exceeds maximum, remove packet should find all cycles below max length
     * then parse through and eliminate super cycles if union of two cycles
     * equals any other cycles, remove that cycle return list if cycles
     */
    public ArrayList<ArrayList<Vertex>> getAllCycles(Vertex sourceNode, Vertex destinationNode) 
    {
        ArrayList<ArrayList<Vertex>> allCycles = new ArrayList<>();

        LinkedList<ArrayList<Vertex>> openList = new LinkedList<>();
        
        ArrayList<Vertex> first = new ArrayList<>();
        first.add(sourceNode);                
        openList.add(first);

        while (!openList.isEmpty()) {

            ArrayList<Vertex> packet = openList.pop();
            Vertex parent = packet.get(packet.size() - 1);

            //if packet.end == packet.front
            //we have a cycle
            //ensure cycle is at least length 3
            if (packet.get(packet.size()-1).equals(destinationNode)) 
            {                
                allCycles.add(packet);                
                continue;
            }

            //search all neighbors
            Collection<Vertex> neighbours = graphObject.getNeighbors(parent);
                    
            for (Vertex buddy : neighbours) {

                //don't send packet back to the neighbor that sent it to you
                if (packet.size() >= 2 && buddy.equals(packet.get(packet.size() - 2))) {
                    continue;
                }

                //prevents cycles within cycles
                //5 -> 6 -> 7 -> 8 -> 6 -> 5 is not valid cycle
                //shouldn't be allowed to add node we already have in list,
                //unless that node is the starting node and we have found a legitimate cycle
                //if we already contain that node, and that node isn't the starting point, 
                //don't go this way
                if (packet.contains(buddy)){// && !packet.get(0).equals(buddy)) {
                    continue;
                }

                //send packet everywhere else
                //as long as packet is below max length
                if (packet.size() < depth) {
                    ArrayList<Vertex> newPacket = new ArrayList<>(packet);
                    newPacket.add(buddy);
                    openList.add(newPacket);
                }

            }
        }

        //if list empty
        if (allCycles.isEmpty()) {            
            return allCycles;
        }
        
        return allCycles;
    }   
    
    public List<Edge> firstPathBest(ArrayList<ArrayList<Vertex>> all)
    {
         List<Edge> list = new LinkedList<> ();
         
        ArrayList<Vertex> firstPath  = all.get(0);                
        
        Vertex first =null;
        Vertex second = null;
        
        for (int i = 0; i < firstPath.size(); i++) 
        {
            if(first==null)
            {
                first = firstPath.get(i);
            }
            else
            {
                second = firstPath.get(i);
                
                Edge edge = graphObject.findEdge(first, second);
                list.add(edge);
                
                first = second;
                second = null;                
            }
        }
        
        return list;
    }
    public List<Edge> printEdgeWise(ArrayList<Vertex> firstPath)
    {
         List<Edge> list = new LinkedList<> ();                 
        
        Vertex first =null;
        Vertex second = null;
        
        for (int i = 0; i < firstPath.size(); i++) 
        {
            if(first==null)
            {
                first = firstPath.get(i);
            }
            else
            {
                second = firstPath.get(i);
                
                Edge edge = graphObject.findEdge(first, second);
                list.add(edge);
                
                first = second;
                second = null;                
            }
        }
        
        return list;
    }
}
