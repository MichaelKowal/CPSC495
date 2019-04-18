/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.performance;

/**
 *
 * @author behnish
 */
public class Average {
    private final int window;
    private double[] data;
    private int pos = 0;

    public Average(int window) {
        this.window = window;
        this.data = new double[window];
        for (int i = 0; i < window; i++) {
            data[i] = 0.0;
        }
    }

    public double update(final double newValue) {
        data[pos] = newValue;
        pos++;
        if (pos == window) {
            pos = 0;
        }
        return calculateAve();
    }

    public double getAverage() {
        return calculateAve();
    }

    private double calculateAve() {
        double sum = 0.0;
        for (int i = 0; i < window; i++) {
            sum += data[i];
        }
        return sum / window;
    }
}