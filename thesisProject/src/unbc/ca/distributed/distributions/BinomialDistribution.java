package unbc.ca.distributed.distributions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.uncommons.maths.Maths;
import org.uncommons.maths.random.BinomialGenerator;

public class BinomialDistribution extends Generator {

    private final int n;
    private final double p;

    public BinomialDistribution(int n, double p) {
        this.n = n;
        this.p = p;
    }

    @Override
    public Map<Double, Double> getExpectedValues() {
        Map values = new HashMap();
        for (int i = 0; i <= this.n; i++) {
            values.put(Double.valueOf(i), Double.valueOf(getExpectedProbability(i)));
        }
        return values;
    }

    private double getExpectedProbability(int successes) {
        double prob = Math.pow(this.p, successes) * Math.pow(1.0D - this.p, this.n - successes);
        BigDecimal coefficient = new BigDecimal(binomialCoefficient(this.n, successes));
        return coefficient.multiply(new BigDecimal(prob)).doubleValue();
    }

    private BigInteger binomialCoefficient(int n, int k) {
        BigInteger nFactorial = Maths.bigFactorial(n);
        BigInteger kFactorial = Maths.bigFactorial(k);
        BigInteger nMinusKFactorial = Maths.bigFactorial(n - k);
        BigInteger divisor = kFactorial.multiply(nMinusKFactorial);
        return nFactorial.divide(divisor);
    }

    @Override
    protected BinomialGenerator createValueGenerator(Random rng) {
        return new BinomialGenerator(this.n, this.p, rng);
    }

    @Override
    public double getExpectedMean() {
        return this.n * this.p;
    }

    @Override
    public double getExpectedStandardDeviation() {
        return Math.sqrt(this.n * this.p * (1.0D - this.p));
    }

    @Override
    public String getDescription() {
        return "Binomial Distribution (n = " + this.n + ", p = " + this.p + ")";
    }

    @Override
    public boolean isDiscrete() {
        return true;
    }
}
