/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.trace;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author behnish
 */
public class TraceObject extends Event {

    private String type;
    private String sender;
    private String reciever;
    private int localClock;
    private double simulationClock;
    private int traceObjectId;
    private int hopCount;
    private boolean lateEvent;
    private Color color;
    private boolean addedOrNot = false;
    private int weight;
    private boolean animationFinished = false;
    private boolean console = false;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    public void setTickMe(double differenceTick) {
        super.setTicksMe(differenceTick);
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public boolean isAnimationFinished() {
        return animationFinished;
    }

    public void setAnimationFinished(boolean animationFinished) {
        this.animationFinished = animationFinished;
    }

    @Override
    public void setStep(int speed) {
        super.setStep(speed);
    }

    public TraceObject(Point p1, Point p2, int step) {
        super(p1, p2, step);
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void setDt(int time) {
        super.setDt(time);
    }

    public boolean isAddedOrNot() {
        return addedOrNot;
    }

    public void setAddedOrNot(boolean addedOrNot) {
        this.addedOrNot = addedOrNot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public int getLocalClock() {
        return localClock;
    }

    public void setLocalClock(int localClock) {
        this.localClock = localClock;
    }

    public double getSimulationClock() {
        return simulationClock;
    }

    public void setSimulationClock(double simulationClock) {
        this.simulationClock = simulationClock;
    }

    public boolean isLateEvent() {
        return lateEvent;
    }

    public void setLateEvent(boolean lateEvent) {
        this.lateEvent = lateEvent;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getTraceObjectId() {
        return traceObjectId;
    }

    public void setTraceObjectId(int traceObjectId) {
        this.traceObjectId = traceObjectId;
    }

    @Override
    public void draw(Graphics2D g) {
        Point p = currentLocation();
        if (isLateEvent()) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        //g.fillOval(p.x, p.y, 10, 10);
        //g.drawOval(p.x, p.y, 10, 10);
        g.drawString(content(), p.x, p.y);
    }

    @Override
    public String toString() {
        return "(Type = " + type + ", Sender = " + sender + ", localClock = " + localClock + ", Simulation Clock = " + simulationClock + ", lateEvent = " + lateEvent + ")";
    }

    public String shortDes() {
        return "(S-" + sender + ",R-" + reciever + ",C-" + simulationClock + ")";
    }

    public String content() {
        if (content != null) {
            switch (content) {
                case "1":
                    return "REQ";
                case "2":
                    return "REP";
                case "3":
                    return "RELS";
                case "4":
                    return "FLUSH";
                case "5":
                    return "       TOK";
                default:
                    return content;
            }
        } else {
            return "Msg";
        }
    }
}
