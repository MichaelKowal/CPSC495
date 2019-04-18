/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.performance;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import unbc.ca.distributed.management.ObjectFactory;

/**
 *
 * @author behnish
 */
public class MultipleSelection {

    private List<Object> runs;
    private Map<String, Map<Integer, SingleEvent>> completeTrace = new LinkedHashMap<>();
    private Map<String, Map<Integer, SingleEvent>> csTrace = new LinkedHashMap<>();
    private Map<String, Map<Integer, SingleEvent>> sendTrace = new LinkedHashMap<>();
    private Map<String, Map<Integer, SingleEvent>> hopTrace = new LinkedHashMap<>();
    private Map<String, Map<Integer, SingleEvent>> finalSend = new LinkedHashMap<>();
    private Map<String, Integer> numberNodes = new LinkedHashMap<>();
    private Map<String, Map<Integer, Double>> lastClocks = new LinkedHashMap<>();
    private Map<String, Map<Integer, Integer>> noOfCSAcess = new LinkedHashMap<>();
    private Map<String, Map<Integer, Double>> csClock = new LinkedHashMap<>();

    private Map<String, Boolean> loaderOrNot = new LinkedHashMap<>();

    private Map<Integer, String> allRuns = new LinkedHashMap<>();

    private Map<String, Map<Integer, String>> fairness = new LinkedHashMap<>();
    private Map<String, Map<Integer, String>> requests = new LinkedHashMap<>();

    public MultipleSelection(List<Object> runs) throws IOException {
        this.runs = runs;
        ObjectFactory.getSidePanel().showDetails.setText("");
        intitialize();
    }

