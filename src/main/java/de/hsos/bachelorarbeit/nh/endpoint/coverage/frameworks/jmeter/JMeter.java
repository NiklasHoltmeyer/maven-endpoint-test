package de.hsos.bachelorarbeit.nh.endpoint.coverage.frameworks.jmeter;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.Request;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.usecases.generateCoverage.EndpointTests;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.util.CountedHashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JMeter implements EndpointTests {
    String jmxPath;

    public JMeter(String jmxPath) {
        this.jmxPath = jmxPath;
    }

    public HashMap<Request, Integer> getAllRequests() throws IOException {
        List<Request> requests = this.parseRequests();
        return new CountedHashMap<Request>().generate(requests);
    }

    private List<Request> parseRequests() throws IOException {
        Pattern startPattern = JMeterPatterns.startPattern;
        Pattern endPattern = JMeterPatterns.endPattern;
        Pattern commentPattern = JMeterPatterns.commentPattern;
        Pattern httpMethod = JMeterPatterns.httpMethod;

        boolean insideTestBlock = false;
        Request currentRequest = new Request();
        List<Request> requests = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(jmxPath));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            if(startPattern.matcher(sCurrentLine).find()){
                insideTestBlock = true;
                currentRequest = new Request();
            }else if(endPattern.matcher(sCurrentLine).find()){
                insideTestBlock = false;
                requests.add(currentRequest);
            } else if(insideTestBlock){
                Matcher matcher = commentPattern.matcher(sCurrentLine);
                if(matcher.find()){
                    currentRequest.setPath(matcher.group(2));
                    continue;
                }
                matcher = httpMethod.matcher(sCurrentLine);
                if(matcher.find()){
                    currentRequest.setHttpMethod(matcher.group(1));
                    continue;
                }
            }
        }
        return requests;
    }
}

