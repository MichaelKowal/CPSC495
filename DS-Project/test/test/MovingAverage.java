/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author IDontKnow
 */
import java.util.LinkedList;
import java.util.Queue;
public class MovingAverage {
    private final Queue<Double> window = new LinkedList<>();
    private final int period;
    private double sum;
 
    public MovingAverage(int period) {
        assert period > 0 : "Period must be a positive integer";
        this.period = period;
    }
 
    public void newNum(double num) {
        sum += num;
        window.add(num);
        if (window.size() > period) {
            sum -= window.remove();
        }
    }
 
    public double getAvg() {
        if (window.isEmpty()) return 0; // technically the average is undefined
        return sum / window.size();
    }
 
    public static void main(String[] args) {
        double[] testData = {1,2,3,4,5,5,4,3,2,1};
        int[] windowSizes = {5};
        for (int windSize : windowSizes) {
            MovingAverage ma = new MovingAverage(windSize);
            for (double x : testData) {
                ma.newNum(x);
                System.out.println("Next number = " + x + ", SMA = " + ma.getAvg());
            }
            System.out.println();
        }
    }
}
