/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeArrowRenderingSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JPopupMenu;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.graph.elements.MouseMenus.EdgeMenu;
import unbc.ca.distributed.graph.elements.MouseMenus.VertexMenu;
import unbc.ca.distributed.graph.elements.PopupMenuPlugin;
import unbc.ca.distributed.graph.elements.Vertex;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class GraphWindow extends VisualizationViewer<Vertex, Edge> implements Printable {

    private static final long serialVersionUID = -232476657567343434L;
        
    private EditingModalGraphMouse gMouse;
    private AbstractLayout<Vertex, Edge> layout;
    private Graph<Vertex, Edge> graphObject;
    /* Nodes and edges selection */
    private PickedState<Vertex> selectedNodes = getPickedVertexState();
    private PickedState<Edge> selectedLinks = getPickedEdgeState();
    /* Zooming action for buttons */
    private ScalingControl scaler = new CrossoverScalingControl();
    public static final float ZOOM_IN_FACTOR = 1.1f;
    public static final float ZOOM_OUT_FACTOR = 0.9f;
    /* Graph Style */
    public static final Color SELECTION = Color.YELLOW;
    public static final Color LINK_STROKE = Color.GRAY;
    public static final Color LINK_STROKE_SELECTED = SELECTION;
    /* Edges  */
    protected DirectionDisplayPredicate<Vertex, Edge> show_arrow;   

//    public Graph<Vertex, Edge> getGraphObject() {
//        return graphObject;
//    }

    public void setGraphObject(Graph<Vertex, Edge> graphObject) {
        this.graphObject = graphObject;
    }

    public GraphWindow(AbstractLayout<Vertex, Edge> layout) {
        
        super(layout);

        this.layout = layout;

        colorOfNode();

        styleGraph();

        edgeControl();

        graphMouseEditor();

        popupMenuMouse();
        
        setBorder(javax.swing.BorderFactory.createEtchedBorder());
    }

    private void popupMenuMouse() {
        PopupMenuPlugin myPlugin = new PopupMenuPlugin();

        JPopupMenu edgeMenu = new EdgeMenu(ObjectFactory.getMainFrame());
        JPopupMenu vertexMenu = new VertexMenu();
        myPlugin.setEdgePopup(edgeMenu);
        myPlugin.setVertexPopup(vertexMenu);

        gMouse.remove(gMouse.getPopupEditingPlugin());  // Removes the existing popup editing plugin

        gMouse.add(myPlugin);   // Add our new plugin to the mouse       

    }
    
    private void colorOfNode() {
        Transformer<Vertex, Paint> vertexPaint = new Transformer<Vertex, Paint>() {
            @Override
            public Paint transform(Vertex i) {
                return Configuration.nodeColor;
            }
        };

        getRenderContext().setVertexFillPaintTransformer(vertexPaint);

        Transformer<Vertex, Shape> vertexSize = new Transformer<Vertex, Shape>() {
            @Override
            public Shape transform(Vertex vertex) {
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, Configuration.diameter, Configuration.diameter);
                return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
            }
        };

        getRenderContext().setVertexShapeTransformer(vertexSize);
    }

    private void graphMouseEditor() {
        gMouse = new EditingModalGraphMouse(getRenderContext(), getNodeFactory(), getLinkFactory());
        setGraphMouse(gMouse);
        addKeyListener(gMouse.getModeKeyListener());
        setModeOfGraph("Editing");
    }

    public void setModeOfGraph(String mode) {
        switch (mode) {
            case "Editing":
                gMouse.setMode(ModalGraphMouse.Mode.EDITING);
                break;
            case "Transforming":
                gMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
                break;
            case "Annoating":
                gMouse.setMode(ModalGraphMouse.Mode.ANNOTATING);
                break;
            case "Picking":
                gMouse.setMode(ModalGraphMouse.Mode.PICKING);
                break;
        }
    }

    /* Find the shortest path between two nodes */
    public List<Edge> getShortestPath(Vertex n1, Vertex n2) {

        Transformer<Edge, Integer> wtTransformer = new Transformer<Edge, Integer>() {
            @Override
            public Integer transform(Edge link) {
                return link.getWeight();
            }
        };

        DijkstraShortestPath alg = new DijkstraShortestPath(graphObject, wtTransformer);
        
        List<Edge> list = alg.getPath(n1, n2);
        
        return list;
    }
    
    /* Find the shortest path between two nodes */
    public List<Edge> getLessHopPath(Vertex n1, Vertex n2) 
    {
        List<Edge> list = null;        
        int depth = getShortestPath(n1, n2).size() + 1 ;
        
        GetAllPaths flood = new GetAllPaths(graphObject, depth);
        
        list = flood.firstPathBest(flood.getAllCycles(n1, n2));
        
        if(list.size()<1)
        {
            System.out.println("I am chooseing shortest path because no less hop count is available");
            return getShortestPath(n1, n2);
        }
        
        return list;
    }

    private void styleGraph() {

        getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Vertex, Edge>());
        getRenderContext().setArrowDrawPaintTransformer(new ConstantTransformer(Color.black));
        getRenderer().getEdgeRenderer().setEdgeArrowRenderingSupport(new BasicEdgeArrowRenderingSupport());

        /* Setting the labels in the graph */

        getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<Edge>());
        getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Vertex>());
        getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);


        /* For node selecttion */

        getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Vertex>(selectedNodes, Configuration.nodeColor, SELECTION) {
            @Override
            public Paint transform(Vertex node) {
                if (pi.isPicked(node)) {
                    return picked_paint;
                } else {
                    return fill_paint;
                }
            }
        });

        /* Edge Selection */

        getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<>(selectedLinks,
                LINK_STROKE,
                LINK_STROKE_SELECTED));

        /* Arrow for edges*/

        show_arrow = new DirectionDisplayPredicate<>(true, false);
        show_arrow.showUndirected(true);

        getRenderContext().setEdgeArrowPredicate(show_arrow);                
    }

    private void edgeControl() {
        Transformer<Edge, Paint> edgePaint = new Transformer<Edge, Paint>() {
            @Override
            public Paint transform(Edge s) {
                return Configuration.edgeColor;
            }
        };
        Transformer<Edge, Stroke> edgeStroke = new Transformer<Edge, Stroke>() {
            @Override
            public Stroke transform(Edge s) {
                return new BasicStroke(Configuration.edgeThickness);
            }
        };

        getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
        getRenderContext().setEdgeStrokeTransformer(edgeStroke);
    }

    /* To get all the coordinates of all nodes */
    public Point2D getNodePostion(Vertex node) {
        double x = layout.getX(node);
        double y = layout.getY(node);

        Point2D point = new Point2D.Double(x, y);

        return point;
    }

    public Factory<Vertex> getNodeFactory() {
        return new Factory<Vertex>() {
            @Override
            public Vertex create() {
                Vertex v = new Vertex();
                
                ObjectFactory.incNodeCount();
                ObjectFactory.getNodes().put(ObjectFactory.getNodeCount(), v);
                v.setLabel(String.valueOf(ObjectFactory.getNodeCount()));
                v.setColor(Configuration.nodeColor);
                                
                return v;
            }
        };
    }

    public Factory<Edge> getLinkFactory() {
        return new Factory<Edge>() {
            int weight = Configuration.defaultWeightOnEdges;

            @Override
            public Edge create() 
            {
                Edge e = new Edge(weight);
                ObjectFactory.incEdgeCount();
                e.setId(ObjectFactory.getEdgeCount());               
                ObjectFactory.getEdges().put(ObjectFactory.getEdgeCount(), e);                
                
                return e;
            }
        };
    }

    public void selectAll() {
        PickedState<Vertex> nodesSelected = getPickedVertexState();
        PickedState<Edge> edges = getPickedEdgeState();

        for (Edge link : graphObject.getEdges()) {
            edges.pick(link, true);
        }

        for (Vertex node : graphObject.getVertices()) {
            nodesSelected.pick(node, true);
        }
    }

    public void unselectAll() {

        PickedState<Vertex> nodesUnselect = getPickedVertexState();
        PickedState<Edge> linksUnslect = getPickedEdgeState();

        nodesUnselect.clear();
        linksUnslect.clear();
    }

    public void zoomIn() {
        scaler.scale(this, ZOOM_IN_FACTOR, ObjectFactory.getMainFrame().getCenter());
    }

    public void zoomOut() {
        scaler.scale(this, ZOOM_OUT_FACTOR, ObjectFactory.getMainFrame().getCenter());
    }

    /**
     * copy the visible part of the graph to a file as a jpeg image
     *
     * @param file
     */
    public void writeJPEGImage(File file) {
        int width = getWidth();
        int height = getHeight();

        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bi.createGraphics();
        paint(graphics);
        graphics.dispose();

        try {
            ImageIO.write(bi, "jpeg", file);
        } catch (IOException e) {
            System.out.println("Error: "+e);
        }
    }

    @Override
    public int print(java.awt.Graphics graphics,
            java.awt.print.PageFormat pageFormat, int pageIndex)
            throws java.awt.print.PrinterException {
        if (pageIndex > 0) {
            return (Printable.NO_SUCH_PAGE);
        } else {
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphics;
            setDoubleBuffered(false);
            g2d.translate(pageFormat.getImageableX(), pageFormat
                    .getImageableY());

            paint(g2d);
            setDoubleBuffered(true);

            return (Printable.PAGE_EXISTS);
        }
    }

    public void colorChooser() {
        Color color = Configuration.nodeColor;
        color = JColorChooser.showDialog(
                this, "Choose a color", color);
        if (color != null) {
            Configuration.nodeColor = color;
            colorOfNode();
        }
        repaint();
    }

    /* For showing the arrows in the graph */
    private final static class DirectionDisplayPredicate<V, E> implements Predicate<Context<Graph<V, E>, E>> {

        protected boolean show_d;
        protected boolean show_u;

        public DirectionDisplayPredicate(boolean show_d, boolean show_u) {
            this.show_d = show_d;
            this.show_u = show_u;
        }

        public void showDirected(boolean b) {
            show_d = b;
        }

        public void showUndirected(boolean b) {
            show_u = b;
        }

        @Override
        public boolean evaluate(Context<Graph<V, E>, E> context) {
            Graph<V, E> graph = context.graph;
            E e = context.element;
            if (graph.getEdgeType(e) == EdgeType.DIRECTED && show_d) {
                return true;
            }
            return graph.getEdgeType(e) == EdgeType.UNDIRECTED && show_u;
        }
    }

    public void deleteSelected() {
        PickedState<Vertex> nodesD = getPickedVertexState();
        PickedState<Edge> links = getPickedEdgeState();

        Set<Vertex> selectDeletedN = new HashSet<>(nodesD.getPicked());
        Set<Edge> selectedLs = new HashSet<>(links.getPicked());

        unselectAll();

        Set<Vertex> nodesToDelete = new HashSet<>();
        Set<Edge> linksToDelete = new HashSet<>();

        for (Edge edge : selectedLs) {
            linksToDelete.add(edge);
        }

        for (Vertex node : selectDeletedN) {
            for (Edge edge : graphObject.getIncidentEdges(node)) {

                linksToDelete.add(edge);
            }

            nodesToDelete.add(node);
        }

        for (Edge edge : linksToDelete) {
            graphObject.removeEdge(edge);
        }

        for (Vertex node : nodesToDelete) {
            graphObject.removeVertex(node);
        }
        repaint();
    }   
}