/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.GUI;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import unbc.ca.distributed.animation.AnimationPanel;
import unbc.ca.distributed.animation.SideBar;
import unbc.ca.distributed.database.DatabaseConn;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.graph.GraphWindow;
import unbc.ca.distributed.graph.MyGraph;
import unbc.ca.distributed.graph.elements.CreateHyperCube;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.library.ObjectSizeCalculator;
import unbc.ca.distributed.library.Simulation;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;
import unbc.ca.distributed.performance.PerformancePanel;
import unbc.ca.distributed.performance.SidePanel;
import unbc.ca.distributed.simDistributed.Dist_Simulation;
import unbc.ca.distributed.library.Core;

/**
 *
 * @author behnish
 */
public final class MainFrame extends JFrame {

    /**
     * Creates new form Main
     */
    private MyGraph<Vertex, Edge> graphObject;
    private AbstractLayout<Vertex, Edge> layout;
    public int widthOfDrawPanel = 1107;
    public int heightOfDrawPanel = 815; // 1107,815 -- old 1093, 727
    private GraphWindow window;
    /* For automatic generation of graphs */
    private int numberOfNodes = 0;

    //private int nodeWeight = 10;
    private Generator weightDist;

    private PopUp popup;
    private SimulationReplyLoad replyDialog;
    private MessagePanel panel;
    private int rows;
    private int cols;
    private String currentRun;
    private boolean unitWeight = false;

    public boolean isUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(boolean unitWeight) {
        this.unitWeight = unitWeight;
    }

    public MainFrame() {
        super("Simulation Suit for Distributed Algorithms");

        initLookAndFeel();
        initComponents();
        setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
        sizeListener();
        giveMessagePanel();
    }

    public Generator getWeightDist() {
        return weightDist;
    }

    public void setWeightDist(Generator weightDist) {
        this.weightDist = weightDist;
    }

    public int getWeightValue() {
        if (isUnitWeight()) {
            return 1;
        } else {
            return Math.round(weightDist.generate());
        }
    }

    public String getCurrentRun() {
        return currentRun;
    }

    public void setCurrentRun(String currentRun) {
        this.currentRun = currentRun;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.cols = columns;
    }

    private void giveMessagePanel() {
        panel = new MessagePanel();
        panel.setSize(widthOfDrawPanel, 25);
        panel.setBounds(0, heightOfDrawPanel - 60, widthOfDrawPanel, 25);

        // add(panel,BorderLayout.PAGE_START);
        ObjectFactory.setMessagePanel(panel);
    }

