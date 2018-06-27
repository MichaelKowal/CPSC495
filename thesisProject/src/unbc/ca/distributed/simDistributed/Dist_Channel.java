/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.simDistributed;

import java.rmi.RemoteException;

/**
 *
 * @author behnish
 */
public class Dist_Channel extends Dist_Sim_port
{
    private double delay;
    private String name;
    public Dist_Channel(String name) throws RemoteException
    {
        super(name);    
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
    
}
