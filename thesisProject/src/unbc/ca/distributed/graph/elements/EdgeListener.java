/*
 * EdgeMenuListener.java
 *
 * Created on March 21, 2007, 2:41 PM; Updated May 29, 2007
 * Copyright March 21, 2007 Grotto Networking
 */

package unbc.ca.distributed.graph.elements;

import edu.uci.ics.jung.visualization.VisualizationViewer;

    /**
     * An interface for menu items that are interested in knowing the currently selected edge and
     * its visualization component context.  Used with PopupVertexEdgeMenuMousePlugin.
     * @author Dr. Greg M. Bernstein
     * @param <E>
     */
    public interface EdgeListener<E> {
    /**
     * Used to set the edge and visualization component.
     * @param e 
     * @param visView 
     */
     void setEdgeAndView(E e, VisualizationViewer visView); 
    
}
