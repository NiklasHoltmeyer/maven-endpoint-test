package de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    protected TestRunnerResult runProcess(String cmd, boolean waitFor){
        return runProcess(cmd, waitFor, null);
    }

    protected TestRunnerResult runProcess(String cmd, boolean waitFor, String workingDirectory){
        List<String> cmdList = Arrays.asList(cmd.split(" "));
        return this.runProcess(cmdList, waitFor, workingDirectory);
    }

    protected TestRunnerResult runProcess(List<String> cmdList, boolean waitFor, String workingDirectory){
        ProcessBuilder pB = new ProcessBuilder(cmdList);
        TestRunnerResult tR = new TestRunnerResult();

        if(workingDirectory!=null){
            File workingDir = new File(workingDirectory);
            if(!workingDir.exists()){
                tR.setErrorMessage("Invalid Workingdir does not exist!: " + workingDirectory);
                tR.setSuccess(false);
                return tR;
            }
            pB.directory(workingDir);
        }

        Process p = null;
        try {
            p = pB.start();
        } catch (IOException e) {
            tR.setErrorMessage(e.toString());
            tR.setSuccess(false);
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
}
