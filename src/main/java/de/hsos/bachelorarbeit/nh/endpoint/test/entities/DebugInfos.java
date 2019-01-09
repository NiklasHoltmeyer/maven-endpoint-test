package de.hsos.bachelorarbeit.nh.endpoint.test.entities;

public class DebugInfos {
    boolean skipValidationTests = false;
    boolean skipCapacityTests = false;
    boolean skipWatchtests = false;

    public boolean isSkipValidationTests() {
        return skipValidationTests;
    }

    public void setSkipValidationTests(boolean skipValidationTests) {
        this.skipValidationTests = skipValidationTests;
    }

    public boolean isSkipCapacityTests() {
        return skipCapacityTests;
    }

    public void setSkipCapacityTests(boolean skipCapacityTests) {
        this.skipCapacityTests = skipCapacityTests;
    }

    public boolean isSkipWatchtests() {
        return skipWatchtests;
    }

    public void setSkipWatchtests(boolean skipWatchtests) {
        this.skipWatchtests = skipWatchtests;
    }

    @Override
    public String toString() {
        return "DebugInfos{" +
                "skipValidationTests=" + skipValidationTests +
                ", skipCapacityTests=" + skipCapacityTests +
                ", skipWatchtests=" + skipWatchtests +
                '}';
    }
}