    public void sizeListener() {
        setLocationRelativeTo(null);
        popup = new PopUp(this, true);
        replyDialog = new SimulationReplyLoad(this, true);

        ObjectFactory.setMainFrame(this);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                widthOfDrawPanel = getWidth();
                heightOfDrawPanel = getHeight() - 25;

                if (window != null) {
                    window.setBounds(0, 0, widthOfDrawPanel, heightOfDrawPanel);
                }
                if (panel != null) {
                    panel.setBounds(0, heightOfDrawPanel - 60, widthOfDrawPanel, 25);
                }

                validate();
                repaint();
            }
        });
    }

    public void initializeGraphEditor() {
        graphObject = new MyGraph<>();

        layout = new CircleLayout(graphObject);

        window = new GraphWindow(layout);
        window.setGraphObject(graphObject);

        this.getContentPane().add(window, BorderLayout.CENTER);
    }

    public void generateGraph(String type) {
        newGraph(type);
    }

    private void initLookAndFeel() {
        String className = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(className);
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex) {
        }
    }

    public Point2D getCenter() {
        Rectangle bounds = getBounds();
        return new Point2D.Double(bounds.width / 2.0, bounds.height / 2.0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openGraph = new javax.swing.JMenuItem();
        saveGraph = new javax.swing.JMenuItem();
        makeImage = new javax.swing.JMenuItem();
        print = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        zoomIn = new javax.swing.JMenuItem();
        zoomOut = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        selectAll = new javax.swing.JMenuItem();
        reset = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        drawGraph = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        nodeColor = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        ringGraph = new javax.swing.JMenuItem();
        starGraph = new javax.swing.JMenuItem();
        treeGraph = new javax.swing.JMenuItem();
        random = new javax.swing.JMenuItem();
        grid = new javax.swing.JMenuItem();
        fullConnected = new javax.swing.JMenuItem();
        hyperCube = new javax.swing.JMenuItem();
        bus = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        editing = new javax.swing.JMenuItem();
        transforming = new javax.swing.JMenuItem();
        picking = new javax.swing.JMenuItem();
        completeGraph = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        workloadGenerator = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        distributed = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        animate = new javax.swing.JMenuItem();
        distributionCorrectness = new javax.swing.JMenu();
        simulationReply = new javax.swing.JMenuItem();
        simulation = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        simulateNetwork = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        howToUse = new javax.swing.JMenuItem();
        about = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("File");

        openGraph.setText("Open Graph");
        openGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openGraphActionPerformed(evt);
            }
        });
        jMenu1.add(openGraph);

        saveGraph.setText("Save Graph");
        saveGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveGraphActionPerformed(evt);
            }
        });
        jMenu1.add(saveGraph);

        makeImage.setText("Make Image");
        makeImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeImageActionPerformed(evt);
            }
        });
        jMenu1.add(makeImage);

        print.setText("Print");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });
        jMenu1.add(print);

        exit.setText("Exit ");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(exit);

        jMenuBar1.add(jMenu1);

        jMenu6.setText("View");

        zoomIn.setText("Zoom In");
        zoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInActionPerformed(evt);
            }
        });
        jMenu6.add(zoomIn);

        zoomOut.setText("Zoom Out");
        zoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutActionPerformed(evt);
            }
        });
        jMenu6.add(zoomOut);

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        jMenu6.add(delete);

        selectAll.setText("Select All");
        selectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllActionPerformed(evt);
            }
        });
        jMenu6.add(selectAll);

        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        jMenu6.add(reset);

        jMenuBar1.add(jMenu6);

        jMenu2.setText("Network");

        drawGraph.setText("Draw Network");
        drawGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawGraphActionPerformed(evt);
            }
        });
        jMenu2.add(drawGraph);

        jMenu5.setText("Network Operations");

        nodeColor.setText("Node Color");
        nodeColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeColorActionPerformed(evt);
            }
        });
        jMenu5.add(nodeColor);

        jMenu2.add(jMenu5);

        jMenu3.setText("Generate Network");

        ringGraph.setText("Ring");
        ringGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ringGraphActionPerformed(evt);
            }
        });
        jMenu3.add(ringGraph);

        starGraph.setText("Star");
        starGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                starGraphActionPerformed(evt);
            }
        });
        jMenu3.add(starGraph);

        treeGraph.setText("Tree");
        treeGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                treeGraphActionPerformed(evt);
            }
        });
        jMenu3.add(treeGraph);

        random.setText("Random");
        random.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomActionPerformed(evt);
            }
        });
        jMenu3.add(random);

        grid.setText("Grid");
        grid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gridActionPerformed(evt);
            }
        });
        jMenu3.add(grid);

        fullConnected.setText("Full Connected");
        fullConnected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullConnectedActionPerformed(evt);
            }
        });
        jMenu3.add(fullConnected);

        hyperCube.setText("HyperCube");
        hyperCube.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hyperCubeActionPerformed(evt);
            }
        });
        jMenu3.add(hyperCube);

        bus.setText("Bus");
        bus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busActionPerformed(evt);
            }
        });
        jMenu3.add(bus);

        jMenu2.add(jMenu3);

        jMenu4.setText("Mode");

        editing.setText("Editing");
        editing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editingActionPerformed(evt);
            }
        });
        jMenu4.add(editing);

        transforming.setText("Transforming");
        transforming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transformingActionPerformed(evt);
            }
        });
        jMenu4.add(transforming);

        picking.setText("Picking");
        picking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickingActionPerformed(evt);
            }
        });
        jMenu4.add(picking);

        jMenu2.add(jMenu4);

        completeGraph.setText("Complete Network");
        completeGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeGraphActionPerformed(evt);
            }
        });
        jMenu2.add(completeGraph);

        jMenuBar1.add(jMenu2);

        jMenu9.setText("Workload");

        workloadGenerator.setText("Workload Generator");
        workloadGenerator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workloadGeneratorActionPerformed(evt);
            }
        });
        jMenu9.add(workloadGenerator);

        jMenuBar1.add(jMenu9);

        jMenu7.setText("Performance");

        jMenuItem3.setText("Performance Metrics");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem3);

        distributed.setText("Distribution Correctness");
        distributed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distributedActionPerformed(evt);
            }
        });
        jMenu7.add(distributed);

        jMenuBar1.add(jMenu7);

        jMenu8.setText("Animation");

        animate.setText("Animate Simulation");
        animate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animateActionPerformed(evt);
            }
        });
        jMenu8.add(animate);

        jMenuBar1.add(jMenu8);

        distributionCorrectness.setText("Simulation");

        simulationReply.setText("Simulation Replay");
        simulationReply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationReplyActionPerformed(evt);
            }
        });
        distributionCorrectness.add(simulationReply);

        simulation.setText("Simulate Current NW");
        simulation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationActionPerformed(evt);
            }
        });
        distributionCorrectness.add(simulation);

        jMenuBar1.add(distributionCorrectness);

        jMenu12.setText("Distributed Simulation");

        simulateNetwork.setText("Simulate Network");
        simulateNetwork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateNetworkActionPerformed(evt);
            }
        });
        jMenu12.add(simulateNetwork);

        jMenuBar1.add(jMenu12);

        jMenu11.setText("Help");

        howToUse.setText("How to Use");
        jMenu11.add(howToUse);

        about.setText("About");
        jMenu11.add(about);

        jMenuItem1.setText("PrintStuff");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem1);

        jMenuBar1.add(jMenu11);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void selectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllActionPerformed
        // TODO add your handling code here:
        window.selectAll();
    }//GEN-LAST:event_selectAllActionPerformed

    private void zoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomInActionPerformed
        // TODO add your handling code here:
        window.zoomIn();
    }//GEN-LAST:event_zoomInActionPerformed

    private void zoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutActionPerformed
        // TODO add your handling code here:
        window.zoomOut();
    }//GEN-LAST:event_zoomOutActionPerformed

    private void makeImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeImageActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(window);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            window.writeJPEGImage(file);            
        }
    }//GEN-LAST:event_makeImageActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        // TODO add your handling code here:
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(window);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException ex) {
                System.out.println("Exception is " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_printActionPerformed

    private void nodeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeColorActionPerformed
        // TODO add your handling code here:
        if (window != null) {
            window.colorChooser();
        } else {
            debugMessage("Drawing Panel doesn't exists");
        }

    }//GEN-LAST:event_nodeColorActionPerformed

    private void debugMessage(String error) {
        panel.messageDisplay.setText(error);
    }

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:
        window.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
        window.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
    }//GEN-LAST:event_resetActionPerformed

    private void saveGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveGraphActionPerformed
        // TODO add your handling code here:  
        if (ObjectFactory.getNodes().size() < 1) {
            return;
        }
        updateNodePositions();
        updateEdges();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\behnish Mann\\Desktop"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            File nodes = fileChooser.getSelectedFile();
            if (ObjectFactory.getNodes().size() > 0) {
                String seperator = "#";
                if (!nodes.exists()) {
                    try {
                        PrintWriter printWriter = new PrintWriter(nodes);

                        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                            Vertex nodeObject = entry.getValue();

                            printWriter.print(nodeObject.getLabel());
                            printWriter.print(seperator);
                            printWriter.print(nodeObject.getX());
                            printWriter.print(seperator);
                            printWriter.print(nodeObject.getY());
                            printWriter.println();
                        }
                        printWriter.print("@@@@@@");
                        printWriter.println();
                        for (Map.Entry<Integer, Edge> entry : ObjectFactory.getEdges().entrySet()) {
                            Edge linkObject = entry.getValue();
                            printWriter.print(linkObject.getSource().getLabel());
                            printWriter.print(seperator);
                            printWriter.print(linkObject.getDestination().getLabel());
                            printWriter.print(seperator);
                            printWriter.print(linkObject.getId());
                            printWriter.print(seperator);
                            printWriter.print(linkObject.getWeight());
                            printWriter.println();
                        }
                        printWriter.close();

                    } catch (IOException ex) {
                        System.out.println("File error " + ex.getMessage());
                    }
                }
            }

        }

    }//GEN-LAST:event_saveGraphActionPerformed

    public void saveGraphIntoFile(String filename) {
        if (ObjectFactory.getNodes().size() < 1) {
            return;
        }
        updateNodePositions();
        updateEdges();

        File nodes = new File(filename);
        if (ObjectFactory.getNodes().size() > 0) {
            String seperator = "#";
            if (!nodes.exists()) {
                try {
                    PrintWriter printWriter = new PrintWriter(nodes);

                    for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                        Vertex nodeObject = entry.getValue();

                        printWriter.print(nodeObject.getLabel());
                        printWriter.print(seperator);
                        printWriter.print(nodeObject.getX());
                        printWriter.print(seperator);
                        printWriter.print(nodeObject.getY());
                        printWriter.println();
                    }
                    printWriter.print("@@@@@@");
                    printWriter.println();
                    for (Map.Entry<Integer, Edge> entry : ObjectFactory.getEdges().entrySet()) {
                        Edge linkObject = entry.getValue();
                        printWriter.print(linkObject.getSource().getLabel());
                        printWriter.print(seperator);
                        printWriter.print(linkObject.getDestination().getLabel());
                        printWriter.print(seperator);
                        printWriter.print(linkObject.getId());
                        printWriter.print(seperator);
                        printWriter.print(linkObject.getWeight());
                        printWriter.println();
                    }
                    printWriter.close();

                } catch (IOException ex) {
                    System.out.println("File error " + ex.getMessage());
                }
            }
        }

    }
    private void openGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openGraphActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        int r = fileChooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            fileChooser.setCurrentDirectory(new File("C:\\Users\\behnish Mann\\Desktop"));

            try {
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;
                boolean check = true;

                while ((strLine = br.readLine()) != null) {
                    if ("@@@@@@".equals(strLine)) {
                        check = false;
                    }

                    if (check && !"@@@@@@".equals(strLine)) {
                        String[] temp = null;
                        temp = strLine.split("#");

                        Vertex node = new Vertex();
                        node.setLabel(temp[0]);
                        node.setPosition(Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));

                        ObjectFactory.getNodes().put(Integer.parseInt(temp[0]), node);
                        ObjectFactory.incNodeCount();

                    } else {
                        if (!"@@@@@@".equals(strLine)) {
                            String[] temp = null;
                            temp = strLine.split("#");
                            Edge edge = new Edge(Integer.parseInt(temp[3]));
                            edge.setId(Integer.parseInt(temp[2]));
                            edge.setSource(ObjectFactory.getNodes().get(Integer.parseInt(temp[0])));
                            edge.setDestination(ObjectFactory.getNodes().get(Integer.parseInt(temp[1])));

                            ObjectFactory.getEdges().put(Integer.parseInt(temp[2]), edge);
                            ObjectFactory.incEdgeCount();

                        }
                    }

                }
                in.close();
            } catch (IOException | NumberFormatException e) {
                System.err.println("Error: " + e.getMessage());
            }
            makeGraph();

        }

    }//GEN-LAST:event_openGraphActionPerformed

    public void reloadGraph(String name) {

        String fileName = name.trim() + "_network";
        try {
            FileInputStream fstream = new FileInputStream(new File(fileName));
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            boolean check = true;

            while ((strLine = br.readLine()) != null) {
                if ("@@@@@@".equals(strLine)) {
                    check = false;
                }

                if (check && !"@@@@@@".equals(strLine)) {
                    String[] temp = null;
                    temp = strLine.split("#");

                    Vertex node = new Vertex();
                    node.setLabel(temp[0]);
                    node.setPosition(Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));

                    ObjectFactory.getNodes().put(Integer.parseInt(temp[0]), node);
                    ObjectFactory.incNodeCount();

                } else {
                    if (!"@@@@@@".equals(strLine)) {
                        String[] temp = null;
                        temp = strLine.split("#");
                        Edge edge = new Edge(Integer.parseInt(temp[3]));
                        edge.setId(Integer.parseInt(temp[2]));
                        edge.setSource(ObjectFactory.getNodes().get(Integer.parseInt(temp[0])));
                        edge.setDestination(ObjectFactory.getNodes().get(Integer.parseInt(temp[1])));

                        ObjectFactory.getEdges().put(Integer.parseInt(temp[2]), edge);
                        ObjectFactory.incEdgeCount();

                    }
                }

            }
            in.close();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error: " + e.getMessage());
        }
        makeGraph();
        setCurrentRun(name);

        JOptionPane.showMessageDialog(this,
                "All details have been loaded into module, you can directly run animation or performance metrics",
                "Information Message",
                JOptionPane.INFORMATION_MESSAGE);
    }
    private void treeGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_treeGraphActionPerformed
        // TODO add your handling code here:  
        popup.lable.setText("Height of tree");
        popup.setTypeGraph("Tree");
        popup.setVisible(true);

    }//GEN-LAST:event_treeGraphActionPerformed

    private void ringGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ringGraphActionPerformed
        // TODO add your handling code here:
        popup.setTypeGraph("Circle");
        popup.setVisible(true);

    }//GEN-LAST:event_ringGraphActionPerformed

    private void starGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_starGraphActionPerformed
        // TODO add your handling code here:
        popup.setTypeGraph("Star");
        popup.setVisible(true);
    }//GEN-LAST:event_starGraphActionPerformed

    private void completeGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeGraphActionPerformed
        // TODO add your handling code here:

        for (Vertex n1 : graphObject.getVertices()) {
            for (Vertex n2 : graphObject.getVertices()) {
                if (n1 != n2) {
                    if (graphObject.findEdge(n2, n1) == null) {
                        Edge e = new Edge(1);
                        ObjectFactory.incEdgeCount();

                        e.setId(ObjectFactory.getEdgeCount());
                        ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

                        graphObject.addEdge(e, n1, n2);

                    }
                }
            }

        }
        window.repaint();
    }//GEN-LAST:event_completeGraphActionPerformed

    private void editingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editingActionPerformed
        // TODO add your handling code here:        
        window.setModeOfGraph(editing.getText());
    }//GEN-LAST:event_editingActionPerformed

    private void transformingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transformingActionPerformed
        // TODO add your handling code here:
        window.setModeOfGraph(transforming.getText());
    }//GEN-LAST:event_transformingActionPerformed

    private void pickingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pickingActionPerformed
        // TODO add your handling code here:
        window.setModeOfGraph(picking.getText());
    }//GEN-LAST:event_pickingActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
        window.deleteSelected();
    }//GEN-LAST:event_deleteActionPerformed

    private void drawGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawGraphActionPerformed
        // TODO add your handling code here:  
        setT("Draw Network");
        if (ObjectFactory.getNodeCount() > 0) {
            Configuration.runningSimulation = false;
            Configuration.currentTraceFile = "trace";
            Core.resetAll();
            resetNodeDataStructure();
            Configuration.simulationLength = 1000.0;
        }
        newGraph("Draw");
    }//GEN-LAST:event_drawGraphActionPerformed

    private void simulationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationActionPerformed
        setT("Simulation Drawn Network");
        singleNetworkButtonClick();
    }//GEN-LAST:event_simulationActionPerformed
    public void singleNetworkButtonClick() {
        // TODO add your handling code here:           
        if (ObjectFactory.getNodes().isEmpty()) {
            showErrorMessage("There is no network to simulate");
        } else {
            remove();
            SingleNetwork single = new SingleNetwork();
            single.setBounds(0, 0, widthOfDrawPanel - 800, heightOfDrawPanel);

            window.setBounds(widthOfDrawPanel - 800, 0, 800, heightOfDrawPanel);
            window.setModeOfGraph(transforming.getText());

            Footer foot = new Footer();
            ObjectFactory.setFooter(foot);
            foot.setBounds(0, heightOfDrawPanel, 100, 50);

            getContentPane().add(window, BorderLayout.CENTER);
            getContentPane().add(single, BorderLayout.LINE_END);

            getContentPane().add(foot, BorderLayout.PAGE_END);

            refreshFrame();
        }
        if (ObjectFactory.getConnection() == null && Configuration.database.equals("Yes")) {
            DatabaseConn conn = new DatabaseConn("localhost", "", "simulation", "root", "");
            ObjectFactory.setConnection(conn);
        }
    }

    public void startSimulation(String algorithm) {
        /* Update the node and edges objects in data structure */

        updateNodePositions();
        updateEdges();

        /* Ends here */
        if (Configuration.route.equals("Short")) {
            /* Making a table of all the shortest path from Node one to all the nodes */
            ObjectFactory.calculateShortPathsForNodes();
            /* End */
        } else {
            /* Making table to all the short hope instead of shortest path */
            ObjectFactory.calculateShortHopPathsForNodes();
            /* End */
        }
        Simulation sNetwork = new Simulation(algorithm);
        ObjectFactory.setSim(sNetwork);
        sNetwork.start();

        /* End */
    }

    public Simulation startMulipleSimulations(String algorithm) {
        /* Update the node and edges objects in data structure */

        updateNodePositions();
        updateEdges();

        if (Configuration.route.equals("Short")) {
            /* Making a table of all the shortest path from Node one to all the nodes */
            ObjectFactory.calculateShortPathsForNodes();
            /* End */
        } else {
            /* Making table to all the short hope instead of shortest path */
            ObjectFactory.calculateShortHopPathsForNodes();
            /* End */
        }

        Simulation simulateNetworkInternal = new Simulation(algorithm);
        ObjectFactory.setSim(simulateNetworkInternal);
        return simulateNetworkInternal;

        /* End */
    }
    private void fullConnectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullConnectedActionPerformed
        // TODO add your handling code here:
        popup.setTypeGraph("Full");
        popup.setVisible(true);
    }//GEN-LAST:event_fullConnectedActionPerformed

    private void workloadGeneratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workloadGeneratorActionPerformed
        // TODO add your handling code here:
        getContentPane().removeAll();
        resetAllDS();
        WorkloadGenerator w;
        if (ObjectFactory.getWorkload() == null) {
            w = new WorkloadGenerator();
            ObjectFactory.setWorkload(w);
        } else {
            w = ObjectFactory.getWorkload();
        }
        w.setBounds(0, 0, widthOfDrawPanel, heightOfDrawPanel);
        getContentPane().add(w, BorderLayout.CENTER);
        getContentPane().add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);
        ObjectFactory.setMultiple(true);
        refreshFrame();

    }//GEN-LAST:event_workloadGeneratorActionPerformed

    private void animateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animateActionPerformed
        // TODO add your handling code here:
        setT("Animation");
        if (ObjectFactory.getNodes().isEmpty()) {
            showErrorMessage("There is no network to animate");
        } else {
            getContentPane().removeAll();
            AnimationPanel animation = new AnimationPanel();
            ObjectFactory.setAnimationPanel(animation);

            animation.setBounds(0, 0, widthOfDrawPanel - 200, heightOfDrawPanel);

            SideBar s = new SideBar();
            ObjectFactory.setSidebar(s);

            s.setBounds(widthOfDrawPanel - 200, 0, 200, heightOfDrawPanel);

            getContentPane().add(animation, BorderLayout.CENTER);
            getContentPane().add(s, BorderLayout.LINE_END);

            add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);
            refreshFrame();

        }
    }//GEN-LAST:event_animateActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        setT("Performance Graphs");
        ObjectFactory.getMessagePanel().messageDisplay.setText("");

        getContentPane().removeAll();
        PerformancePanel performancePanel = new PerformancePanel();
        ObjectFactory.setPerformancePanel(performancePanel);

        performancePanel.setBounds(0, 0, widthOfDrawPanel - 200, heightOfDrawPanel);

        SidePanel sp = new SidePanel();
        ObjectFactory.setSidePanel(sp);

        sp.setBounds(widthOfDrawPanel - 200, 0, 200, heightOfDrawPanel);

        getContentPane().add(performancePanel, BorderLayout.CENTER);
        getContentPane().add(sp, BorderLayout.LINE_END);

        add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);
        refreshFrame();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void gridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gridActionPerformed
        // TODO add your handling code here:
        popup.setTypeGraph("Grid");
        popup.setVisible(true);
    }//GEN-LAST:event_gridActionPerformed

    private void randomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomActionPerformed
        // TODO add your handling code here:
        popup.setTypeGraph("Random");
        popup.setVisible(true);
    }//GEN-LAST:event_randomActionPerformed

    private void simulationReplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationReplyActionPerformed
        // TODO add your handling code here:
        if (ObjectFactory.getNodeCount() > 0) {
            Configuration.runningSimulation = false;
            Configuration.currentTraceFile = "trace";
            Core.resetAll();
            resetNodeDataStructure();
            Configuration.simulationLength = 1000.0;
        }
        replyDialog.setVisible(true);
    }//GEN-LAST:event_simulationReplyActionPerformed

