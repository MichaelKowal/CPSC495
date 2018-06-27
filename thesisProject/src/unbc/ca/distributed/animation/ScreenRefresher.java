package unbc.ca.distributed.animation;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import unbc.ca.distributed.GUI.MainFrame;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;
import unbc.ca.distributed.trace.LoadTrace;
import unbc.ca.distributed.trace.TraceObject;

public class ScreenRefresher extends Thread {

    /*
     * Calling the repaint method every 10ms. This will refresh the screen.
     */
    private boolean simulation = true;
    private int count;
    private int smallDividend = 0;
    private int lastIndex;

    public ScreenRefresher(String traceFile) {
        super.setName("Screen Refresher Thread");

        this.simulation = true;
        this.count = 1;

        ObjectFactory.setAnimationClock(1.0);
        ObjectFactory.getDrawingList().clear();

        loadTrace(traceFile + "_animation");
    }

    @Override
    public void run() {
        ObjectFactory.getAnimationPanel().animate();
        while (simulation) {
            putEvents();
            //draw();
            updateLocations();
            checkMe();
            sleepThread(ObjectFactory.getAnimationClockDelay());
        }

        ObjectFactory.getAnimationPanel().setStopTimer(true);
    }

    public void stopSimulation() {
        this.simulation = false;
    }

    private synchronized void incrementCounter() {
        count++;
    }

    private void sleepThread(int value) {
        try {
            sleep(value);
        } catch (InterruptedException ex) {
            Logger.getLogger(ScreenRefresher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void settingD() {
        for (Map.Entry<Integer, TraceObject> en : ObjectFactory.getTrace().entrySet()) {
            TraceObject event = en.getValue();
            if (event.getType().equals("S")) {
                double div = (double) event.getCountStep() / (double) smallDividend;
                event.setDividend(div);
            }
        }
    }

    private void putEvents() {
        for (Map.Entry<Integer, TraceObject> en : ObjectFactory.getTrace().entrySet()) {
            Integer integer = en.getKey();
            TraceObject event = en.getValue();
            if (!event.isAnimationFinished()) {
                switch (event.getType()) {
                    case "S":
                        if ((event.getSimulationClock() <= ObjectFactory.getAnimationClock()) && !event.isAddedOrNot()) {
                            event.setAddedOrNot(true);
                            lastIndex = integer;
                            ObjectFactory.getDrawingList().put(count, event);
                            incrementCounter();
                        }
                        break;
                    case "CS":
                        if ((event.getSimulationClock() <= ObjectFactory.getAnimationClock()) && (!event.isAddedOrNot()) && (event.getReciever().equals("IN"))) {
                            event.setAddedOrNot(true);
                            ObjectFactory.getNodes().get(Integer.parseInt(event.getSender())).setColor(Color.RED);
                            lastIndex = integer;
                            ObjectFactory.getDrawingList().put(count, event);
                            incrementCounter();
                        } else if ((event.getSimulationClock() <= ObjectFactory.getAnimationClock()) && (!event.isAddedOrNot()) && (event.getReciever().equals("OUT"))) {
                            event.setAddedOrNot(true);
                            ObjectFactory.getNodes().get(Integer.parseInt(event.getSender())).setColor(Configuration.animationNodeColor);
                            lastIndex = integer;
                            ObjectFactory.getDrawingList().put(count, event);
                            incrementCounter();
                        }

                        break;
                }
            }
        }
    }

    private void checkMe() {
        if (ObjectFactory.getDrawingList().isEmpty()) {
            if (ObjectFactory.getTrace().get(lastIndex + 1) != null) {
                ObjectFactory.setAnimationClock(ObjectFactory.getTrace().get(lastIndex + 1).getSimulationClock());
            }
        }
    }

    private void draw() {
        ObjectFactory.getAnimationPanel().repaint();
    }

    private void updateLocations() {
        for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getDrawingList().entrySet()) {
            TraceObject traceObject = entry.getValue();
            if (traceObject.isIntoBounds()) {
                traceObject.moveForward();
            } else {
                ObjectFactory.updateClock(traceObject.getSimulationClock() + traceObject.getWeight());
                traceObject.setAnimationFinished(true);
            }

            if (traceObject.getType().equals("CS")) {
                traceObject.setAnimationFinished(true);
            }
        }
    }

    private void loadTrace(String traceFileName) {
        LoadTrace load = new LoadTrace(traceFileName);
        try {
            load.loadTraceIntoDS();
            setSmallestDividend();
            settingD();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setSmallestDividend() {
        for (Map.Entry<Integer, TraceObject> entry : ObjectFactory.getTrace().entrySet()) {
            TraceObject traceObject = entry.getValue();
            if (traceObject.getType().equals("S")) {
                if (smallDividend == 0) {
                    smallDividend = traceObject.getCountStep();
                } else if (traceObject.getCountStep() < smallDividend) {
                    smallDividend = traceObject.getCountStep();
                }
            }
        }
    }
}
