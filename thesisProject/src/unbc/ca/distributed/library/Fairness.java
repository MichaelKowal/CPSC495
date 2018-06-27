/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.Map;
import unbc.ca.distributed.management.ObjectFactory;
import unbc.ca.distributed.message.TimeLogical;

/**
 *
 * @author behnish Class implementation from
 * http://www.risc.jku.at/software/daj/ but changed according to fit in current
 * framework
 */
public class Fairness extends Assertion {

    private TimeLogical[] tryTimes = new TimeLogical[ObjectFactory.getNodeCount()+1];
    private int procInCR = -1, procTryingLonger = -1;
    

    public Fairness() {
        for (int i = 0; i < tryTimes.length; i++) {
            tryTimes[i] = null;
        }
    }

    @Override
    public boolean test(Map<Integer, Algorithm> algorithms) {
        for (Map.Entry<Integer, Algorithm> entry : algorithms.entrySet()) {
            Integer node = entry.getKey();
            Algorithm algorithm = entry.getValue();

            if (algorithm.getRegion() == 1) {
                tryTimes[node] = algorithm.getLastTryClock();
            }
        }

        for (Map.Entry<Integer, Algorithm> entry : algorithms.entrySet()) {
            Integer CS = entry.getKey();
            Algorithm algorithm = entry.getValue();

            if (algorithm.getRegion() == 2) {
                for (Map.Entry<Integer, Algorithm> entry1 : algorithms.entrySet()) {
                    Integer RE = entry1.getKey();
                    Algorithm algorithm1 = entry1.getValue();
                                                            
                    if (algorithm1.getRegion() == 1) {
                        if(algorithm1.getLastTryClock().lessThan(algorithm.getLastTryClock())){
                            procInCR = CS;
                            procTryingLonger = RE;                            
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public String getText() {
        return procInCR+","+procTryingLonger;
//        return "Process " + procInCR + " is in the critical region, but "
//                + procTryingLonger + "was trying earlier !";
    }
}
