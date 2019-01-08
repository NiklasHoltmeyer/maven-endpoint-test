package de.hsos.bachelorarbeit.nh.endpoint.util.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Unit<T>{
    T value;
    String unit;

    public Unit(T value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "value=" + value +
                ", unit='" + unit + '\'' +
                '}';
    }
}
