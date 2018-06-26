/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.message;

import java.io.Serializable;

/**
 *
 * @author behnish
 */
public class TimeLogical implements Serializable {

    public int clock;
    public int process;

    public TimeLogical(int clock, int process) {
        this.clock = clock;
        this.process = process;
    }

    public boolean lessThan(TimeLogical t) {        
        return 
                clock < t.clock
                || (clock == t.clock && process < t.process) 
                || (clock == t.clock && process == t.process);
    }
    public static TimeLogical copy(TimeLogical time)
    {
        return new TimeLogical(time.clock, time.process);        
    }
    @Override
    public String toString()
    {
        return clock+","+process;
    }
}
