package de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.JUnit.TestRunner;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.UnitTest.UnitTestRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JUnitTestRunner extends UnitTestRunner {
    String baseDir;
    JunitResultParser junitResultParser;
    String testResultPath;
    String mvnFullPath;

    public JUnitTestRunner(String baseDir, String mvnFullPath) {
        this.baseDir = baseDir;
        this.mvnFullPath=mvnFullPath;
        this.init();
    }

    private void init(){
        this.testResultPath = Paths.get(this.baseDir, "target\\surefire-reports").toAbsolutePath().toString();
        junitResultParser = new JunitResultParser(this.testResultPath);
    }

    public List<String> getTestResults() throws IOException {
        File resultFiles = new File(this.testResultPath);
        if(!resultFiles.exists()) throw new IOException("TestResultPath does not exist: " + this.testResultPath);
        return Arrays.asList(Objects.requireNonNull(resultFiles.listFiles((file, fileName) ->
                (file.exists() && fileName.startsWith("TEST") && fileName.endsWith("xml"))
        ))).stream()
                .map(f->f.getAbsoluteFile().toString())
                .collect(Collectors.toList());
    }

    @Override
    protected List<String> getRunCommand() {
        // mvnFullPath
        String appendix = new StringBuilder()
                .append(" ")
                .append("-q -B ")
                .append("jacoco:prepare-agent ")
                .append("install ")
                .append("test ")
                .append("-DskipTests=false -Dmaven.test.failure.ignore=true ")
                .append("jacoco:report ")
                .append("-Dinstrumentation.DignoreTrivial=true ")
                .toString();
        List<String> appendixList = Arrays.asList(appendix.split(" "));
        List<String> result = new ArrayList<>();
        result.add(mvnFullPath);
        result.addAll(appendixList);
        return result;
                //mvn -q -B clean jacoco:prepare-agent install test -DskipTests=false jacoco:report -Dinstrumentation.DignoreTrivial=true
        // javadoc:javadoc cobertura:cobertura  findbugs:findbugs
    }

    @Override
    public UnitTestGroupedResult getResults() throws IOException {
        List<UnitTestResult> list = this.getTestResults()
                .stream()
                .map(junitResultParser::parseResult)
                .collect(Collectors.toList());
        return new UnitTestGroupedResult(list);
    }
}
