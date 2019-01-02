package de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResult;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadTestsResults;
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

public class JMeterReadTestResults implements ReadTestsResults {
    String reportPath;
    private static final String RESULTS = "results.xml";
    private static final String AGGREGATEREPORT = "aggregateReport.xml";
    private static final String ENDPOINTREPORT = "acturator.json";

    public JMeterReadTestResults(String reportPath) throws FileNotFoundException {
        this.reportPath = reportPath;
        this.checkFiles(reportPath);
    }

    private void checkFiles(String reportPath) throws FileNotFoundException {
        String[] filesToCheck = {"", RESULTS, AGGREGATEREPORT};

        for(String relativePath : filesToCheck){
            Path absolutePath = Paths.get(reportPath, relativePath);
            if(!Files.exists(absolutePath)) throw new FileNotFoundException("Missing Report-Folder (" + absolutePath.toString() + ")");
        }
    }

    private Optional<NodeList> getHTTPSamples(){
        String resultsPath = Paths.get(this.reportPath, AGGREGATEREPORT).toString();
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

    private TestResult parseTestResult(Node node, int nodeIndex){
        TestResult testResults = new TestResult();
        NamedNodeMap namedNodeMap = node.getAttributes();

        Optional<String> latency = this.getNodeValue(namedNodeMap, "lt"); // lt 	Latency = time to initial response (milliseconds) - not all samplers support this
        if(latency.isPresent()) testResults.setLatency(new Double(latency.get()));

        Optional<String> elapsedTime = this.getNodeValue(namedNodeMap, "t"); //             t	Elapsed time (milliseconds)
        if(elapsedTime.isPresent()) testResults.setElapsedTime(new Double(elapsedTime.get()));

        Optional<String> url = this.getNodeValue(namedNodeMap, "lb"); //             lb 	Label
        if(url.isPresent()) testResults.setUrl(url.get());

        Optional<String> method = parseMethod(node, 0);
        if(method.isPresent()) testResults.setMethod(method.get());

        Optional<String> requestCount = this.getNodeValue(namedNodeMap, "sc"); //            sc	Sample count (1, unless multiple samples are aggregated)
        if(requestCount.isPresent()) testResults.setRequestCount(new Integer(requestCount.get()));
        else testResults.setRequestCount(1);

        Optional<String> result = this.getNodeValue(namedNodeMap, "s"); //            s	Success flag (true/false)
        if(result.isPresent()) testResults.setSuccess((result.get().equals("true")? true:false));

        Optional<String> bytes = this.getNodeValue(namedNodeMap, "by"); //            by	Bytes
        try{
            if(result.isPresent()) testResults.setSize(Long.valueOf(bytes.get()));
        }catch(Exception e){}

        return testResults;

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

    @Override
    public List<TestResult> getTestResults() {
        List<TestResult> result = new ArrayList<>();
        NodeList httpSamples = this.getHTTPSamples().orElse(null);

        if(httpSamples!=null){
            for(int i = 0; i < httpSamples.getLength(); ++i){
                Node node = httpSamples.item(i);
                result.add(this.parseTestResult(node, i));
            }
        }

        return result;
    }

    public List<EndpointGroupInfo> parseJsonEndpointGroupInfo() throws FileNotFoundException {
        //Quelle: https://stackoverflow.com/a/29965924/5026265
        //String resultsPath = Paths.get(this.reportPath, ENDPOINTREPORT).toString();
        //JsonReader reader =  new JsonReader(new FileReader(resultsPath));;
        //EndpointGroupInfo[] egis = new Gson().fromJson(reader, EndpointGroupInfo[].class);
        //return Arrays.asList(egis);
        return null;
    }
}
