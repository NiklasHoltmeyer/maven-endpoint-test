package de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo;

public class Size {
    String size;
    String unit;

    public Size(String size, String unit) {
        this.size = size;
        this.unit = unit;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Size{" +
                "size='" + size + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
