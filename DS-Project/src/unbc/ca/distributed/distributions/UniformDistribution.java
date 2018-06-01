package unbc.ca.distributed.distributions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.random.DiscreteUniformGenerator;


public class UniformDistribution extends Generator {

    private final int min;
    private final int max;

    public UniformDistribution(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Map<Double, Double> getExpectedValues() {
        Map values = new HashMap();
        for (int i = this.min; i <= this.max; i++) {
            values.put(Double.valueOf(i), Double.valueOf(1.0D / (this.max - this.min + 1)));
        }
        return values;
    }

    @Override
    protected DiscreteUniformGenerator createValueGenerator(Random rng) {
        return new DiscreteUniformGenerator(this.min, this.max, rng);
    }

    @Override
    public double getExpectedMean() {
        return (this.max - this.min) / 2 + this.min;
    }

    @Override
    public double getExpectedStandardDeviation() {
        return (this.max - this.min) / Math.sqrt(12.0D);
    }

    @Override
    public String getDescription() {
        return "Uniform Distribution (Range = " + this.min + "..." + this.max + ")";
    }

    @Override
    public boolean isDiscrete() {
        return true;
    }
}
