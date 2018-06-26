/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.message;

/**
 *
 * @author behnish
 */
public class Msg extends Message {

    public static final int REQUEST = 1;
    public static final int REPLY = 2;
    public static final int RELEASE = 3;
    public static final int FLUSH = 4;    

    public int type;
    public TimeLogical time;

    public Msg(int type, TimeLogical time) {
        this.type = type;
        this.time = time;
        setContent();
    }

    private void setContent() {
        super.setContent(String.valueOf(type));
    }
}
