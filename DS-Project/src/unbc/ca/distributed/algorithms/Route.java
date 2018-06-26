/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.StringMessage;
import unbc.ca.distributed.simDistributed.Dist_Algorithm;
/**
 *
 * @author bmann
 */
public class Route extends Dist_Algorithm
{
    boolean receivedTrue = false;
    @Override
    protected void init() {
        if(getNodeId()==1 && !receivedTrue)
        {
            send(7, new StringMessage("Me"));
            receivedTrue = true;
        }
    }

    @Override
    protected void onReceive(Message msg) {
        if(!receivedTrue)
        {
            System.out.println("Node "+getNodeId()+" receicved message:- "+msg);
            receivedTrue = true;
        }
    }
    
}
