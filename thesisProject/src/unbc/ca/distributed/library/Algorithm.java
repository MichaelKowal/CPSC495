package unbc.ca.distributed.library;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.TimeLogical;

import org.github.com.jvec.JVec;

/**
 * The type Algorithm.
 *
 * @author behnish
 * @author michaelKowal
 * @version 1.1
 * @since 1.6
 */
public abstract class Algorithm {

    public Node node = null;
    private int region;
    private Generator interRDist;
    private Generator csRDist;
    private Generator delayProcess;
    private int lastTryClock;
    private Workload workLoadObject;
    private boolean makeLog;
    private JVec vcInfo;
    
    
    private static byte[] encodedMsg;
    public static String currentAlg;

    public static String getCurrentAlg()
    {
        return currentAlg;
    }
    public Workload getWorkLoadObject() {
        return workLoadObject;
    }

    public void setWorkLoadObject(Workload workLoadObject) {
        this.workLoadObject = workLoadObject;
    }

    public void setMakeLog(boolean checkBoxVal)
    {
        makeLog = checkBoxVal;
    }
    
    protected void startMeUp() {
        init();
        checkmessage();
    }

    public synchronized void checkmessage() {
        if (checkMessages()) {
            Message messageR = receieve();
            if (messageR != null) {
                onReceive(messageR);
            }
        }
    }

    public TimeLogical getLastTryClock() {
        return new TimeLogical(lastTryClock, getNodeId());
    }

    public void setLastTryClock(int lastTryClock) {
        this.lastTryClock = lastTryClock;
    }

    private synchronized boolean checkMessages() {
        return node.sim_waiting() > 0;
    }
    
    protected abstract void init();

    protected abstract void onReceive(Message msg);

    public void setDelayProcess(Generator delayProcess) {
        this.delayProcess = delayProcess;
    }

    protected void setInterRDist(Generator interRDist) {
        this.interRDist = interRDist;
    }

    protected void setCsRDist(Generator csRDist) {
        this.csRDist = csRDist;
    }
    
    protected void setVCInfo(JVec vci)
    {
        vcInfo = vci;
    }

    public double csTimeOutValue() {
        return csRDist.generate();
    }

    public double interTimeOutValue() {
        return interRDist.generate();
    }

