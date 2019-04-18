/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph.elements;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.MyGraph;

/**
 *
 * @author // Version 1.1 // by Andrew R.Wallace but modified to to fit my
 * requirements.
 */
public class CreateHyperCube {

    public int n;
    public TrigTable[] table;
    public Cube[] cube_threads;
    MyGraph<Vertex, Edge> g;
    private Generator distributionOfWeights;
    
    /* For workload generater only */
    Map<Integer, Vertex> simulationNodes;
    int simulationNodeCount;
    Map<Integer, Edge> simulationEdges;
    int simulationEdgeCount;
    boolean workloadCheck = false;
    /* Ends here */
    
    boolean unityWeight;
    

    public CreateHyperCube(int number, MyGraph<Vertex, Edge> g, Generator distributionOfWeights, boolean unityWeight) {
        this.n = number;
        cube_threads = new Cube[n + 1];
        table = new TrigTable[n + 1];
        this.g = g;
        this.distributionOfWeights = distributionOfWeights;
        this.unityWeight = unityWeight;
    }

    public CreateHyperCube(int noOfProcessor, MyGraph<Vertex, Edge> g, Generator weightDistribution, Map<Integer, Vertex> simulationNodes, int simulationNodeCount, Map<Integer, Edge> simulationEdges, int simulationEdgeCount, boolean unityWeight) {
        this.n = noOfProcessor;
        this.g = g;
        this.unityWeight = unityWeight;
        this.distributionOfWeights = weightDistribution;
        
        this.simulationNodes = simulationNodes;
        this.simulationNodeCount = simulationNodeCount;
        
        this.simulationEdges = simulationEdges;
        this.simulationEdgeCount = simulationEdgeCount;
        workloadCheck = true;
        
        cube_threads = new Cube[n + 1];
        table = new TrigTable[n + 1];
    }

    public int createCube() {
        if (table[n] == null) {
            table[n] = new TrigTable(n);
        }
        cube_threads[n] = new Cube(n, table[n], unityWeight);
        cube_threads[n].setG(g);
        cube_threads[n].setDistributionOfWeights(distributionOfWeights);
        if(workloadCheck)
        {
            cube_threads[n].setSimulationNodes(simulationNodes);
            cube_threads[n].setSimulationNodeCount(simulationNodeCount);
            cube_threads[n].setSimulationEdges(simulationEdges);
            cube_threads[n].setSimulationEdgeCount(simulationEdgeCount);
            cube_threads[n].setWorkloadCheck(true);
        }
        cube_threads[n].start();
        try {
            cube_threads[n].join();            
        } catch (InterruptedException ex) {
            Logger.getLogger(CreateHyperCube.class.getName()).log(Level.SEVERE, null, ex);
        }
        simulationNodeCount = cube_threads[n].getSimulationNodeCount();
        simulationEdgeCount = cube_threads[n].getSimulationEdgeCount();
        return cube_threads[n].getSimulationNodeCount();
    }
}

class TrigTable {

    public double[] X1;
    public double[] Y1;

    TrigTable(int N) {
        int I = -1;
        int J;

        X1 = new double[N];
        Y1 = new double[N];

        double C;
        double P = Math.PI / N;

        for (J = 0; J < N; J += 2) {
            I++;
            C = I * P;
            X1[J] = Math.cos(C);
            Y1[J] = Math.sin(C);
        }

        I = N;

        for (J = 1; J < N; J += 2) {
            I--;
            C = I * P;
            X1[J] = Math.cos(C);
            Y1[J] = Math.sin(C);
        }
    }
}
