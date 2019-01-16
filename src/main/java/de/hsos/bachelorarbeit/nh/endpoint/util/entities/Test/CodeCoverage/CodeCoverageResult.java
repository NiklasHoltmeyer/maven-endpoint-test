package de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Result;

public class CodeCoverageResult extends Result {
    transient String className;
    Coverage instruction;
    Coverage branch;
    Coverage line;
    Coverage complexity;
    Coverage method;
    Coverage clazz;

    public void initSumEntity(){
        instruction = new Coverage("INSTRUCTION", 0, 0);
        branch = new Coverage("BRANCH", 0, 0);
        line = new Coverage("LINE", 0, 0);
        complexity = new Coverage("COMPLEXITY", 0, 0);
        method = new Coverage("METHOD", 0, 0);
        clazz = new Coverage("CLASS", 0, 0);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Coverage getInstruction() {
        return instruction;
    }

    public void setInstruction(Coverage instruction) {
        this.instruction = instruction;
    }

    public Coverage getBranch() {
        return branch;
    }

    public void setBranch(Coverage branch) {
        this.branch = branch;
    }

    public Coverage getLine() {
        return line;
    }

    public void setLine(Coverage line) {
        this.line = line;
    }

    public Coverage getComplexity() {
        return complexity;
    }

    public void setComplexity(Coverage complexity) {
        this.complexity = complexity;
    }

    public Coverage getMethod() {
        return method;
    }

    public void setMethod(Coverage method) {
        this.method = method;
    }

    public Coverage getClazz() {
        return clazz;
    }

    public void setClazz(Coverage clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "CodeCoverageResult{" +
                //"\tclassName='" + className + '\'' + "\n +
                "\t, instruction=" + instruction +
                "\t, branch=" + branch +
                "\t, line=" + line +
                "\t, complexity=" + complexity +
                "\t, method=" + method +
                "\t, clazz=" + clazz +
                "}";
    }
}



