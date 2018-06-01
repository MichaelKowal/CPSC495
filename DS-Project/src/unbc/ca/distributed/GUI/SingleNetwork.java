/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.GUI;

import java.awt.BorderLayout;
import java.io.File;
import java.util.LinkedHashMap;
import unbc.ca.distributed.database.DatabaseConn;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.library.Utilites;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class SingleNetwork extends javax.swing.JPanel {

    /**
     * Creates new form SingleNetwork
     */
    public SingleNetwork() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        algorithm = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        database = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        constantCheck = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        simulationRunTime = new javax.swing.JTextField();
        simulate = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        distribution = new javax.swing.JComboBox();
        first = new javax.swing.JLabel();
        csDistMean = new javax.swing.JTextField();
        second = new javax.swing.JLabel();
        csDistVariance = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        distribution1 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        first1 = new javax.swing.JLabel();
        interDistMean = new javax.swing.JTextField();
        second1 = new javax.swing.JLabel();
        interDistVariance = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        distribution2 = new javax.swing.JComboBox();
        first2 = new javax.swing.JLabel();
        delayMean = new javax.swing.JTextField();
        second2 = new javax.swing.JLabel();
        delayVariance = new javax.swing.JTextField();
        customize = new javax.swing.JButton();

        jLabel9.setText("jLabel9");

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Algorithm Name:");

        algorithm.setModel(new javax.swing.DefaultComboBoxModel(Utilites.names()));

        jLabel17.setText("Use Database:");

        database.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        database.setSelectedIndex(1);

        jLabel16.setText("Disable distribution for Hop P :");

        constantCheck.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Yes", "No" }));
        constantCheck.setSelectedIndex(1);
        constantCheck.setToolTipText("");

        jLabel10.setText("Simulation Run Time");

        simulationRunTime.setText("10000");

        simulate.setText("Simulate");
        simulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel10))
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(simulationRunTime, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(constantCheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(algorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(database, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(simulate)
                        .addGap(86, 86, 86))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(algorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(database, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(simulate))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(constantCheck, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(simulationRunTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("Distribution for CS");

        distribution.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binomial", "Poisson", "Uniform", "Exponential", "Gaussian" }));
        distribution.setSelectedIndex(2);
        distribution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distributionActionPerformed(evt);
            }
        });

        first.setText("No of Trails");

        csDistMean.setText("1");

        second.setText("Probability");

        csDistVariance.setText("5");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel4)
                        .addComponent(first))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(second)))
                .addGap(63, 63, 63)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(distribution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(csDistMean, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(csDistVariance, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(distribution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(first)
                    .addComponent(csDistMean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(second)
                    .addComponent(csDistVariance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        distribution1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binomial", "Poisson", "Uniform", "Exponential", "Gaussian" }));
        distribution1.setSelectedIndex(1);
        distribution1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distribution1ActionPerformed(evt);
            }
        });

        jLabel14.setText("Distribution Inter Request Time");

        first1.setText("Min");

        interDistMean.setText("10");
        interDistMean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interDistMeanActionPerformed(evt);
            }
        });

        second1.setText("Max");

        interDistVariance.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(second1)
                    .addComponent(first1)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(interDistMean, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distribution1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(interDistVariance, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(236, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(distribution1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(first1)
                    .addComponent(interDistMean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(second1)
                    .addComponent(interDistVariance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setText("Distribution Hop Processing");

        distribution2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Binomial", "Poisson", "Uniform", "Exponential", "Gaussian" }));
        distribution2.setSelectedIndex(1);
        distribution2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distribution2ActionPerformed(evt);
            }
        });

        first2.setText("Mean");

        delayMean.setText("5");

        second2.setText("Variance");

        delayVariance.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(second2)
                    .addComponent(first2)
                    .addComponent(jLabel15))
                .addGap(37, 37, 37)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delayMean, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delayVariance, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(distribution2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(distribution2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(first2)
                    .addComponent(delayMean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(delayVariance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(second2))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        customize.setText("Customize Node Parms");
        customize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customizeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(customize)))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(customize)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void simulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateActionPerformed
        // TODO add your handling code here:                
        ObjectFactory.getMessagePanel().messageDisplay.setText("");

        if (!ObjectFactory.isCustomizeNode()) {
            for (int i = 1; i <= ObjectFactory.getNodeCount(); i++) {
                LinkedHashMap<String, Generator> nodeDistributionDetails = new LinkedHashMap<>();
                Generator csDis = Utilites.returnDistribution(distribution.getSelectedItem().toString(), Integer.parseInt(csDistMean.getText()), Double.parseDouble(csDistVariance.getText()));
                nodeDistributionDetails.put("Critical Section", csDis);

                Generator intReqDis = Utilites.returnDistribution(distribution1.getSelectedItem().toString(), Integer.parseInt(interDistMean.getText()), Double.parseDouble(interDistVariance.getText()));
                nodeDistributionDetails.put("Inter Request", intReqDis);

                Generator delayProcess = Utilites.returnDistribution(distribution2.getSelectedItem().toString(), Integer.parseInt(delayMean.getText()), Double.parseDouble(delayVariance.getText()));
                nodeDistributionDetails.put("Hop Processing", delayProcess);

                ObjectFactory.getDistributionCollection().put(i, nodeDistributionDetails);
            }
        }

        if (constantCheck.getSelectedItem().toString().equals("Yes")) {
            Configuration.constantValue = 1;
        } else {
            Configuration.constantValue = 0;
        }
        if (database.getSelectedItem().toString().equals("Yes")) {
            Configuration.database = "Yes";
            DatabaseConn conn = new DatabaseConn(Configuration.host, Configuration.port, Configuration.databaseName, Configuration.username, Configuration.password);
            ObjectFactory.setConnection(conn);
        } else {
            Configuration.database = "No";
        }

        Configuration.simulationLength = Double.parseDouble(simulationRunTime.getText());

        ObjectFactory.getMainFrame().startSimulation(algorithm.getSelectedItem().toString());
        File file = new File("trace_network");
        if (file.exists()) {
            file.delete();
        }
        ObjectFactory.getMainFrame().saveGraphIntoFile("trace_network");
    }//GEN-LAST:event_simulateActionPerformed

    private void interDistMeanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interDistMeanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_interDistMeanActionPerformed

    private void customizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customizeActionPerformed
        // TODO add your handling code here:
        ObjectFactory.setCustomizeNode(true);

        if (ObjectFactory.getNodeCount() > 0) {
            ObjectFactory.getMainFrame().remove();
            NodesParameters nodeDetails = new NodesParameters(ObjectFactory.getNodeCount());
            nodeDetails.setBounds(0, 0, ObjectFactory.getMainFrame().widthOfDrawPanel - 800, ObjectFactory.getMainFrame().heightOfDrawPanel);

            NodeSideBar s = new NodeSideBar(false,-1);
            s.setBounds(ObjectFactory.getMainFrame().widthOfDrawPanel - 800, 0, 800, ObjectFactory.getMainFrame().heightOfDrawPanel);

            ObjectFactory.getMainFrame().add(nodeDetails, BorderLayout.CENTER);
            ObjectFactory.getMainFrame().add(s, BorderLayout.LINE_END);
            ObjectFactory.getMainFrame().refreshFrame();
        } else {
            ObjectFactory.getMainFrame().showErrorMessage("No Network to customize");
        }

    }//GEN-LAST:event_customizeActionPerformed

    private void distribution2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distribution2ActionPerformed
        // TODO add your handling code here:
        switch (distribution2.getSelectedItem().toString()) {
            case "Binomial":
                delayVariance.setEnabled(true);
                first2.setText("Trails");
                second2.setText("Probability");
                break;
            case "Poisson":
                first2.setText("Mean");
                delayVariance.setEnabled(false);
                second2.setText("");
                break;
            case "Uniform":
                delayVariance.setEnabled(true);
                first2.setText("Min");
                second2.setText("Max");
                break;
            case "Exponential":
                delayVariance.setEnabled(false);
                second2.setText("");
                first2.setText("Rate");
                break;
            case "Gaussian":
                delayVariance.setEnabled(true);
                first2.setText("Mean");
                second2.setText("Standard Deviation");
                break;
            case "default":
                delayVariance.setEnabled(true);
                first2.setText("Trails");
                second2.setText("Probability");
                break;
        }
    }//GEN-LAST:event_distribution2ActionPerformed

    private void distributionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distributionActionPerformed
        // TODO add your handling code here:
        switch (distribution.getSelectedItem().toString()) {
            case "Binomial":
                csDistVariance.setEnabled(true);
                first.setText("Trails");
                second.setText("Probability");
                break;
            case "Poisson":
                first.setText("Mean");
                csDistVariance.setEnabled(false);
                second.setText("");
                break;
            case "Uniform":
                csDistVariance.setEnabled(true);
                first.setText("Min");
                second.setText("Max");
                break;
            case "Exponential":
                csDistVariance.setEnabled(false);
                second.setText("");
                first.setText("Rate");
                break;
            case "Gaussian":
                csDistVariance.setEnabled(true);
                first.setText("Mean");
                second.setText("Standard Deviation");
                break;
            case "default":
                csDistVariance.setEnabled(true);
                first.setText("Trails");
                second.setText("Probability");
                break;
        }
    }//GEN-LAST:event_distributionActionPerformed

    private void distribution1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distribution1ActionPerformed
        // TODO add your handling code here:
        switch (distribution1.getSelectedItem().toString()) {
            case "Binomial":
                interDistVariance.setEnabled(true);
                first1.setText("Trails");
                second1.setText("Probability");
                break;
            case "Poisson":
                first1.setText("Mean");
                interDistVariance.setEnabled(false);
                second1.setText("");
                break;
            case "Uniform":
                interDistVariance.setEnabled(true);
                first1.setText("Min");
                second1.setText("Max");
                break;
            case "Exponential":
                interDistVariance.setEnabled(false);
                second1.setText("");
                first1.setText("Rate");
                break;
            case "Gaussian":
                interDistVariance.setEnabled(true);
                first1.setText("Mean");
                second1.setText("Standard Deviation");
                break;
            case "default":
                interDistVariance.setEnabled(true);
                first1.setText("Trails");
                second1.setText("Probability");
                break;
        }
    }//GEN-LAST:event_distribution1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox algorithm;
    private javax.swing.JComboBox constantCheck;
    private javax.swing.JTextField csDistMean;
    private javax.swing.JTextField csDistVariance;
    private javax.swing.JButton customize;
    private javax.swing.JComboBox database;
    private javax.swing.JTextField delayMean;
    private javax.swing.JTextField delayVariance;
    private javax.swing.JComboBox distribution;
    private javax.swing.JComboBox distribution1;
    private javax.swing.JComboBox distribution2;
    private javax.swing.JLabel first;
    private javax.swing.JLabel first1;
    private javax.swing.JLabel first2;
    private javax.swing.JTextField interDistMean;
    private javax.swing.JTextField interDistVariance;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel second;
    private javax.swing.JLabel second1;
    private javax.swing.JLabel second2;
    private javax.swing.JButton simulate;
    private javax.swing.JTextField simulationRunTime;
    // End of variables declaration//GEN-END:variables
}
