/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.io.IOException;
import java.util.Map;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;
import org.github.com.jvec.JVec;
import unbc.ca.distributed.shiviz.LogMaker;
import unbc.ca.distributed.GUI.SingleNetwork;

/**
 * @author behnish
 * @author michaelKowal
 * @version 1.1
 * @since 1.6
 */
public class Simulation extends Thread {

    private Network network;
    private String algorithmName;
    private static boolean makeLog;
    
    
    public Simulation(String algorithm) 
    {
        super.setName("Simulation Thread");
        this.algorithmName = algorithm;
        this.network = new Network();
        
        createNetwork();
    }   

    public Network getNetwork() {
        return network;
    }
    
    public static void setMakeLog(boolean checkboxVal)
    {
        makeLog = checkboxVal;
    }
    
    public Node node(Algorithm algorithm, String label) {
        Node nodeObject = new Node(label);       
        nodeObject.saveInNetwork(algorithm, network, label);
        if(makeLog)
            nodeObject.vcInfo = new JVec("Node" + nodeObject.getNodeId(), "LogFiles/Node" + nodeObject.getNodeId() + "LogFile");
        return nodeObject;
    } 
    

    /* Add object of algorithm here */
    private void createNetwork() {

        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Integer integer = entry.getKey();
            
            Algorithm algorithmCodeOnNode = Utilites.returnObject(algorithmName,integer);
            
            algorithmCodeOnNode.setCsRDist(ObjectFactory.getDistributionCollection().get(integer).get("Critical Section"));
            algorithmCodeOnNode.setInterRDist(ObjectFactory.getDistributionCollection().get(integer).get("Inter Request"));
            algorithmCodeOnNode.setDelayProcess(ObjectFactory.getDistributionCollection().get(integer).get("Hop Processing"));
            algorithmCodeOnNode.setMakeLog(makeLog);
            
            node(algorithmCodeOnNode, String.valueOf(integer));
        }

        for (Map.Entry<Integer, Edge> entry : ObjectFactory.getEdges().entrySet()) {
            Edge edge = entry.getValue();
            
            int sourceNode = Integer.parseInt(edge.getSource().getLabel());
            int destinationNode = Integer.parseInt(edge.getDestination().getLabel());

            String sourceChannelName = "node-" + edge.getSource().getLabel() + "-out-" + edge.getDestination().getLabel();

            Channel sourceChannel = new Channel(sourceChannelName);
            sourceChannel.setDelay(edge.getWeight());
            sourceChannel.setName(sourceChannelName);
            sourceChannel.setSourceNodeId(Integer.parseInt(edge.getSource().getLabel()));
            sourceChannel.setDestinationNodeId(Integer.parseInt(edge.getDestination().getLabel()));

            String destinationChannelName = "node-" + edge.getDestination().getLabel() + "-out-" + edge.getSource().getLabel();

            Channel destinationChannel = new Channel(destinationChannelName);
            destinationChannel.setDelay(edge.getWeight());
            destinationChannel.setName(destinationChannelName);
            
            destinationChannel.setDestinationNodeId(Integer.parseInt(edge.getSource().getLabel()));
            destinationChannel.setSourceNodeId(Integer.parseInt(edge.getDestination().getLabel()));

            Node source = network.getNodes().get(sourceNode);
            source.add_port(sourceChannel);
            source.addOutChannel(destinationNode, sourceChannel);

            Node destination = network.getNodes().get(destinationNode);
            destination.add_port(destinationChannel);
            destination.addOutChannel(sourceNode, destinationChannel);

            Core.link_ports("Node" + sourceNode,
                    sourceChannelName,
                    "Node" + destinationNode,
                    destinationChannelName);
        }
    }

    @Override
    public void run() { 
        network.startMe();     
        Configuration.simulationLength = 100.0;
        if(makeLog) {
            try{
                LogMaker.makeShiVizLog(this);
            } catch(IOException e){}
            }
    }
}