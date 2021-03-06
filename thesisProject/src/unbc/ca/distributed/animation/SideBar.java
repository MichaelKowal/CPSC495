/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.animation;

import java.awt.Color;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.library.SimulationRun;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;
import unbc.ca.distributed.trace.TraceObject;

/**
 *
 * @author behnish
 */
public class SideBar extends javax.swing.JPanel {

    /**
     * Creates new form SideBar
     */
    private boolean pauseCheck = false;
    private ScreenRefresher s;

    public SideBar() {
        initComponents();
        stop.setEnabled(false);
        jSlider1.setValue(Configuration.speed);
        if (!ObjectFactory.isMultiple()) {
            multipleRuns.setEnabled(false);
            load.setEnabled(false);
        } else {
            populateCombo(multipleRuns);
        }

    }

    public final void populateCombo(JComboBox combo) {
        DefaultComboBoxModel boxModel = new DefaultComboBoxModel();
        if (ObjectFactory.getSimulationRuns() != null) {
            for (Map.Entry<Integer, SimulationRun> entry : ObjectFactory.getSimulationRuns().entrySet()) {
                SimulationRun simulationRun = entry.getValue();

                boxModel.addElement(simulationRun.getName());
            }
            combo.setModel(boxModel);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        start = new javax.swing.JButton();
        clock = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pause = new javax.swing.JButton();
        stop = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        multipleRuns = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        reset = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        load = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        in = new javax.swing.JButton();
        out = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        moveClock = new javax.swing.JTextField();
        ok = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jSlider1.setMinimum(1);
        jSlider1.setMinorTickSpacing(5);
        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setToolTipText("");
        jSlider1.setValue(1);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        start.setText("Start");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });

        clock.setEditable(false);
        clock.setBackground(new java.awt.Color(0, 0, 0));
        clock.setForeground(new java.awt.Color(255, 255, 255));
        clock.setText("1");

        jLabel1.setText("Simulation clock");

        jLabel2.setText("Speed");

