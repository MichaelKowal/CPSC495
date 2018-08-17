/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.StringMessage;

/**
 *
 * @author behnish
 */
public class Broadcast extends
        Algorithm {

    private boolean sent = false;
    private int initiator = 1;

    @Override
    protected void init() {
        if (getNodeId() == initiator && !sent) {
            broadcastOut(new StringMessage("wave"));
            sent = true;
            currentAlg = this.getClass().getName();
        }
    }

    @Override
    protected void onReceive(Message msg) {
        StringMessage message = (StringMessage) msg;
        if (!sent) {
            StringMessage ack = new StringMessage("ACK");
            send(message.getFinalSender(), ack);

            broadcastOutExceptParent(message.getParent(), new StringMessage("wave"));

            sent = true;
        }
    }
}
