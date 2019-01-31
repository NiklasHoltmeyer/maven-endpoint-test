package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.QualityResults.Performance;


public class PerformanceResult {
    TimeBehaviorResult timeBehavior;
    ResourceUtilizationResult resourceUtilization;
    CapacityResult capacity;
    Double value;

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

    public void nullErrors(){
        if(timeBehavior != null) timeBehavior.nullErrors();
        if(resourceUtilization != null) resourceUtilization.nullErrors();
        if(capacity != null) capacity.nullErrors();
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
