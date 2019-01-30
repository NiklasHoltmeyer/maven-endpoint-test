package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class TimeBehaviorResult {
    Unit<Double> meanResponseTime;
    Unit<Double> meanTurnAroundTime;
    Unit<Double> meanThroughput;

    public TimeBehaviorResult() {
        this.meanResponseTime = new Unit<>(Double.NaN, "AVG(ResponseTime)");
        this.meanTurnAroundTime = new Unit<>(Double.NaN, "AVG(TurnAroundTime)");
        this.meanThroughput = new Unit<>(Double.NaN, "AVG(ThroughPut)");
    }

    public Unit<Double> getMeanResponseTime() {
        return meanResponseTime;
    }

    public Unit<Double> getMeanTurnAroundTime() {
        return meanTurnAroundTime;
    }

    public Unit<Double> getMeanThroughput() {
        return meanThroughput;
    }
}
