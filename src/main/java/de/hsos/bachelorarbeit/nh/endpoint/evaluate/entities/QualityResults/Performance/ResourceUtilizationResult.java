package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class ResourceUtilizationResult {
    Unit<Double> meanProcessorUtilization;
    Unit<Double> meanMemoryUtilization;
    Double value;

    public ResourceUtilizationResult() {
        this.meanProcessorUtilization = new Unit<>(Double.NaN, "AVG(CPU-UTILIZATION)");
        this.meanMemoryUtilization = new Unit<>(Double.NaN, "AVG(MEM-UTILIZATION)");
    }

    public Unit<Double> getMeanProcessorUtilization() {
        return meanProcessorUtilization;
    }

    public Unit<Double> getMeanMemoryUtilization() {
        return meanMemoryUtilization;
    }

    public void nullErrors(){
        if(meanProcessorUtilization!=null && (meanProcessorUtilization.getValue() == null  || meanProcessorUtilization.getValue().isNaN() ||  meanProcessorUtilization.getValue().isInfinite()))  meanProcessorUtilization = null;
        if(meanMemoryUtilization!=null && (meanMemoryUtilization.getValue() == null  ||  meanMemoryUtilization.getValue().isNaN()|| meanMemoryUtilization.getValue().isInfinite()))  meanMemoryUtilization = null;
    }


}

