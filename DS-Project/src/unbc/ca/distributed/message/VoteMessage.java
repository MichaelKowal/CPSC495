/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.message;

/**
 *
 * @author behnish
 */
public class VoteMessage extends Message{

    public int type = -1, data;
    public int src, dst;
    public static final int INQUIRE = 1, REQUEST = 2, RELEASE = 3, RELINQUISH = 4, ACK = 5;

    public VoteMessage(int s, int d, int typ, int dta) {
        src = s;
        dst = d;
        type = typ;
        data = dta;
    }

    public VoteMessage(int typ, int dta) {        
        type = typ;
        data = dta;
    }
    
    public VoteMessage(VoteMessage old) {
        src = old.src;
        dst = old.dst;
        type = old.type;
        data = old.data;
    }

    public VoteMessage setdest(int d) {
        dst = d;
        return (this);
    }

    public String getText() {
        switch (type) {
            case INQUIRE:
                return (data + ": inquire (" + src + "-" + dst + ")");
            case REQUEST:
                return (data + ": request (" + src + "-" + dst + ")");
            case RELEASE:
                return ("release (" + src + "-" + dst + ")");
            case RELINQUISH:
                return ("relinquish (" + src + "-" + dst + ")");
            case ACK:
                return ("ack " + (data == 0 ? "neg" : "pos")
                        + "(" + src + "-" + dst + ")");
        }
        return ("message type not known");
    }
}
