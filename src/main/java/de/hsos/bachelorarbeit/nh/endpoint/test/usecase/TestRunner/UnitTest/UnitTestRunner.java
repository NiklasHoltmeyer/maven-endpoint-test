package de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.UnitTest;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestGroupedResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.TestRunner;

import java.io.IOException;
import java.util.List;

public abstract class UnitTestRunner extends TestRunner {
    public TestRunnerResult runTests(String workingDir){
        List<String> command = this.getRunCommand();
        return this.runProcess(command, true, workingDir);
    }

    protected abstract List<String> getRunCommand();
    public abstract UnitTestGroupedResult getResults() throws IOException;
}
