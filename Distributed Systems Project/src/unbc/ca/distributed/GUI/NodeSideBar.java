/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.GUI;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.Map;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.library.Utilites;
import unbc.ca.distributed.library.Workload;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author IDontKnow
 */
public class NodeSideBar extends javax.swing.JPanel {

    /**
     * Creates new form NodeSideBar
     */
    boolean workloadCheck = false;
    private int workloadId = -1;
    public NodeSideBar(boolean workloadCheck, int workloadId) {
        initComponents();
        this.workloadCheck = workloadCheck;
        this.workloadId = workloadId;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        save = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(save)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(187, 187, 187)
                .addComponent(save)
                .addContainerGap(202, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
        if (!workloadCheck) {
            for (Map.Entry<Integer, NodeDetail> entry : ObjectFactory.getNodesDetails().entrySet()) {
                Integer integer = entry.getKey();
                NodeDetail nodeDetail = entry.getValue();
                LinkedHashMap<String, Generator> nodeDistributionDetails = new LinkedHashMap<>();

                Generator csDis = Utilites.returnDistribution(nodeDetail.distribution.getSelectedItem().toString(),
                        Integer.parseInt(nodeDetail.csDistMean.getText()), Double.parseDouble(nodeDetail.csDistVariance.getText()));
                nodeDistributionDetails.put("Critical Section", csDis);

                Generator intReqDis = Utilites.returnDistribution(nodeDetail.distribution1.getSelectedItem().toString(),
                        Integer.parseInt(nodeDetail.interDistMean.getText()), Double.parseDouble(nodeDetail.interDistVariance.getText()));
                nodeDistributionDetails.put("Inter Request", intReqDis);

                Generator delayProcess = Utilites.returnDistribution(nodeDetail.distribution2.getSelectedItem().toString(),
                        Integer.parseInt(nodeDetail.delayMean.getText()), Double.parseDouble(nodeDetail.delayVariance.getText()));
                nodeDistributionDetails.put("Hop Processing", delayProcess);

                ObjectFactory.getDistributionCollection().put(integer, nodeDistributionDetails);

            }
            ObjectFactory.getMainFrame().singleNetworkButtonClick();
        } else {
            for (Map.Entry<Integer, NodeDetail> entry : ObjectFactory.getNodesDetails().entrySet()) {
                Integer integer = entry.getKey();
                NodeDetail nodeDetail = entry.getValue();
                                LinkedHashMap<String, Generator> nodeDistributionDetails = new LinkedHashMap<>();

                Generator csDis = Utilites.returnDistribution(nodeDetail.distribution.getSelectedItem().toString(),
                        Integer.parseInt(nodeDetail.csDistMean.getText()), Double.parseDouble(nodeDetail.csDistVariance.getText()));
                nodeDistributionDetails.put("Critical Section", csDis);

                Generator intReqDis = Utilites.returnDistribution(nodeDetail.distribution1.getSelectedItem().toString(),
                        Integer.parseInt(nodeDetail.interDistMean.getText()), Double.parseDouble(nodeDetail.interDistVariance.getText()));
                nodeDistributionDetails.put("Inter Request", intReqDis);

                Generator delayProcess = Utilites.returnDistribution(nodeDetail.distribution2.getSelectedItem().toString(),
                        Integer.parseInt(nodeDetail.delayMean.getText()), Double.parseDouble(nodeDetail.delayVariance.getText()));
                nodeDistributionDetails.put("Hop Processing", delayProcess);
                Workload w = ObjectFactory.getWorkloads().get(workloadId);
                w.getWorkLoaddistributionCollection().put(integer, nodeDistributionDetails);
            }
            ObjectFactory.getMainFrame().remove();
            ObjectFactory.getWorkload().setBounds(0, 0, ObjectFactory.getMainFrame().widthOfDrawPanel, ObjectFactory.getMainFrame().heightOfDrawPanel);
            ObjectFactory.getMainFrame().getContentPane().add(ObjectFactory.getWorkload(), BorderLayout.CENTER);
            ObjectFactory.getMainFrame().getContentPane().add(ObjectFactory.getMessagePanel(), BorderLayout.PAGE_END);
            ObjectFactory.setMultiple(true);
            ObjectFactory.getMainFrame().refreshFrame();

        }
    }//GEN-LAST:event_saveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton save;
    // End of variables declaration//GEN-END:variables
}