/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.util.Map;
import java.util.Vector;

/**
 *
 * @author behnish
 */
public class Correctness extends Assertion
{
    private Vector procsInCR = new Vector();
    
    @Override
    public boolean test(Map<Integer, Algorithm> algorithms) {
        int numOfProcsInCR = 0;
        procsInCR.removeAllElements();
        
        for (Map.Entry<Integer, Algorithm> entry : algorithms.entrySet()) {
            Integer integer = entry.getKey();
            Algorithm algorithm = entry.getValue();
            
            if (algorithm.getRegion() == 2) {
                numOfProcsInCR++;
                procsInCR.add(integer);
            }           
        }                
        return numOfProcsInCR <= 1;
    }
    
    @Override
    public String getText() {
        String text = "Processes in the critical region: ";
        for (int i=0; i<procsInCR.size(); i++) {
            text += procsInCR.get(i) + " ";
        }
        return text;
    }

    
}
