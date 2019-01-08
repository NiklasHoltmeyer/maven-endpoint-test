package de.hsos.bachelorarbeit.nh.endpoint.test.entities;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Result;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;

public class WatchResult<T> extends Result {
    private Unit<T> duration;
    private Unit<T> firstValue;
    private Unit<T> average;

    public Unit<T> getDuration() {
        return duration;
    }

    public void setDuration(Unit<T> duration) {
        this.duration = duration;
    }

    public Unit<T> getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(Unit<T> firstValue) {
        this.firstValue = firstValue;
    }

    public Unit<T> getAverage() {
        return average;
    }

    public void setAverage(Unit<T> average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "WatchResult{" +
                "success=" + this.isSuccess() +
                ", errorMessage='" + this.getErrorMessage() + '\'' +
                ", duration=" + duration +
                ", firstValue=" + firstValue +
                ", average=" + average +
                '}';
    }
}