    public double hopeProcessingDelayValue() {
        if (Configuration.constantValue == 1) {
            return 1.0;
        } else {
            return delayProcess.generate();
        }
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    protected void setNode(Node node) {
        this.node = node;
    }

    public int getNodeId() {
        return node.getNodeId();
    }

    public synchronized void send(int destinationNodeId, Message msg) {
        print("-------------------Node " + getNodeId() + " is sending message to node " + destinationNodeId + "-------------------------Message-->" + msg.getContent());
        msg.setFinalReceiver(destinationNodeId);
        msg.setFinalSender(getNodeId());

        addToTraceInternal("F," + getNodeId() + "," + destinationNodeId, msg, false, false, false, true, 0);
        try {
            sendMessage(destinationNodeId, msg.clone());
            
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void sendInternal(int destinationNodeId, Message msg) {

        print("-------------------Node " + getNodeId() + " is sending message to node " + destinationNodeId + "----------------------------Message-->" + msg.getContent());

        msg.setFinalReceiver(destinationNodeId);
        msg.setFinalSender(getNodeId());

        addToTraceInternal("F," + getNodeId() + "," + destinationNodeId, msg, false, false, false, true, 0);
        sendMessage(destinationNodeId, msg);
    }

    private void print(Object O) {
        if (Configuration.debug) {
            System.out.println(O);
        }
    }

    private synchronized void sendMessage(int nodeId, Message message) {
        /* Assigning the sender and reciever of the message for routing them to futher hopes */

        message.setSender(getNodeId());

        int shortestPathCheck = returnConfiguredPath(getNodeId(), nodeId);

        Channel channel = null;

        if (shortestPathCheck == nodeId) {
            channel = node.getOutChannel(nodeId);
        }

        if (channel == null) {
            nodeId = returnConfiguredPath(getNodeId(), nodeId);

            channel = node.getOutChannel(nodeId);

            message.setReceiver(nodeId);

            addToTraceInternal("S," + getNodeId() + "," + nodeId, message, true, false, false, false, 0);
            node.send(channel, message);
        } else {
            addToTraceInternal("S," + getNodeId() + "," + nodeId, message, true, false, false, false, 0);
            message.setReceiver(nodeId);
            node.send(channel, message);
        }
        if(makeLog) {
            try {
                encodedMsg = node.getVCInfo().prepareSend("Node " + getNodeId() + ": Sending Message: " + message.getContent(), 
                    message.getContent().getBytes());
            } catch (IOException e) {}
            }   
    }

    /* 
     * This function is little different from the sendMessage becuase it is being used
     * with routing messages only. It include the extra delay from the distribution
     * we have chosen in the simulation panel     
     */
    private synchronized void sendMessageHop(int nodeId, Message message, double delayToBeAdded) {
        message.setSender(getNodeId());

        int shortestPathCheck = returnConfiguredPath(getNodeId(), nodeId);

        Channel channel = null;

        if (shortestPathCheck == nodeId) {
            channel = node.getOutChannel(nodeId);
        }

        if (channel == null) {
            nodeId = returnConfiguredPath(getNodeId(), nodeId);

            channel = node.getOutChannel(nodeId);

            message.setReceiver(nodeId);

            addToTraceInternal("S," + getNodeId() + "," + nodeId, message, true, false, false, false, 0);
            node.sendHopDelay(channel, message, delayToBeAdded);
        } else {
            addToTraceInternal("S," + getNodeId() + "," + nodeId, message, true, false, false, false, 0);
            message.setReceiver(nodeId);
            node.sendHopDelay(channel, message, delayToBeAdded);
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
    private Message receieve() {
        Message messageRecieved = node.getMessage();

        if (messageRecieved != null) {
            if (messageRecieved.getFinalReceiver() == getNodeId()) {

                print("####################Node " + getNodeId() + " received message from node " + messageRecieved.getFinalSender()
                        + "###################### and MessageContent->" + messageRecieved.getContent());
                addToTraceInternal("R," + getNodeId() + "," + messageRecieved.getFinalSender(), messageRecieved, false, true, false, false, 0);
                if(makeLog){
                try {
                    node.getVCInfo().unpackReceive("Node " + getNodeId() + ": Receiving Message: " + 
                            messageRecieved.getContent(), encodedMsg);
                } catch (IOException e) {}
                }
                return messageRecieved;
                
            } else {

                messageRecieved.incHopCount();

                print("####################Node " + getNodeId() + " received route message from node " + messageRecieved.getSender() + "," + messageRecieved.getFinalSender() + "," + messageRecieved.getFinalReceiver()
                        + "###################### and MessageContent->" + messageRecieved.getContent());
                
                double hopDelay = 1;
                if (Configuration.constantValue != 1) {
                    hopDelay = hopeProcessingDelayValue();
                } else {
                    hopDelay = 1;
                }
                /* Adding hop delay to the message */

                sendMessageHop(messageRecieved.getFinalReceiver(), messageRecieved, hopDelay);
                addToTraceInternal("HP," + getNodeId() + "," + messageRecieved.getFinalReceiver(), messageRecieved, false, false, true, false, hopDelay);
            }
        }
        return null;
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
                    Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private synchronized void broadcastToOutChannels(Message msg) throws CloneNotSupportedException {
        for (Map.Entry<Integer, Channel> entry : node.getOutChannels().entrySet()) {
            Integer integer = entry.getKey();
            sendInternal(integer, msg.clone());
        }
    }

    public synchronized void broadcastOut(Message msg) {
        try {
            broadcastToOutChannels(msg);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void broadcastOutExceptParent(int parent, Message msg) {
        try {
            broadcastToOutChannels(msg, parent);
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void broadcastToOutChannels(Message msg, int parent) throws CloneNotSupportedException {
        for (Map.Entry<Integer, Channel> entry : node.getOutChannels().entrySet()) {
            Integer integer = entry.getKey();
            if (integer != parent) {
                sendInternal(integer, msg.clone());
            }
        }
    }

    protected void test(Assertion a) {
        Network network = node.getNetwork();
        Map<Integer, Algorithm> algorithmCode = network.getAlgorithmCodes();

        //Global assertion violated: 5,7
        //Global assertion violated: 5,12
        if (!a.test(algorithmCode)) {
            if (!network.getLastV().equals(a.getText())) {
                network.setLastV(a.getText());
                String[] processes = a.getText().split(",");
                System.out.println("Global assertion violated: " + a.getText());
                addToTrace("FAIR," + processes[0] + "," + processes[1]);
            }

        }
    }

    public void updateClock4All(int clock) {
        Network network = node.getNetwork();
        for (Map.Entry<Integer, Node> entry : network.getNodes().entrySet()) {
            Node node1 = entry.getValue();
            if (entry.getKey() != getNodeId()) {
                node1.adapNodeClock(clock);
            }
        }

    }

    public void enterCS() {
        node.setCSFlag(true);
        double csTime = csTimeOutValue();
        node.enterCS(csTime);
    }

    public void exitCS() {
        node.setInterR(false);
        double iR = interTimeOutValue();
        node.interRequest(iR);
    }

    public boolean isCSFlag() {
        return node.isCSFlag();
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
        if (content.split(",")[0].equals("CS")) {
            Object[] data = {(int) Core.clock(), "Critical Section ", content.split(",")[1], "NA", content.split(",")[2], getClock()};
            Core.addTableEntry(data);
            if (Configuration.database.equals("Yes")) {
                ObjectFactory.getConnection().executeQuery("INSERT INTO `simulation`.`events` (`id`, `type`, `sender`, `receiver`, `localClock`, `simulationClock`) VALUES (NULL, 'CS', '" + content.split(",")[1] + "', '" + content.split(",")[2] + "', '" + getClock() + "', '" + (int) Core.clock() + "');");
            }
        }
        String str = "<" + content + "," + getClock() + "," + (int) Core.clock() + ">";
        node.sim_trace(1, str);
        node.animationTrace(1, str);
        node.finalTrace(1, str);
    }

    private synchronized void addToTraceInternal(String content, Message msgContent, boolean send, boolean recieve, boolean hop, boolean finalSend, double hopdelay) {
        String str = "";
        if (send) {
            long time = System.nanoTime();
            msgContent.setTime(time);

            if (!ObjectFactory.isMultiple()) {
                Object[] data = {Core.clock(), "Send", content.split(",")[1], content.split(",")[2], msgContent.getContent(), getClock()};
                Core.addTableEntry(data);
            }

            str = "<" + content + ","
                    + getClock() + ","
                    + Core.clock()
                    + "," + msgContent.getContent()
                    + ">";
            node.sim_trace(1, str);
            node.animationTrace(1, str);

            if (Configuration.database.equals("Yes")) {
                ObjectFactory.getConnection().executeQuery("INSERT INTO `simulation`.`events` (`id`, `type`, `sender`, `receiver`, `localClock`, `simulationClock`) VALUES (NULL, 'S', '" + content.split(",")[1] + "', '" + content.split(",")[2] + "', '" + getClock() + "', '" + (int) Core.clock() + "');");
            }

        } else if (recieve) {
            if (!ObjectFactory.isMultiple()) {
                Object[] data = {Core.clock(), "Receive", content.split(",")[2], content.split(",")[1], msgContent.getContent(), getClock()};
                Core.addTableEntry(data);
            }
            str = "<" + content + ","
                    + getClock() + ","
                    + Core.clock() + ","
                    + msgContent.getHopCount()
                    + ">";
            node.sim_trace(1, str);
            if (Configuration.database.equals("Yes")) {
                ObjectFactory.getConnection().executeQuery("INSERT INTO `simulation`.`events` (`id`, `type`, `sender`, `receiver`, `localClock`, `simulationClock`) VALUES (NULL, 'R', '" + content.split(",")[2] + "', '" + content.split(",")[1] + "', '" + getClock() + "', '" + (int) Core.clock() + "');");
            }

        } else if (hop) {
            long in = System.nanoTime();
            long out = msgContent.getTime();
            long processingTime = Math.round((in - out) / Math.pow(10, 3));

            str = "<" + content + ","
                    + getClock() + ","
                    + Core.clock() + "," + processingTime + "," + hopdelay + ">";
            node.sim_trace(1, str);
            node.hopTrace(1, str);
        } else if (finalSend) {
            str = "<" + content + ","
                    + getClock() + ","
                    + Core.clock() + "," + msgContent.getContent() + ">";
            node.finalTrace(1, str);
        } else {
            str = "<" + content + ","
                    + getClock() + ","
                    + Core.clock() + ">";
            node.sim_trace(1, str);
            node.animationTrace(1, str);
        }
    }

    public synchronized int[] getOutputChannels() {
        int[] channelCount = new int[node.getOutChannels().size()];
        int i = 0;

        for (Map.Entry<Integer, Channel> entry : node.getOutChannels().entrySet()) {
            Integer integer = entry.getKey();
            channelCount[i] = integer;
            i++;
        }
        return channelCount;
    }

    public Channel[] getOutputChannelObjects() {
        Channel[] channelCount = new Channel[node.getOutChannels().size()];
        int i = 0;

        for (Map.Entry<Integer, Channel> entry : node.getOutChannels().entrySet()) {
            Channel integer = entry.getValue();
            channelCount[i] = integer;
            i++;
        }
        return channelCount;
    }

    public void activeOrDeactiveChannel(int sNode) {
        for (Map.Entry<Integer, Channel> entry : node.getOutChannels().entrySet()) {
            Channel channel = entry.getValue();
            if (channel.getDestinationNodeId() == sNode) {
                channel.setActive(true);
            } else {
                channel.setActive(false);
            }
        }
    }

    public Channel returnActiveChannal() {
        for (Map.Entry<Integer, Channel> entry : node.getOutChannels().entrySet()) {
            Channel channel = entry.getValue();
            if (channel.isActive()) {
                return channel;
            }
        }
        return null;
    }
}
