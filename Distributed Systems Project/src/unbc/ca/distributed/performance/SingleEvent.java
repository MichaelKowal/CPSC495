/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.performance;

/**
 *
 * @author behnish
 */
public class SingleEvent {
    private String type;
    private String sender;
    private String reciever;
    
    private int localClock;
    private double simulationClock;
    private int traceObjectId;
    
    private int hopCount;
    private String content;
    private double processingTime;
      
    public double getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(double processingTime) {
        this.processingTime = processingTime;
    }        

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getTraceObjectId() {
        return traceObjectId;
    }

    public void setTraceObjectId(int traceObjectId) {
        this.traceObjectId = traceObjectId;
    }
 
    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    @Override
    public String toString() {
        return "(Type = " + type + ", Sender = " + sender + 
                ", Receiver = " + reciever + ", localClock = "
                + "" + localClock + ", Simulation Clock = " + simulationClock;
    }
}