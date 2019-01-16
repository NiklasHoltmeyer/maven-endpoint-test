package de.hsos.bachelorarbeit.nh.endpoint.test.frameworks.jacoco;

import de.hsos.bachelorarbeit.nh.endpoint.test.usecase.TestRunner.CollectCodeCoverage;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.CodeCoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Test.CodeCoverage.Coverage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JacocoCodeCoverageCollector extends CollectCodeCoverage {
    String baseDir;
    String reportAbsoloutePath;

    public JacocoCodeCoverageCollector(String baseDir) {
        this.baseDir = baseDir;
        this.init();
    }

    private void init(){
        reportAbsoloutePath = Paths.get(baseDir, "target\\site\\jacoco\\jacoco.xml").toAbsolutePath().toString();
    }

    private void addCoverage(CodeCoverageResult destination, String type, int missed, int covered){
        Coverage c = new Coverage(type, missed, covered);

        switch(type){
            case "BRANCH":
                destination.setBranch(c);
                break;
            case "LINE":
                destination.setLine(c);
                break;
            case "COMPLEXITY":
                destination.setComplexity(c);
                break;
            case "METHOD":
                destination.setMethod(c);
                break;
            case "CLASS":
                destination.setClazz(c);
                break;
            case "INSTRUCTION":
                destination.setInstruction(c);
                break;
            default:
                break;
        }
    }

    private CodeCoverageResult parseClass(Node clazz){
        String clazzName = clazz.getAttributes().getNamedItem("name").getTextContent().replace("/", ".");
        NodeList childs = clazz.getChildNodes();
        CodeCoverageResult codeCoverageResult = new CodeCoverageResult();
        codeCoverageResult.setClassName(clazzName);

        for(int i = 0; i < childs.getLength(); ++i){
            Node n = childs.item(i);
            if(n.getNodeName().equals("counter")){
                String type = (n.getAttributes().getNamedItem("type").getTextContent());
                int missed = Integer.parseInt((n.getAttributes().getNamedItem("missed").getTextContent()));
                int covered = Integer.parseInt((n.getAttributes().getNamedItem("covered").getTextContent()));
                this.addCoverage(codeCoverageResult, type, missed, covered);
            }
        }

        return codeCoverageResult;
    }
    private List<CodeCoverageResult> parseClasses(NodeList classes){
        List<CodeCoverageResult> result = new ArrayList<>();

        for(int i = 0; i < classes.getLength(); ++i){
            Node clazz = classes.item(i);
            CodeCoverageResult codeCoverageResult = this.parseClass(clazz);
            result.add(codeCoverageResult);
        }

        return result;
    }

    @Override
    public List<CodeCoverageResult> collectCodeCoverage() throws Exception{
        Document doc = getDocument(this.reportAbsoloutePath);
        NodeList nl = doc.getElementsByTagName("class");
        return this.parseClasses(nl);
    }
    private Document getDocument(String absolutePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory  factory = DocumentBuilderFactory
                .newInstance();
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return factory.newDocumentBuilder()
                .parse(absolutePath);
    }
}
;
