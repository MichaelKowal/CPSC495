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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.Msg;
import unbc.ca.distributed.message.TimeLogical;

/**
 *
 * @author behnish
 */
public class Kalyani extends Algorithm {

    private static final int R = 0;
    private static final int T = 1;
    private static final int C = 2;

    private TimeLogical lastTryTime;
    private boolean firstTry = false;

    public String[] RV;
    private ArrayList<TimeLogical> queue = new ArrayList<>();

    private Map<Integer, Msg> defer = new ConcurrentHashMap<>();

    private void oneTimeDeal() {
        if (!firstTry) {
            lastTryTime = new TimeLogical(0, getNodeId());
            RV = new String[totalNodesInNetwork()];
            firstTry = true;
        }
    }

    @Override
    protected void init() {
        oneTimeDeal();

        if ((getRegion() == R) && (isInterFlag())) {
            request();
        } else if (getRegion() == C && !isCSFlag()) {
            finishCS();
        }
        currentAlg = this.getClass().getName();
    }

    public void request() {
        incrementClock();
        setRegion(T);

        lastTryTime = new TimeLogical(getClock(), getNodeId());
        Msg msg = new Msg(Msg.REQUEST, lastTryTime);
        sendToAll(msg);

        addEntry(lastTryTime);

        for (int i = 0; i < totalNodesInNetwork(); i++) {
            if (RV[i] == null) {
                RV[i] = "0";
            }
        }
        RV[getNodeId() - 1] = "1";
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

    private void finishCS() {
        incrementClock();
        queue.remove(lastTryTime);
        addToTrace("CS," + getNodeId() + ",OUT");
        exitCS();
        System.out.println("--------------------Node " + getNodeId() + " is out from critical section--------------------");

        if (queue.size() > 0) {
            Msg flush = new Msg(Msg.FLUSH, lastTryTime);
            flush.setClock(getClock());
            send(queue.get(0).process, flush);            
        }
        for (Map.Entry<Integer, Msg> entry : defer.entrySet()) {
            Integer integer = entry.getKey();
            Msg msg = entry.getValue();
            send(integer, msg);
        }
        defer.clear();

        setRegion(R);
        /* resetting the LRQ DS and RV */
        for (int i = 0; i < totalNodesInNetwork(); i++) {
            RV[i] = "0";
        }
        queue = new ArrayList<>();
    }

    @Override
    protected void onReceive(Message messageR) {
        Msg msg = (Msg) messageR;
        int received = msg.getFinalSender();
        adapClock(msg.time.clock);
        incrementClock();

        if (getNodeId() != received) {
            if (msg.type == Msg.REQUEST) {
                handleRequest(msg, received);
            }
            if (msg.type == Msg.REPLY || msg.type == Msg.FLUSH) {
                handleReply(msg, received);
            }
        }

    }

    public void handleRequest(Msg msg, int received) {
        Msg reply = new Msg(Msg.REPLY, lastTryTime);
        if (getRegion() == T) {
            switch (RV[received - 1]) {
                case "0":
                    addEntry(msg.time);
                    RV[received - 1] = "1";
                    if (checkExecuteCS()) {
                        incrementClock();
                        addToTrace("CS," + getNodeId() + ",IN");
                        enterCS();
                        setRegion(C);
                        System.out.println("**************Node " + getNodeId() + " is in critical section from request section************");
                    }
                    break;
                case "1":
                    defer.put(received, reply);
                    break;
            }
        } else if (getRegion() == R) {
            send(received, reply);
        }
    }

    private boolean checkExecuteCS() {
        for (int i = 0; i < totalNodesInNetwork(); i++) {
            if ("0".equals(RV[i])) {
                return false;
            }
        }
        return queue.get(0).process == getNodeId();
    }

    private void handleReply(Msg msg, int received) {
        RV[received - 1] = "1";
        removeRequests(msg.time);

        if (checkExecuteCS()) {
            incrementClock();
            addToTrace("CS," + getNodeId() + ",IN");
            enterCS();
            setRegion(C);
            System.out.println("_________________Node " + getNodeId() + " is in critical section from reply section_________________");

        }
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
}
