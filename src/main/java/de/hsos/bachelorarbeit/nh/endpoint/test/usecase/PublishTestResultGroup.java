package de.hsos.bachelorarbeit.nh.endpoint.test.usecase;

import com.google.gson.Gson;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.TestResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.PublishResults;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.HttpClientHelper;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import java.io.IOException;

public class PublishTestResultGroup implements PublishResults {
    String destination;
    HttpClientHelper httpClientHelper;
    public PublishTestResultGroup(String historyServer, JsonUtil jsonUtil) {
        this.destination = historyServer + "/" + "testResult";
        this.httpClientHelper = new HttpClientHelper(jsonUtil);
    }

    @Override
    public boolean publish(TestResultGroup result) throws IOException {
        String json = new Gson().toJson(result);
        return this.httpClientHelper.putRequest(json, destination);
    }

}
