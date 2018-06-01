/*
 * MyMouseMenus.java
 *
 * Created on March 21, 2007, 3:34 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */

package unbc.ca.distributed.graph.elements;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * A collection of classes used to assemble pop up mouse menus for the custom
 * edges and vertices developed in this example.
 * @author Dr. Greg M. Bernstein
 */
public class MouseMenus {
    
    public static class EdgeMenu extends JPopupMenu {        
        // private JFrame frame; 
        public EdgeMenu(final JFrame frame) {
            super("Edge Menu");
            // this.frame = frame;
            this.add(new DeleteEdge<Edge>());
            this.addSeparator();
            this.add(new WeightDisplay());            
            this.addSeparator();
            this.add(new EdgePropItem(frame));           
        }
        
    }
    
    public static class EdgePropItem extends JMenuItem implements EdgeListener<Edge>,
            MenuPointListener {
        Edge edge;
        VisualizationViewer visComp;
        Point2D point;
        
        @Override
        public void setEdgeAndView(Edge edge, VisualizationViewer visComp) {
            this.edge = edge;
            this.visComp = visComp;
        }

        @Override
        public void setPoint(Point2D point) {
            this.point = point;
        }
        
        public  EdgePropItem(final JFrame frame) {            
            super("Edit Edge Properties...");
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EdgePropertyDialog dialog = new EdgePropertyDialog(frame, edge);
                    dialog.setLocation((int)point.getX()+ frame.getX(), (int)point.getY()+ frame.getY());
                    dialog.setVisible(true);
                }
                
            });
        }
        
    }
    public static class WeightDisplay extends JMenuItem implements EdgeListener<Edge> {
        @Override
        public void setEdgeAndView(Edge e, VisualizationViewer visComp) {
            this.setText("Weight =" + e.getWeight());
        }
    }        
    
    public static class VertexMenu extends JPopupMenu {
        public VertexMenu() {
            super("Node Menu");
            this.add(new DeleteNode<Vertex>());
            this.addSeparator();
        }
    }
}

