/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph.elements;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.MyGraph;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author http://www.teleportaloo.org/java/cube/index.html Customized to fit
 * the needs
 */
public class Cube extends Thread {

    static int threadCount; // Number of active threads
    int N;			// Dimensions
    TrigTable tab;		// Trib table to use
    int ncol;		// Colour rule to use
    int it;         		// N*N-1, used as a convenience
    int w2;         		// width of window squared. used for c map
    int a, b, c, d;    		// Line ends of current line
    double X0, Y0;  		// Origin
    int I, J, K;      // Iteration vars
    int I1[];            // flags
    int I2[];            // more flags    
    MyGraph<Vertex, Edge> g;
    Set<Vertex> tempNodes = new HashSet<>();
    //int weight;
    private Generator distributionOfWeights;

    /* For workload generater only */
    Map<Integer, Vertex> simulationNodes;
    int simulationNodeCount;
    Map<Integer, Edge> simulationEdges;
    int simulationEdgeCount;
    boolean workloadCheck = false;

    /* Ends here */
    public Map<Integer, Vertex> getSimulationNodes() {
        return simulationNodes;
    }

    public void setSimulationNodes(Map<Integer, Vertex> simulationNodes) {
        this.simulationNodes = simulationNodes;
    }

    public int getSimulationNodeCount() {
        return simulationNodeCount;
    }

    public void setSimulationNodeCount(int simulationNodeCount) {
        this.simulationNodeCount = simulationNodeCount;
    }

    public Map<Integer, Edge> getSimulationEdges() {
        return simulationEdges;
    }

    public void setSimulationEdges(Map<Integer, Edge> simulationEdges) {
        this.simulationEdges = simulationEdges;
    }

    public int getSimulationEdgeCount() {
        return simulationEdgeCount;
    }

    public void setSimulationEdgeCount(int simulationEdgeCount) {
        this.simulationEdgeCount = simulationEdgeCount;
    }

    public void setWorkloadCheck(boolean workloadCheck) {
        this.workloadCheck = workloadCheck;
    }

    public void setDistributionOfWeights(Generator distributionOfWeights) {
        this.distributionOfWeights = distributionOfWeights;
    }

    public int getWeightValue() {
        if (unityW) {
            return 1;
        } else {
            return (int) Math.round(distributionOfWeights.generate());
        }
    }

    public void setG(MyGraph<Vertex, Edge> g) {
        this.g = g;
    }

    boolean unityW = false;

    //--------------------------------------------------------------------------
    public Cube(int n, TrigTable t, boolean unityW) {
        N = n;
        tab = t;
        this.unityW = unityW;

        I1 = new int[N];
        I2 = new int[N];

    }
    //--------------------------------------------------------------------------

    @Override
    public void run() {
        draw_cube();
    }
    //--------------------------------------------------------------------------

    public void draw_cube() {
        Dimension s = new Dimension(ObjectFactory.getMainFrame().widthOfDrawPanel, ObjectFactory.getMainFrame().heightOfDrawPanel - 200);
        double F = 0.0;
        int margin = 20;
        int minWidth
                = (s.width < s.height ? s.width : s.height)
                - 2 * margin;
        double X, Y;
        I = -1;

        w2 = s.width * s.width;

        for (J = 0; J < N; J++) {
            F += tab.Y1[J];
            // Initialise X & Y
            I1[ J] = 0;
            I2[ J] = 0;
        }

        X0 = 0.0;

        for (J = 0; J < N; J++) {
            if (tab.X1[J] < 0.0) {
                X0 += tab.X1[J];
            }
        }

        F = (minWidth) / F;
        X0 = -(X0 - 10 / F);
        X0 *= F;
        Y0 = 0.0;

        it = (int) Math.pow(2, N) - 1;

        for (I = 0; I < it; I++) {
            for (J = 0; J < N; J++) {
                I2[J] = I1[J];
            }

            for (J = 0; J < N; J++) {
                if (I1[J] != 1) {
                    I2[J] = 1;
                    X = 0.0;
                    Y = 0.0;
                    for (K = 0; K < N; K++) {
                        X += I1[K] * tab.X1[K];
                        Y += I1[K] * tab.Y1[K];
                    }
                    // CALL "PLOT",X*F,Y*F 
                    a = margin + (int) (X0 + X * F);
                    b = margin + (int) (Y0 + Y * F);

                    X = 0.0;
                    Y = 0.0;
                    for (K = 0; K < N; K++) {
                        X += I2[K] * tab.X1[K];
                        Y += I2[K] * tab.Y1[K];
                    }
                    c = margin + (int) (X0 + X * F);
                    d = margin + (int) (Y0 + Y * F);

                    Vertex n1;
                    Vertex n2;

                    if (returnIfAny(a, b) == null) {
                        if (workloadCheck) {
                            n1 = new Vertex();
                            simulationNodeCount++;
                            n1.setPosition((double) a, (double) b);

                            n1.setLabel(String.valueOf(simulationNodeCount));
                            simulationNodes.put(simulationNodeCount, n1);
                            g.addVertex(n1);
                            tempNodes.add(n1);

                        } else {
                            n1 = new Vertex();
                            ObjectFactory.incNodeCount();
                            n1.setPosition((double) a, (double) b);

                            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
                            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);
                            g.addVertex(n1);
                            tempNodes.add(n1);
                        }

                    } else {
                        n1 = returnIfAny(a, b);
                    }
                    if (returnIfAny(c, d) == null) {
                        if (workloadCheck) {
                            n2 = new Vertex();
                            simulationNodeCount++;
                            n2.setPosition((double) c, (double) d);

                            n2.setLabel(String.valueOf(simulationNodeCount));
                            simulationNodes.put(simulationNodeCount, n2);
                            g.addVertex(n2);
                            tempNodes.add(n2);
                        } else {
                            n2 = new Vertex();
                            ObjectFactory.incNodeCount();
                            n2.setPosition((double) c, (double) d);

                            n2.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
                            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n2);
                            g.addVertex(n2);
                            tempNodes.add(n2);
                        }

                    } else {
                        n2 = returnIfAny(c, d);
                    }

                    if (workloadCheck) {
                        Edge edge = new Edge(getWeightValue());
                        simulationEdgeCount++;
                        edge.setId(simulationEdgeCount);
                        simulationEdges.put(simulationEdgeCount, edge);

                        g.addEdge(edge, n1, n2);
                    } else {
                        Edge edge = new Edge(getWeightValue());
                        ObjectFactory.incEdgeCount();
                        edge.setId(ObjectFactory.getEdgeCount());
                        ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), edge);

                        g.addEdge(edge, n1, n2);
                    }

                    I2[J] = 0;
                }
            }

            J = 0;

            while (I1[J] != 0) {
                I1[J] = 0;
                J++;
            }

            I1[J] = 1;
        }
    }

    public Vertex returnIfAny(double x, double y) {
        for (Vertex v : tempNodes) {
            if (v.getX() == x && v.getY() == y) {
                return v;
            }
        }
        return null;
    }
}
