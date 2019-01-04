package de.hsos.bachelorarbeit.nh.endpoint.test.jmeter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner;

import javax.ws.rs.core.Response;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JMeterTestRunner extends TestRunner {
    List<Path> testPaths;
    String jMeterFullPath;
    String jMeterCMDPluginFullPath;

    public JMeterTestRunner(String jMeterFullPath, String jMeterCMDPluginFullPath, String testPath, HealthCheck healthCheck) {
        super(testPath, healthCheck);
        testPaths = new ArrayList<>();
        this.jMeterFullPath = jMeterFullPath;
        this.jMeterCMDPluginFullPath=jMeterCMDPluginFullPath;
    }

    @Override
    public TestRunnerResult performTests(TestRunnerResult tr) {
        setTestPaths(tr);
        if(!tr.isSuccess()) return tr;

        TestRunnerResult errorResult = testPaths.stream()
                .map(this::startTest)
                .filter(t->!t.isSuccess())
                .findFirst()
                .orElse(null);

        boolean success = errorResult == null;
        tr.setSuccess(success);

        if(!success){
            tr.setErrorMessage(errorResult.getErrorMessage());
        }

        return tr;
    }

    @Override
    public void collectActuratorInfos(String url) {
        InputStreamReader inputStreamReader = this.getInputStream(url);
        if(inputStreamReader!=null){
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(inputStreamReader, JsonObject.class);
            String jsonString = json.toString();
            String dest = Paths.get(this.testPath, "acturator-executeinfo.json").toAbsolutePath().toString();
            try(Writer writer = new FileWriter(dest)){
                writer.write(jsonString);
            }catch(Exception e){}
        }

    }

    private InputStreamReader getInputStream(String url){
        // Quelle: https://stackoverflow.com/a/38546190/5026265
        try {
            URL _url = new URL(url);
            InputStreamReader reader = new InputStreamReader(_url.openStream());
            return reader;
        }catch(Exception e){
            return null;
        }
    }

    private TestRunnerResult startTest(Path jmxPath) {
        String testName = jmxPath.getFileName().toString();
        Path reportPath = Paths.get(jmxPath.getParent().toString(), "reports", testName);

        String _jmxPath = jmxPath.toString();
        String _reportPath = reportPath.toString();
        String _resultPath = Paths.get(reportPath.toString(), "result.jtl").toAbsolutePath().toString();

        TestRunnerResult tr = runTest(_reportPath, _jmxPath, _resultPath);
        if(tr.isSuccess()){
            tr = collectReports(_reportPath, _resultPath);
        }
        return tr;
    }

    private TestRunnerResult collectReports(String reportPath, String resultPath){
        String reportFullPath = Paths.get(reportPath, "aggregateReport.csv").toAbsolutePath().toString();
        String cmd = new StringBuilder()
                .append(this.jMeterCMDPluginFullPath)
                .append(" --generate-csv ").append(reportFullPath)
                .append(" --input-jtl ").append(resultPath)
                .append(" --plugin-type AggregateReport")
                .toString();

        return this.runProcess(cmd, true);
    }

    private TestRunnerResult runTest(String reportPath, String jmxPath, String resultPath){
        //ProcessBuilder pB = new ProcessBuilder()
        String cmd = new StringBuilder()
                .append(jMeterFullPath)
                .append(" -Jjmeter.save.saveservice.output_format=xml ")
                .append("-JreportPath=\"").append(reportPath).append("\" ")
                .append("-n -t \""+jmxPath+"\" ")
                .append("-l \""+resultPath+"\"")
                .toString();
        return this.runProcess(cmd, true);
    }

    private TestRunnerResult setTestPaths(TestRunnerResult tr){
        List<Path> jmxFiles;
        try {
            this.testPaths = this.getFiles(this.testPath, "jmx").stream()
                .collect(Collectors.toList());
        } catch (IOException e) {
            tr.setErrorMessage("JmeterTestRunner@performTest: " + e.toString());
            return tr;
        }
        return tr;
    }
}

