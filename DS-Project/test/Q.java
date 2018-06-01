
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import unbc.ca.distributed.message.TimeLogical;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author behnish
 */
public class Q {

    int process;
    int clock;
    

    public Q(int process, int clock) {
        this.process = process;
        this.clock = clock;
    }

    @Override
    public String toString() {
        return process + "," + clock;
    }
   
    public boolean lessThan(Q t) {
        return clock < t.clock
                || (clock == t.clock && process < t.process) || (clock == t.clock && process == t.process);
    }
}
