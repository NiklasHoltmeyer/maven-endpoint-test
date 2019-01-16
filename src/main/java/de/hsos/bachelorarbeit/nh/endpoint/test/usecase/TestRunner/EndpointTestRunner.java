package de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.DebugInfos;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class EndpointTestRunner extends TestRunner{
    protected String testPath;
    HealthCheck healthCheck;
    protected DebugInfos debugInfos;

    public EndpointTestRunner(String testPath, HealthCheck healthCheck) {
        this.testPath = testPath;
        this.healthCheck=healthCheck;
    }

    public EndpointTestRunner(String testPath, HealthCheck healthCheck, DebugInfos debugInfos) {
        this.testPath = testPath;
        this.debugInfos = debugInfos;
        this.healthCheck = healthCheck;
    }

    public TestRunnerResult runTests(){
        TestRunnerResult result = new TestRunnerResult();
        if(!this.healthCheck.isAccessable()){
            result.setErrorMessage("Service is offline");
        }else if(!doesPathExist(testPath)){
            result.setErrorMessage("Test-Path: " + testPath + " doest NOT exist!");
        }else{
            this.resetActurators();
            return this.performTests(result);
        }
        return result;
    }

    protected void resetActurators(){
        String resetActurator = "/actuator/executeinfo/reset";
        this.healthCheck.isAccessable(resetActurator);
    }

    protected List<Path> getFiles(String path, String suffix) throws IOException {
        Predicate<? super Path> suffixFilter = (f) -> {
            File file = new File(f.toString());
            return file.isFile() && f.getFileName().toString().trim().endsWith(suffix);
        };

        return this.getFiles(path, suffixFilter);
    }

    private String getFileExtension(File file) {
        //Quelle: https://stackoverflow.com/a/21974043/5026265
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    protected List<Path> getFiles(String path, Predicate<? super Path> filter) throws IOException {
        return Files.walk(Paths.get(path))
                .filter(filter)
                .collect(Collectors.toList());
    }

    private boolean doesPathExist(String path){
        return new File(path).getAbsoluteFile().exists();
    }
    protected abstract TestRunnerResult performTests(TestRunnerResult tr);
    public abstract void collectActuratorInfos(String url) throws Exception;
}
