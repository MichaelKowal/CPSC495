/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.LinkedHashMap;
import java.util.Map;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.message.Message;

import org.github.com.jvec.JVec;
/**
 *
 * @author behnish
 */
public class Node extends Entity {

    private int nodeObjectId;
    private Map<Integer, Channel> channels = new LinkedHashMap<>();
    private Algorithm algorithmCode;
    private Network network;
    private int clock = 0;
    protected JVec vcInfo;

    public void adapNodeClock(int c) {
        if (getClock() < c) {
            setClock(c);
        }
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

    public Algorithm getAlgorithmCode() {
        return algorithmCode;
    }
    
    public JVec getVCInfo()
    {
        return vcInfo;
    }

    public Node(String label) {
        super("Node" + label);
    }

    public void saveInNetwork(Algorithm algorithm, Network net, String label) {
        algorithmCode = algorithm;
        network = net;
        nodeObjectId = Integer.parseInt(label);
        algorithmCode.setNode(this);
        //vcInfo = network.getJVecAtLocation(getNodeId());
        network.addNode(nodeObjectId, this);
    }

    public int getNodeId() {
        return nodeObjectId;
    }

    public void setNodeId(int nodeId) {
        this.nodeObjectId = nodeId;
    }

    public Network getNetwork() {
        return network;
    }

    @Override
    public void body() {
        algorithmCode.startMeUp();
    }

    public void addOutChannel(int nodeId, Channel channel) {
        channels.put(nodeId, channel);
    }

    public Map<Integer, Channel> getOutChannels() {
        return channels;
    }

    public Channel getOutChannel(int nodeIdOutPut) {
        return channels.get(nodeIdOutPut);
    }

    public Message getMessage() {
        Event ev = new Event();

        while (Configuration.simulationLength > Core.clock()) {
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

    public synchronized void send(Channel channel, Message message) {
        sim_schedule(channel, channel.getDelay(), 99998, message);
    }
    public synchronized void sendHopDelay(Channel channel, Message message, double delayToBeAdded) {
        sim_schedule(channel, (channel.getDelay()+delayToBeAdded), 99998, message);
    }   
}