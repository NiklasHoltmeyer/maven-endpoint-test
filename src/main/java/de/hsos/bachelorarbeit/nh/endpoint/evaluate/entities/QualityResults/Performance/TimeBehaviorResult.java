package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class TimeBehaviorResult {
    Unit<Double> meanResponseTime;
    Unit<Double> meanTurnAroundTime;
    Unit<Double> meanThroughput;
    Double value;

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
    public void nullErrors(){
        if(meanResponseTime!=null && (meanResponseTime.getValue() == null || meanResponseTime.getValue().isInfinite() || meanResponseTime.getValue().isNaN()))  meanResponseTime = null;
        if(meanTurnAroundTime!=null && (meanTurnAroundTime.getValue() == null || meanTurnAroundTime.getValue().isInfinite() || meanTurnAroundTime.getValue().isNaN()))  meanTurnAroundTime = null;
        if(meanThroughput!=null && (meanThroughput.getValue() == null || meanThroughput.getValue().isInfinite() || meanThroughput.getValue().isNaN()))  meanThroughput = null;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