// popup.setTypeGraph("Chord");popup.setVisible(true);

    private void simulateNetworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateNetworkActionPerformed
        // TODO add your handling code here:
        if (ObjectFactory.getNodes().isEmpty()) {
            showErrorMessage("There is no network to simulate");
        } else {
            getContentPane().removeAll();
            Distributed distributedWindow = new Distributed();

            distributedWindow.setBounds(0, 0, widthOfDrawPanel - 200, heightOfDrawPanel);

            getContentPane().add(distributedWindow, BorderLayout.CENTER);

            add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);
            refreshFrame();
        }
    }//GEN-LAST:event_simulateNetworkActionPerformed

    private void hyperCubeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hyperCubeActionPerformed
        // TODO add your handling code here:
        popup.lable.setText("Dimensions of Hypercube");
        popup.setTypeGraph("Hypercube");
        popup.setVisible(true);
    }//GEN-LAST:event_hyperCubeActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        System.out.println("Size of  is " + ObjectSizeCalculator.sizeOf('A') + " bits");
        System.out.println("Size of  is " + ObjectSizeCalculator.sizeOf("ABCDEFF") + " bits");
        System.out.println("Size of  is " + ObjectSizeCalculator.sizeOf("ABCDEFFABCDEFFABCDEFFABCDEFFABCDEFFABCDEFFABCDEFFABCDEFF") + " bits");
        System.out.println("Size of  is " + ObjectSizeCalculator.sizeOf(10) + " bits");
        System.out.println("Size of  is " + ObjectSizeCalculator.fullSizeOf(graphObject) + " bits");

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void distributedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distributedActionPerformed
        // TODO add your handling code here:
        setT("Correctness of Distribution");

        getContentPane().removeAll();
        DistributionPanel distributionPanel = new DistributionPanel();

        distributionPanel.setBounds(0, 0, widthOfDrawPanel - 200, heightOfDrawPanel);

        DistributionSidePanel s = new DistributionSidePanel(distributionPanel, this);

        s.setBounds(widthOfDrawPanel - 200, 0, 200, heightOfDrawPanel);

        getContentPane().add(distributionPanel, BorderLayout.CENTER);
        getContentPane().add(s, BorderLayout.LINE_END);

        add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);
        refreshFrame();
    }//GEN-LAST:event_distributedActionPerformed

    private void busActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busActionPerformed
        // TODO add your handling code here:
        popup.setTypeGraph("Bus");
        popup.setVisible(true);

    }//GEN-LAST:event_busActionPerformed

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void remove() {
        getContentPane().removeAll();
        ObjectFactory.getMessagePanel().setSize(widthOfDrawPanel, 25);
        ObjectFactory.getMessagePanel().setBounds(0, heightOfDrawPanel - 60, widthOfDrawPanel, 25);

        add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);

    }

    public List<Component> getAllComponents() {
        Component[] comps = getComponents();
        List<Component> compList = new ArrayList<>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents());
            }
        }
        return compList;
    }

    private void newGraph(String type) {
        ObjectFactory.setMultiple(false);
        resetNodeDataStructure();
        switch (type) {
            case "Draw":
                graphObject = new MyGraph<>();
                layout = getLayoutCircle(graphObject);
                remove();
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel - 25));
                //getContentPane().add(window);
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
            case "Circle":
                remove();
                graphObject = circleGraph();
                layout = getLayoutCircle(graphObject);
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
            case "Bus":
                remove();
                graphObject = busGraph();
                layout = getLayoutCircle(graphObject);
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;                
            case "Chord":
                remove();
                graphObject = chordGraph();
                layout = getLayoutCircle(graphObject);
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                //getContentPane().add(window);
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;

            case "Star":
                remove();
                graphObject = starGraph();
                layout = getLayoutCircle(graphObject);
                /* Draw Last node in middle */
                Vertex n1 = new Vertex();
                ObjectFactory.incNodeCount();
                n1.setLabel(String.valueOf(numberOfNodes));
                ObjectFactory.getNodes().put(numberOfNodes, n1);
                graphObject.addVertex(n1);

                /* Draw Last node in middle for star topology */
                updateNodePositions();
                Point2D p = getCenterPoint(new Point((int) ObjectFactory.getNodes().get(1).getX(), (int) ObjectFactory.getNodes().get(1).getY()),
                        new Point((int) ObjectFactory.getNodes().get(2).getX(), (int) ObjectFactory.getNodes().get(2).getY()),
                        new Point((int) ObjectFactory.getNodes().get(3).getX(), (int) ObjectFactory.getNodes().get(3).getY()));
                updateModelNodePositions(ObjectFactory.getNodes().get(numberOfNodes), p);
                for (int i = 1; i <= numberOfNodes - 1; i++) {
                    Edge e = new Edge(getWeightValue());
                    ObjectFactory.incEdgeCount();
                    e.setId(ObjectFactory.getEdgeCount());

                    ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

                    graphObject.addEdge(e, ObjectFactory.getNodes().get(numberOfNodes), ObjectFactory.getNodes().get(i));

                }
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                //getContentPane().add(window);
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
            case "Tree":
                remove();
                graphObject = treeGraph(numberOfNodes);
                layout = getLayoutCircle(graphObject);
                /* Updating the coordinates in the layout */
                for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                    Vertex vertex = entry.getValue();
                    layout.setLocation(vertex, vertex.getX(), vertex.getY());
                }
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                //getContentPane().add(window);
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
            case "Pryamid":
                remove();
                graphObject = pyramid(numberOfNodes);
                layout = getLayoutCircle(graphObject);
                /* Updating the coordinates in the layout */
                for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                    Vertex vertex = entry.getValue();
                    layout.setLocation(vertex, vertex.getX(), vertex.getY());
                }
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();

                break;
            case "Full":
                remove();
                graphObject = fullGraph();
                layout = getLayoutCircle(graphObject);
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                //getContentPane().add(window);
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
            case "Random":
                remove();
                graphObject = random();
                layout = getLayoutCircle(graphObject);
                for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                    Vertex vertex = entry.getValue();
                    layout.setLocation(vertex, vertex.getX(), vertex.getY());
                }
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();

                break;
            case "Grid":
                remove();
                graphObject = grid();
                layout = getLayoutCircle(graphObject);
                for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                    Vertex vertex = entry.getValue();
                    layout.setLocation(vertex, vertex.getX(), vertex.getY());
                }
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
            case "Hypercube":
                remove();
                graphObject = hypercubeGraph(numberOfNodes);
                layout = getLayoutCircle(graphObject);
                /* Updating the coordinates in the layout */
                for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
                    Vertex vertex = entry.getValue();
                    layout.setLocation(vertex, vertex.getX(), vertex.getY());
                }
                window = new GraphWindow(layout);
                window.setGraphObject(graphObject);
                window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
                //getContentPane().add(window);
                add(window, BorderLayout.CENTER);
                getContentPane().validate();
                getContentPane().repaint();
                break;
        }
    }

    private CircleLayout<Vertex, Edge> getLayoutCircle(Graph<Vertex, Edge> graph) {
        CircleLayout<Vertex, Edge> localLayout = new CircleLayout<>(graph);
        localLayout.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
        return localLayout;
    }

    public void updateModelNodePositions(Vertex nodeChanged, Point2D pointL) {
        layout.setLocation(nodeChanged, pointL);
    }

    public Point2D getCenterPoint(Point p1, Point p2, Point p3) {
        final double TOL = 0.0000001;

        double offset = Math.pow(p2.x, 2) + Math.pow(p2.y, 2);
        double bc = (Math.pow(p1.x, 2) + Math.pow(p1.y, 2) - offset) / 2.0;
        double cd = (offset - Math.pow(p3.x, 2) - Math.pow(p3.y, 2)) / 2.0;
        double det = (p1.x - p2.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p2.y);

        if (Math.abs(det) < TOL) {
            throw new IllegalArgumentException("Yeah, lazy.");
        }

        final double idet = 1 / det;

        double centerx = (bc * (p2.y - p3.y) - cd * (p1.y - p2.y)) * idet;
        double centery = (cd * (p1.x - p2.x) - bc * (p2.x - p3.x)) * idet;

        Point2D point = new Point2D.Double(centerx, centery);

        return point;
    }

    public MyGraph<Vertex, Edge> starGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 1; i < numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            ObjectFactory.incNodeCount();
            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);

            g.addVertex(n1);
        }
        return g;
    }

    public MyGraph<Vertex, Edge> circleGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            ObjectFactory.incNodeCount();

            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);
            g.addVertex(n1);
        }

        for (int i = 1; i <= numberOfNodes; i++) {

            Edge e = new Edge(getWeightValue());
            ObjectFactory.incEdgeCount();

            e.setId(ObjectFactory.getEdgeCount());
            ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

            if (i != numberOfNodes) {
                g.addEdge(e, ObjectFactory.getNodes().get(i), ObjectFactory.getNodes().get(i + 1));
            } else if (i == numberOfNodes) {
                g.addEdge(e, ObjectFactory.getNodes().get(numberOfNodes), ObjectFactory.getNodes().get(1));
            }
        }

        return g;
    }

    public MyGraph<Vertex, Edge> busGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            ObjectFactory.incNodeCount();

            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);
            g.addVertex(n1);
        }

        for (int i = 1; i < numberOfNodes; i++) {

            Edge e = new Edge(getWeightValue());
            ObjectFactory.incEdgeCount();

            e.setId(ObjectFactory.getEdgeCount());
            ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

            if (i != numberOfNodes) {
                g.addEdge(e, ObjectFactory.getNodes().get(i), ObjectFactory.getNodes().get(i + 1));
            }            
        }

        return g;
    }
    static boolean even(int n) {
        return (Math.IEEEremainder((double) n, 2) == 0);
    }

    public MyGraph<Vertex, Edge> chordGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            ObjectFactory.incNodeCount();

            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);
            g.addVertex(n1);
        }

        Vector chordLengths = new Vector();
        chordLengths.add(new Integer(1)); // outer ring always present

        StringTokenizer st = new StringTokenizer(String.valueOf(rows), ",");
        while (st.hasMoreTokens()) {
            chordLengths.add(new Integer(st.nextToken()));
        }
        int testValue;
        for (int i = 0; i < chordLengths.size(); i++) {
            testValue = ((Integer) chordLengths.get(i)).intValue();
            if (testValue > numberOfNodes || testValue <= 0) { // popup window with error message
                showErrorMessage("Chord lengths must be between 1 and the number\n of nodes");

            }
        }

        int mirrors = 0;
        if (even(numberOfNodes) && chordLengths.contains(new Integer(numberOfNodes / 2))) {
            mirrors = numberOfNodes / 2;
        }

        for (int i = 0; i < chordLengths.size() - 1; i++) {
            for (int j = i + 1; j < chordLengths.size(); j++) {
                int a = ((Integer) chordLengths.get(i)).intValue();
                int b = ((Integer) chordLengths.get(j)).intValue();
                if (a == numberOfNodes - b || a == b) {
                    chordLengths.remove(j);
                    j--; // so it continues without missing any (everything has shifted left)
                }
            }
        }

        // Create links
        Vertex targetNode, anchorNode;
        Edge newLink;
        int size = chordLengths.size();
        int index;
        for (int i = 1; i <= numberOfNodes; i++) {
            anchorNode = ObjectFactory.getNodes().get(i);
            for (int j = 0; j < size; j++) {
                if (mirrors > 0 && (i >= numberOfNodes / 2) && ((Integer) chordLengths.get(j)).intValue() == numberOfNodes / 2) {
                } else {
                    index = i + ((Integer) chordLengths.get(j)).intValue();
                    if (index >= numberOfNodes) {
                        index = index - numberOfNodes;
                    }
                    targetNode = ObjectFactory.getNodes().get(index + 1);

                    newLink = new Edge(getWeightValue());
                    ObjectFactory.incEdgeCount();
                    newLink.setId(ObjectFactory.getEdgeCount());
                    ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), newLink);
                    g.addEdge(newLink, anchorNode, targetNode);
                }
            }
        }
        return g;
    }

    private boolean checkEdge(Vertex v1, Vertex v2, MyGraph g) {
        return g.findEdge(v1, v2) != null;
    }

    private MyGraph<Vertex, Edge> fullGraph() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            Vertex n1 = new Vertex();
            ObjectFactory.incNodeCount();

            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);
            g.addVertex(n1);
        }

        for (Vertex n1 : g.getVertices()) {
            for (Vertex n2 : g.getVertices()) {
                if (n1 != n2) {
                    if (g.findEdge(n2, n1) == null) {
                        Edge e = new Edge(getWeightValue());
                        ObjectFactory.incEdgeCount();

                        e.setId(ObjectFactory.getEdgeCount());
                        ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

                        g.addEdge(e, n1, n2);

                    }
                }
            }
        }

        return g;
    }

    public void updateNodePositions() {
        for (Vertex node : graphObject.getVertices()) {

            double x = layout.getX(node);
            double y = layout.getY(node);

            node.setPosition(x, y);
        }
    }

    public void updateEdges() {
        for (Edge e : graphObject.getEdges()) {
            Vertex source = graphObject.getEndpoints(e).getFirst();
            Vertex destination = graphObject.getEndpoints(e).getSecond();

            e.setSource(source);
            e.setDestination(destination);
        }
    }

    public MyGraph<Vertex, Edge> treeGraph(int number) {
        int distant = 150;
        int width = widthOfDrawPanel;
        int middle = width / 2;
        int high = 100;
        int diameter = 100;

        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 0; i < number; i++) {
            int amount = (int) Math.pow(2, i);

            for (int j = 0; j < amount; j++) {
                Vertex n1 = new Vertex();
                ObjectFactory.incNodeCount();

                int x = middle - diameter / 2 - distant * ((int) Math.pow(2, number - i - 1)) * (amount - 2 * j - 1) / 2;
                int y = high * (i + 1);

                n1.setPosition((double) x, (double) y);

                n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
                ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);
                g.addVertex(n1);
            }
        }

        for (int i = 1; i < (int) Math.pow(2, number - 1); i++) {
            int index = i - 1;

            /* Creating left edge for nodes */
            Edge leftEdge = new Edge(getWeightValue());
            ObjectFactory.incEdgeCount();
            leftEdge.setId(ObjectFactory.getEdgeCount());
            ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), leftEdge);

            int left = (index * 2 + 1) + 1;
            g.addEdge(leftEdge, ObjectFactory.getNodes().get(index + 1), ObjectFactory.getNodes().get(left));

            /* Creating Right edge for nodes */
            Edge rightEdge = new Edge(getWeightValue());
            ObjectFactory.incEdgeCount();
            rightEdge.setId(ObjectFactory.getEdgeCount());
            ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), rightEdge);

            int right = (index * 2 + 2) + 1;

            g.addEdge(rightEdge, ObjectFactory.getNodes().get(index + 1), ObjectFactory.getNodes().get(right));
        }

        return g;
    }

    private void makeGraph() {
        getContentPane().removeAll();
        graphObject = customGraph();

        layout = getLayoutCircle(graphObject);
        /* Updating the coordinates in the layout */
        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Vertex vertex = entry.getValue();
            layout.setLocation(vertex, vertex.getX(), vertex.getY());
        }

        window = new GraphWindow(layout);
        window.setGraphObject(graphObject);
        window.setSize(new Dimension(widthOfDrawPanel, heightOfDrawPanel));
        getContentPane().add(window);
        getContentPane().validate();
        getContentPane().repaint();
    }

    private MyGraph<Vertex, Edge> customGraph() {

        MyGraph<Vertex, Edge> g = new MyGraph<>();

        for (int i = 1; i <= ObjectFactory.getNodeCount(); i++) {

            g.addVertex(ObjectFactory.getNodes().get(i));
        }

        for (int i = 1; i <= ObjectFactory.getEdges().size(); i++) {

            g.addEdge(ObjectFactory.getEdges().get(i), ObjectFactory.getEdges().get(i).getSource(), ObjectFactory.getEdges().get(i).getDestination());
        }

        return g;
    }

    public void refreshFrame() {
        getContentPane().repaint();
        getContentPane().validate();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem about;
    private javax.swing.JMenuItem animate;
    private javax.swing.JMenuItem bus;
    private javax.swing.JMenuItem completeGraph;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenuItem distributed;
    private javax.swing.JMenu distributionCorrectness;
    private javax.swing.JMenuItem drawGraph;
    private javax.swing.JMenuItem editing;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenuItem fullConnected;
    private javax.swing.JMenuItem grid;
    private javax.swing.JMenuItem howToUse;
    private javax.swing.JMenuItem hyperCube;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem makeImage;
    private javax.swing.JMenuItem nodeColor;
    private javax.swing.JMenuItem openGraph;
    private javax.swing.JMenuItem picking;
    private javax.swing.JMenuItem print;
    private javax.swing.JMenuItem random;
    private javax.swing.JMenuItem reset;
    private javax.swing.JMenuItem ringGraph;
    private javax.swing.JMenuItem saveGraph;
    private javax.swing.JMenuItem selectAll;
    private javax.swing.JMenuItem simulateNetwork;
    private javax.swing.JMenuItem simulation;
    private javax.swing.JMenuItem simulationReply;
    private javax.swing.JMenuItem starGraph;
    private javax.swing.JMenuItem transforming;
    private javax.swing.JMenuItem treeGraph;
    private javax.swing.JMenuItem workloadGenerator;
    private javax.swing.JMenuItem zoomIn;
    private javax.swing.JMenuItem zoomOut;
    // End of variables declaration//GEN-END:variables

    private void resetNodeDataStructure() {
        ObjectFactory.setNodeCount(0);
        ObjectFactory.setEdgeCount(0);

        ObjectFactory.getNodes().clear();
        ObjectFactory.getEdges().clear();

        ObjectFactory.getShortestPaths().clear();
        ObjectFactory.getLessHopPaths().clear();
    }

    private void resetAllDS() {
        if (ObjectFactory.getWorkloads().size() > 0) {
            ObjectFactory.getWorkloads().clear();
        }
        if (ObjectFactory.getNodes().size() > 0) {
            ObjectFactory.getNodes().clear();
            ObjectFactory.setNodeCount(0);

        }
        if (ObjectFactory.getEdges().size() > 0) {
            ObjectFactory.getEdges().clear();
            ObjectFactory.setEdgeCount(0);
        }
        if (ObjectFactory.getSimulationRuns().size() > 0) {
            ObjectFactory.getSimulationRuns().clear();
        }
        ObjectFactory.getShortestPaths().clear();
        ObjectFactory.getLessHopPaths().clear();
    }

    public void setGraphObject(MyGraph<Vertex, Edge> graphObject) {
        this.graphObject = graphObject;
    }

    public void setLayout(AbstractLayout<Vertex, Edge> layout) {
        this.layout = layout;
    }

    public void setNumberOfNodes(int number) {
        this.numberOfNodes = number;
    }

    public Graph<Vertex, Edge> getGraphObject() {
        return graphObject;
    }

    public GraphWindow getWindow() {
        return window;
    }

    public void setWindow(GraphWindow window) {
        this.window = window;
    }

    private MyGraph<Vertex, Edge> random() {
        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int x, y;
        Vertex newNode, newNeighbour;
        Edge link;
        int j = 1;

        for (int i = 1; i <= numberOfNodes; i++) {
            newNode = new Vertex();
            x = (int) Math.round((widthOfDrawPanel + 30 - Configuration.radius) * Math.random());
            y = (int) Math.round((heightOfDrawPanel - 30 - Configuration.radius) * Math.random());
            newNode.setPosition(x, y);
            if (!tooCloseToOtherNodes(newNode)) {
                ObjectFactory.incNodeCount();

                newNode.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
                ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), newNode);
                g.addVertex(newNode);
            }
        }
        int z, i, m;
        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();

            if (integer == 1) {
                z = 0;
            } else {
                z = (int) Math.round((integer - 2) * Math.random() + 1);
            }
            Vector neighbours = new Vector(z);
            i = 0;
            while (i < z) { // haven't reached full # neighbours yet
                m = (int) Math.round((integer - 1) * Math.random()); // generate a random index between 0 and n to get the next neighbour out of the nodes vector
                newNeighbour = ObjectFactory.getNodes().get(m);
                if (!neighbours.contains(newNeighbour) && newNeighbour != vertex) {
                    newLink(vertex, newNeighbour, g);
                    neighbours.add(newNeighbour);
                    i++;
                }
            }
        }
        return g;
    }

    private void newLink(Vertex n1, Vertex n2, MyGraph g) {
        if (n1 != null && n2 != null) {
            Edge e = new Edge(1);
            ObjectFactory.incEdgeCount();

            e.setId(ObjectFactory.getEdgeCount());
            ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

            g.addEdge(e, n1, n2);
        }
    }

    public boolean tooCloseToOtherNodes(Vertex nodeToCheck) {
        Point centre1, centre2;
        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Vertex nodeInternal = entry.getValue();

            if (nodeInternal != nodeToCheck) {
                centre1 = nodeInternal.getCentre();
                centre2 = nodeToCheck.getCentre();
                if (centre1.distance(centre2) < Configuration.diameter + Configuration.radius) {
                    return true;
                }
            }
        }
        return false;
    }

    private MyGraph<Vertex, Edge> grid() {

        MyGraph<Vertex, Edge> g = new MyGraph<>();
        int totalNodes = rows * cols;

        for (int i = 1; i <= totalNodes; i++) {
            Vertex n1 = new Vertex();
            ObjectFactory.incNodeCount();

            n1.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), n1);

            g.addVertex(n1);
        }

        double dif_x = (3 * widthOfDrawPanel) / (4 * (cols - 1));
        double dif_y = (3 * heightOfDrawPanel) / (4 * (rows - 1));

        double x = widthOfDrawPanel / 8 - Configuration.radius / 2;
        double y = heightOfDrawPanel / 8 - Configuration.radius / 2;

        //double this_x, this_y;       
        double this_x, this_y;
        int number = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this_x = x + j * dif_x;
                this_y = y + i * dif_y;
                ObjectFactory.getNodes().get(number).setPosition(this_x, this_y);
                number++;
            }
        }

        for (int i = 1; i <= totalNodes; i++) {
            if (i <= totalNodes - cols) {
                linkNodes(i, i + cols, g);
            }
            if (i + 1 % cols != 0) {
                if (i % cols != 0) {
                    linkNodes(i, i + 1, g);
                }
            }
        }

        return g;
    }

    public int checkPointInside(double x, double y) {
        int nodeId = 0;
        for (int i = 1; i <= ObjectFactory.getNodes().size(); i++) {
            double x0 = x - ObjectFactory.getNodes().get(i).getX();
            double y0 = y - ObjectFactory.getNodes().get(i).getY();
            int radius = Configuration.radius;
            if (x0 * x0 + y0 * y0 <= radius * radius) {
                nodeId = Integer.parseInt(ObjectFactory.getNodes().get(i).getLabel());
            }

        }
        return nodeId;
    }

    private void linkNodes(int s, int d, MyGraph g) {
        Vertex n1 = ObjectFactory.getNodes().get(s);
        Vertex n2 = ObjectFactory.getNodes().get(d);

        if (n1 != null && n2 != null && g.findEdge(n1, n2) == null) {
            Edge e = new Edge(getWeightValue());
            ObjectFactory.incEdgeCount();

            e.setId(ObjectFactory.getEdgeCount());
            ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);

            g.addEdge(e, n1, n2);
        }
    }

    public void startSimulation_dist(String algorithm, Generator csDis, Generator intReqDis, Generator delayProcess) {
        try {
            /* Update the node and edges objects in data structure */

            updateNodePositions();
            updateEdges();

            /* Making a table of all the shortest path from Node one to all the nodes */
            ObjectFactory.calculateShortPathsForNodes();
            /* End */

            /* Making table to all the short hope instead of shortest path */
            ObjectFactory.calculateShortHopPathsForNodes();

            Dist_Simulation sNetwork = new Dist_Simulation(algorithm, csDis, intReqDis, delayProcess);
            //ObjectFactory.setSim(sNetwork);
            sNetwork.start();

            /* End */
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MyGraph<Vertex, Edge> hypercubeGraph(int numberOfNodes) {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        CreateHyperCube m = new CreateHyperCube(numberOfNodes, g, weightDist, unitWeight);
        m.createCube();

        return g;
    }

    public void setT(String title) {
        setTitle("Simulation Suit for Distributed Algorithms (" + title + ")");
    }

    class SortedArrayList<T> extends ArrayList<T> {

        @SuppressWarnings("unchecked")
        public void insertSorted(T value) {
            add(value);
            Comparable<T> cmp = (Comparable<T>) value;
            for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--) {
                Collections.swap(this, i, i - 1);
            }
        }
    }

    public MyGraph<Vertex, Edge> pyramid(int numberOfNodes1) {
        MyGraph<Vertex, Edge> g = new MyGraph<>();

        Dimension d = new Dimension(widthOfDrawPanel, heightOfDrawPanel);
        int radius = Configuration.radius;
        int cx = d.width / 2,
                cy = d.height / 2,
                r = Math.min(cx, cy) / radius;

        int nodesPerPrevRow = 0;
        int nodesPerRow = radius;
        int breakRow = radius;
        int x = cx - (radius - 1) * r / 2,
                y = cy - (radius - 1) * r;
        for (int i = 1; i <= numberOfNodes1; i++) {
            boolean isRoot = i == (numberOfNodes1 - 1) / 2;
            if (i == breakRow) {
                nodesPerPrevRow = nodesPerRow;
                y += r;
                if (i < numberOfNodes1 / 2) {
                    x -= r / 2;
                    nodesPerRow += 1;
                } else {
                    nodesPerRow -= 1;
                    x += r / 2;
                }
                breakRow += nodesPerRow;
            }

            int coordx = x + (i - breakRow + nodesPerRow) * r;
            Vertex v = new Vertex();
            v.setPosition(coordx, y);
            ObjectFactory.incNodeCount();

            v.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
            ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), v);

            g.addVertex(v);
            //int startRow = breakRow - nodesPerRow;
            //  int startPrevRow = breakRow - nodesPerRow - nodesPerPrevRow;

//			if (i > startRow)
//			{
//				link (nodes[i], nodes[i-1]);
//				((Prog)progs[i]).neighbors.addElement (progs[i-1]);
//				link (nodes[i-1], nodes[i]);
//				((Prog)progs[i-1]).neighbors.addElement (progs[i]);
//			}
//			if (i - nodesPerRow >= startPrevRow && i - nodesPerRow < startRow)
//			{
//				link (nodes[i], nodes[i-nodesPerRow]);
//				((Prog)progs[i]).neighbors.addElement (progs[i-nodesPerRow]);
//				link (nodes[i-nodesPerRow], nodes[i]);
//				((Prog)progs[i-nodesPerRow]).neighbors.addElement (progs[i]);
//			}
//			if (i - nodesPerPrevRow >= startPrevRow && i - nodesPerPrevRow < startRow)
//			{
//				link (nodes[i], nodes[i-nodesPerPrevRow]);
//				((Prog)progs[i]).neighbors.addElement (progs[i-nodesPerPrevRow]);
//				link (nodes[i-nodesPerPrevRow], nodes[i]);
//				((Prog)progs[i-nodesPerPrevRow]).neighbors.addElement (progs[i]);
//			}
        }
        return g;
    }
}
