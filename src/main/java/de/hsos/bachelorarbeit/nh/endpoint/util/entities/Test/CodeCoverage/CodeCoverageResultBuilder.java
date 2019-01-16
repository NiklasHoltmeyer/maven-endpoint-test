package de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage;

public final class CodeCoverageResultBuilder {
    String className;
    Coverage instruction;
    Coverage branch;
    Coverage line;
    Coverage complexity;
    Coverage method;
    Coverage clazz;
    private boolean success = true;
    private String errorMessage;

    private CodeCoverageResultBuilder() {
    }

    public static CodeCoverageResultBuilder aCodeCoverageResult() {
        return new CodeCoverageResultBuilder();
    }

    public CodeCoverageResultBuilder withSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public CodeCoverageResultBuilder withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public CodeCoverageResultBuilder withClassName(String className) {
        this.className = className;
        return this;
    }

    public CodeCoverageResultBuilder withInstruction(Coverage instruction) {
        this.instruction = instruction;
        return this;
    }

    public CodeCoverageResultBuilder withBranch(Coverage branch) {
        this.branch = branch;
        return this;
    }

    public CodeCoverageResultBuilder withLine(Coverage line) {
        this.line = line;
        return this;
    }

    public CodeCoverageResultBuilder withComplexity(Coverage complexity) {
        this.complexity = complexity;
        return this;
    }

    public CodeCoverageResultBuilder withMethod(Coverage method) {
        this.method = method;
        return this;
    }

    public CodeCoverageResultBuilder withClazz(Coverage clazz) {
        this.clazz = clazz;
        return this;
    }

    public CodeCoverageResult build() {
        CodeCoverageResult codeCoverageResult = new CodeCoverageResult();
        codeCoverageResult.setSuccess(success);
        codeCoverageResult.setErrorMessage(errorMessage);
        codeCoverageResult.setClassName(className);
        codeCoverageResult.setInstruction(instruction);
        codeCoverageResult.setBranch(branch);
        codeCoverageResult.setLine(line);
        codeCoverageResult.setComplexity(complexity);
        codeCoverageResult.setMethod(method);
        codeCoverageResult.setClazz(clazz);
        return codeCoverageResult;
    }
}
