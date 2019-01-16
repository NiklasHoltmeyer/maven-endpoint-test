package de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage;

public class Coverage {
    String type;
    int missed;
    int coverd;

    public Coverage(String type, int missed, int coverd) {
        this.type = type;
        this.missed = missed;
        this.coverd = coverd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMissed() {
        return missed;
    }

    public void setMissed(int missed) {
        this.missed = missed;
    }

    public int getCoverd() {
        return coverd;
    }

    public void setCoverd(int coverd) {
        this.coverd = coverd;
    }

    public static Coverage add(Coverage a, Coverage b){
        if(a==null || b == null ||
                a.type ==null || b.type == null||
                !a.type.equals(b.type)) return null;
        int coverd = a.getCoverd() + b.getCoverd();
        int missed = a.getMissed() + b.getMissed();
        return new Coverage(a.type, missed, coverd);

    }

    @Override
    public String toString() {
        return "Coverage{" +
                "type='" + type + '\'' +
                ", missed=" + missed +
                ", coverd=" + coverd +
                '}';
    }
}
