/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class SimulationRun {

    private String algorithm;
    private String workload;
    private int workloadId;
    private Workload workloadObject;
    private int simulationRunid;
    private String name;

    public Workload getWorkloadObject() {
        return workloadObject;
    }

    public void setWorkloadObject(Workload workloadObject) {
        this.workloadObject = workloadObject;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public int getWorkloadId() {
        return workloadId;
    }

    public void setWorkloadId(int workloadId) {
        this.workloadId = workloadId;
    }

    public int getSimulationRunid() {
        return simulationRunid;
    }

    public void setSimulationRunid(int simulationRunid) {
        this.simulationRunid = simulationRunid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String runNameForDisplay = "";
        Workload temp = ObjectFactory.getWorkloads().get(workloadId);
        runNameForDisplay += name + "\n"
                + " Workload Name:- " + workload + "\n"
                + " Number Of Process:- " + temp.getNoOfProcessor() + "\n"
                + " Topology Used:- " + temp.getTopology() + "\n"
                + " Algorithm Used:- " + algorithm + "\n"
                + " Simulation Time:- " + temp.getSimulationTime() + "\n\n";
        ;
        return runNameForDisplay;
    }

}
