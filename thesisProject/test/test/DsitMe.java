/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cern.jet.math.Bessel;
import cern.jet.random.Poisson;
import cern.jet.random.engine.RandomEngine;
import java.util.Random;

/**
 *
 * @author behnish
 */
public class DsitMe {
    public static void main(String[] args)
    {
        RandomEngine e = new RandomEngine() {

            @Override
            public int nextInt() {
                return new Random().nextInt();
            }
        };
        Poisson p = new Poisson(20, e);
        while(true)
        {
            System.out.println("The point is "+ p.nextInt());
        }
        
    }
    
}
