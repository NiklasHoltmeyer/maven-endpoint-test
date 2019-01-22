package de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestRequestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadTestsResults;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class JMeterReadTestResults extends ReadTestsResults {
    //String reportPath;
    private static final String RESULTREPORTRELATIVEPATH = "all-endpoints.jmx\\result.jtl";
    private static final String CAPACITYREPORTRELATIVEPATH = "capacityTests\\capacity.ssv";

    private static String RESULTREPORTABSOLOUTEPATH;
    private static String CAPACITYREPORTABSOLOUTEPATH;

    String reportPath;

    public JMeterReadTestResults(String reportPath) throws FileNotFoundException {
        this.reportPath= reportPath;
        RESULTREPORTABSOLOUTEPATH = Paths.get(this.reportPath, RESULTREPORTRELATIVEPATH).toAbsolutePath().toString();
        CAPACITYREPORTABSOLOUTEPATH = Paths.get(this.reportPath, CAPACITYREPORTRELATIVEPATH).toAbsolutePath().toString();

        for(String p : new String[]{RESULTREPORTABSOLOUTEPATH, CAPACITYREPORTABSOLOUTEPATH}){
            File f = new File(p);
            if(!f.exists()) throw new FileNotFoundException("Report-Missing: " + p);
        }

    }

    private Optional<NodeList> getHTTPSamples(String path){
        String resultsPath = Paths.get(path).toString();
        Document resultDocument;
        try {
            resultDocument = this.readXML(resultsPath);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(resultDocument.getElementsByTagName("httpSample"));
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        //Quelle: https://stackoverflow.com/a/14968272/5026265
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder();
    }

    private Document readXML(String path) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path);
        DocumentBuilder documentBuilder = this.getDocumentBuilder();
        return documentBuilder.parse(file);
    }

    private Optional<String> parseMethod(Node node, int nodeIndex){
        NodeList d = node.getOwnerDocument().getElementsByTagName("method");
        if(d!=null && d.getLength() >= nodeIndex){
            Node n = d.item(nodeIndex);
            if(n!=null) return Optional.of(d.item(nodeIndex).getTextContent());
        }
        return Optional.empty();
    }

    private TestRequestResult parseTestResult(Node node, int nodeIndex){
        TestRequestResult testRequestResults = new TestRequestResult("", "");
        NamedNodeMap namedNodeMap = node.getAttributes();

        Optional<String> label = this.getNodeValue(namedNodeMap, "lb"); //             lb 	Label

        if(label.isPresent()){
            String[] labels = label.get().split(" ");
            testRequestResults.setUrlParameterLess(labels[0]);
            testRequestResults.setPath(labels[1]);
            testRequestResults.setMethod(labels[2]);
        }


        Optional<String> latency = this.getNodeValue(namedNodeMap, "lt"); // lt 	Latency = time to initial response (milliseconds) - not all samplers support this
        if(latency.isPresent()) testRequestResults.setLatency(new Double(latency.get()));

        Optional<String> elapsedTime = this.getNodeValue(namedNodeMap, "t"); //             t	Elapsed time (milliseconds)
        if(elapsedTime.isPresent()) testRequestResults.setTurnAroundTime(new Double(elapsedTime.get()));

        Optional<String> requestCount = this.getNodeValue(namedNodeMap, "sc"); //            sc	Sample count (1, unless multiple samples are aggregated)
        if(requestCount.isPresent()) testRequestResults.setRequestCount(new Integer(requestCount.get()));
        else testRequestResults.setRequestCount(1);

        Optional<String> result = this.getNodeValue(namedNodeMap, "s"); //            s	Success flag (true/false)
        if(result.isPresent()) testRequestResults.setSuccess((result.get().equals("true")? true:false));

        Optional<String> bytes = this.getNodeValue(namedNodeMap, "by"); //            by	Bytes
        try{
            if(result.isPresent()) testRequestResults.setSize(Long.valueOf(bytes.get()));
        }catch(Exception e){}

        return testRequestResults;

        /*

            sby	Sent Bytes
            de	Data encoding
            dt	Data type
            ec	Error count (0 or 1, unless multiple samples are aggregated)
            hn	Hostname where the sample was generated
            it	Idle Time = time not spent sampling (milliseconds) (generally 0)
            ct	Connect Time = time to establish the connection (milliseconds) - not all samplers support this
            na	Number of active threads for all thread groups
            ng	Number of active threads in this group
            rc	Response Code (e.g. 200)
            rm	Response Message (e.g. OK)
            tn	Thread Name
            ts	timeStamp (milliseconds since midnight Jan 1, 1970 UTC)
            varname	Value of the named variable
                 */
    }

    private Optional<String> getNodeValue(NamedNodeMap namedNodeMap, String nodeKey){
        Node x = namedNodeMap.getNamedItem(nodeKey);
        return (x!=null) ? Optional.of(x.getTextContent()) : Optional.empty();
    }

    private List<TestRequestResult> getGeneralTestResults(){
        List<TestRequestResult> result = new ArrayList<>();
        NodeList httpSamples = this.getHTTPSamples(RESULTREPORTABSOLOUTEPATH).orElse(null);

        if(httpSamples!=null){
            for(int i = 0; i < httpSamples.getLength(); ++i){
                Node node = httpSamples.item(i);
                result.add(this.parseTestResult(node, i));
            }
        }else{
            System.out.println("GeneralTestResults NP: " + RESULTREPORTABSOLOUTEPATH);
        }
        return result;
    }

    private void addCapacity(TestResultGroup testResultGroup, String ssv){
        if(ssv != null && !ssv.equals("")){
            String[] _ssv = ssv.trim().split(" ");
            String path = _ssv[0];
            String method = _ssv[1];
            Unit<String> maxCapacity = new Unit<>(_ssv[2], "User(s)/s");
            ///vet/ POST 0.0
            TestResult testResult = testResultGroup.getTestResult(path, method).orElse(null);
            if(testResult==null){
                System.out.println("Couldnt find: " + ssv);
                testResultGroup.getResultGroups().forEach(x->System.out.println(x.getPath()+" "+x.getMethod()));
                testResult = new TestResult(path, method, new ArrayList<>());
                testResultGroup.getResultGroups().add(testResult);
            }
            testResult.setMaxCapacity(maxCapacity);
        }
    }

    public void getAggregated(TestResultGroup testResultGroup){
        Path aggregatedReportPath = Paths.get(CAPACITYREPORTABSOLOUTEPATH);
        File f = new File(aggregatedReportPath.toAbsolutePath().toString());
        if(f.exists()){
            try (Stream<String> stream = Files.lines(aggregatedReportPath).skip(1)) {
                stream.forEach(ssv -> this.addCapacity(testResultGroup, ssv));
            }
            catch (IOException e) {
                System.err.println(e.toString());
            }
        }else{
            System.out.println("Folder doesnt exist");
            System.out.println(f.getAbsolutePath().toString());
        }
    }

    @Override
    public TestResultGroup getTestResult() {
        List<TestRequestResult> generalTestRequestResults = this.getGeneralTestResults();
        List<TestResult> testResults = this.parseTestResults(generalTestRequestResults);
        TestResultGroup testResultGroup = this.parseTestResultGroup(testResults);
        this.getAggregated(testResultGroup);
        return testResultGroup;
    }
}