    private void intitialize() {
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runNameFile = (String) it.next();

            Map<Integer, SingleEvent> comT = new LinkedHashMap<>();
            Map<Integer, SingleEvent> csT = new LinkedHashMap<>();
            Map<Integer, SingleEvent> sendT = new LinkedHashMap<>();
            Map<Integer, SingleEvent> hopT = new LinkedHashMap<>();
            Map<Integer, SingleEvent> finalSendT = new LinkedHashMap<>();
            Map<Integer, Double> processMe = new LinkedHashMap<>();
            Map<Integer, Integer> csCount = new LinkedHashMap<>();
            Map<Integer, Double> csClockT = new LinkedHashMap<>();

            Map<Integer, String> fairV = new LinkedHashMap<>();
            Map<Integer, String> reqV = new LinkedHashMap<>();

            completeTrace.put(runNameFile, comT);
            csTrace.put(runNameFile, csT);
            sendTrace.put(runNameFile, sendT);
            hopTrace.put(runNameFile, hopT);
            finalSend.put(runNameFile, finalSendT);
            lastClocks.put(runNameFile, processMe);
            csClock.put(runNameFile, csClockT);
            noOfCSAcess.put(runNameFile, csCount);
            loaderOrNot.put(runNameFile, Boolean.FALSE);
            fairness.put(runNameFile, fairV);
            requests.put(runNameFile, reqV);

        }
    }

    public void loadNew(List<Object> runs) {
        this.runs = runs;
        ObjectFactory.getSidePanel().showDetails.setText("");
        for (Map.Entry<Integer, String> entry : allRuns.entrySet()) {
            String string = entry.getValue();
            removeLoadedRun(string);
            loaderOrNot.put(string, Boolean.FALSE);
        }
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String object = (String) it.next();
            loadTraceFromFile(object);
        }
    }

    private void removeLoadedRun(String runNameFile) {
        completeTrace.remove(runNameFile);
        csTrace.remove(runNameFile);
        sendTrace.remove(runNameFile);
        hopTrace.remove(runNameFile);
        finalSend.remove(runNameFile);
        lastClocks.remove(runNameFile);
        csClock.remove(runNameFile);
        noOfCSAcess.remove(runNameFile);
        fairness.remove(runNameFile);
        requests.remove(runNameFile);
    }

    private void temploadTraceFromFile(String runNameFile) {
        try {
            loadTrace(runNameFile);
            loadFinalTrace(runNameFile);
            loadHopTrace(runNameFile);

            loaderOrNot.put(runNameFile, Boolean.TRUE);

            for (int i = 1; i <= numberNodes.get(runNameFile); i++) {
                lastClocks.get(runNameFile).put(i, lastClock(i, runNameFile));
                noOfCSAcess.get(runNameFile).put(i, numberOfCSAccess(i, runNameFile));
                csClock.get(runNameFile).put(i, lastCsClock(i, runNameFile));
            }
        } catch (IOException ex) {
            Logger.getLogger(MultipleSelection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTraceFromFile(String runNameFile) {
        try {
            if (!loaderOrNot.get(runNameFile)) {
                System.out.println(runNameFile + " has been loaded");
                Map<Integer, SingleEvent> comT = new LinkedHashMap<>();
                Map<Integer, SingleEvent> csT = new LinkedHashMap<>();
                Map<Integer, SingleEvent> sendT = new LinkedHashMap<>();
                Map<Integer, SingleEvent> hopT = new LinkedHashMap<>();
                Map<Integer, SingleEvent> finalSendT = new LinkedHashMap<>();
                Map<Integer, Double> processMe = new LinkedHashMap<>();
                Map<Integer, Integer> csCount = new LinkedHashMap<>();
                Map<Integer, Double> csClockT = new LinkedHashMap<>();
                Map<Integer, String> fairV = new LinkedHashMap<>();
                Map<Integer, String> reqV = new LinkedHashMap<>();

                completeTrace.put(runNameFile, comT);
                csTrace.put(runNameFile, csT);
                sendTrace.put(runNameFile, sendT);
                hopTrace.put(runNameFile, hopT);
                finalSend.put(runNameFile, finalSendT);
                lastClocks.put(runNameFile, processMe);
                csClock.put(runNameFile, csClockT);
                noOfCSAcess.put(runNameFile, csCount);
                loaderOrNot.put(runNameFile, Boolean.FALSE);
                fairness.put(runNameFile, fairV);
                requests.put(runNameFile, reqV);

                loadTrace(runNameFile);
                loadFinalTrace(runNameFile);
                loadHopTrace(runNameFile);

                loaderOrNot.put(runNameFile, Boolean.TRUE);

                ObjectFactory.getSidePanel().showDetails.append("Simulation " + runNameFile + " has been loaded \n \n");
                System.out.println("Simulation " + runNameFile + " has been loaded");

                for (int i = 1; i <= numberNodes.get(runNameFile); i++) {
                    lastClocks.get(runNameFile).put(i, lastClock(i, runNameFile));
                    noOfCSAcess.get(runNameFile).put(i, numberOfCSAccess(i, runNameFile));
                    csClock.get(runNameFile).put(i, lastCsClock(i, runNameFile));
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MultipleSelection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private int numberOfCSAccess(int processId, String run) {
        int csAccessTimes = 0;

        for (Map.Entry<Integer, SingleEvent> entry : csTrace.get(run).entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (processId == Integer.parseInt(singleEvent.getSender()) && singleEvent.getReciever().equals("IN")) {
                csAccessTimes++;
            }
        }

        return csAccessTimes;
    }

    public JPanel csIdealTime() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            Map<Integer, SingleEvent> map = csTrace.get(runName);

            Map<Double, Double> sync = new LinkedHashMap<>();

            int currentNode = 0;
            boolean flag = false;
            double in = 0, out = 0, difference = 0;
            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();
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
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            XYSeries series = new XYSeries(runName + "-" + algorithm);
            int windowSize;
            if (sync.size() < 50) {
                windowSize = 10;
            } else {
                windowSize = 50;
            }

            Average average = new Average(windowSize);
            int counter = 1;
            for (Map.Entry<Double, Double> entry2 : sync.entrySet()) {
                double double1 = entry2.getKey();
                double double2 = entry2.getValue();
                average.update(double2);

                if (counter % windowSize == 0) {
                    series.add(double1, average.getAverage());
                }
                counter++;
            }
            dataset.addSeries(series);
            removeLoadedRun(runName);
        }

        intitialize();

        JFreeChart chart = ChartFactory.createXYLineChart(
                "CS Ideal Time", // Title
                "Simulation Clock Time", // x-axis Label
                "Clock Units", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
          XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        
        return new ChartPanel(chart);
    }

    public JPanel systemThroughput() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            Map<Integer, SingleEvent> map = csTrace.get(runName);

            double averageCS = averageCSTime(map, runName);

            Map<Double, Double> sync = new LinkedHashMap<>();

            int currentNode = 0;
            boolean flag = false;
            double in = 0, out = 0, difference = 0;
            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();
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

            int windowSize;
            if (sync.size() < 50) {
                windowSize = 10;
            } else {
                windowSize = 50;
            }
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            XYSeries series = new XYSeries(runName + "-" + algorithm);

            Average average = new Average(windowSize);
            int counter = 1;
            for (Map.Entry<Double, Double> entry2 : sync.entrySet()) {
                double double1 = entry2.getKey();
                double double2 = entry2.getValue();
                double value = 1 / (double2 + averageCS);

                average.update(value);
                if (counter % windowSize == 0) {
                    series.add(double1, average.getAverage());
                }
                counter++;
            }
            dataset.addSeries(series);

            removeLoadedRun(runName);
        }

        intitialize();

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
          XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        return new ChartPanel(chart);
    }

    private double averageCSTime(Map<Integer, SingleEvent> csTemp, String runName) {
        Map<Integer, ArrayList<Double>> cs = new LinkedHashMap<>();

        for (int i = 1; i <= numberNodes.get(runName); i++) {
            ArrayList<Double> process = new ArrayList<>();
            cs.put(i, process);
        }

        int currentNode = 0;
        boolean flag = false;
        double in = 0, out = 0, difference = 0;

        for (Map.Entry<Integer, SingleEvent> entry : csTemp.entrySet()) {
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

        return (int) (totalAverageCs / numberNodes.get(runName) + 0.5);
    }

    public JPanel hopCount() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            Map<Integer, SingleEvent> map = completeTrace.get(runName);

            Map<Integer, Map<Integer, Integer>> ds = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                Map<Integer, Integer> single = new LinkedHashMap<>();
                ds.put(i, single);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {

                SingleEvent singleEvent = entry1.getValue();
                if (singleEvent.getType().equals("R")) {
                    ds.get(Integer.parseInt(singleEvent.getReciever())).put(Integer.parseInt(singleEvent.getSender()), singleEvent.getHopCount());
                }
            }
            Map<Integer, Integer> sum = new LinkedHashMap<>();

            for (Map.Entry<Integer, Map<Integer, Integer>> entry2 : ds.entrySet()) {
                Integer integer = entry2.getKey();
                int totalHopCount = 0;
                Map<Integer, Integer> map2 = entry2.getValue();
                for (Map.Entry<Integer, Integer> entry4 : map2.entrySet()) {
                    Integer integer2 = entry4.getValue();
                    totalHopCount += integer2;
                }
                sum.put(integer, totalHopCount);
            }

            String[] column = new String[numberNodes.get(runName)];

            String series1 = runName;
            for (int i = 0; i < sum.size(); i++) {
                column[i] = "P" + (i + 1);
            }

            for (Map.Entry<Integer, Integer> entry5 : sum.entrySet()) {
                Integer integer = entry5.getKey();
                Integer integer1 = entry5.getValue();

                dataset.addValue(integer1, series1, column[integer - 1]);

            }

            removeLoadedRun(runName);
        }

        intitialize();
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

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String string = (String) it.next();
            temploadTraceFromFile(string);
            Map<Integer, SingleEvent> map = csTrace.get(string);

            String[] column = new String[numberNodes.get(string)];

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(string.split(" ")[1])).getAlgorithm();

            String series1 = string + "-" + algorithm;
            for (int i = 0; i < numberNodes.get(string); i++) {
                column[i] = "P" + (i + 1);
            }

            Map<Integer, ArrayList<Double>> cs = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(string); i++) {
                ArrayList<Double> process = new ArrayList<>();
                cs.put(i, process);
            }

            int currentNode = 0;
            boolean flag = false;
            double in = 0, out = 0, difference = 0;
            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();
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
            for (Map.Entry<Integer, ArrayList<Double>> entry2 : cs.entrySet()) {
                Integer integer = entry2.getKey();
                ArrayList<Double> arrayList = entry2.getValue();

                int count = arrayList.size();
                double total = 0;
                double average = 0;
                for (Double double1 : arrayList) {
                    total += double1;
                }

                average = total / count;
                dataset.addValue(average, series1, column[integer - 1]);
            }

            removeLoadedRun(string);
        }

        intitialize();
        JFreeChart chart = ChartFactory.createBarChart(
                "Critical Section Sitting Time", // chart title
                "Process ID", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );
  
        return new ChartPanel(chart);
    }

    public JPanel responseTime() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String string = (String) it.next();
            temploadTraceFromFile(string);
            Map<Integer, SingleEvent> map = completeTrace.get(string);

            String[] column = new String[numberNodes.get(string)];

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(string.split(" ")[1])).getAlgorithm();
            String series1 = string + "-" + algorithm;

            for (int i = 0; i < numberNodes.get(string); i++) {
                column[i] = "P" + (i + 1);
            }

            Map<Integer, ArrayList<Double>> response = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(string); i++) {
                ArrayList<Double> process = new ArrayList<>();
                response.put(i, process);
            }

            int currentNode = 0;
            boolean flag = false;
            double in = 0, out = 0, difference = 0;
            for (int i = 1; i <= numberNodes.get(string); i++) {
                flag = false;
                for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                    SingleEvent singleEvent = entry1.getValue();
                    if (singleEvent.getType().equals("S") && (!flag) && (singleEvent.getContent().equals("1")) && (i == Integer.parseInt(singleEvent.getSender()))) {
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
            for (Map.Entry<Integer, ArrayList<Double>> entry2 : response.entrySet()) {
                Integer integer = entry2.getKey();
                ArrayList<Double> arrayList = entry2.getValue();

                int count = arrayList.size();
                double total = 0;
                double average = 0;
                for (Double double1 : arrayList) {
                    total += double1;
                }

                average = total / count;
                dataset.addValue(average, series1, column[integer - 1]);
            }

            removeLoadedRun(string);
        }

        intitialize();

        JFreeChart chart = ChartFactory.createBarChart(
                "Average Response Time", // chart title
                "Process ID", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );
  
        return new ChartPanel(chart);
    }

    public JPanel hopProcessing() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String string = (String) it.next();
            temploadTraceFromFile(string);

            Map<Integer, SingleEvent> map = hopTrace.get(string);

            String[] column = new String[numberNodes.get(string)];

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(string.split(" ")[1])).getAlgorithm();
            String series = string + "-" + algorithm;

            for (int i = 0; i < numberNodes.get(string); i++) {
                column[i] = "P" + (i + 1);
            }

            Map<Integer, ArrayList<SingleEvent>> hopProcessing = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(string); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                hopProcessing.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                hopProcessing.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
            }

            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry2 : hopProcessing.entrySet()) {
                Integer integer = entry2.getKey();
                ArrayList<SingleEvent> arrayList = entry2.getValue();

                double total = 0;
                double average = 0;
                for (SingleEvent single : arrayList) {
                    total += single.getProcessingTime();
                }

                average = total / arrayList.size();

                dataset.addValue(Math.round(average), series, column[integer - 1]);
            }
            removeLoadedRun(string);
        }

        intitialize();
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

    public JPanel hopProcessingClock() {

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String string = (String) it.next();
            temploadTraceFromFile(string);
            Map<Integer, SingleEvent> map = hopTrace.get(string);


            /* *****************************************************************************/
            int divider = 20;
            int breakDownNumber = (int) completeTrace.get(string).get(completeTrace.get(string).size()).getSimulationClock() / divider;
            Map<Integer, ArrayList<SingleEvent>> internalHopMap = new LinkedHashMap<>();

            for (int i = 0; i < divider; i++) {
                ArrayList<SingleEvent> aList = new ArrayList<>();
                internalHopMap.put(i, aList);
            }

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(string.split(" ")[1])).getAlgorithm();
            XYSeries series = new XYSeries(string + "-" + algorithm);

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                int index = (int) singleEvent.getSimulationClock() / breakDownNumber;
                if (index < divider) {
                    internalHopMap.get(index).add(singleEvent);
                }
            }

            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry2 : internalHopMap.entrySet()) {
                Integer integer = entry2.getKey();
                ArrayList<SingleEvent> arrayList = entry2.getValue();
                int size = arrayList.size();
                int total = 0;
                int avg = 0;
                for (SingleEvent singleEvent : arrayList) {
                    total += singleEvent.getProcessingTime();
                }
                if (size != 0) {
                    avg = total / size;
                }

                series.add(integer * breakDownNumber, avg);
            }
            dataset.addSeries(series);

            /**
             * *****************************************************************************
             */
            removeLoadedRun(string);
        }

        intitialize();
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Hop Processing", // Title
                "Simulation Clock Time", // x-axis Label
                "Mirco seconds", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
          XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
  //      XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true);
