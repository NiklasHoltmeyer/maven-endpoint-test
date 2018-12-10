package de.hsos.bachelorarbeit.nh.JMeter;

import de.hsos.bachelorarbeit.nh.Endpoint.RESTEndpoint;
import de.hsos.bachelorarbeit.nh.Endpoint.Request;
import de.hsos.bachelorarbeit.nh.jmeter.annotation.EndpointTest;
import de.hsos.bachelorarbeit.nh.jmeter.annotation.JSONAssertion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class JMeterUtil {
    Map<String, List<Request>> groupedRequests;
    String testName;
    String defaultHost = "docker";
    int defaultPort = 8079;
    int defaultMaxLatency = 50000;

    public JMeterUtil(List<Request> requests, String testName, String defaultHost, int defaultPort, int defaultMaxLatency) {
        this.groupedRequests=requests.stream()
                .collect(Collectors.groupingBy(x-> "T: " + x.getRestEndpoint().getEndpointTest().threads() +
                        ", L: " + x.getRestEndpoint().getEndpointTest().loops() +
                        ", RUT: " + x.getRestEndpoint().getEndpointTest().rampUpTime()));

        this.defaultHost=defaultHost;
        this.defaultPort=defaultPort;
        this.defaultMaxLatency = defaultMaxLatency;
        this.testName = testName;
    }

    public void createTests(String destination) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        this.addFileHeader(stringBuilder, testName);
        this.groupedRequests.keySet().stream().forEach(x->this.createThreadGroup(stringBuilder, x));
        this.addReports(stringBuilder);
        this.addFileFooter(stringBuilder);
        this.writeFile(stringBuilder, destination);
    }

    private void writeFile(StringBuilder stringBuilder, String destination) throws IOException {
        File file = new File(destination);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(stringBuilder.toString());
        }
    }

    private StringBuilder addFileHeader(StringBuilder stringBuilder, String testPlanName){
        return stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jmeterTestPlan version=\"1.2\" properties=\"5.0\" jmeter=\"5.0 r1840935\">\n" +
                "  <hashTree>\n"+
                "    <TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\""+testPlanName+"\" enabled=\"true\">\n" +
                        "      <stringProp name=\"TestPlan.comments\"></stringProp>\n" +
                        "      <boolProp name=\"TestPlan.functional_mode\">false</boolProp>\n" +
                        "      <boolProp name=\"TestPlan.tearDown_on_shutdown\">true</boolProp>\n" +
                        "      <boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>\n" +
                        "      <elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n" +
                        "        <collectionProp name=\"Arguments.arguments\"/>\n" +
                        "      </elementProp>\n" +
                        "      <stringProp name=\"TestPlan.user_define_classpath\"></stringProp>\n" +
                        "    </TestPlan>\n" +
                        "    <hashTree>\n"
                );
    }

    private StringBuilder addReports(StringBuilder stringBuilder){
        return stringBuilder.append("      <ResultCollector guiclass=\"GraphVisualizer\" testclass=\"ResultCollector\" testname=\"Graph Results\" enabled=\"true\">\n" +
                "        <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                "        <objProp>\n" +
                "          <name>saveConfig</name>\n" +
                "          <value class=\"SampleSaveConfiguration\">\n" +
                "            <time>true</time>\n" +
                "            <latency>true</latency>\n" +
                "            <timestamp>true</timestamp>\n" +
                "            <success>true</success>\n" +
                "            <label>true</label>\n" +
                "            <code>true</code>\n" +
                "            <message>true</message>\n" +
                "            <threadName>true</threadName>\n" +
                "            <dataType>true</dataType>\n" +
                "            <encoding>false</encoding>\n" +
                "            <assertions>true</assertions>\n" +
                "            <subresults>true</subresults>\n" +
                "            <responseData>false</responseData>\n" +
                "            <samplerData>false</samplerData>\n" +
                "            <xml>false</xml>\n" +
                "            <fieldNames>true</fieldNames>\n" +
                "            <responseHeaders>false</responseHeaders>\n" +
                "            <requestHeaders>false</requestHeaders>\n" +
                "            <responseDataOnError>false</responseDataOnError>\n" +
                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "            <bytes>true</bytes>\n" +
                "            <sentBytes>true</sentBytes>\n" +
                "            <url>true</url>\n" +
                "            <threadCounts>true</threadCounts>\n" +
                "            <idleTime>true</idleTime>\n" +
                "            <connectTime>true</connectTime>\n" +
                "          </value>\n" +
                "        </objProp>\n" +
                "        <stringProp name=\"filename\"></stringProp>\n" +
                "      </ResultCollector>\n" +
                "      <hashTree/>\n" +
                "      <ResultCollector guiclass=\"TableVisualizer\" testclass=\"ResultCollector\" testname=\"View Results in Table (errors only)\" enabled=\"true\">\n" +
                "        <boolProp name=\"ResultCollector.error_logging\">true</boolProp>\n" +
                "        <objProp>\n" +
                "          <name>saveConfig</name>\n" +
                "          <value class=\"SampleSaveConfiguration\">\n" +
                "            <time>true</time>\n" +
                "            <latency>true</latency>\n" +
                "            <timestamp>true</timestamp>\n" +
                "            <success>true</success>\n" +
                "            <label>true</label>\n" +
                "            <code>true</code>\n" +
                "            <message>true</message>\n" +
                "            <threadName>true</threadName>\n" +
                "            <dataType>true</dataType>\n" +
                "            <encoding>false</encoding>\n" +
                "            <assertions>true</assertions>\n" +
                "            <subresults>true</subresults>\n" +
                "            <responseData>false</responseData>\n" +
                "            <samplerData>false</samplerData>\n" +
                "            <xml>false</xml>\n" +
                "            <fieldNames>true</fieldNames>\n" +
                "            <responseHeaders>false</responseHeaders>\n" +
                "            <requestHeaders>false</requestHeaders>\n" +
                "            <responseDataOnError>false</responseDataOnError>\n" +
                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "            <bytes>true</bytes>\n" +
                "            <sentBytes>true</sentBytes>\n" +
                "            <url>true</url>\n" +
                "            <threadCounts>true</threadCounts>\n" +
                "            <idleTime>true</idleTime>\n" +
                "            <connectTime>true</connectTime>\n" +
                "          </value>\n" +
                "        </objProp>\n" +
                "        <stringProp name=\"filename\"></stringProp>\n" +
                "      </ResultCollector>\n" +
                "      <hashTree/>\n" +
                "      <ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree (errors only)\" enabled=\"true\">\n" +
                "        <boolProp name=\"ResultCollector.error_logging\">true</boolProp>\n" +
                "        <objProp>\n" +
                "          <name>saveConfig</name>\n" +
                "          <value class=\"SampleSaveConfiguration\">\n" +
                "            <time>true</time>\n" +
                "            <latency>true</latency>\n" +
                "            <timestamp>true</timestamp>\n" +
                "            <success>true</success>\n" +
                "            <label>true</label>\n" +
                "            <code>true</code>\n" +
                "            <message>true</message>\n" +
                "            <threadName>true</threadName>\n" +
                "            <dataType>true</dataType>\n" +
                "            <encoding>false</encoding>\n" +
                "            <assertions>true</assertions>\n" +
                "            <subresults>true</subresults>\n" +
                "            <responseData>false</responseData>\n" +
                "            <samplerData>false</samplerData>\n" +
                "            <xml>false</xml>\n" +
                "            <fieldNames>true</fieldNames>\n" +
                "            <responseHeaders>false</responseHeaders>\n" +
                "            <requestHeaders>false</requestHeaders>\n" +
                "            <responseDataOnError>false</responseDataOnError>\n" +
                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "            <bytes>true</bytes>\n" +
                "            <sentBytes>true</sentBytes>\n" +
                "            <url>true</url>\n" +
                "            <threadCounts>true</threadCounts>\n" +
                "            <idleTime>true</idleTime>\n" +
                "            <connectTime>true</connectTime>\n" +
                "          </value>\n" +
                "        </objProp>\n" +
                "        <stringProp name=\"filename\"></stringProp>\n" +
                "      </ResultCollector>\n" +
                "      <hashTree/>\n" +
                "      <ResultCollector guiclass=\"AssertionVisualizer\" testclass=\"ResultCollector\" testname=\"Assertion Results (errors only)\" enabled=\"true\">\n" +
                "        <boolProp name=\"ResultCollector.error_logging\">true</boolProp>\n" +
                "        <objProp>\n" +
                "          <name>saveConfig</name>\n" +
                "          <value class=\"SampleSaveConfiguration\">\n" +
                "            <time>true</time>\n" +
                "            <latency>true</latency>\n" +
                "            <timestamp>true</timestamp>\n" +
                "            <success>false</success>\n" +
                "            <label>true</label>\n" +
                "            <code>true</code>\n" +
                "            <message>true</message>\n" +
                "            <threadName>true</threadName>\n" +
                "            <dataType>true</dataType>\n" +
                "            <encoding>false</encoding>\n" +
                "            <assertions>true</assertions>\n" +
                "            <subresults>true</subresults>\n" +
                "            <responseData>false</responseData>\n" +
                "            <samplerData>false</samplerData>\n" +
                "            <xml>false</xml>\n" +
                "            <fieldNames>true</fieldNames>\n" +
                "            <responseHeaders>false</responseHeaders>\n" +
                "            <requestHeaders>false</requestHeaders>\n" +
                "            <responseDataOnError>false</responseDataOnError>\n" +
                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "            <bytes>true</bytes>\n" +
                "            <sentBytes>true</sentBytes>\n" +
                "            <url>true</url>\n" +
                "            <threadCounts>true</threadCounts>\n" +
                "            <idleTime>true</idleTime>\n" +
                "            <connectTime>true</connectTime>\n" +
                "          </value>\n" +
                "        </objProp>\n" +
                "        <stringProp name=\"filename\"></stringProp>\n" +
                "      </ResultCollector>\n" +
                "      <hashTree/>\n" +
                "      <ResultCollector guiclass=\"StatVisualizer\" testclass=\"ResultCollector\" testname=\"Aggregate Report\" enabled=\"true\">\n" +
                "        <boolProp name=\"ResultCollector.error_logging\">true</boolProp>\n" +
                "        <objProp>\n" +
                "          <name>saveConfig</name>\n" +
                "          <value class=\"SampleSaveConfiguration\">\n" +
                "            <time>false</time>\n" +
                "            <latency>false</latency>\n" +
                "            <timestamp>false</timestamp>\n" +
                "            <success>true</success>\n" +
                "            <label>false</label>\n" +
                "            <code>false</code>\n" +
                "            <message>false</message>\n" +
                "            <threadName>false</threadName>\n" +
                "            <dataType>false</dataType>\n" +
                "            <encoding>false</encoding>\n" +
                "            <assertions>false</assertions>\n" +
                "            <subresults>false</subresults>\n" +
                "            <responseData>false</responseData>\n" +
                "            <samplerData>false</samplerData>\n" +
                "            <xml>false</xml>\n" +
                "            <fieldNames>false</fieldNames>\n" +
                "            <responseHeaders>false</responseHeaders>\n" +
                "            <requestHeaders>false</requestHeaders>\n" +
                "            <responseDataOnError>false</responseDataOnError>\n" +
                "            <saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>\n" +
                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                "          </value>\n" +
                "        </objProp>\n" +
                "        <stringProp name=\"filename\">${aggregateReport}</stringProp>\n" +
                "      </ResultCollector>\n" +
                "      <hashTree/>\n" +
                "      <Arguments guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n" +
                "        <collectionProp name=\"Arguments.arguments\">\n" +
                "          <elementProp name=\"PWD\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">PWD</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__BeanShell(import org.apache.jmeter.services.FileServer; FileServer.getFileServer().getBaseDir();)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"HOST\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">HOST</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(HOST,"+this.defaultHost+")}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"PORT\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">PORT</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(PORT,"+this.defaultPort+")}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"aggregateReport\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">aggregateReport</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">${__P(aggregateReport,&quot;report.xml&quot;)}</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "          <elementProp name=\"MAXLATENCY\" elementType=\"Argument\">\n" +
                "            <stringProp name=\"Argument.name\">MAXLATENCY</stringProp>\n" +
                "            <stringProp name=\"Argument.value\">"+defaultMaxLatency+"</stringProp>\n" +
                "            <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                "          </elementProp>\n" +
                "        </collectionProp>\n" +
                "      </Arguments>\n" +
                "      <hashTree/>\n");
    }

    private StringBuilder addFileFooter(StringBuilder stringBuilder){
        return stringBuilder.append("    </hashTree>\n" +
                "  </hashTree>\n" +
                "</jmeterTestPlan>\n");
    }

    private StringBuilder createThreadGroup(StringBuilder stringBuilder, String groupKey){
        List<Request> requests = this.groupedRequests.get(groupKey);
        EndpointTest threadGroupInfo = requests.get(0).getRestEndpoint().getEndpointTest();
        stringBuilder.append("      <ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\""+groupKey+"\" enabled=\"true\">\n" +
                "        <stringProp name=\"ThreadGroup.on_sample_error\">stoptest</stringProp>\n" +
                "        <elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">\n" +
                "          <boolProp name=\"LoopController.continue_forever\">false</boolProp>\n" +
                "          <stringProp name=\"LoopController.loops\">"+threadGroupInfo.loops()+"</stringProp>\n" +
                "        </elementProp>\n" +
                "        <stringProp name=\"ThreadGroup.num_threads\">"+threadGroupInfo.threads()+"</stringProp>\n" +
                "        <stringProp name=\"ThreadGroup.ramp_time\">"+threadGroupInfo.rampUpTime()+"</stringProp>\n" +
                "        <boolProp name=\"ThreadGroup.scheduler\">false</boolProp>\n" +
                "        <stringProp name=\"ThreadGroup.duration\"></stringProp>\n" +
                "        <stringProp name=\"ThreadGroup.delay\"></stringProp>\n" +
                "      </ThreadGroup>\n"+
                "      <hashTree>\n");

        requests.stream().forEach(request->this.createHTTPSampler(stringBuilder, request));

        return stringBuilder.append("      </hashTree>\n");
    }

    private StringBuilder createHTTPSampler(StringBuilder stringBuilder, Request request){
        RESTEndpoint restEndpoint = request.getRestEndpoint();
        EndpointTest endpointTest = restEndpoint.getEndpointTest();
        stringBuilder.append("        <HTTPSamplerProxy guiclass=\"HttpTestSampleGui\" testclass=\"HTTPSamplerProxy\" testname=\""+request.getPath()+"\" enabled=\"true\">\n" +
                "          <elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\" guiclass=\"HTTPArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n" +
                "            <collectionProp name=\"Arguments.arguments\"/>\n" +
                "          </elementProp>\n" +
                "          <stringProp name=\"HTTPSampler.domain\">${HOST}</stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.port\">${PORT}</stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.protocol\"></stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.contentEncoding\"></stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.path\">"+request.getPath()+"</stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.method\">"+restEndpoint.getMethod()+"</stringProp>\n" +
                "          <boolProp name=\"HTTPSampler.follow_redirects\">true</boolProp>\n" +
                "          <boolProp name=\"HTTPSampler.auto_redirects\">false</boolProp>\n" +
                "          <boolProp name=\"HTTPSampler.use_keepalive\">true</boolProp>\n" +
                "          <boolProp name=\"HTTPSampler.DO_MULTIPART_POST\">false</boolProp>\n" +
                "          <stringProp name=\"HTTPSampler.embedded_url_re\"></stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.connect_timeout\"></stringProp>\n" +
                "          <stringProp name=\"HTTPSampler.response_timeout\"></stringProp>\n" +
                "          <stringProp name=\"TestPlan.comments\">"+restEndpoint.getPath()+"</stringProp>\n" +
                "        </HTTPSamplerProxy>\n");
        return this.addAssertions(stringBuilder, endpointTest);
    }

    private StringBuilder addAssertions(StringBuilder stringBuilder, EndpointTest endpointTest){
        this.addReponseAssertion(stringBuilder, endpointTest.httpStatus());
        this.addLatencyAssertion(stringBuilder, "${MAXLATENCY}");
        this.addSizeAssertion(stringBuilder, 100);
        this.addJsonAssertion(stringBuilder, endpointTest);
        return stringBuilder.append("      </hashTree>\n");
    }

    private StringBuilder addJsonAssertion(StringBuilder stringBuilder, EndpointTest endpointTest){
        JSONAssertion[] assertions = endpointTest.jsonAssertions();
        for(int i = 0; i<assertions.length; ++i){
            stringBuilder.append("          <JSONPathAssertion guiclass=\"JSONPathAssertionGui\" testclass=\"JSONPathAssertion\" testname=\"JSON Assertion ("+i+")\" enabled=\"true\">\n" +
                    "            <stringProp name=\"JSON_PATH\">"+assertions[i].path()+"</stringProp>\n" +
                    "            <stringProp name=\"EXPECTED_VALUE\">"+assertions[i].expectedValue().replace("\"", "&quot;")+"</stringProp>\n" +
                    "            <boolProp name=\"JSONVALIDATION\">true</boolProp>\n" +
                    "            <boolProp name=\"EXPECT_NULL\">false</boolProp>\n" +
                    "            <boolProp name=\"INVERT\">false</boolProp>\n" +
                    "            <boolProp name=\"ISREGEX\">false</boolProp>\n" +
                    "          </JSONPathAssertion>\n" +
                    "          <hashTree/>\n");
        }
        return stringBuilder;
    }


        private StringBuilder addReponseAssertion(StringBuilder stringBuilder, int statusCode){
        return stringBuilder.append("        <hashTree>\n" +
                "          <ResponseAssertion guiclass=\"AssertionGui\" testclass=\"ResponseAssertion\" testname=\"Response Assertion\" enabled=\"true\">\n" +
                "            <collectionProp name=\"Asserion.test_strings\">\n" +
                "              <stringProp name=\"49587\">"+statusCode+"</stringProp>\n" +
                "            </collectionProp>\n" +
                "            <stringProp name=\"Assertion.custom_message\"></stringProp>\n" +
                "            <stringProp name=\"Assertion.test_field\">Assertion.response_code</stringProp>\n" +
                "            <boolProp name=\"Assertion.assume_success\">false</boolProp>\n" +
                "            <intProp name=\"Assertion.test_type\">8</intProp>\n" +
                "          </ResponseAssertion>\n" +
                "          <hashTree/>\n");
    }

    private StringBuilder addLatencyAssertion(StringBuilder stringBuilder, String maxLatency){
        return stringBuilder.append("          <DurationAssertion guiclass=\"DurationAssertionGui\" testclass=\"DurationAssertion\" testname=\"Duration Assertion\" enabled=\"true\">\n" +
                "            <stringProp name=\"DurationAssertion.duration\">"+maxLatency+"</stringProp>\n" +
                "          </DurationAssertion>\n"+
                "          <hashTree/>\n");
    }

    private StringBuilder addSizeAssertion(StringBuilder stringBuilder, int size){
        return stringBuilder.append("          <SizeAssertion guiclass=\"SizeAssertionGui\" testclass=\"SizeAssertion\" testname=\"Size Assertion\" enabled=\"true\">\n" +
                "            <stringProp name=\"Assertion.test_field\">SizeAssertion.response_network_size</stringProp>\n" +
                "            <stringProp name=\"SizeAssertion.size\">"+size+"</stringProp>\n" +
                "            <intProp name=\"SizeAssertion.operator\">3</intProp>\n" +
                "          </SizeAssertion>\n" +
                "          <hashTree/>\n");
    }


    /*
    // fehlerhaft
    public void createTests(String destination) throws IOException {
        //List<TestPlan> testPlans = new ArrayList<>();
        StandardJMeterEngine jm = new StandardJMeterEngine();
        JMeterUtils.setJMeterHome(this.jMeterHome);

        HashTree hashTree = new HashTree();


        TestPlan testPlan = new TestPlan("Penis");

        this.requests
                .stream()
                .map(this::parseRequest)
                .forEach(x->testPlan.addThreadGroup(x));

        hashTree.add("testPlan", testPlan);
        hashTree.add("loopController", new LoopController());

        jm.configure(hashTree);
        SaveService.saveTree(hashTree, new FileOutputStream("C:/Users/nikla/Desktop/example.jmx"));
    }

    private SetupThreadGroup parseRequest(Request request){
        RESTEndpoint restEndPoint = request.getRestEndpoint();
        EndpointTest endpointTest = restEndPoint.getEndpointTest();

        HTTPSampler httpSampler = this.createHttpSampler(restEndPoint.getMethod(), host, port, restEndPoint.getPath());
        TestElement testElement = this.getDefaultTestElement(endpointTest.loops(), httpSampler);
        SetupThreadGroup setupThreadGroup = this.getDefaultThreadGroup(endpointTest.threads(), endpointTest.rampUpTime(), testElement);
        ResponseAssertion responseAssertion = new ResponseAssertion();
        responseAssertion.setTestFieldResponseCode();
        responseAssertion.setAssumeSuccess(false);

        responseAssertion.addTestString(endpointTest.httpStatus() + "");
        setupThreadGroup.addTestElement(testElement);

        return setupThreadGroup;
    }

    private HTTPSampler createHttpSampler(String method, String host, int port, String path) {
        HTTPSampler httpSampler = new HTTPSampler();
        httpSampler.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSampler.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        httpSampler.setMethod(method);
        httpSampler.setDomain(host);
        httpSampler.setPort(port);
        httpSampler.setPath(path);
        return httpSampler;
    }

    private TestElement getDefaultTestElement(int loops, HTTPSampler httpSampler){
        LoopController loopCtrl = new LoopController();
        loopCtrl.setLoops(loops);
        loopCtrl.addTestElement(httpSampler);
        loopCtrl.setFirst(true);
        return loopCtrl;
    }

    private SetupThreadGroup getDefaultThreadGroup(int threads, int rampUpTime, TestElement loopCtrl){
        SetupThreadGroup setupThreadGroup = new SetupThreadGroup();
        setupThreadGroup.setNumThreads(threads);
        setupThreadGroup.setRampUp(rampUpTime);
        setupThreadGroup.setSamplerController((LoopController)loopCtrl);
        return setupThreadGroup;
    }
    */
}
