/*
 * DeleteVertexMenuItem.java
 *
 * Created on March 21, 2007, 2:03 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */

package unbc.ca.distributed.graph.elements;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JMenuItem;
import unbc.ca.distributed.management.ObjectFactory;

/**
 * A class to implement the deletion of a vertex from within a 
 * PopupVertexEdgeMenuMousePlugin.
 * @author Dr. Greg M. Bernstein
 */
public class DeleteNode<V> extends JMenuItem implements NodeMenuListener<V> {
    private V vertex;
    private VisualizationViewer visComp;
    
    /** Creates a new instance of DeleteVertexMenuItem */
    public DeleteNode() {
        super("Delete Vertex");
        this.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                ObjectFactory.getMainFrame().updateEdges();                
                visComp.getPickedVertexState().pick(vertex, false);                
                visComp.getGraphLayout().getGraph().removeVertex(vertex);
                updateDataStructure(Integer.parseInt(((Vertex)vertex).getLabel()));                
                visComp.repaint();
            }          
        });
    }

    private void updateDataStructure(int nodeId)
    {                                               
        Vertex ver =  ObjectFactory.getNodes().get(nodeId);
        for (Map.Entry<Integer, Edge> entry : ObjectFactory.getEdges().entrySet()) 
        {
            Integer integer = entry.getKey();
            Edge edge = entry.getValue();            
            if(edge.getDestination() == ver || edge.getSource() == ver)
            {                
                ObjectFactory.getEdges().remove(integer);                
            }
        }
        ObjectFactory.getNodes().remove(nodeId);            
    }
    /**
     * Implements the VertexMenuListener interface.
     * @param v 
     * @param visComp 
     */
    @Override
    public void setVertexAndView(V v, VisualizationViewer visComp) {
        this.vertex = v;
        this.visComp = visComp;
        this.setText("Delete " + v.toString());
    }
    
}
