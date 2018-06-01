
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author behnish
 */
public class Main {

    LinkedList<Q> queue = new LinkedList<>();

    public void add() {

        queue.add(new Q(1, 1));
        queue.add(new Q(2, 1));
        queue.add(new Q(3, 1));
        queue.add(new Q(5, 2));
        queue.add(new Q(3, 2));
        queue.add(new Q(2, 2));
        queue.add(new Q(4, 1));
        queue.add(new Q(5, 1));
        queue.add(new Q(5, 3));
        queue.add(new Q(6, 1));
        queue.add(new Q(1, 2));

        Collections.sort(queue, new Comparator<Q>() {
            @Override
            public int compare(Q o1, Q o2) {
                if (o1.clock == o2.clock) {
                    if (o1.process < o2.process) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
                return o1.clock < o2.clock ? -1 : 1;
            }
        });
    }

    public static void main(String[] agrs) {                
        Main m = new Main();
        m.add();
        System.out.println(m.queue.toString());
        
        
        System.out.println("After remvoing");
        
        
        m.removeRequests(new Q(1,2));
        
        System.out.println(m.queue.toString());
        
    }

    public void removeRequests(Q l) {
        if (!queue.isEmpty()) {
            for (Iterator<Q> it = queue.iterator(); it.hasNext();) {
                Q timeLogical = it.next();
                if (timeLogical.lessThan(l)) {
                    it.remove();
                }
            }
        }
    }
}
