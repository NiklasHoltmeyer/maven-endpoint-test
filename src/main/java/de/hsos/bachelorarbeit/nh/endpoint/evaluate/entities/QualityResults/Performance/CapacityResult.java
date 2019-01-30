package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class CapacityResult {
    Unit<Double> capacity;

    public CapacityResult() {
        this.capacity = new Unit<>(Double.NaN, "AVG(CAPACITY)");
    }

    public Unit<Double> getCapacity() {
        return capacity;
    }
}
