/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.simDistributed;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.library.Utilites;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class Dist_Simulation extends Thread {

    private Dist_Network network;
    private String algorithmName;
    private Generator interRDist;
    private Generator csRDist;
    private Generator delayProcessing;
    private Dist_Sim_systemIF simSystem;

    public Dist_Simulation(String algorithm, Generator csDis, Generator intReqDis, Generator delayProcessing) throws NotBoundException, MalformedURLException, RemoteException {
        super.setName("Simulation Thread");
        this.csRDist = csDis;
        this.interRDist = intReqDis;
        this.algorithmName = algorithm;
        this.delayProcessing = delayProcessing;

        this.network = new Dist_Network();

        simSystem = (Dist_Sim_systemIF) Naming.lookup("//" + Configuration.host + ":" + Configuration.port + "/DistributedSimulation");
        createNetwork();
    }

    public Dist_Network getNetwork() {
        return network;
    }

    public Dist_Node node(Dist_Algorithm algorithm, String label) throws RemoteException {
        Dist_Node nodeObject = new Dist_Node(label, simSystem);
        nodeObject.setInCurrentNetwork(ObjectFactory.getNodes().get(Integer.parseInt(label)).isIsIncludedInNework());
        nodeObject.saveInNetwork(algorithm, network, label);

        return nodeObject;
    }


    /* Add object of algorithm here */
    private void createNetwork() throws RemoteException {

        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();
            if (vertex.isIsIncludedInNework()) {

                Dist_Algorithm algorithmCodeOnNode = Utilites.returnObject_dist(algorithmName);
                algorithmCodeOnNode.setCsRDist(csRDist);
                algorithmCodeOnNode.setInterRDist(interRDist);
                algorithmCodeOnNode.setDelayProcess(delayProcessing);

                node(algorithmCodeOnNode, String.valueOf(integer));
                ObjectFactory.getCurrentNetwork().add(integer);
            }
        }

        for (Map.Entry<Integer, Edge> entry : ObjectFactory.getEdges().entrySet()) {
            Edge edge = entry.getValue();

            int sourceNode = Integer.parseInt(edge.getSource().getLabel());
            int destinationNode = Integer.parseInt(edge.getDestination().getLabel());

            String sourceChannelName = "node-" + edge.getSource().getLabel() + "-out-" + edge.getDestination().getLabel();

            Dist_Channel sourceChannel = new Dist_Channel(sourceChannelName);
            sourceChannel.setDelay(edge.getWeight());
            sourceChannel.setName(sourceChannelName);

            String destinationChannelName = "node-" + edge.getDestination().getLabel() + "-out-" + edge.getSource().getLabel();

            Dist_Channel destinationChannel = new Dist_Channel(destinationChannelName);
            destinationChannel.setDelay(edge.getWeight());
            destinationChannel.setName(destinationChannelName);

            if (ObjectFactory.getCurrentNetwork().contains(sourceNode)) {
                Dist_Node source = network.getNodes().get(sourceNode);
                source.add_port(sourceChannel);
                source.addOutChannel(destinationNode, sourceChannel);
            }
            if (ObjectFactory.getCurrentNetwork().contains(destinationNode)) {
                Dist_Node destination = network.getNodes().get(destinationNode);
                destination.add_port(destinationChannel);
                destination.addOutChannel(sourceNode, destinationChannel);
            }
        }
    }

    @Override
    public void run() {
        network.startMe();
    }
}
