package unbc.ca.distributed.shiviz;

import java.io.*;
import java.util.ArrayList;

import java.util.Scanner;
import unbc.ca.distributed.library.Simulation;
public class LogMaker {

    public static void makeShiVizLog(Simulation currentSim) throws IOException {
        int numOfFiles = currentSim.getNetwork().getNodes().size();
        ArrayList<File> files = new ArrayList<File>();
        for (int i = 1; i <= numOfFiles; i++)
            files.add(new File("LogFiles/Node" + i + "LogFile-shiviz.txt"));
        File log = new File("LogFiles/Log");
        FileWriter fw = new FileWriter(log);
        String regExp = "(?<host>\\S*) (?<clock>{.*})\\n(?<event>.*)\n\\S {\"\\S\" :1}\n";
        fw.write(regExp);
        for(File file : files) {
            Scanner input = new Scanner(file);
            while(input.hasNextLine()) {
                fw.append(input.nextLine() + "\n");
            }
            input.close();
        }
        fw.flush();
        fw.close();
    }
}
