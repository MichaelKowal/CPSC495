/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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

/**
 *
 * @author behnish
 */
public class Carvalho extends Algorithm {

    private boolean oneTimeDeal = false;

    private int I = 0;
    private int R = 1;
    private int C = 2;

    private TimeLogical lastTryTime = null;
    private boolean replyToken[];
    private ConcurrentHashMap<Integer, Msg> deferredMessage = new ConcurrentHashMap<>();

    private Assertion fairness;
    private Assertion correctness;

    public Carvalho() {
        setRegion(I);
        correctness = new Correctness();
        fairness = new Fairness();
    }
    @Override
    protected void init() {
        oneTimeDeal();
        test(fairness);
        test(correctness);
        if ((getRegion() == I) && (isInterFlag())) {
            sendRequest();
        } else if (getRegion() == C && !isCSFlag()) {
            releaseCS();
        }
        currentAlg = this.getClass().getName();
    }

    @Override
    protected void onReceive(Message messageR) {
        Msg msg = (Msg) messageR;

        int received = msg.getFinalSender();
        adapClock(msg.getClock());
        incrementClock();

        if (getNodeId() != received) {
            if (msg.type == Msg.REQUEST) {
                handleRequestMessage(received, msg);
            }
            if (msg.type == Msg.REPLY) {
                replyToken[received - 1] = true;
            }
        }
        if (checkGateWay()) {
            executeCS();
        }
    }

    private void handleRequestMessage(int received, Msg msg) {
        Msg ackMsg = new Msg(Msg.REPLY, new TimeLogical(getClock(), getNodeId()));
        ackMsg.setClock(getClock());
        
        if (getRegion() == I) {
            replyToken[received - 1] = false;
            send(received, ackMsg);
        } else if (getRegion() == C) {
            deferredMessage.put(received, ackMsg);
        } else if (getRegion() == R) {
            if (lastTryTime != null && msg.time.lessThan(lastTryTime)) {
                if (replyToken[received - 1]) {
                    replyToken[received - 1] = false;

                    Msg request = new Msg(Msg.REQUEST, lastTryTime);
                    request.setClock(getClock());
                    send(received, request);
                }
                send(received, ackMsg);
            } else {
                deferredMessage.put(received, ackMsg);
            }
        }
    }

    private void oneTimeDeal() {
        if (!oneTimeDeal) { 
             replyToken = new boolean[totalNodesInNetwork()];
            for (int i = 0; i < replyToken.length; i++) {
                replyToken[i] = false;
            }
            replyToken[getNodeId() - 1] = true;
        }
        oneTimeDeal = true;
    }

    public boolean checkGateWay() {
        for (int i = 0; i < replyToken.length; i++) {
            if (!replyToken[i]) {
                return false;
            }
        }
        return true;
    }

    public void sendRequest() {
        setRegion(R);
        addToTrace("REQ,"+getNodeId());

        if (!checkGateWay()) {
            incrementClock();
            lastTryTime = new TimeLogical(getClock(), getNodeId());
            Msg request = new Msg(Msg.REQUEST, lastTryTime);
            request.setClock(getClock());

            for (int i = 0; i < replyToken.length; i++) {
                if (i != getNodeId() - 1 && !replyToken[i]) {
                    send(i + 1, request);
                }
            }
        } else {
            executeCS();
        }
    }

    public void executeCS() {
        if (getRegion() == R) {
            enterCS();
            incrementClock();
            setRegion(C);
            System.out.println("----------Carvalho------Node " + getNodeId() + "  is in critical section --------------------");
            addToTrace("CS," + getNodeId() + ",IN");
            lastTryTime = null;
        }
    }

    private void releaseCS() {
        incrementClock();
        System.out.println("************Carvalho**************Node " + getNodeId() + "  is out critical section*********************");
        addToTrace("CS," + getNodeId() + ",OUT");
        exitCS();
        setRegion(I);

        for (Map.Entry<Integer, Msg> entry : deferredMessage.entrySet()) {
            Integer integer = entry.getKey();
            Msg carvMsg = entry.getValue();
            replyToken[integer - 1] = false;

            send(integer, carvMsg);
            deferredMessage.remove(integer);
        }
    }
}
