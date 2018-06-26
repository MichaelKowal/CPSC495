/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import java.util.LinkedList;
import java.util.Queue;
import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.library.Assertion;
import unbc.ca.distributed.library.Correctness;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.RaymondMsg;
import unbc.ca.distributed.message.StringMessage;

/**
 *
 * @author behnish
 */
public class Raymond extends Algorithm {

    /* For broadcast Algorithm */
    private boolean sent = false;
    private int rootNode = 1;
    private boolean oneTimeDeal = false;

    private Queue<Integer> requestQ = new LinkedList<>();
    private boolean using = false;
    private boolean asked = false;
    private int holder = -1;

 private Assertion safety;

    public Raymond() {
        setRegion(0);
        safety = new Correctness();
    }
    private void oneTimeExecution() {
        if (getNodeId() == rootNode && !sent && !oneTimeDeal) {
            broadcastOut(new StringMessage("WAVE"));
            sent = true;
            oneTimeDeal = true;
            System.out.println("-------Raymond---------Node " + getNodeId() + "  is in critical section --------------------");
            addToTrace("CS," + getNodeId() + ",IN");
            enterCS();
            using = true;
            holder = getNodeId();
        }
    }

    private boolean makeRequest() {
        return holder != getNodeId() && !asked;
    }

    @Override
    protected void init() {
        test(safety);
        oneTimeExecution();
        if (holder != -1) {
            if (makeRequest() && isInterFlag()) {
                RaymondMsg m = new RaymondMsg(RaymondMsg.REQUEST);
                asked = true;
                if (requestQ.isEmpty()) {
                    queueAdd(getNodeId());
                    send(holder, m);
                } else {
                    queueAdd(getNodeId());
                }
            }

            if (!isCSFlag() && using) {
                using = false;
                System.out.println("***********Raymond***************Node " + getNodeId() + "  is out critical section*********************");
                addToTrace("CS," + getNodeId() + ",OUT");
                exitCS();
                asked = false;
                sendPrivilege();
                setRegion(0);
            }
        }
    }

    private void sendPrivilege() {
        if (!requestQ.isEmpty()) {
            holder = requestQ.poll();
            send(holder, new RaymondMsg(RaymondMsg.PREVILIGE));
        } 
        if (!requestQ.isEmpty()) {
            RaymondMsg p = new RaymondMsg(RaymondMsg.REQUEST);
            send(holder, p);
        }
    }

    @Override
    protected void onReceive(Message msg) {
        /* For handling the broadcast algorithm and making the minimum spanning tree for the network */
        if (msg instanceof StringMessage) {
            StringMessage message = (StringMessage) msg;
            if (message.getMessage().equals("WAVE") && !sent) {
                holder = message.getFinalSender();
                broadcastOutExceptParent(message.getFinalSender(), new StringMessage("WAVE"));                
                sent = true;
            }
        }
        /* Ends */

        /* Handling the message related to the Raymond mutual exclusion algorithm */
        if (msg instanceof RaymondMsg) {
            RaymondMsg m = (RaymondMsg) msg;
            if (m.type == RaymondMsg.REQUEST) {
                handleRequest(m);
            } else if (m.type == RaymondMsg.PREVILIGE) {
                handlePrivilege(m);
            }
        }
    }

    private void handleRequest(RaymondMsg m) {
        if (assignPrevilige()) {
            queueAdd(m.getFinalSender());
            RaymondMsg p = new RaymondMsg(RaymondMsg.PREVILIGE);
            holder = requestQ.poll();
            send(holder, p);
        } else {
            if (requestQ.isEmpty()) {
                queueAdd(m.getFinalSender());
                if (!using) {
                    send(holder, m);
                }
            } else {
                queueAdd(m.getFinalSender());
            }
        }
    }

    private void handlePrivilege(RaymondMsg m) {
        if (asked && requestQ.peek() == getNodeId()) {
            executeCS();
        } else {
            holder = requestQ.poll();
            send(holder, m);
            if (!requestQ.isEmpty()) {
                RaymondMsg p = new RaymondMsg(RaymondMsg.REQUEST);
                send(holder, p);
            }
        }
    }

    private void executeCS() {
        System.out.println("-------Raymond---------Node " + getNodeId() + "  is in critical section --------------------");
        addToTrace("CS," + getNodeId() + ",IN");
        enterCS();
        using = true;
        holder = requestQ.poll();
        setRegion(2);
    }

    private boolean assignPrevilige() {
        if (requestQ.isEmpty()) {
            return holder == getNodeId() && !using;
        } else {
            return holder == getNodeId() && !using && requestQ.peek() != getNodeId();
        }
    }

    public void queueAdd(int value) {
        if (!requestQ.contains(value)) {
            requestQ.add(value);
        }
    }
}
