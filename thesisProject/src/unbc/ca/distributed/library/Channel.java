/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

/**
 *
 * @author behnish
 */
public class Channel extends Port {

    private double delay;
    private String name;
    private int state;
    private boolean active  = false;
    
    public static int SENT = 1;
    public static int RECEIVED = 2;
    public static int IDLE = 3;
    
    private int sourceNodeId;
    private int destinationNodeId;

    public int getSourceNodeId() {
        return sourceNodeId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean direction) {
        this.active = direction;
    }    

    public void setSourceNodeId(int sourceNodeId) {
        this.sourceNodeId = sourceNodeId;
    }

    public int getDestinationNodeId() {
        return destinationNodeId;
    }

    public void setDestinationNodeId(int destinationNodeId) {
        this.destinationNodeId = destinationNodeId;
    } 

    public synchronized void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public Channel(String name) {
        super(name);
        this.name = name;
        state = Channel.IDLE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}