/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.StringMessage;

/**
 *
 * @author bmann
 */
public class Route extends Algorithm
{
    boolean receivedTrue = false;
    @Override
    protected void init() {
        if(getNodeId()==1 && !receivedTrue)
        {
            send(7, new StringMessage("Me"));
            receivedTrue = true;
        }
        currentAlg = this.getClass().getName();
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
