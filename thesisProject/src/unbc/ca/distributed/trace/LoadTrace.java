/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import unbc.ca.distributed.graph.elements.Edge;
import unbc.ca.distributed.management.Configuration;
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class LoadTrace {

    String currentTraceFile;

    public LoadTrace(String currentTraceFile) {        
        this.currentTraceFile = currentTraceFile;
    }

    public void loadTraceIntoDS() throws IOException {
        int i = 1;
        int j = 1;
        int largeClock = 0;
        File file = new File(currentTraceFile);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line != null) {
                    line = line.replace("<", "");
                    line = line.replace(">", "");

                    String[] raw = line.split(",");
                    switch (raw[0]) {
                        case "S": {
                            TraceObject t = new TraceObject(ObjectFactory.getNodes().get(Integer.parseInt(raw[1])).location(),
                                    ObjectFactory.getNodes().get(Integer.parseInt(raw[2])).location(),
                                    Configuration.speed);
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            
                            t.setDt(returnWeight(raw[1], raw[2]));
                            t.setWeight(returnWeight(raw[1], raw[2]));
                            t.setTickMe(returnTicksMe(raw[1], raw[2]));
                            
                            t.setCountSteps();
                            t.setTraceObjectId(i);                            
                            t.setContent(raw[5]);                            
                            
                            if (Integer.parseInt(raw[3]) < largeClock) {
                                t.setLateEvent(true);
                            }                            
                            ObjectFactory.getTrace().put(i, t);
                            largeClock = Integer.parseInt(raw[3]);                                                        
                            i++;
                            break;
                        }
                        case "R": {
                            TraceObject t = new TraceObject(ObjectFactory.getNodes().get(Integer.parseInt(raw[1])).location(),
                                    ObjectFactory.getNodes().get(Integer.parseInt(raw[2])).location(),
                                    Configuration.speed);
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            
                            t.setTraceObjectId(j);
                            t.setHopCount(Integer.parseInt(raw[5]));
                            
                            if (Integer.parseInt(raw[3]) < largeClock) {
                                t.setLateEvent(true);
                            }                            
                            ObjectFactory.getReceiverTrace().put(j, t);                            
                            largeClock = Integer.parseInt(raw[3]);
                            j++;
                            break;
                        }
                        case "CS": 
                        {                            
                            TraceObject t = new TraceObject(null, null, Configuration.speed);
                            
                            t.setType(raw[0]);
                            t.setSender(raw[1]);                            
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            t.setTraceObjectId(i);
                            
                            ObjectFactory.getTrace().put(i, t);                            
                            largeClock = Integer.parseInt(raw[3]);
                            i++;
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private int returnWeight(String source, String destination) {
        Edge e = ObjectFactory.getMainFrame().getGraphObject().findEdge(ObjectFactory.getNodes().get(Integer.parseInt(source)), ObjectFactory.getNodes().get(Integer.parseInt(destination)));
        return e.getWeight();
    }
    private double returnTicksMe(String source, String destination) {
        Edge e = ObjectFactory.getMainFrame().getGraphObject().findEdge(ObjectFactory.getNodes().get(Integer.parseInt(source)), ObjectFactory.getNodes().get(Integer.parseInt(destination)));
        return e.getTicks();
    }
}