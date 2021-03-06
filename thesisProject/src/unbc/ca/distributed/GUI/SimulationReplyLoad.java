/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.GUI;

import java.io.File;
import java.util.ArrayList;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class SimulationReplyLoad extends javax.swing.JDialog {

    /**
     * Creates new form SimulationReplyLoad
     */
    public SimulationReplyLoad(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setTitle("Simulation Reloading Window");
        setLocationRelativeTo(null);
        getAllFilse();
    }

    private void getAllFilse() {
        File curDir = new File(".");
        ArrayList<String> avoid = new ArrayList<>();
        avoid.add("manifest.mf");
        avoid.add("build.xml");
        File[] filesList = curDir.listFiles();
        if (filesList.length != 0) {
            simulationRunsFound.removeAllItems();
        }
        for (File f : filesList) {
            if (f.isFile()) {
                if (!avoid.contains(f.getName())) {
                    String[] temp = f.getName().split("_");                    
                    if (temp.length == 1) {
                        simulationRunsFound.addItem(f.getName() + "\n");
                    }
                }
            }
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

        jLabel1 = new javax.swing.JLabel();
        load = new javax.swing.JButton();
        simulationRunsFound = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Following simulation runs have been Found in your project directory");

        load.setText("Load");
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });

        simulationRunsFound.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Run Found" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(load))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(simulationRunsFound, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(simulationRunsFound, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(load))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
        // TODO add your handling code here:
        String selectedFile = simulationRunsFound.getSelectedItem().toString();
        ObjectFactory.getMainFrame().reloadGraph(selectedFile);
        dispose();
    }//GEN-LAST:event_loadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton load;
    private javax.swing.JComboBox simulationRunsFound;
    // End of variables declaration//GEN-END:variables
}
