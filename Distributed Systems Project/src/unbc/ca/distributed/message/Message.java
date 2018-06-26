/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.message;

/**
 *
 * @author behnish
 */
import java.io.Serializable;

/**
 *
 * @author behnish
 */
public abstract class Message implements Serializable, Cloneable {

    private static final long serialVersionUID = 1366472112822681655L;
    private int clock;
    private int sender;
    private int receiver;
    private int finalReceiver = 0;
    private int finalSender = 0;
    private int hopCount = 1;
    private int parent = 0;                
    private String content;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
    
   
    public int getFinalSender() {
        return finalSender;
    }

    public void setFinalSender(int finalSender) {
        this.finalSender = finalSender;
    }

    public int getFinalReceiver() {
        return finalReceiver;
    }

    public void setFinalReceiver(int finalReceiver) {
        this.finalReceiver = finalReceiver;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    public void incHopCount() {
        this.hopCount++;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getClock() {
        return clock;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return null;
    }   

  @Override public Message clone() throws CloneNotSupportedException {
    //get initial bit-by-bit copy, which handles all immutable fields
    Message result = (Message)super.clone();

    //mutable fields need to be made independent of this object, for reasons
    //similar to those for defensive copies - to prevent unwanted access to
    //this object's internal state


    return result;
  }  

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
  
}