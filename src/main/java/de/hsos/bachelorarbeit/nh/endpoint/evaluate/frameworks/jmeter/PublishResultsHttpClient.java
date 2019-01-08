package de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.jmeter;

import com.google.gson.Gson;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.PublishResults;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PublishResultsHttpClient implements PublishResults {
    String destination;
    JsonUtil jsonUtil;

    public PublishResultsHttpClient(String historyServer) {
        this.destination = historyServer + "/" + "testResult";
        this.jsonUtil = new GsonJsonUtil();
    }

    @Override
    public boolean publish(TestResultGroup result) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String json = new Gson().toJson(result);
        HttpPut request = new HttpPut(destination);
        StringEntity params =new StringEntity(json);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        //application/json;charset=UTF-8
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        System.out.println("Status: " + response.getStatusLine().getStatusCode());
        String responseXml = EntityUtils.toString(response.getEntity());
        EntityUtils.consume(response.getEntity());

        System.out.println("responseXml: " + responseXml);
System.out.println(response.toString());


        return response.getStatusLine().getStatusCode() == 200;
    }
}
