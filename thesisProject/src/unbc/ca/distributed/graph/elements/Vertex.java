/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.graph.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import unbc.ca.distributed.management.Configuration;

/**
 *
 * @author behnish
 */
public class Vertex {

    private String label;
    private double x;
    private double y;
    private Color color;
    private boolean isIncludedInNework = true;

    public boolean isIsIncludedInNework() {
        return isIncludedInNework;
    }

    public void setIsIncludedInNework(boolean isIncludedInNework) {
        this.isIncludedInNework = isIncludedInNework;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Node " + label;
    }

    /**
     * Draw this node.
     * @param g
     */
    public void draw(Graphics2D g) {                        
        g.setColor(color);
        g.fillOval((int) x-20, (int) y-5, Configuration.radius*4, Configuration.radius*4);
        g.setColor(Color.BLACK);
        g.drawOval((int) x-20, (int) y-5, Configuration.radius*4, Configuration.radius*4);
        g.drawString("(" + label + ", N)", (int) x-20, (int) y-5);
    }

    public Point location() {
        Point p1 = new Point((int) x, (int) y);
        return p1;
    }

    public Point getCentre() {
        return new Point((int) x + Configuration.radius, (int) y + Configuration.radius);
    }
}