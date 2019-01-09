package de.hsos.bachelorarbeit.nh.endpoint.util.usecases;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class HttpClientHelper {
    JsonUtil jsonUtil;

    public HttpClientHelper(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    public boolean putRequest(String json, String url) throws IOException {
        HttpClient httpClient = getHttpClient();
        HttpPut request = new HttpPut(url);
        StringEntity params =new StringEntity(json);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        //application/json;charset=UTF-8
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        return response.getStatusLine().getStatusCode() == 200;
    }

    public boolean getRequest(String destination) throws IOException {
        HttpClient httpClient = getHttpClient();
        HttpGet request = new HttpGet(destination);
        HttpResponse response = httpClient.execute(request);

        return response.getStatusLine().getStatusCode() == 200;
    }

    private HttpClient getHttpClient(){
        return HttpClientBuilder.create().build();
    }
}
