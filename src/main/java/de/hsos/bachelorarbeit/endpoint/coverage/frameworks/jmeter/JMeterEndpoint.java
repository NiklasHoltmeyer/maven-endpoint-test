package de.hsos.bachelorarbeit.endpoint.coverage.frameworks.jmeter;

public class JMeterEndpoint {
    String path;
    int tests;

    public JMeterEndpoint(String path, int tests) {
        this.tests = tests;
        this.path = path;
    }

    public int getTests() {
        return tests;
    }

    public String getPath() {
        return path;
    }

    public String toString(){
        return "Path= " + this.path + ", Tests=" + this.tests;
    }
}
