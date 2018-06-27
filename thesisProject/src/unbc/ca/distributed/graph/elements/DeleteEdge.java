/*
 * DeleteEdgeMenuItem.java
 *
 * Created on March 21, 2007, 2:47 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking added some functions to work according to my simulator
 *
 */

package unbc.ca.distributed.graph.elements;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import unbc.ca.distributed.management.ObjectFactory;

/**
 * A class to implement the deletion of an edge from within a 
 * PopupVertexEdgeMenuMousePlugin.
 * @author Dr. Greg M. Bernstein
 */
public class DeleteEdge<E> extends JMenuItem implements EdgeListener<E> {
    private E edge;
    private VisualizationViewer visComp;
    
    /** Creates a new instance of DeleteEdgeMenuItem */
    public DeleteEdge() {
        super("Delete Edge");
        this.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                ObjectFactory.getMainFrame().updateEdges();     
                
                visComp.getPickedEdgeState().pick(edge, false);
                updateDataStructure((Edge)edge);
                
                visComp.getGraphLayout().getGraph().removeEdge(edge);                
                visComp.repaint();
            }
        });
    }

    private void updateDataStructure(Edge edge)
    {
        ObjectFactory.getEdges().remove(edge.getId());
    }
    /**
     * Implements the EdgeMenuListener interface to update the menu item with info
     * on the currently chosen edge.
     * @param edge 
     * @param visComp 
     */
    @Override
    public void setEdgeAndView(E edge, VisualizationViewer visComp) {
        this.edge = edge;
        this.visComp = visComp;
        this.setText("Delete Edge");
    }
    
}
