/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.simDistributed;

import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.message.Message;

/**
 *
 * @author behnish
 */
public class Dist_Node extends Dist_Sim_entity{
    
    private int nodeObjectId;
    private Map<Integer, Dist_Channel> channels = new LinkedHashMap<>();
    private Dist_Algorithm algorithmCode;
    private Dist_Network network;
    private int clock = 0;
    private boolean inCurrentNetwork = true;
        
    private Dist_Sim_systemIF system;

    public boolean isInCurrentNetwork() {
        return inCurrentNetwork;
    }

    public void setInCurrentNetwork(boolean inCurrentNetwork) {
        this.inCurrentNetwork = inCurrentNetwork;
    }
    
    

    public Dist_Sim_systemIF getSystem() {
        return system;
    }
    
    
   public Dist_Node(String label, Dist_Sim_systemIF system) throws RemoteException {
        super("Node" + label,system);
        this.system = system;
        
    }
    public int getClock() {
        return clock;
    }

    public synchronized void setClock(int clock) {
        this.clock = clock;
    }

    public synchronized void incrementClock() {
        this.clock++;
    }

    public Dist_Algorithm getAlgorithmCode() {
        return algorithmCode;
    }

    

    public void saveInNetwork(Dist_Algorithm algorithm, Dist_Network net, String label) {
        algorithmCode = algorithm;
        network = net;
        nodeObjectId = Integer.parseInt(label);


        algorithmCode.setNode(this);
        network.addNode(nodeObjectId, this);
    }

    public int getNodeId() {
        return nodeObjectId;
    }

    public void setNodeId(int nodeId) {
        this.nodeObjectId = nodeId;
    }

    public Dist_Network getNetwork() {
        return network;
    }

    @Override
    public void body() {
        try {
            algorithmCode.startMeUp();
        } catch (RemoteException ex) {
            Logger.getLogger(Dist_Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addOutChannel(int nodeId, Dist_Channel channel) {
        channels.put(nodeId, channel);
    }

    public Map<Integer, Dist_Channel> getOutChannels() {
        return channels;
    }

    public Dist_Channel getOutChannel(int nodeIdOutPut) {
        return channels.get(nodeIdOutPut);
    }

    public Message getMessage() throws RemoteException {
        Dist_Sim_event ev = new Dist_Sim_event();

        while (Configuration.simulationLength > system.sim_clock()) {
            sim_get_next(ev);

            Message message = (Message) ev.get_data();

            if (message != null) {
                int sender = message.getSender();
                message.setParent(sender);

                return message;
            }

        }
        return null;
    }

    public synchronized void send(Dist_Channel channel, Message message) {
        sim_schedule(channel, channel.getDelay(), 99999, message);
        //System.out.println("Node "+getNodeId()+" is sendin message -->"+ message.getContent());
    }    
}
