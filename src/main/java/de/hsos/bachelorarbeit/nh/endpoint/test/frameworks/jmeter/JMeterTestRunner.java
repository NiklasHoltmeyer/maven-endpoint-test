package de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.jmeter;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndPointExecutionInfo.EndpointExecutionInfo;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.CapacityResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.DebugInfos;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.TestRunnerResult;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.EndpointTestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.HealthCheck;
import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.Watch;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import javafx.util.Pair;
import org.apache.maven.plugin.logging.Log;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JMeterTestRunner extends EndpointTestRunner {
    List<Path> testPaths;
    String jMeterFullPath;
    String jMeterCMDPluginFullPath;
    Watch watch;
    Log log;

    public JMeterTestRunner(Log log, String jMeterFullPath, String jMeterCMDPluginFullPath, String testPath, HealthCheck healthCheck, Watch watch, DebugInfos debugInfos) {
        super(testPath, healthCheck, debugInfos);
        testPaths = new ArrayList<>();
        this.jMeterFullPath = jMeterFullPath;
        this.jMeterCMDPluginFullPath=jMeterCMDPluginFullPath;
        this.log=log;
        this.watch=watch;
    }

    @Override
    public TestRunnerResult performTests(TestRunnerResult tr) {
        setTestPaths(tr);
        if(!tr.isSuccess()) return tr;

        log.info("Start-Test(s)");
        System.out.println("Start-Test(s)");
        if(!debugInfos.isSkipValidationTests()){
            TestRunnerResult validationTests = testPaths.stream()
                    .filter(x->x.getFileName().toString().toLowerCase(Locale.GERMAN).endsWith("all-endpoints.jmx"))
                    .map(this::startTest)
                    .filter(t->!t.isSuccess())
                    .findFirst()
                    .orElse(null);

            boolean success = validationTests == null;
            tr.setSuccess(success);

            if(!success){
                tr.setErrorMessage(validationTests.getErrorMessage());
            }
        }else{
            System.out.println("Validation-Test-Skipped");
        }

        if(!debugInfos.isSkipCapacityTests()){
            System.out.println("Capacity-Tests");
            Path capacityResultPath = Paths.get(testPath, "reports", "capacityTests", "capacity.ssv");
            List<CapacityResult> cList = getReportFiles("capacitytests").stream()
                    .map(x->this.performCapacityTest(x))
                    .collect(Collectors.toList());
            try {
                this.saveCapactiyTest(cList, capacityResultPath);
            } catch (IOException e) {
                log.error("Jmeter@performTests");
                log.error(e.toString());
                e.printStackTrace();
            }
        }else{
            System.out.println("Capacity-Test-Skipped");

        }
        if(!debugInfos.isSkipWatchtests()){
            System.out.println("Watch-Tests");
            List<WatchResultGroup> wList = getReportFiles("watchtests")
                    .stream()
                    .map(this::performWatchTests)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Path watchResultPath = Paths.get(testPath, "reports", "watchTests", "watch.ssv");
            System.out.println("Path: " + watchResultPath);
            try {
                this.saveWatchResult(wList, watchResultPath);
            } catch (IOException e) {
                log.error("Jmeter@performTests");
                log.error(e.toString());
                e.printStackTrace();
            }
        }else{
            System.out.println("Watch-Test-Skipped");
        }
        log.info("Testsfinished!");
        return tr;
    }

    protected WatchResultGroup performWatchTests(Path jmxPath){
        Path reportPath = Paths.get(jmxPath.getParent().toString(), "reports", "watchTests");
        String _jmxPath = jmxPath.toString();
        String _reportPath = reportPath.toString();
        String _resultPath = Paths.get(reportPath.toString(), "result.jtl").toAbsolutePath().toString();

        String path = getPath(_jmxPath).orElse(null);
        String method = getMethod(_jmxPath).orElse(null);

        System.out.println("PerformanceWatchTest: " + path + "@" + method);

        Pair<String, String> pair = new Pair<String, String>("threadCount", "10");
        List<Pair<String, String>> pairList =  Arrays.asList(pair);

        try{
            this.watch.start();
        }catch(Exception e){
            System.err.println(e.toString());
            System.err.println("performWatchTests@Watch-Start failed");
            return null;
        }

        TestRunnerResult tr = runTest(_reportPath, _jmxPath, _resultPath, pairList, true);

        WatchResultGroup wrg;
        try{
            wrg = this.watch.stop();
        }catch(Exception e){
            System.err.println(e.toString());
            System.err.println("performWatchTests@Watch-Start failed");
            return null;
        }
        if(wrg!=null){
            wrg.setMethod(method);
            wrg.setPath(path);
            return wrg;
        }else{
            System.err.println("performWatchTests@WRG@NP");
        }
        return null;
    }

    List<Path> getReportFiles(String name){
        return this.testPaths.stream()
                .filter(x->x.getParent().getFileName().toString().toLowerCase(Locale.GERMAN).equals(name))
                .collect(Collectors.toList());
    }

//todo: schön machen wenn zeit
    @SuppressWarnings("Duplicates")
    private void saveWatchResult(List<WatchResultGroup> watchResults, Path resultPath) throws IOException {
        Map<Pair<String,String>, List<WatchResultGroup>> pairListMap = watchResults.stream().collect(Collectors.groupingBy(p->new Pair<String, String>(p.getPath(), p.getMethod())));

        File file = new File(resultPath.getParent().toAbsolutePath().toString());
        file.mkdirs();
        file.createNewFile();
        try(FileWriter fw = new FileWriter(resultPath.toAbsolutePath().toString(),false)){
            BufferedWriter bW = new BufferedWriter(fw);
            bW.append("path method cpuAVG cpuAVGUnit memAVG memAVG-Unit");
            for(Pair<String,String> endpoint : pairListMap.keySet()){
                bW.newLine();
                List<WatchResultGroup> results = pairListMap.get(endpoint);
                String path = endpoint.getKey();
                String method = endpoint.getValue();
                String values = getWRGSSV(results);;
                if(!values.equals("")){
                    bW.write(path + " " + method + " " + values);
                }else{
                    System.out.println("WRGSSV => values.equals(\"\")");
                }
            }
            bW.close();
        }
    }

    private String getWRGSSV(List<WatchResultGroup> watchResultGroups){
        //
        StringBuilder sb = new StringBuilder();

        if(watchResultGroups.isEmpty()){
            System.out.println("WRG - isEmpty!!!");
            return sb.toString();
        }

        String units[] = new String[]{watchResultGroups.get(0).getCpu().getAverage().getUnit(),
                watchResultGroups.get(0).getMemory().getAverage().getUnit()};

        if(units != null){
            Double cpuAVG = watchResultGroups.stream().mapToDouble(wr->wr.getCpu().getAverage().getValue()).average().orElse(Double.NaN);
            Double memoryAVG = watchResultGroups.stream().mapToDouble(wr->wr.getMemory().getAverage().getValue()).average().orElse(Double.NaN);

            if(cpuAVG.isNaN()) cpuAVG = null;
            if(memoryAVG.isNaN()) memoryAVG = null;

            sb.append(cpuAVG).append(" ").append(units[0]).append(" ").append(memoryAVG).append(" ").append(units[1]);
        }else{
            System.out.println("Units ==null");
        }

        return sb.toString();
    }

    private void saveCapactiyTest(List<CapacityResult> capacityResult, Path resultPath) throws IOException {
        Map<Pair<String,String>, List<CapacityResult>> pairListMap = capacityResult.stream().collect(Collectors.groupingBy(p->new Pair<String, String>(p.getPath(), p.getMethod())));
        File file = new File(resultPath.getParent().toAbsolutePath().toString());
        file.mkdirs();
        file.createNewFile();
        try(FileWriter fw = new FileWriter(resultPath.toAbsolutePath().toString(),false)){
            BufferedWriter bW = new BufferedWriter(fw);
            bW.append("path method averageMaxCapacity");
            for(Pair<String,String> endpoint : pairListMap.keySet()){
                bW.newLine();
                List<CapacityResult> results = pairListMap.get(endpoint);
                double capacity = results.stream().mapToInt(CapacityResult::getMaxCapacity).average().orElse(0);
                String path = endpoint.getKey();
                String method = endpoint.getValue();
                bW.write(path + " " + method + " " + capacity);
            }
            bW.close();
        }
    }

    private CapacityResult performCapacityTest(Path jmxPath){
        Path reportPath = Paths.get(jmxPath.getParent().toString(), "reports", "capacityTest");

        String _jmxPath = jmxPath.toString();
        String _reportPath = reportPath.toString();
        String _resultPath = Paths.get(reportPath.toString(), "result.jtl").toAbsolutePath().toString();
        String path = getPath(_jmxPath).orElse(null);
        String method = getMethod(_jmxPath).orElse(null);
        System.out.println("PerformanceCapacityTest: " + path + "@" + method);
        int maxCapacity = performMaxCapacityTest(_jmxPath, _reportPath, _resultPath, 0, 100, 50);
        //int maxCapacity = 1;
        CapacityResult result = new CapacityResult(path, method, maxCapacity);
        return result;
    }
    private Optional<String> getPath(String jmxPath) {
        String key = "TestPlan.comments";
        try (Stream<String> stream = Files.lines(Paths.get(jmxPath))) {
            String s = stream
                    .filter(string->string.contains(key))
                    .filter(string->!string.trim().equals("<stringProp name=\"TestPlan.comments\"></stringProp>"))
                    .findFirst()
                    .orElse(null);
            if(s!=null){
                return Optional.of(s.replaceFirst("(\\s)*", "").replace("<stringProp name=\""+key+"\">","").replace("</stringProp>", ""));
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private Optional<String> getMethod(String jmxPath){
        String key = "HTTPSampler.method";
        try (Stream<String> stream = Files.lines(Paths.get(jmxPath))) {
            String s = stream
                    .filter(string->string.contains(key))
                    .findFirst()
                    .orElse(null);
            if(s!=null){
                return Optional.of(s.replaceFirst("(\\s)*", "").replace("<stringProp name=\""+key+"\">","").replace("</stringProp>", ""));
            }
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private int performMaxCapacityTest(String jmxPath, String reportPath, String resultPath, int currentMax, int currentValue){
        return performMaxCapacityTest(jmxPath, reportPath, resultPath, currentMax, currentValue, 10);
    }

    private int performMaxCapacityTest(String jmxPath, String reportPath, String resultPath, int currentMax, int currentValue, int increment){
        System.out.println("CM: " + currentMax + " CV: " + currentValue + " IN: " + increment);
         if(currentMax >= currentValue) return currentMax;
        else{
            Pair<String, String> pair = new Pair<String, String>("threadCount", currentValue+"");
            List<Pair<String, String>> pairList =  Arrays.asList(pair);
            TestRunnerResult tr = runTest(reportPath, jmxPath, resultPath, pairList, true);
            boolean didFail = didTestFail(resultPath);
            if(didFail){
                increment /= 2;
                if(increment<1) increment = 1;
                currentValue -= increment;
            }else{
                currentMax = currentValue;
                currentValue += increment;
            }

            return performMaxCapacityTest(jmxPath, reportPath, resultPath, currentMax, currentValue, increment);
        }
    }

    private boolean didTestFail(String resultPath){
        try (Stream<String> stream = Files.lines(Paths.get(resultPath))) {
            return stream.filter(line ->line.contains("<failure>true</failure>") || line.contains("<error>true</error>"))
                    .findFirst()
                    .orElse(null) != null;

        }catch (Exception e){
            return true;
        }
    }

    @Override
    public void collectActuratorInfos(String url) throws Exception {
        JsonUtil jsonUtil = new GsonJsonUtil();
        List<EndpointExecutionInfo>  json = jsonUtil.fromJsonURL(url, List.class);
        String dest = Paths.get(this.testPath, "reports", "executioninfo").toAbsolutePath().toString(); // folder
        new File(dest).mkdirs();
        dest = Paths.get(dest, "acturator-executeinfo.json").toAbsolutePath().toString(); //file
        jsonUtil.writeJson(json, dest);
    }

    private InputStreamReader getInputStream(String url) throws IOException {
        // Quelle: https://stackoverflow.com/a/38546190/5026265
        URL _url = new URL(url);
        InputStreamReader reader = new InputStreamReader(_url.openStream());
        return reader;
    }

    private TestRunnerResult startTest(Path jmxPath) {
        String testName = jmxPath.getFileName().toString();
        Timestamp ts = new Timestamp(new Date().getTime());

        log.info("Start-Test: " + testName + ", " + ts);

        Path reportPath = Paths.get(jmxPath.getParent().toString(), "reports", testName);

        String _jmxPath = jmxPath.toString();
        String _reportPath = reportPath.toString();
        String _resultPath = Paths.get(reportPath.toString(), "result.jtl").toAbsolutePath().toString();

        TestRunnerResult tr = runTest(_reportPath, _jmxPath, _resultPath, true);
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

    private TestRunnerResult runTest(String reportPath, String jmxPath, String resultPath, boolean overwrite) {
        return this.runTest(reportPath, jmxPath, resultPath, new ArrayList<>(), overwrite);
    }

    private TestRunnerResult runTest(String reportPath, String jmxPath, String resultPath, List<Pair<String, String>> parameters, boolean overwrite){
        if(overwrite){
            File f = new File(resultPath);
            if(f.exists()) f.delete();
        }
        StringBuilder cmdStringBuilder = new StringBuilder()
                .append(jMeterFullPath)
                .append(" -Jjmeter.save.saveservice.output_format=xml ")
                .append("-JreportPath=\"").append(reportPath).append("\" ");

        for(Pair<String, String> p : parameters){
            cmdStringBuilder.append("-J").append(p.getKey()).append("=\"").append(p.getValue()).append("\" ");
        }

        String cmd = cmdStringBuilder
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

