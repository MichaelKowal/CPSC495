/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.performance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import unbc.ca.distributed.library.SimulationRun;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class SidePanel extends javax.swing.JPanel {

    /**
     * Creates new form SidePanel
     */
    private MultipleSelection m = null;
    private int selectionSize = 0;

    public SidePanel() {
        initComponents();
        if (ObjectFactory.getSimulationRuns().size() > 0) {
            populateCombo(simulationRuns);
        } else {
            simulationRuns.setEnabled(false);
            clear.setEnabled(false);
            details.setEnabled(false);
        }

    }

    public final void populateCombo(JList combo) {
        DefaultListModel boxModel = new DefaultListModel();
        if (ObjectFactory.getSimulationRuns().size() > 0) {
            for (Map.Entry<Integer, SimulationRun> entry : ObjectFactory.getSimulationRuns().entrySet()) {
                SimulationRun simulationRun = entry.getValue();
                boxModel.addElement(simulationRun.getName());
            }
        } else {
            boxModel.addElement("No Run");
        }
        combo.setModel(boxModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        show = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        showDetails = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        type = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        minmaxaverage = new javax.swing.JComboBox();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        simulationRuns = new javax.swing.JList();
        clear = new javax.swing.JButton();
        details = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        show.setText("Show");
        show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });

        jLabel3.setText("Detail of Run parameters");

        showDetails.setEditable(false);
        showDetails.setColumns(20);
        showDetails.setLineWrap(true);
        showDetails.setRows(5);
        jScrollPane2.setViewportView(showDetails);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("Analyse performance against:");

        type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Processes", "Simulation Clock", "Average", "Interarrival", "Special" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Performance Metrics");

        minmaxaverage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NA", "Min", "Max", "Average" }));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Hope Count", "Critical Sitting", "Response Time", "CS IDEAL Time", "Throughput", "Average number of Messages", "Hop Processing", "Total Messages", "Critical Section Access", "Sychronization Delay", "Fairness" }));
        jComboBox1.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(minmaxaverage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(minmaxaverage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Comarision");

        simulationRuns.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "No Run" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(simulationRuns);

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        details.setText("Details");
        details.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(details, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(clear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(details))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(show)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(show)
                .addGap(32, 32, 32)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionPerformed
        // TODO add your handling code here:        
        if (simulationRuns.getSelectedValuesList().size() > 1 && ObjectFactory.isMultiple()) {
            JPanel chartPanel = null;
            try {
                m = new MultipleSelection(simulationRuns.getSelectedValuesList());

                switch (jComboBox1.getSelectedItem().toString()) {
                    case "Hope Count":
                        chartPanel = m.hopCount();
                        break;
                    case "Critical Sitting":
                        chartPanel = m.csSittingTime();
                        break;
                    case "Response Time":
                        if (type.getSelectedItem().toString().equals("Processes")) {
                            chartPanel = m.responseTime();
                        } else if (type.getSelectedItem().toString().equals("Simulation Clock")) {
                            chartPanel = m.responseTimeProcessWise(minmaxaverage.getSelectedItem().toString());
                        } else if (type.getSelectedItem().toString().equals("Average")) {
                            chartPanel = m.responseTimeProcessWiseAverage();
                        }
                        break;
                    case "Throughput":
                        if (type.getSelectedItem().toString().equals("Average")) {
                            chartPanel = m.systemThroughputAverage();
                        } else {
                            chartPanel = m.systemThroughput();
                        }
                        break;
                    case "CS IDEAL Time":
                        if (type.getSelectedItem().toString().equals("Average")) {
                            chartPanel = m.csIdealTimeAverage();
                        } else {
                            chartPanel = m.csIdealTime();
                        }
                        break;
                    case "Average number of Messages":
                        if (type.getSelectedItem().toString().equals("Processes")) {
                            chartPanel = m.numberOfMessage();
                        } else if (type.getSelectedItem().toString().equals("Interarrival")) {
                            chartPanel = m.averageNumberOfMessagesInterarrival();
                        } else {
                            chartPanel = m.numberOfMessageProcessesWise();
                        }
                        break;
                    case "Hop Processing":
                        if (type.getSelectedItem().toString().equals("Processes")) {
                            chartPanel = m.hopProcessing();
                        } else {
                            chartPanel = m.hopProcessingClock();
                        }
                        break;
                    case "Critical Section Access":
                        if (type.getSelectedItem().toString().equals("Average")) {
                            chartPanel = m.totalCSAccessAverage();
                        } else {
                            chartPanel = m.totalCSAccess();
                        }
                        break;
                    case "Total Messages":
                        if (type.getSelectedItem().toString().equals("Processes") || type.getSelectedItem().toString().equals("Average")) {
                            chartPanel = m.totalNumberOfMessagesAverage();
                        }
                        else if (type.getSelectedItem().toString().equals("Special"))
                        {
                            chartPanel = m.specialMetric();
                        }                        
                        else {
                            chartPanel = m.totalNumberOfMessages();
                        }
                        break;
                    case "Sychronization Delay":
                        if (type.getSelectedItem().toString().equals("Processes")) {
                            chartPanel = m.sychronizationDelay(minmaxaverage.getSelectedItem().toString());
                        } else if (type.getSelectedItem().toString().equals("Interarrival")) {
                            chartPanel = m.sychronizationDelayAverageInterarrival();
                        } else {
                            chartPanel = m.sychronizationDelayAverage();
                        }
                        break;      
                        case "Fairness":
                            chartPanel = m.fairness();
                            break;
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SidePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SidePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (chartPanel != null) {
                ObjectFactory.getPerformancePanel().setGraphPanel(chartPanel);

                ObjectFactory.getPerformancePanel().graph.removeAll();
                chartPanel.setBounds(10, 10, ObjectFactory.getPerformancePanel().graph.getWidth() - 20, ObjectFactory.getPerformancePanel().graph.getHeight() - 20);
                ObjectFactory.getPerformancePanel().graph.add(chartPanel);
                ObjectFactory.getPerformancePanel().graph.updateUI();

                String loadedRuns = "";
                for (Iterator it = simulationRuns.getSelectedValuesList().iterator(); it.hasNext();) {
                    String object = (String) it.next();
                    loadedRuns += object + ", ";
                }
                ObjectFactory.getMessagePanel().messageDisplay.setText(jComboBox1.getSelectedItem().toString() + " has been loaded from Tracefiles :- " + loadedRuns.substring(0, loadedRuns.length() - 1));
            }

        } else {
            JPanel chartPanel = null;
            String traceFile = null;
            if (simulationRuns.getSelectedValue() != null) {
                if (!"".equals(simulationRuns.getSelectedValue().toString())) {
                    traceFile = simulationRuns.getSelectedValue().toString();
                }
            } else {
                if (ObjectFactory.getMainFrame().getCurrentRun() == null) {
                    traceFile = "trace";
                } else {
                    traceFile = ObjectFactory.getMainFrame().getCurrentRun().trim();
                }
            }

            ChartProducer c = null;
            try {
                c = new ChartProducer(traceFile);
            } catch (Exception ex) {
                Logger.getLogger(SidePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            switch (jComboBox1.getSelectedItem().toString()) {
                case "Hope Count":
                    chartPanel = c.hopCount();
                    break;
                case "Critical Sitting":
                    chartPanel = c.csSittingTime();
                    break;
                case "Response Time":
                    if (type.getSelectedItem().toString().equals("Processes")) {
                        chartPanel = c.responseTime();
                    } else {
                        chartPanel = c.responseTime(minmaxaverage.getSelectedItem().toString());
                    }
                    break;
                case "Throughput":
                    chartPanel = c.systemThroughput();
                    break;
                case "CS IDEAL Time":
                    chartPanel = c.csIdealTime();
                    break;
                case "Average number of Messages":
                    chartPanel = c.numberOfMessage();
                    break;
                case "Hop Processing":
                    if (type.getSelectedItem().toString().equals("Processes")) {
                        chartPanel = c.averageHopProcessingTime();
                    } else {
                        chartPanel = c.hopProcessing();
                    }

                    break;
                case "Total Messages":
                    chartPanel = c.totalNumberOfMessages();
                    break;
                case "Critical Section Access":
                    chartPanel = c.totalCSAccess();
                    break;
                case "Bit Complexity":
                    chartPanel = c.bitComplexity();
                    break;
                case "Sychronization Delay":
                    chartPanel = c.sychronizationDelay(minmaxaverage.getSelectedItem().toString());
                    break;
                case "Fairness":
                    chartPanel = c.calculateFairness();
                    break;
            }

            if (chartPanel != null) {
                ObjectFactory.getPerformancePanel().setGraphPanel(chartPanel);

                ObjectFactory.getPerformancePanel().graph.removeAll();
                chartPanel.setBounds(10, 10, ObjectFactory.getPerformancePanel().graph.getWidth() - 20, ObjectFactory.getPerformancePanel().graph.getHeight() - 20);
                ObjectFactory.getPerformancePanel().graph.add(chartPanel);
                ObjectFactory.getPerformancePanel().graph.updateUI();

                ObjectFactory.getMessagePanel().messageDisplay.setText(jComboBox1.getSelectedItem().toString() + " has been loaded from Tracefile->" + traceFile);
            }
        }
    }//GEN-LAST:event_showActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        // TODO add your handling code here:
        simulationRuns.clearSelection();
    }//GEN-LAST:event_clearActionPerformed

    private void detailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailsActionPerformed
        // TODO add your handling code here:
        showDetails.setText("");
        if (simulationRuns.getSelectedValuesList().size() > 1) {
            for (Iterator it = simulationRuns.getSelectedValuesList().iterator(); it.hasNext();) {
                String temp = (String) it.next();

                String[] array = temp.split(" ");

                int simulationId = Integer.parseInt(array[1]);
                showDetails.append(ObjectFactory.getSimulationRuns().get(simulationId).toString());

            }
        } else {
            String temp = (String) simulationRuns.getSelectedValue();

            String[] array = temp.split(" ");

            int simulationId = Integer.parseInt(array[1]);
            showDetails.setText(ObjectFactory.getSimulationRuns().get(simulationId).toString());
        }
    }//GEN-LAST:event_detailsActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clear;
    private javax.swing.JButton details;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox minmaxaverage;
    private javax.swing.JButton show;
    public javax.swing.JTextArea showDetails;
    private javax.swing.JList simulationRuns;
    private javax.swing.JComboBox type;
    // End of variables declaration//GEN-END:variables
}