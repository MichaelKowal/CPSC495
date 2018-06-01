package unbc.ca.distributed.distributions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.random.GaussianGenerator;

public class GaussianDistribution extends Generator {

    private final double mean;
    private final double standardDeviation;

    public GaussianDistribution(double mean, double standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    protected GaussianGenerator createValueGenerator(Random rng) {
        return new GaussianGenerator(this.mean, this.standardDeviation, rng);
    }

    public Map<Double, Double> getExpectedValues() {
        Map values = new HashMap();

        double x = 0.0D;
        double p;
        do {
            p = getExpectedProbability(this.mean + x);
            values.put(Double.valueOf(this.mean + x), Double.valueOf(p));
            values.put(Double.valueOf(this.mean - x), Double.valueOf(p));
            x += 3.0D * this.standardDeviation / 10.0D;
        } while (p > 0.001D);
        return values;
    }

    private double getExpectedProbability(double x) {
        double y = 1.0D / (this.standardDeviation * Math.sqrt(6.283185307179586D));
        double z = -(Math.pow(x - this.mean, 2.0D) / (2.0D * Math.pow(this.standardDeviation, 2.0D)));
        return y * Math.exp(z);
    }

    public double getExpectedMean() {
        return this.mean;
    }

    public double getExpectedStandardDeviation() {
        return this.standardDeviation;
    }

    public String getDescription() {
        return "Gaussian Distribution (μ = " + this.mean + ", σ = " + this.standardDeviation + ")";
    }

    public boolean isDiscrete() {
        return false;
    }
}
