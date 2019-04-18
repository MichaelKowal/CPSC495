/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.simDistributed;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class Dist_Server extends Thread implements Dist_Linker {
    Dist_Sim_system simSystem;
    String host;
    String port;
    int numEntities;

    public Dist_Server(){}
    
    public Dist_Server(String host, String port, int numEntities) throws RemoteException {
        this.host = host;
        this.port = port;
        this.numEntities = numEntities;    
        try {
            simSystem = new Dist_Sim_system(ObjectFactory.getNodeCount(), this);
            simSystem.initialise();

            Naming.rebind("//" + Configuration.host + ":" + Configuration.port + "/DistributedSimulation", simSystem);
            System.out.println("DistributedSimulation bound in registry on host "+ Configuration.host+" and port "+ Configuration.port);

        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Master error: " + e.getMessage());
        }
    }

    @Override
    public void linkPorts() {
        for (Map.Entry<Integer, Edge> entry : ObjectFactory.getEdges().entrySet()) {
            try {
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
                
                 simSystem.link_ports("Node" + sourceNode,
                    sourceChannelName,
                    "Node" + destinationNode,
                    destinationChannelName);                 
            }            
            catch (RemoteException ex) {
                Logger.getLogger(Dist_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
    @Override
    public void run()
    {
        try {
            LocateRegistry.createRegistry(Integer.parseInt(Configuration.port));
            String registryHost = "localhost";
        String registryPort = "2001";
        int numberOfEntities = 2;
        
        Dist_Server m = new Dist_Server(registryHost, registryPort, numberOfEntities);
        
        } catch (RemoteException ex) {
            Logger.getLogger(Dist_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}