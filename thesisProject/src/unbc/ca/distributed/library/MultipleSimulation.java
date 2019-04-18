/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class MultipleSimulation extends Thread {

    private double tenPercentage;

    @Override
    public void run() {
        int i = 0;
        for (Map.Entry<Integer, SimulationRun> entry : ObjectFactory.getSimulationRuns().entrySet()) {
            SimulationRun simulationRun = entry.getValue();

            if (i > 0) {
                Configuration.runningSimulation = true;
            }

            Configuration.currentTraceFile = simulationRun.getName();

            if (simulationRun.getWorkloadObject().isConstant()) {
                Configuration.constantValue = 1;
            } else {
                Configuration.constantValue = 0;
            }

            ObjectFactory.getMainFrame().setGraphObject(simulationRun.getWorkloadObject().getGraphObject());
            ObjectFactory.getMainFrame().setLayout(simulationRun.getWorkloadObject().getLayout());

            ObjectFactory.setNodeCount(simulationRun.getWorkloadObject().getSimulationNodeCount());
            ObjectFactory.setEdgeCount(simulationRun.getWorkloadObject().getSimulationEdgeCount());

            ObjectFactory.setNodes(simulationRun.getWorkloadObject().getSimulationNodes());
            ObjectFactory.setEdges(simulationRun.getWorkloadObject().getSimulationEdges());

            System.out.println(simulationRun.getWorkloadObject().getSimulationNodeCount());
            ObjectFactory.setDistributionCollection(simulationRun.getWorkloadObject().getWorkLoaddistributionCollection());

            Configuration.simulationLength = ObjectFactory.getWorkloads().get(simulationRun.getWorkloadId()).getSimulationTime();

            Simulation sim = ObjectFactory.getMainFrame().startMulipleSimulations(simulationRun.getAlgorithm());

            if (ObjectFactory.getWorkloads().get(simulationRun.getWorkloadId()).isConstantForHopProcessing()) {
                Configuration.constantValue = 1;
            } else {
                Configuration.constantValue = 0;
            }
            sim.setName("Simulation " + i);
            ObjectFactory.getMainFrame().saveGraphIntoFile(simulationRun.getName() + "_network");
            sim.start();

            tenPercentage = Configuration.simulationLength / 10;
            ObjectFactory.getSimPanel().simulationProgress.setValue(0);
            try {
                sim.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MultipleSimulation.class.getName()).log(Level.SEVERE, null, ex);
            }
            ObjectFactory.getSimPanel().jTextArea1.append("Simulation number " + i + " is completed \n");
            i++;
        }
    }

    private void globalClock(final String runname, final double endTime) {
        new Thread() {
            @Override
            public void run() {
                super.setName("Clock updating Thread for " + runname);
                while (endTime > Core.clock()) {
                    int value = (int) Core.clock();
                    if (Core.clock() > tenPercentage && Core.clock() < tenPercentage * 2) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(10);
                    } else if (Core.clock() > tenPercentage * 2 && Core.clock() < tenPercentage * 3) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(20);
                    } else if (Core.clock() > tenPercentage * 3 && Core.clock() < tenPercentage * 4) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(30);
                    } else if (Core.clock() > tenPercentage * 4 && Core.clock() < tenPercentage * 5) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(40);
                    } else if (Core.clock() > tenPercentage * 5 && Core.clock() < tenPercentage * 6) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(50);
                    } else if (Core.clock() > tenPercentage * 6 && Core.clock() < tenPercentage * 7) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(60);
                    } else if (Core.clock() > tenPercentage * 7 && Core.clock() < tenPercentage * 8) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(70);
                    } else if (Core.clock() > tenPercentage * 8 && Core.clock() < tenPercentage * 9) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(80);
                    } else if (Core.clock() > tenPercentage * 9 && Core.clock() < endTime) {
                        ObjectFactory.getSimPanel().simulationProgress.setValue(90);
                    }
                    ObjectFactory.getMessagePanel().messageDisplay.setText("Global Clock :- " + value);
                }
                ObjectFactory.getSimPanel().simulationProgress.setValue(100);
            }
        }.start();
    }
}
