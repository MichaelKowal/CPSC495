/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.performance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author behnish
 */
public class ChartProducer {

    private String traceFile;
    private Map<Integer, SingleEvent> completeTrace = new LinkedHashMap<>();
    private Map<Integer, SingleEvent> csTrace = new LinkedHashMap<>();
    private Map<Integer, SingleEvent> sendTrace = new LinkedHashMap<>();
    private Map<Integer, SingleEvent> hopTrace = new LinkedHashMap<>();
    private Map<Integer, SingleEvent> finalSend = new LinkedHashMap<>();
    private int numberOfNodes;
    private Map<Integer, Double> lastClocks = new LinkedHashMap<>();
    private Map<Integer, Double> csClock = new LinkedHashMap<>();

    private Map<Integer, String> fairness = new LinkedHashMap<>();
    private Map<Integer, String> requests = new LinkedHashMap<>();

    public ChartProducer(String traceFile) throws Exception {
        this.traceFile = traceFile;
        loadTrace();
        loadHopTrace();
        loadFinalTrace();
        for (int i = 1; i <= numberOfNodes; i++) {
            lastClocks.put(i, lastClock(i));
            csClock.put(i, lastCsClock(i));
        }
    }

    public JPanel hopCount() {

        Map<Integer, Map<Integer, Integer>> ds = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            Map<Integer, Integer> single = new LinkedHashMap<>();
            ds.put(i, single);
        }

