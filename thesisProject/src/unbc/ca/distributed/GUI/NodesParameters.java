/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package unbc.ca.distributed.GUI;

/**
 *
 * @author IDontKnow
 */
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import unbc.ca.distributed.management.ObjectFactory;
 
public class NodesParameters extends JPanel {
    private int nodeCount;
    public NodesParameters(int nodeCount) {
        super(new GridLayout(1, 1));
        this.nodeCount = nodeCount;
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
         
        JTabbedPane tabbedPane = new JTabbedPane();                 
        
        for (int i = 1; i <= nodeCount; i++) {
            NodeDetail panel1 = new NodeDetail();
            tabbedPane.addTab("Node "+i, panel1);
            ObjectFactory.getNodesDetails().put(i, panel1);
        }

        add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }       
}