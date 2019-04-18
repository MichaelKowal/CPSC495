/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph;

import edu.uci.ics.jung.graph.UndirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class MyGraph<Vertex, Edge> extends UndirectedOrderedSparseMultigraph<Vertex, Edge> {

    public MyGraph() {
        super();
    }

    @Override
    public boolean addEdge(Edge edge, Pair<? extends Vertex> endpoints, EdgeType edgeType) {
        Pair<Vertex> new_endpoints = getValidatedEndpoints(edge, endpoints);
        if (new_endpoints == null) {
            return false;
        }

        Vertex v1 = new_endpoints.getFirst();
        Vertex v2 = new_endpoints.getSecond();
        
        
        if (v1.equals(v2)) 
        {
            // Removing the Object we created in the edge factory. Otherwise it will lead to 
            // having a empty outchannel.
            String[] edgeName = edge.toString().split("-");
            String[] temp = edgeName[0].split(":");
            ObjectFactory.getEdges().remove(Integer.parseInt(temp[1].trim()));            
            
            return false;
        } else {
            return super.addEdge(edge, endpoints, edgeType);
        }
    }        
}