//        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//        domain.setVerticalTickLabels(true);
        return new ChartPanel(chart);
    }

    private double lastClock(int processId, String run) {
        double simulationClockProcess = 0;

        for (Map.Entry<Integer, SingleEvent> entry : csTrace.get(run).entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (processId == Integer.parseInt(singleEvent.getSender())) {
                if (simulationClockProcess < singleEvent.getSimulationClock()) {
                    simulationClockProcess = singleEvent.getSimulationClock();
                }
            }
        }

        for (Map.Entry<Integer, SingleEvent> entry : finalSend.get(run).entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (processId == Integer.parseInt(singleEvent.getSender()) && singleEvent.getType().equals("F") && !singleEvent.getContent().equals("1")) {
                if (simulationClockProcess < singleEvent.getSimulationClock()) {
                    simulationClockProcess = singleEvent.getSimulationClock();
                }
            }
        }

        return simulationClockProcess;
    }

    private double lastCsClock(int processId, String run) {
        double simulationClockProcess = 0;

        for (Map.Entry<Integer, SingleEvent> entry : csTrace.get(run).entrySet()) {
            SingleEvent singleEvent = entry.getValue();
            if (processId == Integer.parseInt(singleEvent.getSender())) {
                if (simulationClockProcess < singleEvent.getSimulationClock()) {
                    simulationClockProcess = singleEvent.getSimulationClock();
                }
            }
        }
        return simulationClockProcess;
    }

    public JPanel numberOfMessage() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);
            Map<Integer, SingleEvent> map = finalSend.get(runName);

            String[] column = new String[numberNodes.get(runName)];

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            String series1 = runName + "-" + algorithm;
            for (int i = 0; i < numberNodes.get(runName); i++) {
                column[i] = "P" + (i + 1);
            }

            Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (singleEvent.getType().equals("F") && !singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }

            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total = arrayList.size();
                double average = 0;

                if (csTrace.isEmpty()) {
                    average = total;
                } else {
                    average = total / numberOfCSAccess(integer, runName);
                }
                dataset.addValue(Math.round(average), series1, column[integer - 1]);
            }

            removeLoadedRun(runName);
        }

        intitialize();
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
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            Map<Integer, SingleEvent> map = sendTrace.get(runName);

            String[] column = new String[numberNodes.get(runName)];

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            String series1 = runName + "-" + algorithm;
            for (int i = 0; i < numberNodes.get(runName); i++) {
                column[i] = "P" + (i + 1);
            }

            Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (!singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }

            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total = arrayList.size();
                double average = 0;

                if (csTrace.isEmpty()) {
                    average = total;
                } else {
                    average = total / numberOfCSAccess(integer, runName);
                }
                dataset.addValue(Math.round(average), series1, column[integer - 1]);
            }

            removeLoadedRun(runName);
        }

        intitialize();
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

    private void loadTrace(String traceFile) throws IOException {
        if (traceFile != null) {
            int i = 1;
            int j = 1;
            int f = 1;
            int re = 1;
            Set<Integer> node = new HashSet<>();
            File file = new File(traceFile);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
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
                                    sendTrace.get(traceFile).put(j, t);
                                }
                                completeTrace.get(traceFile).put(i, t);
                                j++;
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
                                completeTrace.get(traceFile).put(i, t);
                                i++;
                                break;
                            }
                            case "FAIR": {
                                fairness.get(traceFile).put(f, raw[1] + "," + raw[2]);
                                f++;
                                break;
                            }
                            case "REQ": {
                                requests.get(traceFile).put(f, raw[1]);
                                re++;
                                break;
                            }
                        }
                    }
                }
            }
            numberNodes.put(traceFile, node.size());
        }
    }

    private void loadHopTrace(String traceFile) throws IOException {
        if (traceFile != null) {
            int k = 1;
            File file = new File(traceFile + "_hop");
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
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
                                hopTrace.get(traceFile).put(k, t);
                                k++;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void loadFinalTrace(String traceFile) throws IOException {
        if (traceFile != null) {
            int k = 1;
            int j = 1;
            File file = new File(traceFile + "_final");
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
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
                                finalSend.get(traceFile).put(k, t);
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
                                finalSend.get(traceFile).put(k, t);
                                csTrace.get(traceFile).put(j, t);
                                j++;
                                k++;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public JPanel numberOfMessageProcessesWise() {

        XYSeriesCollection dataset = new XYSeriesCollection();

        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);
            Map<Integer, SingleEvent> map = finalSend.get(runName);

            Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (singleEvent.getType().equals("F") && !singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }

            double average = 0;
            double anotherAv = 0;
            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total = arrayList.size();

                average = total / numberOfCSAccess(integer, runName);
                anotherAv += average;

            }

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            if (coolStuff.get(algorithm) != null) {
                coolStuff.get(algorithm).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), anotherAv / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), anotherAv / numberNodes.get(runName));
                coolStuff.put(algorithm, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();

        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Number of messages", // Title
                "Number of Processes", // x-axis Label
                "Value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
//        XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true);
//        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//        domain.setVerticalTickLabels(true);
  XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        return new ChartPanel(chart);
    }

    public JPanel sychronizationDelay(String type) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "";

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            Map<Integer, SingleEvent> mapF = finalSend.get(runName);

            Map<Integer, Double> in = new LinkedHashMap<>();
            Map<Integer, Boolean> flag = new LinkedHashMap<>();

            Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                flag.put(i, Boolean.FALSE);
                Map<Double, Double> internal = new LinkedHashMap<>();
                detailsSync.put(i, internal);
            }

            for (Map.Entry<Integer, SingleEvent> entry : mapF.entrySet()) {
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
            switch (type) {
                case "NA":
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
                    break;
                case "Min": {
                    title = "Sychronization Delay (Minimum)";
                    XYSeries series = new XYSeries(algorithm + " " + runName);
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
                    title = "Sychronization Delay (Maximum)";
                    XYSeries series = new XYSeries(algorithm + " " + runName);
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
                    title = "Sychronization Delay (Average)";
                    XYSeries series = new XYSeries(algorithm + " " + runName);
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
            removeLoadedRun(runName);
        }

        intitialize();
        JFreeChart chart = ChartFactory.createXYLineChart(
                title, // Title
                "Process ID", // x-axis Label
                "Clock Units", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
//        if (!type.equals("NA")) {
//            XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//            renderer.setBaseShapesVisible(true);
//            NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//            domain.setVerticalTickLabels(true);
//        }
          XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        return new ChartPanel(chart);
    }

    public JPanel totalCSAccess() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Iterator<Object> it1 = runs.iterator(); it1.hasNext();) {
            String runNameTemp = (String) it1.next();
            temploadTraceFromFile(runNameTemp);
            String[] column = new String[numberNodes.get(runNameTemp)];
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runNameTemp.split(" ")[1])).getAlgorithm();
            String series = runNameTemp + "-" + algorithm;

            for (int i = 0; i < numberNodes.get(runNameTemp); i++) {
                column[i] = "P" + (i + 1);
            }

            for (int i = 1; i <= numberNodes.get(runNameTemp); i++) {
                dataset.addValue(numberOfCSAccess(i, runNameTemp), series, column[i - 1]);
            }
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

    public JPanel responseTimeProcessWise(String type) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "";

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            Map<Integer, SingleEvent> mapF = finalSend.get(runName);

            Map<Integer, Double> in = new LinkedHashMap<>();
            Map<Integer, Boolean> flag = new LinkedHashMap<>();

            Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                flag.put(i, Boolean.FALSE);
                Map<Double, Double> internal = new LinkedHashMap<>();
                detailsSync.put(i, internal);
            }

            for (Map.Entry<Integer, SingleEvent> entry : mapF.entrySet()) {
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
                    XYSeries series = new XYSeries(algorithm + " " + runName);
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
                    XYSeries series = new XYSeries(algorithm + " " + runName);
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
                    XYSeries series = new XYSeries(algorithm + " " + runName);
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
            removeLoadedRun(runName);
        }

        intitialize();
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
//        if (!type.equals("NA")) {
//            XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//            renderer.setBaseShapesVisible(true);
//            NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//            domain.setVerticalTickLabels(true);
//        }
          XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        return new ChartPanel(chart);
    }

    // Calculating average for simulation runs
    public JPanel responseTimeProcessWiseAverage() {
        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "";

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            Map<Integer, SingleEvent> mapF = finalSend.get(runName);

            Map<Integer, Double> in = new LinkedHashMap<>();
            Map<Integer, Boolean> flag = new LinkedHashMap<>();

            Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                flag.put(i, Boolean.FALSE);
                Map<Double, Double> internal = new LinkedHashMap<>();
                detailsSync.put(i, internal);
            }

            for (Map.Entry<Integer, SingleEvent> entry : mapF.entrySet()) {
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

            title = "Response Time (Average)";

            double grandTotal = 0;
            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Map<Double, Double> map = entry.getValue();

                double total = 0;
                for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                    Double double2 = entry1.getValue();
                    total += double2;
                }
                grandTotal += total / map.size();
            }

            String key = algorithm + "-" + ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();
            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), grandTotal / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), grandTotal / numberNodes.get(runName));
                coolStuff.put(key, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Response Time", // Title
                "Number of Processes", // x-axis Label
                "Mirco Seconds", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
//        XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true);
//        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//        domain.setVerticalTickLabels(true);
  XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        return new ChartPanel(chart);
    }

    public JPanel sychronizationDelayAverage() {
        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "";

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            Map<Integer, SingleEvent> mapF = finalSend.get(runName);

            Map<Integer, Double> in = new LinkedHashMap<>();
            Map<Integer, Boolean> flag = new LinkedHashMap<>();

            Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                flag.put(i, Boolean.FALSE);
                Map<Double, Double> internal = new LinkedHashMap<>();
                detailsSync.put(i, internal);
            }

            for (Map.Entry<Integer, SingleEvent> entry : mapF.entrySet()) {
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

            title = "Synchronize delay (Average)";

            double grandTotal = 0;
            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Map<Double, Double> map = entry.getValue();

                double total = 0;
                for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                    Double double2 = entry1.getValue();
                    total += double2;
                }
                grandTotal += total / map.size();
            }

            String key = algorithm + "-" + ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();

            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), grandTotal / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), grandTotal / numberNodes.get(runName));
                coolStuff.put(key, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Synchronization delay", // Title
                "Number of Processes", // x-axis Label
                "Mirco Seconds", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        XYPlot plot = (XYPlot) chart.getPlot();

        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);

        return new ChartPanel(chart);
    }

    public JPanel systemThroughputAverage() {

        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);
            Map<Integer, SingleEvent> map = csTrace.get(runName);

            double averageCS = averageCSTime(map, runName);

            Map<Double, Double> sync = new LinkedHashMap<>();

            int currentNode = 0;
            boolean flag = false;
            double in = 0, out = 0, difference = 0;
            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();
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

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();

            double grandTotal = 0;
            for (Map.Entry<Double, Double> entry2 : sync.entrySet()) {
                double double2 = entry2.getValue();
                double value = 1 / (double2 + averageCS);
                grandTotal += value;

            }
            String key = algorithm + "-" + ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();
            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
                        grandTotal / sync.size());
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
                        grandTotal / sync.size());
                coolStuff.put(key, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();

        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average System Throughput", // Title
                "Simulation Clock Time", // x-axis Label
                "Value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);

        return new ChartPanel(chart);

    }

    public JPanel csIdealTimeAverage() {
        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            Map<Integer, SingleEvent> map = csTrace.get(runName);

            Map<Double, Double> sync = new LinkedHashMap<>();

            int currentNode = 0;
            boolean flag = false;
            double in = 0, out = 0, difference = 0;
            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();
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
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();

            double grandTotal = 0;
            for (Map.Entry<Double, Double> entry2 : sync.entrySet()) {
                double double2 = entry2.getValue();
                grandTotal += double2;
            }

            String key = algorithm + "-" + ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();
            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
                        grandTotal / sync.size());
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
                        grandTotal / sync.size());
                coolStuff.put(key, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average CS Ideal Time", // Title
                "Simulation Clock Time", // x-axis Label
                "Clock Units", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
        
        XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);
        return new ChartPanel(chart);
    }

    public JPanel totalCSAccessAverage() {
        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it1 = runs.iterator(); it1.hasNext();) {
            String runName = (String) it1.next();
            temploadTraceFromFile(runName);

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            double grandTotal = 0;
            for (int i = 1; i <= numberNodes.get(runName); i++) {
                grandTotal += numberOfCSAccess(i, runName);
            }

            String key = algorithm + "-" + ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();

            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
                        grandTotal / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
                        grandTotal / numberNodes.get(runName));
                coolStuff.put(key, details);
            }
            removeLoadedRun(runName);
        }

        intitialize();
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Critical Section Access", // Title
                "Number of Processes", // x-axis Label
                "Value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);

        return new ChartPanel(chart);
    }

    public JPanel totalNumberOfMessagesAverage() {
        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            System.out.println("Loaded run " + runName);
            temploadTraceFromFile(runName);
            Map<Integer, SingleEvent> map = sendTrace.get(runName);

            String topology = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();

            String key = topology + "-" + algorithm;
            Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (!singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }
            double grandTotal = 0;
            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total = arrayList.size();
                double average = 0;

                if (csTrace.isEmpty()) {
                    average = total;
                } else {
                    average = total / numberOfCSAccess(integer, runName);
                }
                grandTotal += average;
            }

            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(numberNodes.get(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getName()),
                        grandTotal / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(numberNodes.get(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getName()),
                        grandTotal / numberNodes.get(runName));
                coolStuff.put(key, details);
            }
            removeLoadedRun(runName);
        }

        intitialize();
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Number of messages sent", // chart title
                "Number of Processes", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(false);
        rangeAxis.setUpperMargin(0.12);

        return new ChartPanel(chart);
    }

    public JPanel averageNumberOfMessagesInterarrival() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        String algorithm = null;
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            temploadTraceFromFile(runName);
            Map<Integer, SingleEvent> map = finalSend.get(runName);

            Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (singleEvent.getType().equals("F") && !singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }

            double average = 0;
            double anotherAv = 0;
            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total = arrayList.size();

                average = total / numberOfCSAccess(integer, runName);
                anotherAv += average;

            }

            double ir = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getInterQ();
            String key = String.valueOf(ir);

            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), anotherAv / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), anotherAv / numberNodes.get(runName));
                coolStuff.put(key, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();

        XYSeries series = new XYSeries("Arrival Rate ");
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {

            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Double integer1 = entry1.getValue();
                series.add(Double.parseDouble(string), integer1);
            }

        }
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Number of messages", // Title
                "Inter Arrival Rate", // x-axis Label
                "Value", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