        pause.setText("Pause");
        pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseActionPerformed(evt);
            }
        });

        stop.setText("Stop");
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopActionPerformed(evt);
            }
        });

        jLabel3.setText("Animation Panel Options");

        jLabel4.setText("Multiple Simulation");

        multipleRuns.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Run" }));

        jLabel5.setText("Select Run");

        jLabel6.setText("Zooming");

        reset.setText("Reset");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        jLabel7.setText("Animation Controls");

        load.setText("Load");
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });

        console.setColumns(20);
        console.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        console.setLineWrap(true);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        jLabel8.setText("Console");

        in.setText("In");
        in.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inActionPerformed(evt);
            }
        });

        out.setText("Out");
        out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outActionPerformed(evt);
            }
        });

        jLabel9.setText("Goto Clock");

        moveClock.setBackground(new java.awt.Color(0, 0, 0));
        moveClock.setForeground(new java.awt.Color(255, 255, 255));
        moveClock.setText("10");
        moveClock.setCaretColor(new java.awt.Color(255, 255, 255));

        ok.setText("Ok");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(45, 45, 45)
                            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel4)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(start)
                            .addGap(18, 18, 18)
                            .addComponent(pause)
                            .addGap(18, 18, 18)
                            .addComponent(stop)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel9))
                            .addGap(26, 26, 26)
                            .addComponent(moveClock)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(ok)))
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(34, 34, 34)
                        .addComponent(multipleRuns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(load))
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(clock, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(in)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(out)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reset)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(in)
                    .addComponent(out)
                    .addComponent(reset))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(clock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(moveClock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(88, 88, 88)
                                .addComponent(jLabel9)
                                .addGap(26, 26, 26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ok)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(multipleRuns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(load))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(start)
                            .addComponent(pause)
                            .addComponent(stop))
                        .addGap(41, 41, 41)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed
        // TODO add your handling code here:  
        ObjectFactory.getAnimationPanel().setStopTimer(false);
        ok.setEnabled(true);
        if (pauseCheck) {
            pauseCheck = false;
            for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getTrace().entrySet()) {
                TraceObject traceObject = entry.getValue();
                traceObject.setStep(1);
            }
        } else {
            if (!ObjectFactory.isMultiple()) {
                if (ObjectFactory.getMainFrame().getCurrentRun() == null) {
                    s = new ScreenRefresher("trace");
                } else {
                    s = new ScreenRefresher(ObjectFactory.getMainFrame().getCurrentRun().trim());
                }
                s.start();
            } else {
                String item = (String) multipleRuns.getSelectedItem();
                s = new ScreenRefresher(item);
                s.start();
            }

        }
        start.setEnabled(false);
        stop.setEnabled(true);
        ObjectFactory.setAnimationClock(ObjectFactory.getTrace().get(1).getSimulationClock());
    }//GEN-LAST:event_startActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        // TODO add your handling code here:
        JSlider source = (JSlider) evt.getSource();
        if (!source.getValueIsAdjusting()) {
            Configuration.speed = source.getValue();
            for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getTrace().entrySet()) {
                TraceObject traceObject = entry.getValue();
                traceObject.setStep(source.getValue());

            }
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopActionPerformed
        // TODO add your handling code here:
        ObjectFactory.getAnimationPanel().setStopTimer(true);
        stop.setEnabled(false);
        s.stopSimulation();
        start.setEnabled(true);
        ObjectFactory.getDrawingList().clear();
        ObjectFactory.setAnimationClock(1.0);
        ObjectFactory.getAnimationPanel().repaint();
        clock.setText("1.0");

        for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getTrace().entrySet()) {
            TraceObject traceObject = entry.getValue();
            traceObject.setAnimationFinished(false);
        }
        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Vertex vertex = entry.getValue();
            vertex.setColor(Configuration.animationNodeColor);

        }
        console.setText("");
    }//GEN-LAST:event_stopActionPerformed

    private void pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseActionPerformed
        // TODO add your handling code here:
        if (!pauseCheck) {
            for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getTrace().entrySet()) {
                TraceObject traceObject = entry.getValue();
                traceObject.setStep(0);
            }
            pauseCheck = true;
            start.setEnabled(true);
        }
    }//GEN-LAST:event_pauseActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        // TODO add your handling code here:        
        ObjectFactory.getAnimationPanel().setZoomFactor(1.0D);
        ObjectFactory.getAnimationPanel().revalidate();
        ObjectFactory.getAnimationPanel().repaint();

    }//GEN-LAST:event_resetActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
        // TODO add your handling code here:
        String item = (String) multipleRuns.getSelectedItem();

        loadDS(item);
        writeToConsole(item + " has been loaded");

        ObjectFactory.getAnimationPanel().repaint();
    }//GEN-LAST:event_loadActionPerformed

    private void inActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inActionPerformed
        // TODO add your handling code here:
        ObjectFactory.getAnimationPanel().zoomIn();
        ObjectFactory.getAnimationPanel().revalidate();
        ObjectFactory.getAnimationPanel().repaint();
    }//GEN-LAST:event_inActionPerformed

    private void outActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outActionPerformed
        // TODO add your handling code here:
        ObjectFactory.getAnimationPanel().zoomOut();
        ObjectFactory.getAnimationPanel().revalidate();
        ObjectFactory.getAnimationPanel().repaint();

    }//GEN-LAST:event_outActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        // TODO add your handling code here:
        if (s == null) {
            start.setEnabled(false);
            stop.setEnabled(true);
        } else {
            s.stopSimulation();
        }
        ObjectFactory.getDrawingList().clear();
        ObjectFactory.setAnimationClock(Integer.parseInt(moveClock.getText()));
        ObjectFactory.getAnimationPanel().repaint();
        clock.setText(moveClock.getText());

        for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getTrace().entrySet()) {
            TraceObject traceObject = entry.getValue();
            if (traceObject.getSimulationClock() <= Integer.parseInt(moveClock.getText())) {
                traceObject.setAnimationFinished(true);
            } else {
                traceObject.setAnimationFinished(false);
            }
        }
        for (Map.Entry<Integer, Vertex> entry : ObjectFactory.getNodes().entrySet()) {
            Vertex vertex = entry.getValue();
            vertex.setColor(Configuration.animationNodeColor);

        }
        console.setText("");

        if (!ObjectFactory.isMultiple()) {
            if (ObjectFactory.getMainFrame().getCurrentRun() == null) {
                s = new ScreenRefresher("trace");
            } else {
                s = new ScreenRefresher(ObjectFactory.getMainFrame().getCurrentRun().trim());
            }
            ObjectFactory.setAnimationClock(Double.parseDouble(moveClock.getText()));
            s.start();
        } else {
            String item = (String) multipleRuns.getSelectedItem();
            s = new ScreenRefresher(item);
            s.start();
        }


    }//GEN-LAST:event_okActionPerformed

    private void loadDS(String item) {
        int id = Integer.parseInt(item.split(" ")[1]);
        SimulationRun simulationRun = ObjectFactory.getSimulationRuns().get(id);
        ObjectFactory.getMainFrame().setGraphObject(simulationRun.getWorkloadObject().returnMeGraph());
        ObjectFactory.getMainFrame().setLayout(simulationRun.getWorkloadObject().getLayout());

        ObjectFactory.setNodeCount(simulationRun.getWorkloadObject().getSimulationNodeCount());
        ObjectFactory.setEdgeCount(simulationRun.getWorkloadObject().getSimulationEdgeCount());

        ObjectFactory.getNodes().clear();
        ObjectFactory.getEdges().clear();

        loadEdges(simulationRun.getWorkloadObject().getSimulationEdges());
        loadNodes(simulationRun.getWorkloadObject().getSimulationNodes());

        ObjectFactory.getMainFrame().updateEdges();
        ObjectFactory.getMainFrame().updateNodePositions();

        ObjectFactory.getAnimationPanel().repaint();
    }

    public void loadNodes(Map<Integer, Vertex> needToBeClone) {
        for (Map.Entry<Integer, Vertex> entry : needToBeClone.entrySet()) {
            Integer integer = entry.getKey();
            Vertex vertex = entry.getValue();            
            ObjectFactory.getNodes().put(integer, vertex);
        }
    }

    public void loadEdges(Map<Integer, Edge> needToBeCloneEdge) {
        for (Map.Entry<Integer, Edge> entry : needToBeCloneEdge.entrySet()) {
            Integer integer = entry.getKey();
            Edge edge = entry.getValue();
            ObjectFactory.getEdges().put(integer, edge);
        }
    }

    private void writeToConsole(String content) {
        console.append(content + "\n");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField clock;
    public javax.swing.JTextArea console;
    private javax.swing.JButton in;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JButton load;
    private javax.swing.JTextField moveClock;
    private javax.swing.JComboBox multipleRuns;
    private javax.swing.JButton ok;
    private javax.swing.JButton out;
    private javax.swing.JButton pause;
    private javax.swing.JButton reset;
    private javax.swing.JButton start;
    private javax.swing.JButton stop;
    // End of variables declaration//GEN-END:variables
}
