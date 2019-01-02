package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

public class Usage {
    double usage;
    String unit;

    public Usage(double usage, String unit) {
        this.usage = usage;
        this.unit = unit;
    }

    public Usage(double usage) {
        this.usage = usage;
        this.unit = "%";
    }

    public double getUsage() {
        return usage;
    }

    public void setUsage(double usage) {
        this.usage = usage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Usage{" +
                "usage=" + usage +
                ", unit='" + unit + '\'' +
                '}';
    }
}
