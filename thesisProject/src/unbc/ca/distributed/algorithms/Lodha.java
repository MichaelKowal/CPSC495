/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.library.Assertion;
import unbc.ca.distributed.library.Correctness;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.Msg;
import unbc.ca.distributed.message.TimeLogical;

/**
 *
 * @author behnish
 */
public class Lodha extends Algorithm {

    private static final int I = 0;
    private static final int R = 1;
    private static final int C = 2;

    private boolean firstTry = false;
    private TimeLogical lastTryTime;

    private Map<Integer, String> RV = new LinkedHashMap<>();
    private ArrayList<TimeLogical> queue = new ArrayList<>();
    private Map<Integer, Msg> deferred = new LinkedHashMap<>();

    private Assertion safety;

    public Lodha() {
        setRegion(I);
        safety = new Correctness();
    }

    private void oneTimeDeal() {
        if (!firstTry) {
            lastTryTime = new TimeLogical(0, getNodeId());
            for (int i = 1; i <= totalNodesInNetwork(); i++) {
                RV.put(i, "0");
            }
            firstTry = true;
        }
    }

    @Override
    protected void init() {
        oneTimeDeal();
        test(safety);
        if ((getRegion() == I) && (isInterFlag())) {
            requestCS();
        } else if (getRegion() == C && !isCSFlag()) {
            finishCS();
        }
        currentAlg = this.getClass().getName();
    }

    private void finishCS() {
        incrementClock();
        addToTrace("CS," + getNodeId() + ",OUT");
        exitCS();
        System.out.println("----------Lodha and Kalyani----------Node " + getNodeId() + " is out from critical section--------------------");

        queue.remove(lastTryTime);
        setRegion(I);
        sendFlushAndReplies();
    }

    private void sendFlushAndReplies() {
        int process = 0;
        if (!queue.isEmpty()) {
            process = queue.get(0).process;
            Msg flush = new Msg(Msg.FLUSH, TimeLogical.copy(lastTryTime));
            flush.setClock(getClock());
            send(queue.get(0).process, flush);
        }

        // Sending replies to deferred Requests
        for (Map.Entry<Integer, Msg> entry : deferred.entrySet()) {
            Integer integer = entry.getKey();
            Msg msg = entry.getValue();

            if (integer != process) {
                send(integer, msg);
            }
        }

        deferred.clear();

    }

    private void requestCS() {
        incrementClock();
        queue.clear();
        lastTryTime = new TimeLogical(getClock(), getNodeId());

        Msg request = new Msg(Msg.REQUEST, lastTryTime);
        request.setClock(getClock());

        addEntry(lastTryTime);
        sendToAll(request);
        for (int i = 1; i <= totalNodesInNetwork(); i++) {
            RV.put(i, "0");
        }
        RV.put(getNodeId(), "1");
        setRegion(R);
    }

    @Override
    protected void onReceive(Message msg) {
        Msg message = (Msg) msg;
        adapClock(message.getClock());

        if (message.type == Msg.REQUEST) {
            handleRequest(message);
        } else if (message.type == Msg.REPLY) {
            handleReply(message);
        } else if (message.type == Msg.FLUSH) {
            handleFlush(message);
        }
    }

    private void handleFlush(Msg message) {
        RV.put(message.getFinalSender(), "1");
        if(message.time.clock!=0)
        {
            removeRequests(message.time);
        }
        if (checkCS()) {
            executeCS();
        }
    }

    private void handleReply(Msg message) {
        RV.put(message.getFinalSender(), "1");
        removeRequests(message.time);
        if (checkCS()) {
            executeCS();
        }
    }

    private void handleRequest(Msg message) {
        if (getRegion() == R) {
            if (RV.get(message.getFinalSender()).equals("0")) {

                addEntry(message.time);
                RV.put(message.getFinalSender(), "1");
                if (checkCS()) {
                    executeCS();
                }
            } else {
                deferred.put(message.getFinalSender(), new Msg(Msg.REPLY, TimeLogical.copy(lastTryTime)));
            }
        } else if (getRegion() == I) {
            Msg reply = new Msg(Msg.REPLY, TimeLogical.copy(lastTryTime));
            reply.setClock(getClock());
            send(message.getFinalSender(), reply);            
        }
    }

    private boolean checkCS() {
        for (Map.Entry<Integer, String> entry : RV.entrySet()) {
            String string = entry.getValue();
            if (string.equals("0")) {               
                return false;
            }
        }
        if (!queue.isEmpty()) {
            return queue.get(0).process == getNodeId();
        }        
        return true;
    }

    public void removeRequests(TimeLogical l) {
        if (!queue.isEmpty()) {
            for (Iterator<TimeLogical> it = queue.iterator(); it.hasNext();) {
                TimeLogical timeLogical = it.next();
                if (timeLogical.lessThan(l)) {
                    it.remove();
                }
            }
        }
    }

    public void executeCS() {
        incrementClock();
        addToTrace("CS," + getNodeId() + ",IN");
        enterCS();
        setRegion(C);
        System.out.println("*******Lodha and Kalyani*******Node " + getNodeId() + " is in critical section************");
    }

    public void addEntry(TimeLogical entry) {
        queue.add(entry);
        Collections.sort(queue, new Comparator<TimeLogical>() {
            @Override
            public int compare(TimeLogical o1, TimeLogical o2) {
                if (o1.clock == o2.clock) {
                    if (o1.process < o2.process) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
                return o1.clock < o2.clock ? -1 : 1;
            }
        });
    }
}
