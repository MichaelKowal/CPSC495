/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.LinkedHashMap;
import java.util.Map;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

import org.github.com.jvec.JVec;
/**
 *
 * @author behnish
 */
public class Network {

    private Map<Integer, Node> nodes = new LinkedHashMap<>();
    private Map<Integer, Algorithm> algorithmCodes = new LinkedHashMap<>();
    protected Map<Integer, JVec> jVecs = new LinkedHashMap<>();
    private String lastV="";

    public String getLastV() {
        return lastV;
    }

    public void setLastV(String lastV) {
        this.lastV = lastV;
    }    

    public Network() {        
        if (Configuration.runningSimulation) {
            Core.resetAll();
        }
        Core.initialise();
        
    }

    public Map<Integer, Algorithm> getAlgorithmCodes() {
        return algorithmCodes;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }
    
    public JVec getJVecAtLocation(int i)
    {
        return jVecs.get(i);
    }

    public void addNode(int nodeId, Node node) {
        nodes.put(nodeId, node);        
    }

    public void startMe()
    {
        for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
            Node node = entry.getValue();
            algorithmCodes.put(entry.getKey(), node.getAlgorithmCode());
            Core.add(node);
        }

        if (!Configuration.runningSimulation) {
            Configuration.runningSimulation = true;
        }
        //globalClock();
        Core.run();
    }

    public void exit() {
        Core.end_current_run();
    }

    public final void globalClock() {
        new Thread() {
            @Override
            public void run() {
                super.setName("Clock updating Thread");
                while (Configuration.simulationLength < Core.clock()) {
                    int value = (int) Core.clock();
                    ObjectFactory.getMessagePanel().messageDisplay.setText("Global Clock :- " + String.valueOf(value));
                }
            }
        }.start();
    }
}
