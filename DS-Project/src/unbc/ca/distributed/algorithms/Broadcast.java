/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.StringMessage;
import unbc.ca.distributed.simDistributed.Dist_Algorithm;
/**
 *
 * @author behnish
 */
public class Broadcast extends
        Dist_Algorithm {

    private boolean sent = false;
    private int initiator = 1;

    @Override
    protected void init() {
        if (getNodeId() == initiator && !sent) {
            broadcastOut(new StringMessage("wave"));
            sent = true;
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
