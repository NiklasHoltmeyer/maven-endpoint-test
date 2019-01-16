package de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.JUnit.TestRunner;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Result;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.UnitTest.UnitTestResultBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class JunitResultParser {
    String testResultPath;

    public JunitResultParser(String testResultPath) {
        this.testResultPath = testResultPath;
    }

    private Document getDocument(String absolutePath){
        try {
            return getDocumentBuilder().parse(absolutePath);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseClassName(String fullTestName){
        if(fullTestName==null) return "NULL";

        String[] tmp = fullTestName.split("\\.");
        int lastIndex = tmp.length -1;

        if(lastIndex < 0) return fullTestName;

        if(tmp[lastIndex].endsWith("Test")){
            tmp[lastIndex] = replaceLast(tmp[lastIndex], "Test", "");
        }else if(tmp[lastIndex].startsWith("Test")){
            tmp[lastIndex] = tmp[lastIndex].replaceFirst("Test", "");
        }

        return join(Arrays.asList(tmp), ".");
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        // https://stackoverflow.com/a/2282982/5026265
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    private UnitTestResult parseResult(NodeList nodeList) {
        Node n = nodeList.item(0);
        String fullTestName = n.getAttributes().getNamedItem("name").getTextContent();
        String fullClassName = parseClassName(fullTestName);

        int errors = 0;
        int skipped = 0;
        int tests = 0;
        int failures = 0;
        double time = 0;

        for( int i = 0 ; i < nodeList.getLength(); ++i){
            n = nodeList.item(i);
            String errorsString = n.getAttributes().getNamedItem("errors").getTextContent();
            String skippedString = n.getAttributes().getNamedItem("skipped").getTextContent();
            String testsString = n.getAttributes().getNamedItem("tests").getTextContent();
            String timeString = n.getAttributes().getNamedItem("time").getTextContent();
            String failuresString = n.getAttributes().getNamedItem("failures").getTextContent();
            try {
                errors += Integer.parseInt(errorsString);
                skipped += Integer.parseInt(skippedString);
                tests += Integer.parseInt(testsString);
                time += Double.parseDouble(timeString);
                failures += Integer.parseInt(failuresString);

            }catch(NumberFormatException e){
                e.printStackTrace();
                return UnitTestResultBuilder.anUnitTestResult()
                        .withClassName(fullClassName)
                        .withSuccess(false)
                        .withErrorMessage("Class = " + fullClassName + "\n" + e.toString())
                        .build();
            }
        }

        return UnitTestResultBuilder.anUnitTestResult()
                .withClassName(fullClassName)
                .withErrors(errors)
                .withSkipped(skipped)
                .withTests(tests)
                .withTime(time)
                .withFailures(failures)
                .withSuccess(errors < 1)
                .build();
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        return documentBuilderFactory.newDocumentBuilder();
    }

    public UnitTestResult parseResult(String absolutePath){
        Document document = this.getDocument(absolutePath);
        if(document == null){
            return new UnitTestResult(new Result(false, "Could not parse Test-Result!"));
        }
        NodeList nodeList = document.getElementsByTagName("testsuite");

        if(nodeList.getLength() < 1){
            return new UnitTestResult(new Result(false, "No Testresults found inside: " + absolutePath));
        }

        return this.parseResult(nodeList);
    }


    public static String join(Collection var0, String var1) {
        //Quelle: com.sun.deploy.util.StringUtils
        StringBuffer var2 = new StringBuffer();

        for(Iterator var3 = var0.iterator(); var3.hasNext(); var2.append((String)var3.next())) {
            if (var2.length() != 0) {
                var2.append(var1);
            }
        }

        return var2.toString();
    }

}
class ReportDocument{
    public Document document;
    public String absolutePath;
}