//        XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true);
//        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//        domain.setVerticalTickLabels(true);

        return new ChartPanel(chart);
    }

    public JPanel sychronizationDelayAverageInterarrival() {
        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "";
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            Map<Integer, SingleEvent> mapF = finalSend.get(runName);

            Map<Integer, Double> in = new LinkedHashMap<>();
            Map<Integer, Boolean> flag = new LinkedHashMap<>();

            Map<Integer, Map<Double, Double>> detailsSync = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                flag.put(i, Boolean.FALSE);
                Map<Double, Double> internal = new LinkedHashMap<>();
                detailsSync.put(i, internal);
            }

            for (Map.Entry<Integer, SingleEvent> entry : mapF.entrySet()) {
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

            title = "Synchronize delay (Average)";

            double grandTotal = 0;
            for (Map.Entry<Integer, Map<Double, Double>> entry : detailsSync.entrySet()) {
                Map<Double, Double> map = entry.getValue();

                double total = 0;
                for (Map.Entry<Double, Double> entry1 : map.entrySet()) {
                    Double double2 = entry1.getValue();
                    total += double2;
                }
                grandTotal += total / map.size();
            }

            //String key = algorithm + "-" + ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();
            double ir = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getInterQ();
            String key = String.valueOf(ir);
            if (coolStuff.get(key) != null) {
                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), grandTotal / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(), grandTotal / numberNodes.get(runName));
                coolStuff.put(key, details);
            }

            removeLoadedRun(runName);
        }

        intitialize();
        XYSeries series = new XYSeries("Arrival Rate");

        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Double integer1 = entry1.getValue();
                series.add(Double.parseDouble(string), integer1);
            }
        }
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Synchronization delay", // Title
                "Inter Arrival Rate", // x-axis Label
                "Mirco Seconds", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        return new ChartPanel(chart);
    }

    public JPanel specialMetric() {

        Map<String, Map<Integer, Double>> coolStuff = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();

            temploadTraceFromFile(runName);
            Map<Integer, SingleEvent> map = sendTrace.get(runName);

            String topology = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getTopology();
            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();

            String key = topology + "-" + algorithm + " With Hop";
            Map<Integer, ArrayList<SingleEvent>> sendMessages = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (!singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }
            double grandTotal = 0;
            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total = arrayList.size();
                double average = 0;

                if (csTrace.isEmpty()) {
                    average = total;
                } else {
                    average = total / numberOfCSAccess(integer, runName);
                }
                grandTotal += average;
            }

            if (coolStuff.get(key) != null) {
//                coolStuff.get(key).put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
//                        grandTotal / numberNodes.get(runName));
                coolStuff.get(key).put(numberNodes.get(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getName()),
                        grandTotal / numberNodes.get(runName));
            } else {
                Map<Integer, Double> details = new HashMap<>();
//                details.put(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getWorkloadObject().getNoOfProcessor(),
//                        grandTotal / numberNodes.get(runName));
                details.put(numberNodes.get(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getName()),
                        grandTotal / numberNodes.get(runName));
                coolStuff.put(key, details);
            }

            /* For the other performance metric */
            Map<Integer, SingleEvent> map1 = finalSend.get(runName);

            Map<Integer, ArrayList<SingleEvent>> sendMessages1 = new LinkedHashMap<>();

            for (int i = 1; i <= numberNodes.get(runName); i++) {
                ArrayList<SingleEvent> process = new ArrayList<>();
                sendMessages1.put(i, process);
            }

            for (Map.Entry<Integer, SingleEvent> entry1 : map1.entrySet()) {
                SingleEvent singleEvent = entry1.getValue();

                if ((lastClocks.get(runName).get(Integer.parseInt(singleEvent.getSender())) >= singleEvent.getSimulationClock())) {
                    if (singleEvent.getType().equals("F") && singleEvent.getContent().equals("1") && singleEvent.getSimulationClock() <= csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages1.get(Integer.parseInt(singleEvent.getSender())).add(singleEvent);
                        }
                    } else if (singleEvent.getType().equals("F") && !singleEvent.getContent().equals("0")) {
                        if (singleEvent.getSimulationClock() < csClock.get(runName).get(Integer.parseInt(singleEvent.getSender()))) {
                            sendMessages1.get(Integer.parseInt(singleEvent.getReciever())).add(singleEvent);
                        }
                    }
                }
            }

            double grandT = 0;
            for (Map.Entry<Integer, ArrayList<SingleEvent>> entry3 : sendMessages1.entrySet()) {
                Integer integer = entry3.getKey();
                ArrayList<SingleEvent> arrayList = entry3.getValue();

                double total1 = arrayList.size();
                double average1 = 0;

                if (csTrace.isEmpty()) {
                    average1 = total1;
                } else {
                    average1 = total1 / numberOfCSAccess(integer, runName);
                }
                grandT += average1;

            }

            String secondkey = topology + "-" + algorithm + " Without Hop";
            double value;
            if (ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm().equals("Raymond")) {
                value = 2.0;
            } else {
                value = grandT / numberNodes.get(runName);
            }
            if (coolStuff.get(secondkey) != null) {
                coolStuff.get(secondkey).put(numberNodes.get(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getName()),
                        value);
            } else {
                Map<Integer, Double> details = new HashMap<>();
                details.put(numberNodes.get(ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getName()),
                        value);
                coolStuff.put(secondkey, details);
            }
            /* Ends here */

            /* resetting the datastructure */
            removeLoadedRun(runName);
        }

        intitialize();
        for (Map.Entry<String, Map<Integer, Double>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Double> map = entry.getValue();

            XYSeries series = new XYSeries(string);

            for (Map.Entry<Integer, Double> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Double integer1 = entry1.getValue();
                series.add(integer, integer1);

                System.out.println("Integer:- " + integer + " values:-" + integer1);

            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Number of messages sent", // chart title
                "Number of Processes", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setSeriesPaint(1, Color.BLACK);
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(3, Color.BLACK);

        return new ChartPanel(chart);
    }

    public JPanel fairness() {

        Map<String, Map<Integer, Integer>> coolStuff = new HashMap<>();
        Map<String, Map<Integer, Integer>> coolStuff2 = new HashMap<>();
        Map<String, Map<Integer, Integer>> coolStuff3 = new HashMap<>();
        Map<String, String> algo = new HashMap<>();
        XYSeriesCollection dataset = new XYSeriesCollection();
        String title = "";
        for (Iterator<Object> it = runs.iterator(); it.hasNext();) {
            String runName = (String) it.next();
            temploadTraceFromFile(runName);

            String algorithm = ObjectFactory.getSimulationRuns().get(Integer.parseInt(runName.split(" ")[1])).getAlgorithm();
            algo.put(runName, algorithm);

            Map<Integer, String> map = fairness.get(runName);

            Map<Integer, Integer> counts = new LinkedHashMap<>();

            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                String[] string = entry.getValue().split(",");
                if (counts.get(Integer.parseInt(string[1])) == null) {
                    counts.put(Integer.parseInt(string[1]), 1);
                } else {
                    int count = counts.get(Integer.parseInt(string[1]));
                    count++;
                    counts.put(Integer.parseInt(string[1]), count);
                }

            }
            coolStuff.put(runName, counts);

            Map<Integer, Integer> counts2 = new LinkedHashMap<>();
            for (int i = 1; i <= numberNodes.get(runName); i++) {
                counts2.put(i, noOfCSAcess.get(runName).get(i));
            }

            coolStuff2.put(runName, counts2);

            Map<Integer, String> map2 = requests.get(runName);
            Map<Integer, Integer> counts3 = new LinkedHashMap<>();
            
            for (Map.Entry<Integer, Integer> entry3 : counts2.entrySet()) {
                Integer nodeIDD = entry3.getKey();
                Integer count = entry3.getValue();
                if (noOfCSAcess.get(runName).get(nodeIDD) != count) {
                    count = noOfCSAcess.get(runName).get(nodeIDD);
                }
                counts3.put(nodeIDD,count);
            }
            
            coolStuff3.put(runName, counts3);
            
            removeLoadedRun(runName);
        }

        intitialize();

        for (Map.Entry<String, Map<Integer, Integer>> entry : coolStuff.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Integer> map = entry.getValue();

            XYSeries series = new XYSeries("For " + algo.get(string) + " (Number of violation)");

            for (Map.Entry<Integer, Integer> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Integer integer1 = entry1.getValue();
                series.add(integer, integer1);

            }
            dataset.addSeries(series);
        }

        for (Map.Entry<String, Map<Integer, Integer>> entry : coolStuff2.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Integer> map = entry.getValue();

            XYSeries series3 = new XYSeries("For " + algo.get(string) + " (Number of CS entries)");

            for (Map.Entry<Integer, Integer> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Integer integer1 = entry1.getValue();
                series3.add(integer, integer1);

            }
            dataset.addSeries(series3);
        }
        
        for (Map.Entry<String, Map<Integer, Integer>> entry : coolStuff3.entrySet()) {
            String string = entry.getKey();
            Map<Integer, Integer> map = entry.getValue();

            XYSeries series3 = new XYSeries("For " + algo.get(string) + " (Number of CS requests)");

            for (Map.Entry<Integer, Integer> entry1 : map.entrySet()) {
                Integer integer = entry1.getKey();
                Integer integer1 = entry1.getValue();
                series3.add(integer, integer1);

            }
            dataset.addSeries(series3);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Fairness", // Title
                "Processor ID", // x-axis Label
                "Count", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );
//        XYPlot xyPlot = (XYPlot) chart.getPlot();
//
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true);
//        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
//        domain.setVerticalTickLabels(true);
        XYPlot plot = (XYPlot) chart.getPlot();
        chart.setBackgroundPaint(Color.WHITE);

        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                0,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                2,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                4,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );
        
        // set the stroke for each series...
        plot.getRenderer().setSeriesStroke(
                1,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{10.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                3,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{6.0f, 6.0f}, 0.0f
                )
        );
        plot.getRenderer().setSeriesStroke(
                5,
                new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{2.0f, 6.0f}, 0.0f
                )
        );

        // customise the renderer...
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);

        renderer.setSeriesPaint(0, Color.BLACK); // algo violations        
        renderer.setSeriesPaint(2, Color.BLACK);
        renderer.setSeriesPaint(4, Color.BLACK);
        
        renderer.setSeriesPaint(1, Color.RED); 
        renderer.setSeriesPaint(3, Color.RED);
        renderer.setSeriesPaint(5, Color.RED);        

        return new ChartPanel(chart);
    }
}
