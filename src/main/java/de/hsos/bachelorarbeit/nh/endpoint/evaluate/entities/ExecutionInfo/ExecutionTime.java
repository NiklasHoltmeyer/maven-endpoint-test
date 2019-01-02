package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

public class ExecutionTime {
    long time;
    String unit;

    public ExecutionTime(long time, String unit) {
        this.time = time;
        this.unit = unit;
    }

    public ExecutionTime(long time) {
        this.time = time;
        this.unit = "ms";
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ExecutionTime{" +
                "time=" + time +
                ", unit='" + unit + '\'' +
                '}';
    }
}
