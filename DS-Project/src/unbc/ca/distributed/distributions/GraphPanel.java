package unbc.ca.distributed.distributions;

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphPanel extends JPanel {

    private final ChartPanel chartPanel = new ChartPanel(null);

    public GraphPanel() {
        super(new BorderLayout());
        add(this.chartPanel, "Center");
    }

    public void generateGraph(String title, Map<Double, Double> observedValues, Map<Double, Double> expectedValues, double expectedMean, double expectedStandardDeviation, boolean discrete) {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        XYSeries observedSeries = new XYSeries("Observed");
        dataSet.addSeries(observedSeries);
        XYSeries expectedSeries = new XYSeries("Expected");
        dataSet.addSeries(expectedSeries);

        for (Map.Entry entry : observedValues.entrySet()) {
            observedSeries.add((Number) entry.getKey(), (Number) entry.getValue());
        }

        for (Map.Entry entry : expectedValues.entrySet()) {
            expectedSeries.add((Number) entry.getKey(), (Number) entry.getValue());
        }

        JFreeChart chart = ChartFactory.createXYLineChart(title, "Value", "Probability", dataSet, PlotOrientation.VERTICAL, true, false, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        if (discrete) {
            plot.setRenderer(new XYLineAndShapeRenderer());
        } else {
            XYSplineRenderer renderer = new XYSplineRenderer();
            renderer.setBaseShapesVisible(false);
            plot.setRenderer(renderer);
        }

        this.chartPanel.setChart(chart);
    }
}
