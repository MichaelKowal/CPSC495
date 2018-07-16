package unbc.ca.distributed.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import unbc.ca.distributed.library.Algorithm;
import unbc.ca.distributed.library.Assertion;
import unbc.ca.distributed.library.Correctness;
import unbc.ca.distributed.message.TimeLogical;
import unbc.ca.distributed.message.Message;
import unbc.ca.distributed.message.Msg;

import org.github.com.jvec.JVec;

public class Lamport extends Algorithm {

    private int I = 0;
    private int R = 1;
    private int C = 2;
    public TimeLogical lastTryTime;
    public ArrayList<TimeLogical> queue = new ArrayList<>();
    public int replyMessages;
    private Assertion safety;
    private int counter = 0;
    
    public Lamport() {
        setRegion(I);
        safety = new Correctness();
    }

    @Override
    protected void init() {
        test(safety);
        if (getRegion() == I && isInterFlag()) {
            request();
        } else if (getRegion() == C && !isCSFlag()) {
            freeRessource();
        }
        
    }

    @Override
    protected void onReceive(Message message) {
        Msg msg = (Msg) message;

        adapClock(msg.getClock());
        incrementClock();

        if (msg.type == Msg.REPLY) {
            replyMessages++;
            checkCS();
        }
        if (msg.type == Msg.REQUEST) {
            handleRequest(msg, msg.getFinalSender());
        }
        if (msg.type == Msg.RELEASE) {
            handleRelease(msg, msg.getFinalSender());
        }
    }

    public void handleRelease(Msg msg, int received) {
        queue.remove(msg.time);
        checkCS();
    }

    public void freeRessource() {
        queue.remove(lastTryTime);
        incrementClock();
        System.out.println("************LAMPORT**************Node " + getNodeId() + "  is out critical section*********************");
        addToTrace("CS," + getNodeId() + ",OUT");
        exitCS();
        setRegion(I);

        Msg release = new Msg(Msg.RELEASE, lastTryTime);
        release.setClock(getClock());
        sendToAll(release);

        lastTryTime = null;
    }

    public void handleRequest(Msg msg, int received) {
        addToQueue(msg.time);
        Msg reply = new Msg(Msg.REPLY, new TimeLogical(getClock(), getNodeId()));
        reply.setClock(getClock());
        send(received, reply);
    }

    public void checkCS() {
        if (getRegion() == R) {
            if (replyMessages == totalNodesInNetwork() - 1 && queue.get(0).process == getNodeId()) {
                setRegion(C);
                enterCS();                
                System.out.println("----------LAMPORT------Node " + getNodeId() + "  is in critical section --------------------");
                addToTrace("CS," + getNodeId() + ",IN");
                replyMessages = 0;
            }
        }
    }

    public void request() {
        incrementClock();
        setRegion(R);
        lastTryTime = new TimeLogical(getClock(), getNodeId());
        Msg request = new Msg(Msg.REQUEST, lastTryTime);
        request.setClock(getClock());

        sendToAll(request);
        addToQueue(lastTryTime);
    }

    public void addToQueue(TimeLogical time) {
        queue.add(time);
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
