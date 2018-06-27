package unbc.ca.distributed.distributions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.Maths;
import org.uncommons.maths.random.PoissonGenerator;

public class PoissonDistribution extends Generator {

    private final double mean;

    public PoissonDistribution(double mean) {
        this.mean = mean;
    }

    @Override
    public Map<Double, Double> getExpectedValues() {
        Map values = new HashMap();
        int index = 0;
        double p;
        do {
            p = getExpectedProbability(index);
            values.put(Double.valueOf(index), Double.valueOf(p));
            index++;
        } while (p > 0.001D);
        return values;
    }

    private double getExpectedProbability(int events) {
        BigDecimal kFactorial = new BigDecimal(Maths.bigFactorial(events));
        double numerator = Math.exp(-this.mean) * Math.pow(this.mean, events);
        return new BigDecimal(numerator).divide(kFactorial, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    protected PoissonGenerator createValueGenerator(Random rng) {
        return new PoissonGenerator(this.mean, rng);
    }

    @Override
    public double getExpectedMean() {
        return this.mean;
    }

    @Override
    public double getExpectedStandardDeviation() {
        return Math.sqrt(this.mean);
    }

    @Override
    public String getDescription() {
        return "Poisson Distribution (λ = " + this.mean + ")";
    }

    @Override
    public boolean isDiscrete() {
        return true;
    }
}
