package unbc.ca.distributed.distributions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.random.ExponentialGenerator;

public class ExponentialDistribution extends Generator {

    private final double rate;

    public ExponentialDistribution(double rate) {
        this.rate = rate;
    }

    @Override
    protected ExponentialGenerator createValueGenerator(Random rng) {
        return new ExponentialGenerator(this.rate, rng);
    }

    @Override
    public Map<Double, Double> getExpectedValues() {
        Map values = new HashMap();

        double x = 0.0D;
        int k= 1;
        double p;
        do {
            p = getExpectedProbability(x);
            values.put(Double.valueOf(x), Double.valueOf(p));
            x += 1.0D / (2.0D * this.rate);
            k++;
        } while (p > 0.001D);
        return values;
    }

    private double getExpectedProbability(double x) {
        if (x < 0.0D) {
            return 0.0D;
        }

        return this.rate * Math.exp(-this.rate * x);
    }

    @Override
    public double getExpectedMean() {
        return Math.pow(this.rate, -1.0D);
    }

    @Override
    public double getExpectedStandardDeviation() {
        return Math.sqrt(Math.pow(this.rate, -2.0D));
    }

    @Override
    public String getDescription() {
        return "Exponential Distribution (λ = " + this.rate + ")";
    }

    @Override
    public boolean isDiscrete() {
        return false;
    }
}
