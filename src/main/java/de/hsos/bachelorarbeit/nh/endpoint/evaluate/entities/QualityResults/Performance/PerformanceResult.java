package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance;


public class PerformanceResult {
    TimeBehaviorResult timeBehavior;
    ResourceUtilizationResult resourceUtilization;
    CapacityResult capacity;

    public PerformanceResult() {
        this.timeBehavior = new TimeBehaviorResult();
        this.resourceUtilization = new ResourceUtilizationResult();
        this.capacity = new CapacityResult();
    }

    public TimeBehaviorResult getTimeBehavior() {
        return timeBehavior;
    }

    public ResourceUtilizationResult getResourceUtilization() {
        return resourceUtilization;
    }

    public CapacityResult getCapacity() {
        return capacity;
    }

}
