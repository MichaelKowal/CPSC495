/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph.elements;

import cern.colt.matrix.linalg.Blas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import unbc.ca.distributed.management.Configuration;

/**
 *
 * @author behnish
 */
public class Edge implements Comparable {

    private int weight;
    private int id;
    private Vertex source;
    private Vertex destination;
    private double ticks;
    private double newDistamce;

    public double getNewDistamce() {
        return newDistamce;
    }

    public void setNewDistamce(double newDistamce) {
        this.newDistamce = newDistamce;
    }        

    public double getTicks() {
        return ticks;
    }

    public void setTicks(double ticks) {
        this.ticks = ticks;
    }       

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Edge(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge: " + id + " - W: " + weight;
    }

    @Override
    public int compareTo(Object ob) {
        if (!(ob instanceof Edge)) {
            throw new ClassCastException("Invalid object");
        }

        int objectId = ((Edge) ob).getId();

        if (this.getId() > objectId) {
            return 1;
        } else if (this.getId() < objectId) {
            return -1;
        } else {
            return 0;
        }

    }

    public void draw(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;

        g.setStroke(new BasicStroke(Configuration.edgeThickness));

        Point p1 = new Point((int) source.getX(), (int) source.getY());
        Point p2 = new Point((int) destination.getX(), (int) destination.getY());
               
        int x1,y1;
        x1 = (p1.x+p2.x)/2;
        y1 = (p1.y+p2.y)/2;
        
        g.drawString("W-"+weight, x1, y1);        
        
        g.setColor(Configuration.edgeColor);

        g.drawLine(p1.x, p1.y+20, p2.x, p2.y+20);
        g.setStroke(new BasicStroke(Configuration.edgeThickness));
        g.setColor(Color.BLACK);
    }
    
    
}
