/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.library.Assertion;
import unbc.ca.distributed.library.Correctness;
import unbc.ca.distributed.library.Fairness;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.Msg;
import unbc.ca.distributed.message.TimeLogical;
import unbc.ca.distributed.simDistributed.Dist_Algorithm;
/**
 *
 * @author behnish
 */
public class Ricart extends Dist_Algorithm {

    public static final int I = 0; // idle
    public static final int R = 1; // requesting
    public static final int C = 2; // critical section

    public TimeLogical lastTryTime;
    public int replyMessages;
    private Map<Integer, Msg> defer = new ConcurrentHashMap<>();
    private Assertion safety;
    private Assertion fairness;

    public Ricart() {
        setRegion(I);
        safety = new Correctness();
        fairness = new Fairness();
    }

    public void tryRessource() {
        incrementClock();
        setRegion(R);
        lastTryTime = new TimeLogical(getClock(), getNodeId());
        setLastTryClock(getClock());

        Msg msg = new Msg(Msg.REQUEST, lastTryTime);
        addToTrace("REQ,"+getNodeId());
        sendToAll(msg);
    }

    public void freeRessource() {
        incrementClock();
        System.out.println("***********Ricart***************Node " + getNodeId() + "  is out critical section*********************");
        addToTrace("CS," + getNodeId() + ",OUT");
        exitCS();
        setRegion(I);
        for (Map.Entry<Integer, Msg> entry : defer.entrySet()) {
            Integer receiver = entry.getKey();
            Msg mutual = (Msg) entry.getValue();

            send(receiver, mutual);
            defer.remove(receiver);
        }
    }

    @Override
    protected void init() {
        test(safety);
        test(fairness);
        if ((getRegion() == I) && (isInterFlag())) {
            tryRessource();
        } else if (getRegion() == C && !isCSFlag()) {
            freeRessource();
        }
    }

    private void handleTryMessage(int received, Msg msg) {
        Msg ackMsg = new Msg(Msg.REPLY, new TimeLogical(getClock(), getNodeId()));
        if (getRegion() == I) {
            send(received, ackMsg);
        } else if (getRegion() == C) {
            defer.put(received, ackMsg);
        } else if (getRegion() == R) {
            if (lastTryTime != null && msg.time.lessThan(lastTryTime)) {
                send(received, ackMsg);
            } else {
                defer.put(received, ackMsg);
            }
        }
    }

    @Override
    protected void onReceive(Message messageR) {
        Msg msg = (Msg) messageR;

        int received = msg.getFinalSender();
        adapClock(msg.time.clock);
        incrementClock();

        if (msg.type == Msg.REQUEST && received != getNodeId()) {
            handleTryMessage(received, msg);
        }
        if (msg.type == Msg.REPLY && received != getNodeId()) {
            replyMessages++;
        }
        if (getRegion() == R && replyMessages == totalNodesInNetwork() - 1) {
            System.out.println("-------Ricart---------Node " + getNodeId() + "  is in critical section --------------------");
            addToTrace("CS," + getNodeId() + ",IN");
            enterCS();
            setRegion(C);
            lastTryTime = null;
            replyMessages = 0;
        }
    }
}
