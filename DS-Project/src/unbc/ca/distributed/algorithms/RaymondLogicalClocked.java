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
import unbc.ca.distributed.library.Fairness;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.RaymondMsg;
import unbc.ca.distributed.message.StringMessage;
import unbc.ca.distributed.simDistributed.Dist_Algorithm;
/**
 *
 * @author behnish
 */
public class RaymondLogicalClocked extends Dist_Algorithm {

    /* For broadcast Algorithm */
    private boolean sent = false;
    private int rootNode = 1;
    private boolean oneTimeDeal = false;

    private Queue<Integer> requestQ = new LinkedList<>();
    private boolean using = false;
    private boolean asked = false;
    private int holder = -1;

    public int I = 0; // idle
    public int R = 1; // requesting
    public int C = 2; // critical section

    private boolean enteredCS = false;

    private Assertion safety;
    private Assertion fairness;
    
    private boolean printParent = false;

    public RaymondLogicalClocked() {
        setRegion(I);
        safety = new Correctness();
        fairness = new Fairness();
    }

    private void oneTimeExecution() {
        if (getNodeId() == rootNode && !sent && !oneTimeDeal) {
            broadcastOut(new StringMessage("WAVE"));
            sent = true;
            oneTimeDeal = true;
            setLastTryClock(getClock());
            System.out.println("-------Raymond---------Node " + getNodeId() + "  is in critical section --------------------");
            addToTrace("CS," + getNodeId() + ",IN");
            enterCS();
            setRegion(C);
            using = true;
            holder = getNodeId();            
            enteredCS = true;
        }
    }

    private boolean makeRequest() {
        return holder != getNodeId() && !asked;
    }

    @Override
    protected void init() {
        test(safety);
        if (enteredCS) {
            test(fairness);
        }

        oneTimeExecution();
        if (holder != -1) {
            if (makeRequest() && isInterFlag()) {
                RaymondMsg m = new RaymondMsg(RaymondMsg.REQUEST);
                
                incrementClock();
                m.setClock(getClock());                
                setLastTryClock(getClock());                
                

                asked = true;
                setRegion(R);
                addToTrace("REQ,"+getNodeId());
                
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
                setRegion(I);
            }
        }
    }

    private void sendPrivilege() {
        if (!requestQ.isEmpty()) {
            holder = requestQ.poll();
            
            RaymondMsg pri = new RaymondMsg(RaymondMsg.PREVILIGE);
            pri.setClock(getClock());                 
            send(holder, pri);
        }
        if (!requestQ.isEmpty()) {
            RaymondMsg p = new RaymondMsg(RaymondMsg.REQUEST);
            
            incrementClock();
            p.setClock(getClock());
            setLastTryClock(getClock());                            
            //addToTrace("REQ,"+getNodeId());

            send(holder, p);
            setRegion(R);
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
                
                if(!printParent)
                {
                    System.out.println("Node "+getNodeId()+" has Parent -> "+holder);
                    printParent = true;
                }
            }
        }
        /* Ends */

        /* Handling the message related to the Raymond mutual exclusion algorithm */
        if (msg instanceof RaymondMsg) {
            RaymondMsg m = (RaymondMsg) msg;
            adapClock(m.getClock());
            incrementClock();

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
            p.setClock(getClock());
            holder = requestQ.poll();
            send(holder, p);
        } else {
            if (requestQ.isEmpty()) {
                queueAdd(m.getFinalSender());
                if (!using) {
                    m.setClock(getClock());
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
            m.setClock(getClock());            
            send(holder, m);
            if (!requestQ.isEmpty()) {
                RaymondMsg p = new RaymondMsg(RaymondMsg.REQUEST);
                p.setClock(getClock());
                setLastTryClock(getClock());                
                incrementClock();
                //addToTrace("REQ,"+getNodeId());
                send(holder, p);
                setRegion(R);
            }
        }
    }

    private void executeCS() {
        enteredCS = true;
        System.out.println("-------Raymond---------Node " + getNodeId() + "  is in critical section --------------------");
        addToTrace("CS," + getNodeId() + ",IN");
        enterCS();
        using = true;
        holder = requestQ.poll();
        setRegion(C);
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
