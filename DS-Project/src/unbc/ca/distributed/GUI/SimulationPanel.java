package unbc.ca.distributed.GUI;

import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import unbc.ca.distributed.library.ConfigTableModel;
import unbc.ca.distributed.library.MultipleSimulation;
import unbc.ca.distributed.library.SimulationRun;
import unbc.ca.distributed.library.Utilites;
import unbc.ca.distributed.library.Workload;
import unbc.ca.distributed.management.ObjectFactory;

// Lodha and Kalyani, Lamport Mutual Exclusion, Roucairol and Carvalho, Ricart Agarwala
public class SimulationPanel extends javax.swing.JPanel {

    int count = 0;
    private DefaultListModel listModel;

    /**
     * Creates new form Configuration
     */
    public SimulationPanel() {
        initComponents();
        listModel = new DefaultListModel();
        ObjectFactory.getMainFrame().setT("Simulation Run Panel");
        loadWorkloadList();
    }

    private void loadWorkloadList() {
        workloadsList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = workloads();

            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private String[] workloads() {
        String[] workloads = new String[ObjectFactory.getWorkloads().size()];
        for (Map.Entry<Integer, Workload> entry : ObjectFactory.getWorkloads().entrySet()) {
            int index = entry.getKey();
            Workload workload1 = entry.getValue();
            workloads[index] = workload1.getName();
        }
        return workloads;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        addToSimulations = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        simulateRuns = new javax.swing.JButton();
        back = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        algorithm = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        workloadsList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        simulationProgress = new javax.swing.JProgressBar();

        setLayout(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Mutual Exclusion Algorithm");

        addToSimulations.setText("<html> Add to simulation run &darr;</html>");
        addToSimulations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToSimulationsActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Simulation Id", "Mutual Exclusion Algorithm", "Workload"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setText("Workload");

        simulateRuns.setText("Start");
        simulateRuns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateRunsActionPerformed(evt);
            }
        });

        back.setText(" Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        algorithm.setModel(new javax.swing.AbstractListModel() {

            String[] strings = Utilites.names();

            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(algorithm);

        jButton1.setText("Delete");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(workloadsList);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel3)
                        .add(39, 39, 39)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 126, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(29, 29, 29)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(121, 121, 121)
                        .add(addToSimulations, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 212, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(22, 22, 22)
                        .add(back)
                        .add(669, 669, 669)
                        .add(simulateRuns))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(0, 33, Short.MAX_VALUE)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 722, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 38, Short.MAX_VALUE)
                        .add(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(38, 38, 38)
                        .add(jLabel3)
                        .add(9, 9, 9)
                        .add(addToSimulations, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(38, 38, 38)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(38, 38, 38)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel4)
                            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 54, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .add(18, 18, 18)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(simulateRuns)
                    .add(back))
                .add(6, 6, 6))
        );

        add(jPanel1);
        jPanel1.setBounds(20, 67, 870, 470);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Simulations");
        add(jLabel1);
        jLabel1.setBounds(30, 20, 119, 29);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        add(jScrollPane3);
        jScrollPane3.setBounds(20, 630, 870, 96);

        jLabel2.setText("Debug Messages");
        add(jLabel2);
        jLabel2.setBounds(20, 600, 110, 14);

        jLabel5.setText("Progress");
        add(jLabel5);
        jLabel5.setBounds(20, 560, 110, 14);
        add(simulationProgress);
        simulationProgress.setBounds(90, 560, 146, 20);
    }// </editor-fold>//GEN-END:initComponents

private void addToSimulationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToSimulationsActionPerformed
    for (int i = 0; i < algorithm.getSelectedValuesList().size(); i++) {
        for (int j = 0; j < workloadsList.getSelectedValuesList().size(); j++) {
            int workloadId = Integer.parseInt(workloadsList.getSelectedValuesList().get(j).toString().split(" ")[1]);
            SimulationRun s = new SimulationRun();
            s.setAlgorithm(algorithm.getSelectedValuesList().get(i).toString());
            s.setWorkload(workloadsList.getSelectedValuesList().get(j).toString());
            s.setWorkloadId(workloadId);
            s.setWorkloadObject(ObjectFactory.getWorkloads().get(workloadId));

            s.setSimulationRunid(count);
            s.setName("Run " + count);
            ObjectFactory.getSimulationRuns().put(count, s);
            count++;
        }
    }
    jTable1.setModel(new ConfigTableModel(ObjectFactory.getSimulationRuns()));
}//GEN-LAST:event_addToSimulationsActionPerformed

private void simulateRunsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateRunsActionPerformed
// TODO add your handling code here:                   
    MultipleSimulation mSimulations = new MultipleSimulation();
    mSimulations.start();
}//GEN-LAST:event_simulateRunsActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        // TODO add your handling code here:

        int dialogResult = JOptionPane.showConfirmDialog(null, "You will loose previous workloads by going back");
        if (dialogResult == JOptionPane.YES_OPTION) {
            ObjectFactory.getMainFrame().getContentPane().removeAll();
            WorkloadGenerator w = new WorkloadGenerator();
            w.setBounds(0, 0, ObjectFactory.getMainFrame().widthOfDrawPanel, ObjectFactory.getMainFrame().heightOfDrawPanel);

            ObjectFactory.getMainFrame().getContentPane().add(w);
            ObjectFactory.getMainFrame().refreshFrame();
        }


    }//GEN-LAST:event_backActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:        
        int[] toDelete = jTable1.getSelectedRows();
        if (toDelete.length > 0) {
            for (int i = 0; i < toDelete.length; i++) {
                ObjectFactory.getSimulationRuns().remove(toDelete[i]);
                count--;
            }
            repaint();
        } else {
            ObjectFactory.getMainFrame().showErrorMessage("Please select atleast one simulation run");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToSimulations;
    private javax.swing.JList algorithm;
    private javax.swing.JButton back;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    public javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton simulateRuns;
    public javax.swing.JProgressBar simulationProgress;
    private javax.swing.JList workloadsList;
    // End of variables declaration//GEN-END:variables
}
