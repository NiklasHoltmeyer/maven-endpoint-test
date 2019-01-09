package de.hsos.bachelorarbeit.nh.endpoint.test.usecase;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.DebugInfos;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.WatchResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class TestRunner {
    protected String testPath;
    HealthCheck healthCheck;
    protected DebugInfos debugInfos;

    public TestRunner(String testPath, HealthCheck healthCheck) {
        this.testPath = testPath;
        this.healthCheck=healthCheck;
    }

    public TestRunner(String testPath,HealthCheck healthCheck,  DebugInfos debugInfos) {
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

    protected TestRunnerResult runProcess(String cmd, boolean waitFor){
        List<String> cmdList = (Arrays.asList(cmd.split(" ")));
        ProcessBuilder pB = new ProcessBuilder(cmdList);
        TestRunnerResult tR = new TestRunnerResult();

        Process p = null;
        try {
            p = pB.start();
        } catch (IOException e) {
            tR.setErrorMessage(e.toString());
            return tR;
        }

        if(waitFor){
            try {
                int returnValue = p.waitFor();
                if(returnValue != 0) tR.setErrorMessage("Invalid-Return-Value(TestRunner@runProcess): " + returnValue);
            } catch (InterruptedException e) {
                tR.setErrorMessage(e.toString());
                return tR;
            }
        }

        return tR;
    }

    private boolean doesPathExist(String path){
        return new File(path).getAbsoluteFile().exists();
    }
    protected abstract TestRunnerResult performTests(TestRunnerResult tr);
    public abstract void collectActuratorInfos(String url) throws Exception;
}
