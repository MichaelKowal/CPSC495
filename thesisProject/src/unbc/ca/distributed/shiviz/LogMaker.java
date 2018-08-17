package unbc.ca.distributed.shiviz;

import java.io.*;
import java.util.ArrayList;

import java.util.Scanner;
import unbc.ca.distributed.library.Simulation;
import unbc.ca.distributed.library.Algorithm;
/**
 * Builds a complete log file with all of the individual logs created for 
 * each node. Adds all of the necessary ShiViz formatting.
 * @author michaelkowal
 * @version 1
 * @since 1.10
 */
public class LogMaker {

    /**
     * Called when a simulation is finished. Consolidates all node log files
     * @param currentSim The simulation that the log file is for. 
     * @throws IOException 
     */
    public static void makeShiVizLog(Simulation currentSim) throws IOException {
        int numOfFiles = currentSim.getNetwork().getNodes().size();
        ArrayList<File> files = new ArrayList<File>();
        for (int i = 1; i <= numOfFiles; i++)
            files.add(new File("LogFiles/Node" + i + "LogFile-shiviz.txt"));
        File log = new File("LogFiles/ShiVizLog-" + Algorithm.getCurrentAlg()+ "-" + java.time.LocalDate.now());
        FileWriter fw = new FileWriter(log);
        String regExp = "(?<host>\\S*) (?<clock>{.*})\\n(?<event>.*)\n\\S {\"\\S\" :1}\n";
        fw.write(regExp);
        for(File file : files) {
            Scanner input = new Scanner(file);
            while(input.hasNextLine()) {
                fw.append(input.nextLine() + "\n");
            }
            input.close();
            file.delete();
        }
        fw.flush();
        fw.close();
    }
}
