/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.simDistributed;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.library.Algorithm;

/**
 *
 * @author behnish
 */
public abstract class Dist_Algorithm extends Algorithm {

    private Dist_Node node = null;
    private int region;
    private Generator interRDist;
    private Generator csRDist;
    private Generator delayProcess;

    private int returnRandomVariable() {
        Random r = new Random();
        int Low = 99999;
        int High = 9999999;
        int random = r.nextInt(High - Low) + Low;
        return random;
    }

    public void setDelayProcess(Generator delayProcess) {
        this.delayProcess = delayProcess;        

    }

    protected void setInterRDist(Generator interRDist) {
        this.interRDist = interRDist;        
    }

    protected void setCsRDist(Generator csRDist) {
        this.csRDist = csRDist;
    }

    public double csTimeOutValue() {
        return Math.round(csRDist.generate());
    }

    public double interTimeOutValue() {
        return Math.round(interRDist.generate());        
    }

    public void hopeProcessingDelay() {
        pullAtMost(Math.round(csRDist.generate()));        
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    protected void startMeUp() throws RemoteException {
        while (Configuration.simulationLength > node.getSystem().sim_clock()) {
            init();
            checkmessage();
        }
    }

    public synchronized void checkmessage() throws RemoteException {
        if (checkMessages()) {
            Message messageR = receieve();
            if (messageR != null) {
                onReceive(messageR);
            }
        }
    }

    protected abstract void init();

    protected abstract void onReceive(Message msg);

    protected void setNode(Dist_Node node) {
        this.node = node;
    }

    public int getNodeId() {
        return node.getNodeId();
    }

    public synchronized void send(int destinationNodeId, Message msg) {
        print("-------------------Node " + getNodeId() + " is sending message to node " + destinationNodeId + "-------------------------Message-->" + msg.getContent());

        msg.setFinalReceiver(destinationNodeId);
        msg.setFinalSender(getNodeId());
        try {
            addToTraceInternal("F," + getNodeId() + "," + destinationNodeId, msg, false, false, false, true);
        } catch (RemoteException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            sendMessage(destinationNodeId, msg.clone(), false);
        } catch (CloneNotSupportedException | RemoteException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void sendInternal(int destinationNodeId, Message msg) throws RemoteException {

        print("-------------------Node " + getNodeId() + " is sending message to node " + destinationNodeId + "----------------------------Message-->" + msg.getContent());

        msg.setFinalReceiver(destinationNodeId);
        msg.setFinalSender(getNodeId());

        addToTraceInternal("F," + getNodeId() + "," + destinationNodeId, msg, false, false, false, true);
        sendMessage(destinationNodeId, msg, false);
    }

    public void print(Object O) {
        if (Configuration.debug) {
            System.out.println(O);
        }
    }

    private synchronized void sendMessage(int nodeId, Message message, boolean check) throws RemoteException {
        /* Assigning the sender and reciever of the message for routing them to futher hopes */

        message.setSender(getNodeId());

        int shortestPathCheck = returnConfiguredPath(getNodeId(), nodeId);

        Dist_Channel channel = null;

        if (shortestPathCheck == nodeId) {
            channel = node.getOutChannel(nodeId);
        }

        if (channel == null) {
            nodeId = returnConfiguredPath(getNodeId(), nodeId);

            channel = node.getOutChannel(nodeId);

            message.setReceiver(nodeId);


            addToTraceInternal("S," + getNodeId() + "," + nodeId, message, true, false, false, false);
            node.send(channel, message);
        } else {
            addToTraceInternal("S," + getNodeId() + "," + nodeId, message, true, false, false, false);
            message.setReceiver(nodeId);
            node.send(channel, message);
        }
    }

    private synchronized int returnConfiguredPath(int source, int destination) {
        int nodeIdInPath;

        if (Configuration.route.equals("Short")) {
            nodeIdInPath = nextNodeInPath(source, destination);
        } else {
            nodeIdInPath = nextNodeInPathForLessHop(source, destination);
        }
        return nodeIdInPath;
    }

    private synchronized int nextNodeInPath(int source, int destination) {
        int nextNode = 0;

        Edge path = ObjectFactory.getShortestPaths().get(source).get(destination).get(0);

        if (Integer.parseInt(path.getDestination().getLabel()) == source) {
            nextNode = Integer.parseInt(path.getSource().getLabel());
        } else {
            nextNode = Integer.parseInt(path.getDestination().getLabel());
        }
        return nextNode;
    }

    private synchronized int nextNodeInPathForLessHop(int source, int destination) {
        int nextNode = 0;

        Edge path = ObjectFactory.getLessHopPaths().get(source).get(destination).get(0);

        if (Integer.parseInt(path.getDestination().getLabel()) == source) {
            nextNode = Integer.parseInt(path.getSource().getLabel());
        } else {
            nextNode = Integer.parseInt(path.getDestination().getLabel());
        }
        return nextNode;
    }

    /**
     *
     * @return message if it belongs to current node
     */
    private Message receieve() throws RemoteException {
        Message messageRecieved = node.getMessage();

        if (messageRecieved != null) {
            if (messageRecieved.getFinalReceiver() == getNodeId()) {

                print("####################Node " + getNodeId() + " received message from node " + messageRecieved.getFinalSender()
                        + "###################### and MessageContent->" + messageRecieved.getContent());

                addToTraceInternal("R," + getNodeId() + "," + messageRecieved.getFinalSender(), messageRecieved, false, true, false, false);
                return messageRecieved;
            } else {

                messageRecieved.incHopCount();

                print("####################Node " + getNodeId() + " received route message from node " + messageRecieved.getSender() + "," + messageRecieved.getFinalSender() + "," + messageRecieved.getFinalReceiver()
                        + "###################### and MessageContent->" + messageRecieved.getContent());


                //messageRecieved.setContent("0");
                hopeProcessingDelay();
                addToTraceInternal("HP," + getNodeId() + "," + messageRecieved.getFinalReceiver(), messageRecieved, false, false, true, false);
                sendMessage(messageRecieved.getFinalReceiver(), messageRecieved, true);
            }
        }
        return null;
    }

    public synchronized int[] getOutputChannels() {
        int[] channelCount = new int[node.getOutChannels().size()];
        int i = 0;

        for (Map.Entry<Integer, Dist_Channel> entry : node.getOutChannels().entrySet()) {
            Integer integer = entry.getKey();
            channelCount[i] = integer;
            i++;
        }
        return channelCount;
    }

    public synchronized void pullAtMost(double times) {
        node.sim_hold(times);
    }

    private synchronized boolean checkMessages() {
        // wait for little time before checking the events        
        pullAtMost(0.3);
        return node.sim_waiting() > 0;
    }

    public int totalNodesInNetwork() {
        return ObjectFactory.getNodeCount();
    }

    public synchronized void sendToAll(Message msg) {
        for (int i = 1; i <= totalNodesInNetwork(); i++) {
            if (i != getNodeId()) {
                try {
                    sendInternal(i, msg.clone());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private synchronized void broadcastToOutChannels(Message msg) throws CloneNotSupportedException, RemoteException {
        for (Map.Entry<Integer, Dist_Channel> entry : node.getOutChannels().entrySet()) {
            Integer integer = entry.getKey();
            sendInternal(integer, msg.clone());
        }
    }

    public synchronized void broadcastOut(Message msg) {
        try {
            broadcastToOutChannels(msg);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void broadcastOutExceptParent(int parent, Message msg) {
        try {
            broadcastToOutChannels(msg, parent);
        } catch (CloneNotSupportedException | RemoteException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void broadcastToOutChannels(Message msg, int parent) throws CloneNotSupportedException, RemoteException {
        for (Map.Entry<Integer, Dist_Channel> entry : node.getOutChannels().entrySet()) {
            Integer integer = entry.getKey();
            if (integer != parent) {
                sendInternal(integer, msg.clone());
            }
        }
    }

    public synchronized void hold(int value) {
        node.sim_hold(value);
    }

    public void enterCS() {
        try {
            node.setFlag(true);
            node.setInterR(false);

            node.enterCS(csTimeOutValue());
            node.interRequest(csTimeOutValue() + interTimeOutValue());
        } catch (RemoteException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isCSFlag() {
        return node.isFlag();
    }

    public boolean isInterFlag() {
        return node.isInterR();
    }

    public synchronized void adapClock(int c) {
        if (node.getClock() < c) {
            node.setClock(c);
        }
    }

    public void incrementClock() {
        node.incrementClock();
    }

    public int getClock() {
        return node.getClock();
    }

    public synchronized void addToTrace(String content) {
        String str = null;
        try {
            str = "<" + content + "," + getClock() + "," + node.getSystem().sim_clock() + ">";
        } catch (RemoteException ex) {
            Logger.getLogger(Dist_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        node.sim_trace(1, str);
        //node.animationTrace(1, str);
        //node.finalTrace(1, str);        
    }

    private synchronized void addToTraceInternal(String content, Message msgContent, boolean send, boolean recieve, boolean hop, boolean finalSend) throws RemoteException {
        String str = "";
        if (send) {
            long time = System.nanoTime();
            msgContent.setTime(time);
            str = "<" + content + ","
                    + getClock() + ","
                    + node.getSystem().sim_clock()
                    + "," + msgContent.getContent()
                    + ">";
            node.sim_trace(1, str);
            node.animationTrace(1, str);
        } else if (recieve) {
            str = "<" + content + ","
                    + getClock() + ","
                    + node.getSystem().sim_clock() + ","
                    + msgContent.getHopCount()
                    + ">";
            node.sim_trace(1, str);
        } else if (hop) {
            long in = System.nanoTime();
            long out = msgContent.getTime();
            long processingTime = Math.round((in - out) / Math.pow(10, 3));

            str = "<" + content + ","
                    + getClock() + ","
                    + node.getSystem().sim_clock() + "," + processingTime + ">";
            node.sim_trace(1, str);
            node.hopTrace(1, str);
        } else if (finalSend) {
            str = "<" + content + ","
                    + getClock() + ","
                    + node.getSystem().sim_clock() + "," + msgContent.getContent() + ">";
            node.sim_trace(1, str);
            node.finalTrace(1, str);
        } else {
            str = "<" + content + ","
                    + getClock() + ","
                    + node.getSystem().sim_clock() + ">";
            node.sim_trace(1, str);
            node.animationTrace(1, str);
        }

    }
}
