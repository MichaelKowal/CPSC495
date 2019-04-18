/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.simDistributed;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class Dist_Network {

    private Map<Integer, Dist_Node> nodes = new LinkedHashMap<>();
    private Map<Integer, Dist_Algorithm> algorithmCodes = new LinkedHashMap<>();
    Dist_Sim_systemIF simSystem;

    public Dist_Network() throws NotBoundException, MalformedURLException, RemoteException {
        simSystem = (Dist_Sim_systemIF) Naming.lookup("//" + Configuration.host + ":" + Configuration.port + "/DistributedSimulation");
        globalClock();
    }

    public Map<Integer, Dist_Algorithm> getAlgorithmCodes() {
        return algorithmCodes;
    }

    public Map<Integer, Dist_Node> getNodes() {
        return nodes;
    }

    public void addNode(int nodeId, Dist_Node node) {
        nodes.put(nodeId, node);
    }

    public void startMe() {
        for (Map.Entry<Integer, Dist_Node> entry : nodes.entrySet()) {
            try {
                Dist_Node node = entry.getValue();

                algorithmCodes.put(entry.getKey(), node.getAlgorithmCode());
                simSystem.add(node);
                simSystem.readyToRun();

            } catch (RemoteException ex) {
                Logger.getLogger(Dist_Network.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public final void globalClock() {
        new Thread() {
            @Override
            public void run() {
                try {
                    super.setName("Clock updating Thread");
                    while (Configuration.simulationLength > simSystem.sim_clock()) {
                        int value = (int) simSystem.sim_clock();
                        ObjectFactory.getMessagePanel().messageDisplay.setText("Global Clock :- " + value);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(Dist_Network.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
    }
}