        for (Map.Entry<Integer, SingleEvent> entry : completeTrace.entrySet()) {

            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getType().equals("R")) {
                ds.get(Integer.parseInt(singleEvent.getReciever())).put(Integer.parseInt(singleEvent.getSender()), singleEvent.getHopCount());
            }
        }
        Map<Integer, Integer> sum = new LinkedHashMap<>();

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : ds.entrySet()) {
            Integer integer = entry.getKey();
            int totalHopCount = 0;
            Map<Integer, Integer> map = entry.getValue();
            for (Map.Entry<Integer, Integer> entry1 : map.entrySet()) {
                Integer integer2 = entry1.getValue();
                totalHopCount += integer2;
            }
            sum.put(integer, totalHopCount);
        }

        String[] column = new String[sum.size()];

        String series1 = "Hop Count";
        for (int i = 0; i < sum.size(); i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<Integer, Integer> entry : sum.entrySet()) {
            Integer integer = entry.getKey();
            Integer integer1 = entry.getValue();

            dataset.addValue(integer1, series1, column[integer - 1]);

        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Hop Count", // chart title
                "Process ID", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }

    public JPanel csSittingTime() {
        String[] column = new String[numberOfNodes];

        String series1 = "Critical Section Sitting Time";
        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<Integer, ArrayList<Double>> cs = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            ArrayList<Double> process = new ArrayList<>();
            cs.put(i, process);
        }

        int currentNode = 0;
        boolean flag = false;
        double in = 0, out = 0, difference = 0;
        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getReciever().equals("IN") && !flag) {
                currentNode = Integer.parseInt(singleEvent.getSender());
                in = singleEvent.getSimulationClock();
                flag = true;
            } else {
                if (currentNode == Integer.parseInt(singleEvent.getSender()) && currentNode != 0) {
                    out = singleEvent.getSimulationClock();
                    difference = out - in;
                    cs.get(currentNode).add(difference);

                    /* Resetting variables */
                    flag = false;
                    in = 0;
                    out = 0;
                    difference = 0;
                    currentNode = 0;
                }
            }
        }
        for (Map.Entry<Integer, ArrayList<Double>> entry : cs.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<Double> arrayList = entry.getValue();

            int count = arrayList.size();
            double total = 0;
            double average;
            for (Double double1 : arrayList) {
                total += double1;
            }

            average = total / count;
            dataset.addValue(average, series1, column[integer - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Critical Section Sitting Time", // chart title
                "Process ID", // domain axis label
                "Simulation Clock Units", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }

    private double averageCSTime() {
        Map<Integer, ArrayList<Double>> cs = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            ArrayList<Double> process = new ArrayList<>();
            cs.put(i, process);
        }

        int currentNode = 0;
        boolean flag = false;
        double in = 0, out = 0, difference = 0;
        for (Map.Entry<Integer, SingleEvent> entry : csTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getReciever().equals("IN") && !flag) {
                currentNode = Integer.parseInt(singleEvent.getSender());
                in = singleEvent.getSimulationClock();
                flag = true;
            } else {
                if (currentNode == Integer.parseInt(singleEvent.getSender()) && currentNode != 0) {
                    out = singleEvent.getSimulationClock();
                    difference = out - in;
                    cs.get(currentNode).add(difference);

                    /* Resetting variables */
                    flag = false;
                    in = 0;
                    out = 0;
                    difference = 0;
                    currentNode = 0;
                }
            }
        }
        double totalAverageCs = 0;
        for (Map.Entry<Integer, ArrayList<Double>> entry : cs.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<Double> arrayList = entry.getValue();

            int count = arrayList.size();
            double total = 0;
            double average = 0;
            for (Double double1 : arrayList) {
                total += double1;
            }

            average = total / count;
            totalAverageCs += average;
        }

        return (totalAverageCs / numberOfNodes + 0.5);
    }

    public JPanel responseTime() {
        String[] column = new String[numberOfNodes];

        String series1 = "Response Time";
        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<Integer, ArrayList<Double>> response = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            ArrayList<Double> process = new ArrayList<>();
            response.put(i, process);
        }

        int currentNode = 0;
        boolean flag = false;
        double in = 0, out = 0, difference = 0;
        for (int i = 1; i <= numberOfNodes; i++) {
            flag = false;
            for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
                SingleEvent singleEvent = entry.getValue();
                if (singleEvent.getType().equals("F") && (!flag) && (singleEvent.getContent().equals("1")) && (i == Integer.parseInt(singleEvent.getSender()))) {
                    currentNode = Integer.parseInt(singleEvent.getSender());
                    in = singleEvent.getSimulationClock();
                    flag = true;
                } else if (singleEvent.getReciever().equals("OUT") && (currentNode == Integer.parseInt(singleEvent.getSender()))) {
                    if (currentNode == i) {
                        out = singleEvent.getSimulationClock();
                        difference = out - in;
                        response.get(i).add(difference);

                        /* Resetting variables */
                        flag = false;
                        in = 0;
                        out = 0;
                        difference = 0;
                        currentNode = 0;
                    }
                }
            }
        }
        for (Map.Entry<Integer, ArrayList<Double>> entry : response.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<Double> arrayList = entry.getValue();

            int count = arrayList.size();
            double total = 0;
            double average = 0;
            for (Double double1 : arrayList) {
                total += double1;
            }

            average = total / count;
            dataset.addValue(average, series1, column[integer - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Response Time", // chart title
                "Process ID", // domain axis label
                "Simulation Clock Units", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }

    private double lastCsClock(int processId) {
        double simulationClockProcess = 0;

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getType().equals("CS") && processId == Integer.parseInt(singleEvent.getSender())) {
                if (simulationClockProcess < singleEvent.getSimulationClock()) {
                    simulationClockProcess = singleEvent.getSimulationClock();
                }
            }
        }
        return simulationClockProcess;
    }

    private double lastClock(int processId) {
        double simulationClockProcess = 0;

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getType().equals("F") && processId == Integer.parseInt(singleEvent.getSender())) {
                if (simulationClockProcess < singleEvent.getSimulationClock()) {
                    simulationClockProcess = singleEvent.getSimulationClock();
                }
            }
        }

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (processId == Integer.parseInt(singleEvent.getSender()) && singleEvent.getType().equals("F") && !singleEvent.getContent().equals("1")) {
                if (simulationClockProcess < singleEvent.getSimulationClock()) {
                    simulationClockProcess = singleEvent.getSimulationClock();
                }
            }
        }

        return simulationClockProcess;
    }

    private int numberOfCSAccess(int processId) {
        int csAccessTimes = 0;

        for (Map.Entry<Integer, SingleEvent> entry : csTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (processId == Integer.parseInt(singleEvent.getSender()) && singleEvent.getReciever().equals("IN")) {
                csAccessTimes++;
            }
        }

        return csAccessTimes;
    }

    public JPanel numberOfMessage() {
        String[] column = new String[numberOfNodes];
        String series1 = "Number of Messages Sent by Process";

        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            ArrayList<SingleEvent> process = new ArrayList<>();
            sendMessages.put(i, process);
        }

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();

            if ((lastClocks.get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(Integer.parseInt(singleEvent.getSender()))) {
                    if (singleEvent.getSimulationClock() < csClock.get(Integer.parseInt(singleEvent.getSender()))) {
                        sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                    }
                } else if (singleEvent.getType().equals("F") && !singleEvent.getContent().equals("0")) {
                    if (singleEvent.getSimulationClock() < csClock.get(Integer.parseInt(singleEvent.getSender()))) {
                        sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                    }
                }
            }
        }

        for (Map.Entry<Integer, ArrayList<SingleEvent>> entry : sendMessages.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<SingleEvent> arrayList = entry.getValue();

            double total = 0;
            double average = 0;
            for (SingleEvent single : arrayList) {
                total++;
            }

            if (csTrace.isEmpty()) {
                average = total;
            } else {
                average = total / numberOfCSAccess(integer);
            }
            dataset.addValue(Math.round(average), series1, column[integer - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Number of messages sent", // chart title
                "Process ID", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }

    public JPanel totalNumberOfMessages() {
        String[] column = new String[numberOfNodes];
        String series1 = "Total Number of Messages Sent by Process";

        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            ArrayList<SingleEvent> process = new ArrayList<>();
            sendMessages.put(i, process);
        }

        for (Map.Entry<Integer, SingleEvent> entry : completeTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();

            if (singleEvent.getType().equals("S")) {
                sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
            }
        }

        for (Map.Entry<Integer, ArrayList<SingleEvent>> entry : sendMessages.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<SingleEvent> arrayList = entry.getValue();

            double total = 0;
            double average = 0;
            for (SingleEvent single : arrayList) {
                total++;
            }

            if (csTrace.isEmpty()) {
                average = total;
            } else {
                average = total / numberOfCSAccess(integer);
            }
            dataset.addValue(Math.round(average), series1, column[integer - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Total Number of messages sent", // chart title
                "Process ID", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }
    /* 
     * Generate performance graph according to process i.e average of all hop processing    
     */

    public JPanel averageHopProcessingTime() {
        String[] column = new String[numberOfNodes];
        String series = "Average Hop Processing";

        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        Map<Integer, ArrayList<SingleEvent>> hopProcessing = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            ArrayList<SingleEvent> process = new ArrayList<>();
            hopProcessing.put(i, process);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<Integer, SingleEvent> entry : hopTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();

            hopProcessing.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
        }

        for (Map.Entry<Integer, ArrayList<SingleEvent>> entry : hopProcessing.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<SingleEvent> arrayList = entry.getValue();

            double total = 0;
            double average = 0;
            for (SingleEvent single : arrayList) {
                total += single.getProcessingTime();
            }

            average = total / arrayList.size();

            dataset.addValue(Math.round(average), series, column[integer - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Average Hop Processing at Processes", // chart title
                "Process ID", // domain axis label
                "Micro Seconds", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }

    /* Method to load hop Trace files */
    /* 
     * Function which loads the data structures from the trace file
     * 
     */
    private void loadTrace() throws Exception {
        if (traceFile != null) {
            int i = 1;
            int j = 1;
            int f = 1;
            int re = 1;
            Set<Integer> node = new HashSet<>();
            ReadFile file = new ReadFile(traceFile);
            for (String line : file) {
                if (line != null) {
                    line = line.replace("<", "");
                    line = line.replace(">", "");

                    String[] raw = line.split(",");
                    node.add(Integer.parseInt(raw[1]));
                    switch (raw[0]) {
                        case "S":
                        case "R": {
                            SingleEvent t = new SingleEvent();
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            t.setTraceObjectId(i);
                            if (raw[0].equals("R")) {
                                t.setHopCount(Integer.parseInt(raw[5]));
                            }
                            if (raw[0].equals("S")) {
                                t.setContent(raw[5]);
                                sendTrace.put(j, t);
                            }
                            completeTrace.put(i, t);
                            i++;
                            break;
                        }
                        case "CS": {
                            SingleEvent t = new SingleEvent();
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            completeTrace.put(i, t);
                            i++;
                            break;
                        }
                        case "FAIR": {
                            fairness.put(f, raw[1] + "," + raw[2]);
                            f++;
                            break;
                        }
                        case "REQ": {
                            requests.put(re, raw[1]);
                            re++;
                            break;
                        }
                    }
                }
            }
            numberOfNodes = node.size();
        }
    }

    private void loadHopTrace() throws Exception {
        if (traceFile != null) {
            int k = 1;
            ReadFile file = new ReadFile(traceFile + "_hop");
            for (String line : file) {
                if (line != null) {
                    line = line.replace("<", "");
                    line = line.replace(">", "");

                    String[] raw = line.split(",");
                    switch (raw[0]) {
                        case "HP": {
                            SingleEvent t = new SingleEvent();
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            t.setProcessingTime(Double.parseDouble(raw[5]));
                            hopTrace.put(k, t);
                            k++;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void loadFinalTrace() throws Exception {
        if (traceFile != null) {
            int k = 1;
            int j = 1;
            ReadFile file = new ReadFile(traceFile + "_final");
            for (String line : file) {

                if (line != null) {
                    line = line.replace("<", "");
                    line = line.replace(">", "");

                    String[] raw = line.split(",");
                    switch (raw[0]) {
                        case "F": {
                            SingleEvent t = new SingleEvent();
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            t.setContent(raw[5]);
                            finalSend.put(k, t);
                            k++;
                            break;
                        }
                        case "CS": {
                            SingleEvent t = new SingleEvent();
                            t.setType(raw[0]);
                            t.setSender(raw[1]);
                            t.setReciever(raw[2]);
                            t.setLocalClock(Integer.parseInt(raw[3]));
                            t.setSimulationClock(Double.parseDouble(raw[4]));
                            finalSend.put(k, t);
                            csTrace.put(j, t);
                            j++;
                            k++;
                            break;
                        }
                    }
                }
            }
        }
    }

    public JPanel totalCSAccess() {
        String[] column = new String[numberOfNodes];
        String series1 = "Number of times critical section access";

        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 1; i <= numberOfNodes; i++) {
            dataset.addValue(numberOfCSAccess(i), series1, column[i - 1]);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Critical Section Access by nodes", // chart title
                "Process ID", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);

    }

    public JPanel bitComplexity() {
        String[] column = new String[numberOfNodes];
        String series1 = "Number of bits exchange";

        for (int i = 0; i < numberOfNodes; i++) {
            column[i] = "P" + (i + 1);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createBarChart(
                "Critical Section Access by nodes", // chart title
                "Process ID", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        return new ChartPanel(chart);
    }

    /**
     * *************************************************************************
     * @return graph panel
     */
    /* Method for producing performance metrics for hop processing according to simulation clock */
    public JPanel hopProcessing() {
        int divider = 20;
        int breakDownNumber = (int) completeTrace.get(completeTrace.size()).getSimulationClock() / divider;
        Map<Integer, ArrayList<SingleEvent>> internalHopMap = new LinkedHashMap<>();

        for (int i = 0; i < divider; i++) {
            ArrayList<SingleEvent> aList = new ArrayList<>();
            internalHopMap.put(i, aList);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("Hop Processing");

        for (Map.Entry<Integer, SingleEvent> entry : hopTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();

            int index = (int) singleEvent.getSimulationClock() / breakDownNumber;
            if (index < divider) {
                internalHopMap.get(index).add(singleEvent);
            }
        }

        for (Map.Entry<Integer, ArrayList<SingleEvent>> entry : internalHopMap.entrySet()) {
            Integer integer = entry.getKey();
            ArrayList<SingleEvent> arrayList = entry.getValue();
            int size = arrayList.size();
            int total = 0;
            int avg = 0;
            for (SingleEvent singleEvent : arrayList) {
                total += singleEvent.getProcessingTime();
            }
            if (size != 0) {
                avg = total / size;
            }

            series.add((integer + 1) * breakDownNumber, avg);

        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Hop Processing", // Title
                "Simulation Clock Time", // x-axis Label
                "Number of Simulation Clock Time", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        return new ChartPanel(chart);
    }

    public JPanel csIdealTime() {
        Map<Double, Double> sync = new LinkedHashMap<>();

        int currentNode = 0;
        boolean flag = false;
        double in = 0, out = 0, difference = 0;
        for (Map.Entry<Integer, SingleEvent> entry : csTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getReciever().equals("OUT") && !flag) {
                currentNode = Integer.parseInt(singleEvent.getSender());
                in = singleEvent.getSimulationClock();
                flag = true;
            } else {
                if (currentNode != Integer.parseInt(singleEvent.getSender()) && currentNode != 0) {
                    out = singleEvent.getSimulationClock();
                    difference = out - in;
                    sync.put(in, difference);

                    /* Resetting variables */
                    flag = false;
                    in = 0;
                    out = 0;
                    difference = 0;
                    currentNode = 0;
                }
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("CS Ideal Time");

        int windowSize;
        if (sync.size() < 10) {
            windowSize = sync.size();
        } else {
            windowSize = 50;
        }

        Average average = new Average(windowSize);
        int counter = 1;
        for (Map.Entry<Double, Double> entry : sync.entrySet()) {
            double double1 = entry.getKey();
            double double2 = entry.getValue();
            average.update(double2);
            if (counter % windowSize == 0) {
                series.add(double1, average.getAverage());
            } else if (sync.size() < 10) {
                series.add(double1, average.getAverage());
            }
            counter++;
        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "CS Ideal Time", // Title
                "Simulation Clock Time", // x-axis Label
                "Simulation Clock Units", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }

    public JPanel systemThroughput() {
        double averageCS = averageCSTime();

        Map<Double, Double> sync = new LinkedHashMap<>();

        int currentNode = 0;
        boolean flag = false;
        double in = 0, out = 0, difference = 0;
        for (Map.Entry<Integer, SingleEvent> entry : csTrace.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (singleEvent.getReciever().equals("OUT") && !flag) {
                currentNode = Integer.parseInt(singleEvent.getSender());
                in = singleEvent.getSimulationClock();
                flag = true;
            } else {
                if (currentNode != Integer.parseInt(singleEvent.getSender()) && currentNode != 0) {
                    out = singleEvent.getSimulationClock();
                    difference = out - in;
                    sync.put(in, difference);

                    /* Resetting variables */
                    flag = false;
                    in = 0;
                    out = 0;
                    difference = 0;
                    currentNode = 0;
                }
            }
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("System Throughput");

        int windowSize;
        if (sync.size() < 10) {
            windowSize = sync.size();
        } else {
            windowSize = 50;
        }

        Average average = new Average(windowSize);
        int counter = 1;
        for (Map.Entry<Double, Double> entry : sync.entrySet()) {
            double double1 = entry.getKey();
            double double2 = entry.getValue();
            double value = 1 / (double2 + averageCS);
            average.update(value);

            if (counter % windowSize == 0) {
                series.add(double1, average.getAverage());
            }
            counter++;
        }

        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "System Throughput", // Title
                "Simulation Clock Time", // x-axis Label
                "Value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }

    public JPanel sychronizationDelay(String type) {
        Map<Integer, Double> in = new LinkedHashMap<>();
        Map<Integer, Boolean> flag = new LinkedHashMap<>();

        Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            flag.put(i, Boolean.FALSE);
            Map<Double, Double> internal = new LinkedHashMap<>();
            detailsSync.put(i, internal);
        }

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            int nodeId = Integer.parseInt(singleEvent.getSender());
            if (!flag.get(nodeId)) {
                if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1")) {
                    flag.put(nodeId, Boolean.TRUE);
                    in.put(nodeId, singleEvent.getSimulationClock());
                }
            } else {
                if (singleEvent.getType().equals("CS") && singleEvent.getReciever().equals("IN")) {
                    detailsSync.get(nodeId).put(singleEvent.getSimulationClock(), singleEvent.getSimulationClock() - in.get(nodeId));
                    flag.put(nodeId, false);
                }
            }
        }
        String title = "";
        XYSeriesCollection dataset = new XYSeriesCollection();

        if (type.equals("NA")) {
            title = "Sychronization Delay";
            int windowSize;
            if (detailsSync.get(1).size() < 50) {
                windowSize = 10;
            } else {
                windowSize = 25;
            }
            Average average = new Average(windowSize);
            int counter = 1;

            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Integer integer = entry.getKey();
                Map<Double, Double> map = entry.getValue();

                XYSeries series = new XYSeries("P" + integer);
                for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                    Double double1 = entry1.getKey();
                    Double double2 = entry1.getValue();
                    average.update(double2);

                    if (counter % windowSize == 0) {
                        series.add((Number) double1, average.getAverage());
                    }
                    counter++;
                }
                dataset.addSeries(series);
            }

        } else if (type.equals("Min")) {
            title = "Sychronization Delay (Minimum)";
            XYSeries series = new XYSeries("Processor ID");
            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Integer integer = entry.getKey();
                Map<Double, Double> map = entry.getValue();

                Double min = Collections.min(map.values());
                series.add(integer, min);
            }
            dataset.addSeries(series);
        } else if (type.equals("Max")) {
            title = "Sychronization Delay (Maximum)";
            XYSeries series = new XYSeries("Processor ID");
            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Integer integer = entry.getKey();
                Map<Double, Double> map = entry.getValue();
                Double min = Collections.max(map.values());
                series.add(integer, min);
            }
            dataset.addSeries(series);
        } else if (type.equals("Average")) {
            title = "Sychronization Delay (Average)";
            XYSeries series = new XYSeries("Processor ID");
            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Integer integer = entry.getKey();
                Map<Double, Double> map = entry.getValue();

                double total = 0;
                for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                    Double double2 = entry1.getValue();
                    total += double2;
                }
                series.add((Number) integer, total / map.size());
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // Title
                "Simulation Clock Time", // x-axis Label
                "Clock Units", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        if (!type.equals("NA")) {
            XYPlot xyPlot = (XYPlot) chart.getPlot();

            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
            renderer.setBaseShapesVisible(true);
            NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
            domain.setVerticalTickLabels(true);
        }
        return new ChartPanel(chart);
    }

    public JPanel responseTime(String type) {
        Map<Integer, Double> in = new LinkedHashMap<>();
        Map<Integer, Boolean> flag = new LinkedHashMap<>();

        Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfNodes; i++) {
            flag.put(i, Boolean.FALSE);
            Map<Double, Double> internal = new LinkedHashMap<>();
            detailsSync.put(i, internal);
        }

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            int nodeId = Integer.parseInt(singleEvent.getSender());
            if (!flag.get(nodeId)) {
                if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1")) {
                    flag.put(nodeId, Boolean.TRUE);
                    in.put(nodeId, singleEvent.getSimulationClock());
                }
            } else {
                if (singleEvent.getType().equals("CS") && singleEvent.getReciever().equals("OUT")) {
                    detailsSync.get(nodeId).put(singleEvent.getSimulationClock(), singleEvent.getSimulationClock() - in.get(nodeId));
                    flag.put(nodeId, false);
                }
            }
        }
        String title = "";
        XYSeriesCollection dataset = new XYSeriesCollection();

        switch (type) {
            case "NA":
                title = "Response Time";
                int windowSize;
                if (detailsSync.get(1).size() < 50) {
                    windowSize = 10;
                } else {
                    windowSize = 25;
                }
                Average average = new Average(windowSize);
                int counter = 1;
                for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                    Integer integer = entry.getKey();
                    Map<Double, Double> map = entry.getValue();

                    XYSeries series = new XYSeries("P" + integer);
                    for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                        Double double1 = entry1.getKey();
                        Double double2 = entry1.getValue();
                        average.update(double2);

                        if (counter % windowSize == 0) {
                            series.add((Number) double1, average.getAverage());
                        }
                        counter++;
                    }
                    dataset.addSeries(series);
                }
                break;
            case "Min": {
                title = "Response Time (Minimum)";
                XYSeries series = new XYSeries("Processor ID");
                for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                    Integer integer = entry.getKey();
                    Map<Double, Double> map = entry.getValue();

                    Double min = Collections.min(map.values());
                    series.add(integer, min);
                }
                dataset.addSeries(series);
                break;
            }
            case "Max": {
                title = "Response Time (Maximum)";
                XYSeries series = new XYSeries("Processor ID");
                for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                    Integer integer = entry.getKey();
                    Map<Double, Double> map = entry.getValue();
                    Double min = Collections.max(map.values());
                    series.add(integer, min);
                }
                dataset.addSeries(series);
                break;
            }
            case "Average": {
                title = "Response Time (Average)";
                XYSeries series = new XYSeries("Processor ID");
                for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                    Integer integer = entry.getKey();
                    Map<Double, Double> map = entry.getValue();

                    double total = 0;
                    for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                        Double double2 = entry1.getValue();
                        total += double2;
                    }
                    series.add((Number) integer, total / map.size());
                }
                dataset.addSeries(series);
                break;
            }
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // Title
                "Processor ID", // x-axis Label
                "Clock Units", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        if (!type.equals("NA")) {
            XYPlot xyPlot = (XYPlot) chart.getPlot();

            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
            renderer.setBaseShapesVisible(true);
            NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
            domain.setVerticalTickLabels(true);
        }
        return new ChartPanel(chart);
    }

    public JPanel calculateFairness() {
        Map<Integer, Integer> counts = new LinkedHashMap<>();
        Map<Integer, Integer> counts2 = new LinkedHashMap<>();

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Map.Entry<Integer, String> entry : fairness.entrySet()) {
            String[] string = entry.getValue().split(",");
            if (counts.get(Integer.parseInt(string[1])) == null) {
                counts.put(Integer.parseInt(string[1]), 1);
            } else {
                int count = counts.get(Integer.parseInt(string[1]));
                count++;
                counts.put(Integer.parseInt(string[1]), count);
            }

        }

        for (Map.Entry<Integer, String> entry : requests.entrySet()) {
            String node = entry.getValue();
            if (counts2.get(Integer.parseInt(node)) == null) {
                counts2.put(Integer.parseInt(node), 1);
            } else {
                int count1 = counts2.get(Integer.parseInt(node));
                count1++;
                counts2.put(Integer.parseInt(node), count1);
            }

        }

        XYSeries series = new XYSeries("Fairness Violations");
        XYSeries series2 = new XYSeries("Number of Requests");
        XYSeries series3 = new XYSeries("Number of CS entries");

        for (int i = 1; i <= numberOfNodes; i++) {
            series3.add((Integer) i, (Integer) numberOfCSAccess(i));
        }

        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            Integer nodeIDD = entry.getKey();
            Integer count = entry.getValue();
            series.add(nodeIDD, count);

            System.out.println("Node " + nodeIDD + " has violations " + count);
        }

        for (Map.Entry<Integer, Integer> entry : counts2.entrySet()) {
            Integer nodeIDD = entry.getKey();
            Integer count = entry.getValue();
            if (numberOfCSAccess(nodeIDD) != count) {
                count = numberOfCSAccess(nodeIDD);
            }
            series2.add(nodeIDD, count);

            System.out.println("Node " + nodeIDD + " has request " + count);
        }

        dataset.addSeries(series);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Fairness Violations", // Title
                "Processor ID", // x-axis Label
                "Count", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        XYPlot xyPlot = (XYPlot) chart.getPlot();

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
        renderer.setBaseShapesVisible(true);
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setVerticalTickLabels(true);
        return new ChartPanel(chart);
    }
}
